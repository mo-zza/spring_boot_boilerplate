package com.mozza.springboilerplate.controller;

import com.mozza.springboilerplate.auth.dto.TokenResponse;
import com.mozza.springboilerplate.domain.member.dto.MemberRequest;
import com.mozza.springboilerplate.domain.member.dto.SignInRequest;
import com.mozza.springboilerplate.service.AuthService;
import com.mozza.springboilerplate.util.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/sign-up")
    public ResponseEntity<TokenResponse> signUpForUser(
            @RequestBody @Valid MemberRequest request
    ) {
        TokenResponse response = authService.registerUser(request);
        return Response.created(response);
    }

    @PostMapping("/sign-up/admin")
    public ResponseEntity<TokenResponse> signUpForAdmin(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid MemberRequest request
    ) {
        UUID memberId = UUID.fromString(userDetails.getUsername());
        TokenResponse response = authService.registerAdminByMemberId(request, memberId);
        return Response.created(response);
    }

    @PostMapping("/sign-up/dev")
    public ResponseEntity<TokenResponse> signUpForDev(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody @Valid MemberRequest request
    ) {
        UUID memberId = UUID.fromString(userDetails.getUsername());
        TokenResponse response = authService.registerDevByMemberId(request, memberId);
        return Response.created(response);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<TokenResponse> signIn(
            @RequestBody @Valid SignInRequest request
    ) {
        TokenResponse response = authService.validateMember(request);
        return Response.ok(response);
    }
}
