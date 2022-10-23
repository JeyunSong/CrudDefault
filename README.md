# 🦊Spring CRUD PROJECT (2022/10/22)
계속해서 Develop 진행 중입니다.

## USE
- Spring Web
- Spring Security
- JWT
- MySQL
- Validation
- JPA
- AWS S3

## API
- AUTH 
> Spring Security JWT를 활용한 회원가입/로그인 
>> ID + NAME 중복체크
- USER
> 유저 정보 조회 + 수정 + 삭제
- MY PAGE
> 내가 작성한 게시글 + 댓글 조회
>> 좋아요 한 게시물 불러오기 
- POST
> 게시물 조회 + 작성 + 수정 + 삭제 + 좋아요
>> 게시물 전체 조회 시 페이징 기능 | 키워드 검색 | 인기 게시물 조회 
>>> AWS S3를 사용한 이미지 업로드 + 삭제 기능
>>>> 게시물 상세 조회 시 댓글 출력
- COMMENT
> 댓글 조회 + 작성 + 수정 + 삭제
>> 대댓글 작성 + 수정 + 삭제
>>> 댓글 상세 조회 시 대댓글 출력
