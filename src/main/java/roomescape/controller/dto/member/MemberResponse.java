package roomescape.controller.dto.member;

import roomescape.domain.member.Member;

public record MemberResponse(
        Long id,
        String name,
        String email,
        String memberRole
) {
    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getMemberRole().name()
        );
    }
}
