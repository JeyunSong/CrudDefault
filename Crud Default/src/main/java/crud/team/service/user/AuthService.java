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

        // 1. Login ID/PW ??? ???????????? AuthenticationToken ??????
        UsernamePasswordAuthenticationToken authenticationToken = loginRequestDto.toAuthentication();

        // 2. ????????? ?????? (????????? ???????????? ??????) ??? ??????????????? ??????
        //    authenticate ???????????? ????????? ??? ??? CustomUserDetailsService ?????? ???????????? loadUserByUsername ???????????? ?????????
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. ?????? ????????? ???????????? JWT ?????? ??????
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

//        // 4. RefreshToken ??????
//        RefreshToken refreshToken = RefreshToken.builder()
//                .key(authentication.getName())
//                .value(tokenDto.getRefreshToken())
//                .build();
//
//        refreshTokenRepository.save(refreshToken);

        redisService.setValues(authentication.getName(), tokenDto.getRefreshToken());

        response.addHeader("AccessToken", tokenDto.getAccessToken());
        response.addHeader("RefreshToken", tokenDto.getRefreshToken());

        // 5. ?????? ??????
        return new TokenResponseDto(tokenDto.getAccessToken(), tokenDto.getRefreshToken());
    }


    @Transactional
    public TokenResponseDto reissue(TokenRequestDto tokenRequestDto, HttpServletResponse response) {

        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());
        redisService.checkRefreshToken(authentication.getName(), tokenRequestDto.getRefreshToken());

        // ?????? ?????? ????????? ?????? ?????????
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        response.addHeader("AccessToken", tokenDto.getAccessToken());
        response.addHeader("RefreshToken", tokenDto.getRefreshToken());

        return new TokenResponseDto(tokenDto.getAccessToken(), tokenDto.getRefreshToken());

// >> MySQL Version RefreshToken Control
//         //1. Refresh Token ??????
//        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
//            throw new RuntimeException("Refresh Token ??? ???????????? ????????????.");
//        }
//
//        // 2. Access Token ?????? Member ID ????????????
//        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

//        // 3. ??????????????? Member ID ??? ???????????? Refresh Token ??? ?????????
//        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
//                .orElseThrow(() -> new RuntimeException("???????????? ??? ??????????????????."));
//
//        // 4. Refresh Token ??????????????? ??????
//        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
//            throw new RuntimeException("????????? ?????? ????????? ???????????? ????????????.");
//        }
//
//        // 5. ????????? ?????? ??????
//        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
//
//        // 6. ????????? ?????? ????????????
//        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
//        refreshTokenRepository.save(newRefreshToken);
//
//        // ?????? ??????
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
