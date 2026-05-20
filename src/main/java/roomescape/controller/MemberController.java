package roomescape.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.TokenResponse;
import roomescape.auth.jwt.JwtProvider;
import roomescape.controller.dto.auth.LoginMember;
import roomescape.controller.dto.auth.LoginRequest;
import roomescape.controller.dto.auth.SignupRequest;
import roomescape.domain.member.Member;
import roomescape.auth.SessionConstant;
import roomescape.service.MemberService;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    public MemberController(MemberService memberService, JwtProvider jwtProvider) {
        this.memberService = memberService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequest signupRequest) {
        memberService.signup(signupRequest.email(), signupRequest.name(), signupRequest.password());
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest loginRequest, HttpSession session) {
        Member member = memberService.login(loginRequest.email(), loginRequest.password());
        session.setAttribute(SessionConstant.LOGIN_SESSION, LoginMember.from(member));
        return ResponseEntity.status(200).build();
    }

    @PostMapping("/login/token")
    public ResponseEntity<TokenResponse> loginWithToken(@Valid @RequestBody LoginRequest loginRequest, HttpSession session) {
        Member member = memberService.login(loginRequest.email(), loginRequest.password());
        String token = jwtProvider.createToken(member);
        return ResponseEntity.status(200).body(TokenResponse.of(token));
    }

}
