package crud.team.service.post;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import crud.team.config.s3.CommonUtils;
import crud.team.dto.comment.CommentSimpleResponseDto;
import crud.team.dto.post.*;
import crud.team.entity.comment.Comment;
import crud.team.entity.post.Like;
import crud.team.entity.post.Post;
import crud.team.entity.user.User;
import crud.team.exception.RequestException;
import crud.team.repository.comment.CommentRepository;
import crud.team.repository.post.LikeRepository;
import crud.team.repository.post.PostRepository;
import crud.team.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static crud.team.exception.ExceptionType.ACCESS_DENIED_EXCEPTION;
import static crud.team.exception.ExceptionType.NOT_FOUND_EXCEPTION;


@RequiredArgsConstructor
@Service
public class PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    String bucket;

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

        Post post = postRepository.save(new Post(postCreateRequestDto.getTitle(), postCreateRequestDto.getContent(), user, imgurl, fileName)); // + imgUrl
        return PostCreateResponseDto.toDto(post);
    }

    // 게시글 전체 조회
    @Transactional(readOnly = true)
    public List<PostSimpleDto> findAll(Pageable pageable) {
        Page<Post> posts = postRepository.findAllPageBy(pageable);
        List<PostSimpleDto> postSimpleDtos = new ArrayList<>();
        posts.stream().forEach(i -> postSimpleDtos.add(new PostSimpleDto().toDto(i,commentRepository.findByPostId(i.getId()).size())));
        return postSimpleDtos;
    }

    // 게시글 상세 조회
    @Transactional(readOnly = true)
    public PostDetailSearchDto findPost(int id) {
        List<Comment> comment = commentRepository.findByPostId(id);
        List<CommentSimpleResponseDto> commentSimpleResponseDtos = new ArrayList<>();
        comment.forEach(i -> commentSimpleResponseDtos.add(CommentSimpleResponseDto.toDto(i)));

        return PostDetailSearchDto.toDto(postRepository.findById(id).orElseThrow(() -> new RequestException(NOT_FOUND_EXCEPTION)), commentSimpleResponseDtos);
    }

    // 게시글 수정
    @Transactional
    public PostDto editPost(int id, PostUpdateRequestDto postUpdateRequestDto, User user) {
        Post post = postRepository.findById(id).orElseThrow(() -> new RequestException(NOT_FOUND_EXCEPTION));
        if (!user.equals(post.getUser())) {
            throw new RequestException(ACCESS_DENIED_EXCEPTION);
        }
        post.updatePost(postUpdateRequestDto.getTitle(), postUpdateRequestDto.getContent());
        return PostDto.toDto(post);
    }

    // 게시글 삭제
    @Transactional
    public void deletePost(int id, User user) {
        Post post = postRepository.findById(id).orElseThrow(() -> new RequestException(NOT_FOUND_EXCEPTION));

        if (!user.equals(post.getUser())) {
            throw new RequestException(ACCESS_DENIED_EXCEPTION);
        }

        if (!post.getImgUrl().equals("")) {
            amazonS3Client.deleteObject(bucket, post.getFileName());
        }

        postRepository.delete(post);
    }

    // 게시글 키워드 검색
    @Transactional(readOnly = true)
    public List<PostSimpleDto> search(String keyword, Pageable pageable) {
        Page<Post> boards = postRepository.findByTitleContaining(keyword, pageable);
        List<PostSimpleDto> postSimpleDtos = new ArrayList<>();
        boards.stream().forEach(i -> postSimpleDtos.add(new PostSimpleDto().toDto(i, commentRepository.findByPostId(i.getId()).size())));
        return postSimpleDtos;
    }

    // 게시글 좋아요
    @Transactional
    public LikeResponseDto like(int id, User user) {
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
        return LikeResponseDto.toDto(post.getLikeNum());
    }

    // 인기 게시글 조회
    @Transactional(readOnly = true)
    public List<PostSimpleDto> findPopularPost(Pageable pageable) {
        Page<Post> posts = postRepository.findByLikeNumGreaterThanEqual(pageable, 5);
        List<PostSimpleDto> postSimpleDtos = new ArrayList<>();
        posts.stream().forEach(i -> postSimpleDtos.add(new PostSimpleDto().toDto(i, commentRepository.findByPostId(i.getId()).size())));
        return postSimpleDtos;
    }
}
