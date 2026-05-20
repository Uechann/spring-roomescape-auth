package roomescape.domain.member;

import java.util.Objects;

public class Member {

    private Long id;
    private final String name;
    private final String email;
    private final String password;
    private final MemberRole memberRole;

    public Member(String name, String email, String password, MemberRole memberRole) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.memberRole = memberRole;
    }

    public Member(Long id, String name, String email, String password, MemberRole memberRole) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.memberRole = memberRole;
    }

    public static Member of(Long memberId, Member member) {
        return new Member(
                memberId,
                member.getName(),
                member.getEmail(),
                member.getPassword(),
                member.getMemberRole()
        );
    }

    public boolean matchingPassword(String password) {
        return this.password.equals(password);
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

    public String getPassword() {
        return password;
    }

    public MemberRole getMemberRole() {
        return memberRole;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Member member)) return false;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
