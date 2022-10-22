package crud.team.controller.post;

import crud.team.exception.RequestException;
import crud.team.dto.post.PostCreateRequestDto;
import crud.team.dto.post.PostUpdateRequestDto;
import crud.team.entity.user.User;
import crud.team.repository.user.UserRepository;
import crud.team.response.Response;
import crud.team.service.post.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

import static crud.team.exception.ExceptionType.ACCESS_DENIED_EXCEPTION;
import static crud.team.response.Response.success;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api")
public class PostController {
    private final PostService postService;
    private final UserRepository userRepository;

    // 게시물 작성
    @PostMapping("/post")
    @ResponseStatus(HttpStatus.CREATED)
//    public Response create(@Valid @RequestBody PostCreateRequestDto postCreateRequestDto) {
    public Response create(@RequestPart(value="file",required = false) MultipartFile multipartFile,
                           @Valid @RequestPart PostCreateRequestDto postCreateRequestDto) throws IOException {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RequestException(ACCESS_DENIED_EXCEPTION));

        return success(postService.create(postCreateRequestDto, user, multipartFile));
    }

    // 게시글 전체 검색
    @GetMapping("/post")
    @ResponseStatus(HttpStatus.OK)
    public Response findAllPost(@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        // ex) http://localhost:8080/api/boards/all/{categoryId}/?page=0
        return success(postService.findAll(pageable));
    }

    // 게시글 검색
    @GetMapping("/post/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response findPost(@PathVariable int id) {
        return success(postService.findPost(id));
    }

    // 게시글 수정
    @PutMapping("/post/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response editPost(@PathVariable int id, @Valid @RequestBody PostUpdateRequestDto postUpdateRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RequestException(ACCESS_DENIED_EXCEPTION));

        return success(postService.editPost(id, postUpdateRequestDto, user));
    }

    // 게시글 삭제
    @DeleteMapping("/post/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response deletePost(@PathVariable int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RequestException(ACCESS_DENIED_EXCEPTION));
        postService.deletePost(id, user);
        return success();
    }

    // 게시글 키워드 검색
    // ex) http://localhost:8080/api/post/search?keyword=example&page=0
    @GetMapping("/post/search")
    @ResponseStatus(HttpStatus.OK)
    public Response search(String keyword, @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return success(postService.search(keyword, pageable));
    }

    // 게시글 좋아요
    @PostMapping("/post/{id}/like")
    @ResponseStatus(HttpStatus.OK)
    public Response like(@PathVariable int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RequestException(ACCESS_DENIED_EXCEPTION));
        return success(postService.like(id, user));
    }

    // 인기 게시글 조회
    @GetMapping("/post/popular")
    @ResponseStatus(HttpStatus.OK)
    public Response PopularPost(@PageableDefault(size = 5, sort = "like", direction = Sort.Direction.DESC) Pageable pageable) {
        return success(postService.findPopularPost(pageable));
    }
}