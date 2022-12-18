package crud.team.repository.post;

import crud.team.dto.P;
import crud.team.entity.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryCustom {

    // 필터링 조회
    Page<Post> mainFilter(String imgCheck, String writeKeyword, String keyword, String sorting, Pageable pageable);

    // 키워드 조회
    Page<Post> keywordFilter(Pageable pageable, String keyword);

    // Warmup
    List<P.PostSimpleResponseDto> warmupNamedPost();
}
