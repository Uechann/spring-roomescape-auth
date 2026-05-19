package roomescape.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRole;

import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcMemberRepository implements MemberRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public JdbcMemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("member")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Member save(Member member) {
        Map<String, Object> params = createParams(member);
        Long memberId = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return Member.of(memberId, member);
    }

    private Map<String, Object> createParams(Member member) {
        return Map.of(
                "name", member.getName(),
                "email", member.getEmail(),
                "password", member.getPassword(),
                "role", member.getMemberRole().name()
        );
    }

    // 사용자 email로 조회
    @Override
    public Optional<Member> findByEmail(String email) {
        String sql = """
                SELECT 
                    m.id,
                    m.name,
                    m.email,
                    m.password,
                    m.role
                FROM 
                    member m
                WHERE m.email = ?
                """;

        return jdbcTemplate.query(sql, rowMapper(), email).stream().findFirst();
    }


    // 사용자 id로 조회
    @Override
    public Optional<Member> findById(String id) {
        String sql = """
                SELECT 
                    m.id,
                    m.name,
                    m.email,
                    m.password,
                    m.role
                FROM 
                    member m
                WHERE m.id = ?
                """;

        return jdbcTemplate.query(sql, rowMapper(), id).stream().findFirst();
    }

    private RowMapper<Member> rowMapper() {
        return (rs, rowNum) -> new Member(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password"),
                MemberRole.valueOf(rs.getString("role"))
        );
    }
}
