package crud.team.controller.post;

import crud.team.dto.P.PostCreateRequestDto;
import crud.team.dto.P.PostUpdateRequestDto;
import crud.team.entity.user.User;
import crud.team.exception.RequestException;
import crud.team.repository.user.UserRepository;
import crud.team.response.Response;
import crud.team.service.post.PostService;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api")
public class PostController {
    private final PostService postService;
    private final UserRepository userRepository;



    // Warm UP
    @GetMapping("/warmup")
    public Response warmup() {
        postService.warmup();
        return success();
    }

    // 게시물 작성
    @PostMapping("/post")
    @ResponseStatus(HttpStatus.CREATED)
    public Response create(@RequestPart(value="file",required = false) MultipartFile multipartFile,
                           @Valid @RequestPart PostCreateRequestDto postCreateRequestDto) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RequestException(ACCESS_DENIED_EXCEPTION));

        return success(postService.create(postCreateRequestDto, user, multipartFile));
    }

    // 게시글 전체 검색
    @GetMapping("/post")
    public Response findAllPost(@PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return success(postService.findAll(pageable));
    }

    // 게시글 검색
    @GetMapping("/post/{id}")
    public Response findPost(@PathVariable int id) {
        return success(postService.findPost(id));
    }

    // 게시글 수정
    @PutMapping("/post/{id}")
    public Response editPost(@PathVariable int id, @Valid @RequestBody PostUpdateRequestDto postUpdateRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RequestException(ACCESS_DENIED_EXCEPTION));

        return success(postService.editPost(id, postUpdateRequestDto, user));
    }

    // 게시글 삭제
    @DeleteMapping("/post/{id}")
    public Response deletePost(@PathVariable int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RequestException(ACCESS_DENIED_EXCEPTION));
        postService.deletePost(id, user);
        return success();
    }

    // 키워드 검색
    @GetMapping("/post/search")
    public Response keywordSearch(String keyword, @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return success(postService.search(keyword, pageable));
    }

    // 필터링 검색
    @GetMapping("/post/search/filter")
    public Response filterSearch( String imgCheck, String writeKeyword, String keyword, String sorting, @PageableDefault(size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return success(postService.filterSearch(imgCheck, writeKeyword, keyword, sorting, pageable));
    }

    // 게시글 좋아요
    @PostMapping("/post/{id}/like")
    public Response like(@PathVariable int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RequestException(ACCESS_DENIED_EXCEPTION));
        return success(postService.like(id, user));
    }

    // 인기 게시글 조회
    @GetMapping("/post/popular")
    public Response PopularPost(@PageableDefault(size = 5, sort = "like", direction = Sort.Direction.DESC) Pageable pageable) {
        return success(postService.findPopularPost(pageable));
    }
}