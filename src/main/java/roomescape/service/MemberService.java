package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRole;
import roomescape.global.exception.CustomException;
import roomescape.repository.MemberRepository;

import static roomescape.global.exception.ErrorCode.MEMBER_NOT_FOUNT;
import static roomescape.global.exception.ErrorCode.PASSWORD_NOT_CORRECT;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member signup(String email, String name, String password) {
        Member member = new Member(name, email, password, MemberRole.USER);
        return memberRepository.save(member);
    }

    public Member login(String email, String password) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUNT));

        if (!member.matchingPassword(password)) {
            throw new CustomException(PASSWORD_NOT_CORRECT);
        }
        return member;
    }
}
