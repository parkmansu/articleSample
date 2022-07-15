# 간단한 기능의 게시판 구현

* 회원가입 후 로그인하여 간단하게 게시판 작성, 수정, 삭제, 검색을 할 수 있는 게시판을 구현 해보았습니다.

## 기술스택

* spring boot
* H2 DB - console
* JPA

## 기능 구현 내용 

* 회원가입
* 로그인
* 게시판 작성, 수정, 삭제
* 게시판 검색

## 구현 내용 테스트 시나리오

1. 회원가입(1) -> 로그인 -> 게시판 작성, 수정, 삭제, 상세보기 -> 게시판 검색
2. 회원가입(2) -> 로그인 -> 게시판 수정 및 삭제 시 본인여부 판단

### 회원가입
* 필수 포함요소(아이디, 비밀번호, 별칭)
* spring Security를 활용한 비밀번호 암호화 진행.

![image](https://user-images.githubusercontent.com/101081641/179185411-41315de0-ce7c-458d-a3fd-3a5d701aed8d.png)
```
String password = passwordEncoder.encode(requestDto.getPassword());
```

* 간단한 유효성 검사
  * 아이디 - 중복여부 / 공란 / 이메일형식
  ```
      private void checkUserName(String username) {
        Optional<Users> foundByUserName = userRepository.findByUsername(username);
        if (foundByUserName.isPresent()) {
            throw new IllegalArgumentException("유저아이디가 이미 존재합니다.");
        }
        Pattern userNamePattern = Pattern.compile("\\w+@\\w+\\.\\w+(\\.\\w+)?");
        Matcher userNameMatcher = userNamePattern.matcher(username);
        if (username.length() == 0) {
            throw new IllegalArgumentException("아이디는 필수 입력값입니다.");
        }
        if (!userNameMatcher.matches()) {
            throw new IllegalArgumentException("아이디를 이메일 형식으로 입력해주세요.");
        }
    }
  ```
  * 비밀번호 - 공란 / 영문,숫자를 사용하여 6~20자로 입력 / 비밀번호, 비밀번호확인 일치 여부
  ```
      private void checkUserPw(String pwd, String pwdCheck) {
        Pattern userPwPattern = Pattern.compile("^(?=.*\\d)(?=.*[a-zA-Z])[0-9a-zA-Z]{6,20}$");
        Matcher userPwMatcher = userPwPattern.matcher(pwd);
        if (pwd.length() == 0) {
            throw new IllegalArgumentException("비밀번호는 필수 입력값입니다.");
        }
        if (!userPwMatcher.matches()) {
            throw new IllegalArgumentException("비밀번호는 영문, 숫자를 사용하여 6~20자로 입력해주세요.");
        }
        // password 일치여부
        if (pwdCheck.length() == 0) {
            throw new IllegalArgumentException("비밀번호 확인은 필수 입력값입니다.");
        }
        if (!pwd.equals(pwdCheck)) {
            throw new IllegalArgumentException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }
    }
  ```
  * 닉네임 - 중복여부 / 공란 / 2~8자로 입력
  ```
      public void checkNickName(String nickname) {
        String checkNickname = nickname.replaceAll(" ", "");
        Pattern nickNamePattern = Pattern.compile("^\\S{2,8}$");
        Matcher nickNameMatcher = nickNamePattern.matcher(checkNickname);

        Optional<Users> foundByNickName = userRepository.findByNickname(nickname);
        if (foundByNickName.isPresent()) {
            throw new IllegalArgumentException("유저닉네임이 이미 존재합니다.");
        }
        if (nickname.length() == 0) {
            throw new IllegalArgumentException("닉네임은 필수 입력값니다.");
        }
        if (!nickNameMatcher.matches()) {
            throw new IllegalArgumentException("닉네임은 2~8자로 입력해주세요.");
        }
    }
  ```
### 로그인
spring security와 JWT를 활용한 로그인 구현(로그인한 회원만 게시판에 대한 접근이 가능)
* 로그인 후 api 호출 시 JWT항상 체크
* 로그인시 JWT를 Headers(Authorization)에 보내준다.

![image](https://user-images.githubusercontent.com/101081641/179190756-df015651-3c7f-426b-9b78-dbdef50521d6.png)
* 로그인한 JWT를 해독시 회원가입했던 아이디/닉네임이 동일하다

![image](https://user-images.githubusercontent.com/101081641/179191172-8e9b1e2b-2ce0-4ca8-aaf6-e4f130e7a71b.png)

### 게시판 작성, 수정, 삭제
* 게시판 작성 - 게시판 제목 / 내용 / 등록시간, 수정시간 / 작성자 ID 저장

![image](https://user-images.githubusercontent.com/101081641/179192344-1025cf40-7c2a-4975-a8a6-6c645ebc9a0a.png)
* 게시판 수정 - 게시판 제목 / 내용 / 수정시간 변경

![image](https://user-images.githubusercontent.com/101081641/179192566-cbc8449b-4324-41af-916a-d22d2769473e.png)
* 게시판 삭제 - 게시판 삭제시 해당 게시판의 내용 DB에서 삭제

![image](https://user-images.githubusercontent.com/101081641/179193008-276723fb-2191-4d90-b3fa-bb265c6650cc.png)


### 게시판 상세보기
* 상세보기 시 제목 / 내용 / 작성자 별칭 / 등록시간 조회

![image](https://user-images.githubusercontent.com/101081641/179195231-4523edb3-9e92-4292-af20-6f057032a8c1.png)


### 게시판 리스트
* 게시판 리스트 조회 시 페이징 처리(1page / 5)(최신순으로 정렬)
  * DB를 보시면 7개의 게시판이 있는것을 확인
  
![image](https://user-images.githubusercontent.com/101081641/179193334-cbd63e2f-d5af-42c2-ac29-dc145060ade3.png)
  * postman으로 조회시 1page 5개의 게시판 조회되는것을 확인
  
![image](https://user-images.githubusercontent.com/101081641/179193583-0455a3dd-6c75-455d-92dc-9190daf71dd9.png)
  * postman으로 조회시 2page 2개의 게시판 조회되는것을 확인
  
![image](https://user-images.githubusercontent.com/101081641/179193630-9140e211-4005-46f1-97e4-fdaa565f0652.png)


### 게시판 검색
* 검색시 제목 또는 내용에 검색어가 있을 시 검색 조회

![image](https://user-images.githubusercontent.com/101081641/179194070-29b75686-66f3-4b36-b0f0-eaa875fd5d59.png)


### 다른 사용자로 게시판 수정 및 삭제
* 다른 사용자로 로그인하여 작성자가 다를 때 수정 및 삭제 여부
  * 아래의 코드를 통해 로그인사용자와 작성자가 다를 때 수정 및 삭제가 불가능하게 유효성 검사 진행
  
  ![image](https://user-images.githubusercontent.com/101081641/179196280-e7221ec1-4294-4320-9bda-a5fc79b671c6.png)

```
    //게시판 작성자 검사
    public Post checkPostAndWriter(Long postId, Users users){

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalArgumentException("게시판 Id에 해당하는 글이 없습니다.")
        );

        Long postUserId = post.getUsers().getId();
        if(!users.getId().equals(postUserId)){
            throw new IllegalArgumentException("작성자가 아니므로 게시판 글 수정이 불가합니다.");
        }
        return post;
    }
```



