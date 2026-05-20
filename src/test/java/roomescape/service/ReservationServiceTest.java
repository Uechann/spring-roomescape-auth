package roomescape.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.Theme;
import roomescape.domain.Time;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRole;
import roomescape.domain.reservation.Reservation;
import roomescape.repository.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReservationServiceTest {

    private ReservationService reservationService;
    private Time savedTime;
    private Theme savedTheme;

    private static final Member MEMBER_2 = new Member(2L, "게스트", "guest@test.com", "password", MemberRole.USER);

    @BeforeEach
    void setUp() {
        FakeTimeRepository fakeReservationTimeDao = new FakeTimeRepository();
        FakeThemeRepository fakeThemeRepository = new FakeThemeRepository();
        FakeMemberRepository fakeMemberRepository = new FakeMemberRepository();

        reservationService = new ReservationService(
                new FakeReservationRepository(),
                fakeReservationTimeDao,
                fakeThemeRepository,
                new FakeThemeSlotRepository(),
                fakeMemberRepository
        );
        savedTime = fakeReservationTimeDao.save(Time.of(LocalTime.of(10, 0)));
        savedTheme = fakeThemeRepository.save(new Theme("이름", "설명", "test.com"));
    }

    @Test
    @DisplayName("원시값을 받아 연관된 객체를 조회하여 조립한 뒤 예약을 생성한다.")
    void saveReservation() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        Reservation reservation = reservationService.saveReservation(2L, futureDate, savedTime.getId(),
                savedTheme.getId());
        assertThat(reservation.getTime().getStartAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    @DisplayName("존재하는 예약을 식별자를 통해 삭제하면 목록에서 사라진다.")
    void removeReservation() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        Reservation reservation = reservationService.saveReservation(2L, futureDate, savedTime.getId(),
                savedTheme.getId());
        reservationService.removeReservation(reservation.getId());
        assertThat(reservationService.allReservations()).isEmpty();
    }

    @Test
    @DisplayName("모든 예약 목록을 조회하여 반환한다.")
    void allReservations() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        reservationService.saveReservation(2L, futureDate, savedTime.getId(), savedTheme.getId());
        List<Reservation> reservations = reservationService.allReservations();
        assertThat(reservations).hasSize(1);
    }

    @Test
    @DisplayName("식별자를 통해 특정 예약 객체를 조회한다.")
    void findReservation() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        Reservation savedReservation = reservationService.saveReservation(2L, futureDate, savedTime.getId(),
                savedTheme.getId());
        Reservation foundReservation = reservationService.findReservation(savedReservation.getId());
        assertThat(foundReservation.getMember()).isEqualTo(MEMBER_2);
    }
}
