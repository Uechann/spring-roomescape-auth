package roomescape.auth.component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.LoginRequired;
import roomescape.auth.SessionConstant;
import roomescape.auth.extractor.AuthExtractor;
import roomescape.controller.dto.auth.LoginMember;
import roomescape.global.exception.CustomException;

import java.util.List;

import static roomescape.auth.SessionConstant.LOGIN_SESSION;
import static roomescape.global.exception.ErrorCode.UNAUTHORIZED;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final List<AuthExtractor> authExtractors;

    // 스프링이 AuthExtractor로 등록된 빈들을 모두 등록시켜줌
    public AuthInterceptor(List<AuthExtractor> authExtractors) {
        this.authExtractors = authExtractors;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        // LoginRequired 어노테이션이 있는지 확인
        LoginRequired methodAnnotation = handlerMethod.getMethodAnnotation(LoginRequired.class);
        if (methodAnnotation == null) {
            return true;
        }

        LoginMember loginMember = authExtractors.stream()
                .filter(authExtractor -> authExtractor.supports(request))
                .findFirst()
                .flatMap(authExtractor -> authExtractor.extract(request))
                .orElseThrow(() -> new CustomException(UNAUTHORIZED));

        // HttpServletRequest에 저장 loginMember를 해둔다.
        request.setAttribute("loginMember", loginMember);
        return true;
    }
}
