package crud.team.entity.post;

import crud.team.entity.user.User;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Data
@Builder
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(nullable = false)
    private int likeNum;

    @Column(nullable = false)
    private int commentNum;

    @Column
    private String imgUrl;

    @Column
    private String fileName;

    @DateTimeFormat
    private LocalDateTime writeTime; // 날짜
    // 2022-10-21 14:41:30:15215412
    // 41분 전 작성 - 업데이트
    @PrePersist
    public void createDate() {
        this.writeTime = LocalDateTime.now();
    }

    public Post(String title, String content, User user, String imgUrl, String fileName) {
        this.title = title;
        this.content = content;
        this.writer = user.getName();
        this.user = user;
        this.imgUrl = imgUrl;
        this.fileName = fileName;
    }
    public void updatePost(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void PlusLike(){
        this.likeNum += 1;
    }
    public void MinusLike(){
        this.likeNum -= 1;
    }

    public void CommentNum(int commentNum) {
        this.commentNum = commentNum;
    }
}