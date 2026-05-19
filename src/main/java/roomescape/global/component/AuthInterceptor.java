package roomescape.global.component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import roomescape.auth.LoginRequired;
import roomescape.global.exception.CustomException;

import static roomescape.global.component.SessionConstant.LOGIN_SESSION;
import static roomescape.global.exception.ErrorCode.UNAUTHORIZED;

@Component
public class AuthInterceptor implements HandlerInterceptor {


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

        // 세션에 로그인 정보가 없으면 예외 처리
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(LOGIN_SESSION) == null) {
            throw new CustomException(UNAUTHORIZED);
        }

        return true;
    }
}
