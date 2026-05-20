package roomescape.auth.extractor;

import jakarta.servlet.http.HttpServletRequest;
import roomescape.controller.dto.auth.LoginMember;

import java.util.Optional;

public interface AuthExtractor {

    /** 이 요청을 본인이 처리할 수 있는 인증 요청인지 판단하는 인터페이스 */
    boolean supports(HttpServletRequest httpServletRequest);

    /** 인증정보에서 LoginMember 정보를 추출하고 반환한다. */
    Optional<LoginMember> extract(HttpServletRequest httpServletRequest);
}
