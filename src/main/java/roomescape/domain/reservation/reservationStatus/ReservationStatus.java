package roomescape.domain.reservation.reservationStatus;

import roomescape.domain.reservation.Reservation;

public interface ReservationStatus {

    void cancel(Reservation reservation);
    void confirm(Reservation reservation);
    void complete(Reservation reservation);
    String getName();
}
