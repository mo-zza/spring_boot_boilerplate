package com.mozza.springboilerplate.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mozza.springboilerplate.auth.constant.MemberRole;
import com.mozza.springboilerplate.auth.jwt.JwtFilter;
import com.mozza.springboilerplate.domain.member.dto.MemberResponse;
import com.mozza.springboilerplate.domain.member.dto.MemberResult;
import com.mozza.springboilerplate.domain.member.entity.Member;
import com.mozza.springboilerplate.domain.payment.constant.PaymentMethod;
import com.mozza.springboilerplate.domain.payment.dto.PaymentRequest;
import com.mozza.springboilerplate.domain.payment.dto.PaymentResult;
import com.mozza.springboilerplate.domain.payment.dto.PaymentsWithCountResponse;
import com.mozza.springboilerplate.domain.payment.entity.Payment;
import com.mozza.springboilerplate.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.UUID;

import static com.mozza.springboilerplate.utils.ApiDocumentUtils.getDocumentRequest;
import static com.mozza.springboilerplate.utils.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs(uriScheme = "https", uriHost = "docs.api.com", uriPort = 443)
@WebMvcTest(value = MemberController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtFilter.class))
@ExtendWith({SpringExtension.class, MockitoExtension.class})
public class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    HttpHeaders headers;

    @BeforeEach
    public void setUpHeaderSpec() {
        headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer Token");
    }

    @Nested
    public class GetMember {
        @Test
        public void shouldBeReturnMemberResponse() throws Exception {
            // given
            Member member = getMemberEntity();
            MemberResponse response = getMemberResponse(member);
            given(memberService.getMemberById(any(UUID.class))).willReturn(response);

            // when
            ResultActions result = mockMvc.perform(get("/members")
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(user(member.getId().toString())));

            // then
            result.andExpect(status().isOk())
                    .andDo(document("get-member",
                            getDocumentRequest(),
                            getDocumentResponse(),
                            requestHeaders(
                                    headerWithName("Authorization").description("Bearer Token")),
                            responseFields(
                                    fieldWithPath("id").type(JsonFieldType.STRING).description("회원 아이디"),
                                    fieldWithPath("name").type(JsonFieldType.STRING).description("회원 이름"),
                                    fieldWithPath("email").type(JsonFieldType.STRING).description("회원 이메일"),
                                    fieldWithPath("phone_number").type(JsonFieldType.STRING).description("회원 전화번호"),
                                    fieldWithPath("payments").type(JsonFieldType.ARRAY).description("회원 결제내역"),
                                    fieldWithPath("created_date").type(JsonFieldType.STRING).description("회원 생성일"),
                                    fieldWithPath("modified_date").type(JsonFieldType.STRING).description("회원 수정일")
                            )));
        }
    }

    @Nested
    public class GetPaymentWithCount {
        @Test
        public void shouldResponsePaymentsWithCount() throws Exception {
            // given
            Member member = getMemberEntity();
            Payment payment = getPaymentEntity(member);
            PaymentResult paymentResult = PaymentResult.fromEntity(payment);
            List<PaymentResult> payments = List.of(paymentResult);
            PaymentsWithCountResponse response = PaymentsWithCountResponse.fromResultAndCount(payments, 1L);
            given(memberService.getPaymentWithCountByMemberId(any(UUID.class))).willReturn(response);

            // when
            ResultActions result = mockMvc.perform(get("/members/payments")
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .with(user(member.getId().toString())));

            // then
            result.andExpect(status().isOk())
                    .andDo(document("get-member-payments-with-count",
                            getDocumentRequest(),
                            getDocumentResponse(),
                            requestHeaders(
                                    headerWithName("Authorization").description("Bearer Token")),
                            responseFields(
                                    fieldWithPath("payments[]").type(JsonFieldType.ARRAY).description("결제내역"),
                                    fieldWithPath("payments[].id").type(JsonFieldType.STRING).description("결제내역 아이디"),
                                    fieldWithPath("payments[].method").type(JsonFieldType.STRING).description("결제 수단"),
                                    fieldWithPath("payments[].card_number").type(JsonFieldType.STRING).description("결제 카드번호"),
                                    fieldWithPath("payments[].card_expiration_date").type(JsonFieldType.STRING).description("결제 카드 만료일"),
                                    fieldWithPath("payments[].created_date").type(JsonFieldType.STRING).description("결제 생성일"),
                                    fieldWithPath("payments[].modified_date").type(JsonFieldType.STRING).description("결제 수정일"),
                                    fieldWithPath("count").type(JsonFieldType.NUMBER).description("결제내역 개수")
                            )));
        }
    }

    private Member getMemberEntity() {
        return Member.builder()
                .name("test")
                .email("test@dev.com")
                .role(MemberRole.USER)
                .phoneNumber("000-0000-0000")
                .password("password")
                .build();
    }

    private Payment getPaymentEntity(Member member) {
        return Payment.builder()
                .method(PaymentMethod.CARD)
                .member(member)
                .cardNumber("1234-1234-1234-1234")
                .cardExpirationDate("12/34")
                .build();
    }

    private MemberResponse getMemberResponse(Member member) {
        MemberResult memberResult = MemberResult.fromEntity(member);
        return MemberResponse.fromResult(memberResult);
    }

}
