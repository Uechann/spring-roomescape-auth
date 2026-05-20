package roomescape.auth.extractor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import roomescape.auth.SessionConstant;
import roomescape.controller.dto.auth.LoginMember;

import java.util.Optional;

@Component
public class SessionAuthExtractor implements AuthExtractor {
    @Override
    public boolean supports(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession(false);
        return session != null && session.getAttribute(SessionConstant.LOGIN_SESSION) != null;
    }

    @Override
    public Optional<LoginMember> extract(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession(false);

        Object attribute = session.getAttribute(SessionConstant.LOGIN_SESSION);

        if (!(attribute instanceof LoginMember loginMember)) return Optional.empty();

        return Optional.of(loginMember);
    }
}
