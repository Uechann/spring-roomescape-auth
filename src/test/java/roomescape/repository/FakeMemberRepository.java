package roomescape.repository;

import roomescape.domain.member.Member;

import java.util.Optional;

public class FakeMemberRepository implements MemberRepository {

    @Override
    public Member save(Member member) {
        return null;
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.empty();
    }
}
