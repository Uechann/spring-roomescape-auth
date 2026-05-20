package roomescape.domain.reservation;

import roomescape.domain.member.Member;
import roomescape.domain.Theme;
import roomescape.domain.Time;
import roomescape.domain.reservation.reservationStatus.PendingStatus;
import roomescape.domain.reservation.reservationStatus.ReservationStatus;

import java.time.LocalDate;
import java.util.Objects;

public class Reservation {

    private final Long id;
    private final Member member;
    private final LocalDate date;
    private final Time time;
    private final Theme theme;
    private ReservationStatus reservationStatus;

    public Reservation(Member member, LocalDate date, Time time, Theme theme) {
        validate(member, date, time, theme);
        this.id = null;
        this.member = member;
        this.date = date;
        this.time = time;
        this.theme = theme;
        this.reservationStatus = PendingStatus.getInstance();
    }

    public Reservation(Long id, Member member, LocalDate date, Time time, Theme theme, ReservationStatus reservationStatus) {
        validate(member, date, time, theme);
        this.id = id;
        this.member = member;
        this.date = date;
        this.time = time;
        this.theme = theme;
        this.reservationStatus = reservationStatus;
    }

    public static Reservation of(Long id, Reservation reservation) {
        return new Reservation(
                id,
                reservation.getMember(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getTheme(),
                reservation.getReservationStatus());
    }

    private void validate(Member member, LocalDate date, Time time, Theme theme) {
        if (member == null) {
            throw new IllegalArgumentException("예약자는 필수이며 비어있을 수 없습니다.");
        }
        if (date == null) {
            throw new IllegalArgumentException("예약 날짜는 필수입니다.");
        }
        if (time == null) {
            throw new IllegalArgumentException("유효하지 않은 예약 시간대입니다.");
        }
        if (theme == null) {
            throw new IllegalArgumentException("유효하지 않은 테마입니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public LocalDate getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }

    public ReservationStatus getReservationStatus() {
        return reservationStatus;
    }

    public String getReservationStatusName() {
        return reservationStatus.getName();
    }

    public void changeStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public void confirm() {
        reservationStatus.confirm(this);
    }

    public void cancel() {
        reservationStatus.cancel(this);
    }

    public void complete() {
        reservationStatus.complete(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
