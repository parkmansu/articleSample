package com.example.articlesample.Controller;


import com.example.articlesample.dto.SignupRequestDto;
import com.example.articlesample.dto.UserCheckRequestDto;
import com.example.articlesample.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //회원가입
    @PostMapping("/user/signup")
    public ResponseEntity<String> registerUser(@RequestBody SignupRequestDto requestDto) {
        userService.registerUser(requestDto);
        return ResponseEntity.ok()
                .body("회원가입 완료");
    }

    //이메일, 비밀번호 유효성 확인
    @PostMapping("/user/check")
    public ResponseEntity<String> checkUser(@RequestBody UserCheckRequestDto requestDto) {
        userService.checkUser(requestDto);
        return ResponseEntity.ok()
                .body("유효성 완료");
    }


}
