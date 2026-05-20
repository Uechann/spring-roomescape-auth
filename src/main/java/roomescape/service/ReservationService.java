package roomescape.service;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.Theme;
import roomescape.domain.ThemeSlot;
import roomescape.domain.Time;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.Reservation;
import roomescape.global.exception.CustomException;
import roomescape.global.exception.ErrorCode;
import roomescape.repository.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static roomescape.global.exception.ErrorCode.MEMBER_NOT_FOUND;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TimeRepository timeRepository;
    private final ThemeRepository themeRepository;
    private final ThemeSlotRepository themeSlotRepository;
    private final MemberRepository memberRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            TimeRepository timeRepository,
            ThemeRepository themeRepository,
            ThemeSlotRepository themeSlotRepository, MemberRepository memberRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.timeRepository = timeRepository;
        this.themeRepository = themeRepository;
        this.themeSlotRepository = themeSlotRepository;
        this.memberRepository = memberRepository;
    }

    public List<Reservation> allReservations() {
        return reservationRepository.findAll();
    }

    @Transactional
    public Reservation saveReservation(Long memberId, LocalDate date, Long reservationTimeId, Long themeId) {
        validateBeforeDate(date);
        validateIsExistBy(date, reservationTimeId, themeId);

        Theme theme = getThemeOrElseThrow(themeId);
        Time time = getTimeOrElseThrow(reservationTimeId);
        validateDateTime(date, time);

        Member member = getMemberOrElseThrow(memberId);

        Reservation reservation = reservationRepository.save(new Reservation(member, date, time, theme));
        themeSlotRepository.update(new ThemeSlot(theme, date, time, true));
        return reservation;
    }

    @Transactional
    public void removeReservation(long reservationId) {
        Reservation reservation = getReservationOrElseThrow(reservationId);
        reservationRepository.deleteById(reservationId);
        themeSlotRepository.update(new ThemeSlot(reservation.getTheme(), reservation.getDate(), reservation.getTime(), false));
    }

    public Reservation findReservation(long reservationId) {
        return getReservationOrElseThrow(reservationId);
    }

    public List<Reservation> findReservationBy(Long memberId) {
        return reservationRepository.findByMemberId(memberId);
    }

    @Transactional
    public void cancelReservation(Long reservationId) {
        Reservation reservation = getReservationOrElseThrow(reservationId);
        reservation.cancel();
        reservationRepository.updateStatus(reservation);
        themeSlotRepository.update(new ThemeSlot(reservation.getTheme(), reservation.getDate(), reservation.getTime(), false));
    }

    @Transactional
    public Reservation modifyReservation(Long reservationId, LocalDate date, Long timeId, Long themeId) {
        Time time = getTimeOrElseThrow(timeId);
        Theme theme = getThemeOrElseThrow(themeId);
        Reservation reservation = getReservationOrElseThrow(reservationId);

        validateIsExistBy(date, timeId, themeId);
        validateDateTime(date, time);

        Reservation updateReservation = new Reservation(
                reservationId,
                reservation.getMember(),
                date,
                time,
                theme,
                reservation.getReservationStatus()
        );
        reservationRepository.updateDateAndTimeAndTheme(updateReservation);
        return updateReservation;
    }

    @NonNull
    private Theme getThemeOrElseThrow(Long themeId) {
        return themeRepository.findById(themeId)
                .orElseThrow(() -> new CustomException(ErrorCode.THEME_NOT_FOUND));
    }

    @NonNull
    private Time getTimeOrElseThrow(Long reservationTimeId) {
        return timeRepository.findById(reservationTimeId)
                .orElseThrow(() -> new CustomException(ErrorCode.TIME_NOT_FOUND));
    }

    @NonNull
    private Reservation getReservationOrElseThrow(long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESERVATION_NOT_FOUND));
    }

    @NonNull
    private Member getMemberOrElseThrow(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
    }

    private void validateBeforeDate(LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new CustomException(ErrorCode.RESERVATION_NOT_ALLOWED_DATE);
        }
    }

    private void validateIsExistBy(LocalDate date, Long reservationTimeId, Long themeId) {
        if (reservationRepository.isExistBy(themeId, date, reservationTimeId)) {
            throw new CustomException(ErrorCode.RESERVATION_ALREADY_EXIST);
        }
    }

    private void validateDateTime(LocalDate date, Time time) {
        if (date.equals(LocalDate.now()) && time.isBefore(LocalTime.now())) {
            throw new CustomException(ErrorCode.RESERVATION_TIME_OUT);
        }
    }
}
