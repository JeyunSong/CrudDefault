# 🍎Spring CRUD PROJECT
계속해서 Develop 진행 중입니다.

## ✔ Tech Stack
### **Application**
  
- **JAVA 11**
- **Spring Boot** _2.7.0
- **Spring Security** _0.11.2
- **JPA**
- **Query DSL** _5.0.0
- **Full Text Search**

### **Data**

- **AWS RDS - MySQL** _8.028
- **AWS ElastiCache for Redis** _7.0.4
- **AWS S3**
</br></br>

## ✔ 기능
#### AUTH 
- Spring Security JWT를 활용한 회원가입/로그인 
#### USER
- 유저 정보 조회 + 수정 + 삭제
#### MY PAGE
- 내가 작성한 게시글 + 댓글 조회
- 좋아요 한 게시물 불러오기 
#### POST
- 게시물 키워드 조회 -> Full Text Search
- 게시물 필터링 조회 -> Query DSL
- AWS S3를 사용한 이미지 업로드 + 삭제 기능
- 게시물 캐싱 + Warmup 파이프라인
- 좋아요 기능
#### COMMENT
- 댓글 + 대댓글
