package roomescape.controller.dto.reservation;

import roomescape.controller.dto.theme.ThemeResponse;
import roomescape.controller.dto.time.TimeResponse;

import java.time.LocalDate;

public record ReservationResponse(
        long id,
        String name,
        LocalDate date,
        TimeResponse time,
        ThemeResponse theme,
        String status
) { }
