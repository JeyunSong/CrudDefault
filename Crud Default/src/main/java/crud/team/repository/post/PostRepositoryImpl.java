package crud.team.repository.post;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.core.util.StringUtils;
import com.querydsl.jpa.impl.JPAQueryFactory;
import crud.team.dto.P.PostSimpleRequestDto;
import crud.team.entity.post.Post;
import crud.team.entity.post.QPost;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class PostRepositoryImpl implements PostRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    QPost qPost = QPost.post;


    // 필터링 조회
    @Override
    public Page<Post> mainFilter(String imgCheck, String writeKeyword, String keyword, String sorting, Pageable pageable) {

        List<Post> result = queryFactory.from(qPost)
                .select(Projections.constructor(Post.class))
                .where(keywordContain(keyword),
                        writerContain(writeKeyword),
                        isImg(imgCheck))
                .orderBy(sorting(sorting))
                .fetch();

        return new PageImpl<>(result, pageable, result.size());
    }


    // 키워드 조회
    @Override
    public Page<Post> keywordFilter(Pageable pageable, String keyword) {

        List<Post> result = queryFactory.from(qPost)
                .select(Projections.constructor(Post.class))
                .where(keywordMatch(keyword))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(result, pageable, result.size());
    }

    // WarmUp -> Return ProductDetailResponseDto Category Top 100
    public List<PostSimpleRequestDto> warmupNamedPost() {
        return queryFactory.from(qPost)
                .select(Projections.constructor(PostSimpleRequestDto.class,qPost
                ))
                .orderBy(qPost.likeNum.desc())
                .limit(500)
                .fetch();
    }


    // Full Text Search Filter
    private BooleanExpression keywordMatch(String keyword) {
        if (StringUtils.isNullOrEmpty(keyword)) return null;
        NumberTemplate booleanTemplate = Expressions.numberTemplate(Double.class,
                "function('match',{0},{1})", qPost.title, "+" + keyword + "*");
        return booleanTemplate.gt(0);
    }

    // 키워드 Filter
    private BooleanExpression keywordContain(String keyword) {
        if (StringUtils.isNullOrEmpty(keyword)) return null;
        return qPost.title.contains(keyword);
    }

    // 키워드 Filter
    private BooleanExpression writerContain(String writerKeyword) {
        if (StringUtils.isNullOrEmpty(writerKeyword)) return null;
        return qPost.writer.contains(writerKeyword);
    }


    // 정렬 Filter
    private OrderSpecifier<?> sorting(String sorting) {
        if (!sorting.equals("인기순")) return qPost.id.desc();
        return qPost.likeNum.asc();
    }

    // 이미지 유무 Filter
    private BooleanExpression isImg(String stock) {
        if (StringUtils.isNullOrEmpty(stock)) return null;
        if (stock.equals("1")) return null; // "1" : 이미지 없는 게시물 포함 -> null 리턴
        return qPost.imgUrl.ne(""); // "0" : 이미지 없는 게시물 미포함
    }
}
