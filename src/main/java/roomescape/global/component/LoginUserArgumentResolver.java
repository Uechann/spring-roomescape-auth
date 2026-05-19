package roomescape.global.component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import roomescape.auth.LoginUser;
import roomescape.controller.dto.LoginMember;
import roomescape.global.exception.CustomException;

import static roomescape.global.component.SessionConstant.LOGIN_SESSION;
import static roomescape.global.exception.ErrorCode.UNAUTHORIZED;

@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver{

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 어노테이션 소유 여부 && 객체 타입 여부
        boolean hasAnnotation = parameter.hasParameterAnnotation(LoginUser.class);
        boolean assignableFrom = LoginMember.class.isAssignableFrom(parameter.getParameterType());
        return hasAnnotation && assignableFrom;
    }

    @Nullable
    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            @Nullable ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            @Nullable WebDataBinderFactory binderFactory ) throws Exception {

        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        HttpSession session = servletRequest.getSession(false);
        if (session == null) {
            throw new CustomException(UNAUTHORIZED);
        }

        Object attribute = session.getAttribute(LOGIN_SESSION);
        if (attribute == null) {
            throw new CustomException(UNAUTHORIZED);

        }
        return attribute;
    }
}
