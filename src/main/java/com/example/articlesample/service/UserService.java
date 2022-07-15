package com.example.articlesample.service;


import com.example.articlesample.dto.SignupRequestDto;
import com.example.articlesample.dto.UserCheckRequestDto;
import com.example.articlesample.model.Users;
import com.example.articlesample.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public void registerUser(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        checkUserName(username);
        String nickname = requestDto.getNickname();
        checkNickName(nickname);
        String password = passwordEncoder.encode(requestDto.getPassword());

        Users user = new Users(username,password,nickname);
        userRepository.save(user);
    }

    //이메일, 비밀번호 유효성 확인
    public void checkUser(UserCheckRequestDto requestDto) {
        String username = requestDto.getUsername();
        String pwd = requestDto.getPassword();
        String pwdCheck = requestDto.getPwdCheck();

        //아이디 유효성 검사
        checkUserName(username);

        //비밀번호 유효성 검사
        checkUserPw(pwd, pwdCheck);

    }

    // 아이디 유효성 검사
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

    //비밀번호 유효성 검사
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

    //닉네임 유효성 검사
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










}
