package roomescape.controller.dto;

import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRole;

public class LoginMember {

    private Long id;
    private final String name;
    private final String email;
    private final MemberRole memberRole;

    public LoginMember(Long id, String name, String email, MemberRole memberRole) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.memberRole = memberRole;
    }

    public static LoginMember from(Member member) {
        return new LoginMember(member.getId(), member.getName(), member.getEmail(), member.getMemberRole());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public MemberRole getMemberRole() {
        return memberRole;
    }
}
