package roomescape.controller.dto.reservation;

import roomescape.controller.dto.member.MemberResponse;
import roomescape.controller.dto.theme.ThemeResponse;
import roomescape.controller.dto.time.TimeResponse;
import roomescape.domain.reservation.Reservation;

import java.time.LocalDate;

public record ReservationResponse(
        Long id,
        MemberResponse member,
        LocalDate date,
        TimeResponse time,
        ThemeResponse theme,
        String status
) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                MemberResponse.from(reservation.getMember()),
                reservation.getDate(),
                TimeResponse.from(reservation.getTime()),
                ThemeResponse.from(reservation.getTheme()),
                reservation.getReservationStatusName()
        );
    }
}
