package roomescape.auth.extractor;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import roomescape.auth.jwt.JwtProvider;
import roomescape.controller.dto.auth.LoginMember;
import roomescape.domain.member.MemberRole;

import java.util.Optional;

@Component
public class TokenAuthExtractor implements AuthExtractor {

    private static final String HEADER= "Authorization";
    private static final String PREFIX  = "Bearer ";

    private final JwtProvider jwtProvider;

    public TokenAuthExtractor(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean supports(HttpServletRequest httpServletRequest) {
        String header = httpServletRequest.getHeader(HEADER);
        return header != null && header.startsWith(PREFIX);
    }

    @Override
    public Optional<LoginMember> extract(HttpServletRequest httpServletRequest) {

        String header = httpServletRequest.getHeader(HEADER);
        if (header == null || !header.startsWith(PREFIX)) return Optional.empty();

        String token = header.substring(PREFIX.length());

        try {
            Claims claims = jwtProvider.parse(token);
            Long memberId = Long.parseLong(claims.getSubject());
            String email = claims.get("email", String.class);
            String name = claims.get("name", String.class);
            MemberRole role = MemberRole.valueOf(claims.get("role", String.class));
            return Optional.of(new LoginMember(memberId, email, name, role));
        } catch (JwtException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}