package roomescape.controller.dto.time;

import roomescape.domain.ThemeSlot;
import roomescape.domain.Time;

import java.time.LocalTime;
import java.util.List;

public record TimeResponse(long id, LocalTime startAt, boolean isAvailable) {

    public static TimeResponse from(Time time) {
        return new TimeResponse(time.getId(), time.getStartAt(), true);
    }

    public static TimeResponse from(ThemeSlot themeSlot) {
        return new TimeResponse(themeSlot.getTime().getId(), themeSlot.getTime().getStartAt(), !themeSlot.isReserved());
    }
}
