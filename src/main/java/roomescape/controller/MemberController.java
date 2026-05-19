package roomescape.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import roomescape.controller.dto.LoginMember;
import roomescape.controller.dto.LoginRequest;
import roomescape.controller.dto.SignupRequest;
import roomescape.domain.member.Member;
import roomescape.global.component.SessionConstant;
import roomescape.service.MemberService;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequest signupRequest) {
        memberService.signup(signupRequest.email(), signupRequest.name(), signupRequest.password());
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> signup(@Valid @RequestBody LoginRequest loginRequest, HttpSession session) {
        Member member = memberService.login(loginRequest.email(), loginRequest.password());
        session.setAttribute(SessionConstant.LOGIN_SESSION, LoginMember.from(member));
        return ResponseEntity.status(201).build();
    }
}
