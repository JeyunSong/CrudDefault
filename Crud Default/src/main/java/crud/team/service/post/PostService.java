package crud.team.service.post;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import crud.team.config.s3.CommonUtils;
import crud.team.dto.L;
import crud.team.entity.post.Like;
import crud.team.entity.post.Post;
import crud.team.entity.user.User;
import crud.team.exception.RequestException;
import crud.team.repository.post.LikeRepository;
import crud.team.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static crud.team.dto.P.*;
import static crud.team.exception.ExceptionType.ACCESS_DENIED_EXCEPTION;
import static crud.team.exception.ExceptionType.NOT_FOUND_EXCEPTION;


@Slf4j
@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final AmazonS3Client amazonS3Client;
    private final RedisTemplate<String, PostSimpleResponseDto> redisTemplate;


    @Value("${cloud.aws.s3.bucket}")
    String bucket;

    // Named Post - WarmUp
    @Transactional
    public void warmup(){
        log.info("Warm Up Named Post PipeLine Start....");

        List<PostSimpleResponseDto> list = postRepository.warmupNamedPost();
        pipeline(list);
    }

    // Named Post - PipeLine
    public void pipeline(List<PostSimpleResponseDto> list) {
        RedisSerializer keySerializer = redisTemplate.getStringSerializer();
        RedisSerializer valueSerializer = redisTemplate.getValueSerializer();

        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            list.forEach(i -> {
                String key = "post::"+i.getPostId();
                connection.set(keySerializer.serialize(key), //stringCommands 수정
                        valueSerializer.serialize(i));
            });
            return null;
        });
    }

    // 게시글 작성
    @Transactional
    public PostCreateResponseDto create(PostCreateRequestDto postCreateRequestDto, User user, MultipartFile multipartFile) throws IOException {

        String imgurl = "";

        String fileName = null;
        if (!multipartFile.isEmpty()) {
            fileName = CommonUtils.buildFileName(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(multipartFile.getContentType());

            byte[] bytes = IOUtils.toByteArray(multipartFile.getInputStream());
            objectMetadata.setContentLength(bytes.length);
            ByteArrayInputStream byteArrayIs = new ByteArrayInputStream(bytes);

            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, byteArrayIs, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
            imgurl = amazonS3Client.getUrl(bucket, fileName).toString();
        }

        Post post = postRepository.save(new Post(postCreateRequestDto.getTitle(), postCreateRequestDto.getContent(), user, imgurl, fileName));

        return PostCreateResponseDto.toDto(post);
    }


    // 게시글 전체 조회
    @Transactional(readOnly = true)
    public List<PostSimpleResponseDto> findAll(Pageable pageable) {

        log.info("Search All Log Start....");

        Page<Post> posts = postRepository.findAllPageBy(pageable);
        List<PostSimpleResponseDto> postSimpleResponseDtos = new ArrayList<>();
        posts.stream().forEach(i -> postSimpleResponseDtos.add(PostSimpleResponseDto.toDto(i)));

        return postSimpleResponseDtos;
    }


    // 게시글 상세 조회 -> Cache Aside
    @Cacheable(value = "post", key = "#id") // [post::1], [name : "" , cre ...]
    @Transactional(readOnly = true)
    public PostDetailResponseDto findPost(int id) {

        log.info("Search Once Log Start....");

        Post post = postRepository.findById(id).orElseThrow(() -> new RequestException(NOT_FOUND_EXCEPTION));

        return PostDetailResponseDto.toDto(post);
    }


    // 게시글 키워드 조회
    // Need QueryDSL
    @Transactional(readOnly = true)
    public List<PostSimpleResponseDto> search(String keyword, Pageable pageable) {
        Page<Post> posts = postRepository.keywordFilter(pageable, keyword);

        List<PostSimpleResponseDto> postSimpleResponseDtos = new ArrayList<>();

        posts.stream().forEach(i -> postSimpleResponseDtos.add(PostSimpleResponseDto.toDto(i)));
        return postSimpleResponseDtos;
    }

    // 게시글 필터링 조회
    // Need QueryDSL
    @Transactional(readOnly = true)
    public List<PostSimpleResponseDto> filterSearch(String imgCheck, String writeKeyword, String keyword, String sorting, Pageable pageable) {
        Page<Post> posts = postRepository.mainFilter(imgCheck, writeKeyword, keyword, sorting, pageable);

        List<PostSimpleResponseDto> postSimpleResponseDtos = new ArrayList<>();

        posts.stream().forEach(i -> postSimpleResponseDtos.add(PostSimpleResponseDto.toDto(i)));
        return postSimpleResponseDtos;
    }


    // 인기 게시글 조회
    @Transactional(readOnly = true)
    public List<PostSimpleResponseDto> findPopularPost(Pageable pageable) {
        Page<Post> posts = postRepository.findByLikeNumGreaterThanEqual(pageable, 5);
        List<PostSimpleResponseDto> postSimpleResponseDtos = new ArrayList<>();
        posts.stream().forEach(i -> postSimpleResponseDtos.add(PostSimpleResponseDto.toDto(i)));
        return postSimpleResponseDtos;
    }


    // 게시글 수정
    @CachePut(value="post", key = "#id")
    @Transactional
    public PostDetailResponseDto editPost(int id, PostUpdateRequestDto postUpdateRequestDto, User user) {

        log.info("Update Log Start....");

        Post post = postRepository.findById(id).orElseThrow(() -> new RequestException(NOT_FOUND_EXCEPTION));

        if (!user.equals(post.getUser())) {
            throw new RequestException(ACCESS_DENIED_EXCEPTION);
        }
        post.updatePost(postUpdateRequestDto.getTitle(), postUpdateRequestDto.getContent());

        return PostDetailResponseDto.toDto(post);
    }

    // 게시글 삭제
    @CacheEvict(value="post", key = "#id")
    @Transactional
    public void deletePost(int id, User user) {

        log.info("Delete Log Start....");

        Post post = postRepository.findById(id).orElseThrow(() -> new RequestException(NOT_FOUND_EXCEPTION));

        if (!user.equals(post.getUser())) {
            throw new RequestException(ACCESS_DENIED_EXCEPTION);
        }

        if (!post.getImgUrl().equals("")) {
            amazonS3Client.deleteObject(bucket, post.getFileName());
        }

        postRepository.delete(post);
    }


    // 게시글 좋아요
    @CacheEvict(value="post", key = "#id")
    @Transactional
    public L like(int id, User user) {
        Post post = postRepository.findById(id).orElseThrow(() -> new RequestException(NOT_FOUND_EXCEPTION));

        if (likeRepository.findByPostAndUser(post, user) == null) {
            post.PlusLike();
            Like like = new Like(post, user);
            likeRepository.save(like);
        } else {
            Like like = likeRepository.findByPostAndUser(post, user);
            post.MinusLike();
            likeRepository.delete(like);
        }
        return L.toDto(post.getLikeNum());
    }
}
