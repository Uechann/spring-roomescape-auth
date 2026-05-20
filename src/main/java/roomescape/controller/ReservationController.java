package roomescape.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import roomescape.auth.LoginRequired;
import roomescape.auth.LoginUser;
import roomescape.controller.dto.auth.LoginMember;
import roomescape.controller.dto.reservation.ReservationModifyRequest;
import roomescape.controller.dto.reservation.ReservationRequest;
import roomescape.controller.dto.reservation.ReservationResponse;
import roomescape.domain.reservation.Reservation;
import roomescape.service.ReservationService;

import java.util.List;

@RestController
@RequestMapping("/reservations")
@Validated
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    @LoginRequired
    public ResponseEntity<ReservationResponse> createReservation(
            @LoginUser LoginMember loginMember,
            @Valid @RequestBody ReservationRequest reservationRequest) {
        Reservation reservation = reservationService.saveReservation(
                loginMember.getId(),
                reservationRequest.date(),
                reservationRequest.timeId(),
                reservationRequest.themeId()
        );
        ReservationResponse reservationResponse = ReservationResponse.from(reservation);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationResponse);
    }

    @GetMapping
    @LoginRequired
    public ResponseEntity<List<ReservationResponse>> getReservations() {
        return ResponseEntity.ok(convertToReservationResponse(reservationService.allReservations()));
    }

    @GetMapping("/my")
    @LoginRequired
    public ResponseEntity<List<ReservationResponse>> getMyReservations(
            @LoginUser LoginMember loginMember
    ) {
        return ResponseEntity.ok(convertToReservationResponse(reservationService.findReservationBy(loginMember.getId())));
    }

    @PatchMapping("/{reservationId}/cancel")
    @LoginRequired
    public ResponseEntity<Void> cancelMyReservation(@PathVariable Long reservationId) {
        reservationService.cancelReservation(reservationId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{reservationId}")
    @LoginRequired
    public ResponseEntity<ReservationResponse> modifyMyReservation(
            @PathVariable Long reservationId,
            @Valid @RequestBody ReservationModifyRequest request
    ) {
        Reservation reservation = reservationService.modifyReservation(reservationId, request.date(), request.timeId(), request.themeId());
        return ResponseEntity.ok().body(ReservationResponse.from(reservation));
    }

    @DeleteMapping("/{id}")
    @LoginRequired
    public ResponseEntity<Void> deleteReservation(@PathVariable long id) {
        reservationService.removeReservation(id);
        return ResponseEntity.noContent().build();
    }

    private List<ReservationResponse> convertToReservationResponse(List<Reservation> reservations) {
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }
}
