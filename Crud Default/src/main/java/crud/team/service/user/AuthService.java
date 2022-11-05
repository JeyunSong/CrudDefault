package crud.team.service.user;


import crud.team.config.jwt.TokenProvider;
import crud.team.dto.user.*;
import crud.team.entity.user.Authority;
import crud.team.entity.user.User;
import crud.team.exception.RequestException;
import crud.team.repository.user.UserRepository;
import crud.team.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

import static crud.team.exception.ExceptionType.ALREADY_EXISTS_EXCEPTION;
import static crud.team.exception.ExceptionType.LOGIN_FAIL_EXCEPTION;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RedisService redisService;



    @Transactional
    public void signup(SignUpRequestDto signUpRequestDto) {
        if (userRepository.existsByEmail(signUpRequestDto.getEmail()))
            throw new RequestException(ALREADY_EXISTS_EXCEPTION);

        userRepository.save(User.builder()
                .email(signUpRequestDto.getEmail())
                .password(passwordEncoder.encode(signUpRequestDto.getPassword()))
                .name(signUpRequestDto.getName())
                .authority(Authority.ROLE_USER)
                .build());


    }

    @Transactional
    public void emailDuplicate(EmailValidDto emailValidDto) {
        validateEmailInfo(emailValidDto);
    }

    @Transactional
    public void nameDuplicate(NameValidDto nameValidDto) {
        validateNameInfo(nameValidDto);
    }


    @Transactional
    public TokenResponseDto login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        User user = userRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(() -> new RequestException(LOGIN_FAIL_EXCEPTION));
        validatePassword(loginRequestDto, user);

        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = loginRequestDto.toAuthentication();

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

//        // 4. RefreshToken 저장
//        RefreshToken refreshToken = RefreshToken.builder()
//                .key(authentication.getName())
//                .value(tokenDto.getRefreshToken())
//                .build();
//
//        refreshTokenRepository.save(refreshToken);

        redisService.setValues(authentication.getName(), tokenDto.getRefreshToken());

        response.addHeader("AccessToken", tokenDto.getAccessToken());
        response.addHeader("RefreshToken", tokenDto.getRefreshToken());

        // 5. 토큰 발급
        return new TokenResponseDto(tokenDto.getAccessToken(), tokenDto.getRefreshToken());
    }


    @Transactional
    public TokenResponseDto reissue(TokenRequestDto tokenRequestDto, HttpServletResponse response) {

        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());
        redisService.checkRefreshToken(authentication.getName(), tokenRequestDto.getRefreshToken());

        // 예외 처리 통과후 토큰 재생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        response.addHeader("AccessToken", tokenDto.getAccessToken());
        response.addHeader("RefreshToken", tokenDto.getRefreshToken());

        return new TokenResponseDto(tokenDto.getAccessToken(), tokenDto.getRefreshToken());

// >> MySQL Version RefreshToken Control
//         //1. Refresh Token 검증
//        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
//            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
//        }
//
//        // 2. Access Token 에서 Member ID 가져오기
//        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

//        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
//        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
//                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));
//
//        // 4. Refresh Token 일치하는지 검사
//        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
//            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
//        }
//
//        // 5. 새로운 토큰 생성
//        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
//
//        // 6. 저장소 정보 업데이트
//        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
//        refreshTokenRepository.save(newRefreshToken);
//
//        // 토큰 발급
//        return new TokenResponseDto(tokenDto.getAccessToken(), tokenDto.getRefreshToken());dd

    }


    private void validateEmailInfo(EmailValidDto emailValidDto) {
        if (userRepository.existsByEmail(emailValidDto.getEmail()))
            throw new RequestException(ALREADY_EXISTS_EXCEPTION);
    }

    private void validateNameInfo(NameValidDto nameValidDto) {
        if (userRepository.existsByName(nameValidDto.getName()))
            throw new RequestException(ALREADY_EXISTS_EXCEPTION);
    }

    private void validatePassword(LoginRequestDto loginRequestDto, User user) {
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new RequestException(LOGIN_FAIL_EXCEPTION);
        }
    }

}
