package com.mozza.springboilerplate.controller;

import com.mozza.springboilerplate.domain.member.dto.MemberResponse;
import com.mozza.springboilerplate.domain.payment.dto.PaymentRequest;
import com.mozza.springboilerplate.domain.payment.dto.PaymentsWithCountResponse;
import com.mozza.springboilerplate.service.MemberService;
import com.mozza.springboilerplate.util.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<MemberResponse> getMember(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UUID memberId = UUID.fromString(userDetails.getUsername());
        MemberResponse response = memberService.getMemberById(memberId);
        return Response.ok(response);
    }

    @GetMapping("/payments")
    public ResponseEntity<PaymentsWithCountResponse> getPaymentWithCount(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        UUID memberId = UUID.fromString(userDetails.getUsername());
        PaymentsWithCountResponse response = memberService.getPaymentWithCountByMemberId(memberId);
        return Response.ok(response);
    }

    @PutMapping("/payments")
    public ResponseEntity<PaymentsWithCountResponse> addPayment(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid PaymentRequest request
    ) {
        UUID memberId = UUID.fromString(userDetails.getUsername());
        PaymentsWithCountResponse response = memberService.addPaymentByMemberId(request, memberId);
        return Response.ok(response);
    }

    @DeleteMapping("/payments/{paymentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePayment(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable UUID paymentId
    ) {
        UUID memberId = UUID.fromString(userDetails.getUsername());
        memberService.deletePaymentByMemberId(paymentId, memberId);
    }
}
