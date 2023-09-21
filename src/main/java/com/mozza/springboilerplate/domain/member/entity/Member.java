package com.mozza.springboilerplate.domain.member.entity;

import com.mozza.springboilerplate.auth.constant.MemberRole;
import com.mozza.springboilerplate.common.entity.BaseEntity;
import com.mozza.springboilerplate.domain.payment.entity.Payment;
import com.mozza.springboilerplate.util.Crypto;
import jakarta.persistence.*;
import lombok.*;

import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@Table(indexes = {
        @Index(name = "idx_member_id", columnList = "id", unique = true),
        @Index(name = "idx_member_email", columnList = "email", unique = true),
        @Index(name = "idx_member_phone_number", columnList = "phoneNumber", unique = true),
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {
    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(255) default 'USER'")
    private MemberRole role;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String phoneNumber;

    @Column(unique = true, nullable = false)
    private String salt;

    @Column(unique = true, nullable = false)
    private String encryptedPassword;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private Set<Payment> payments = new HashSet<>();

    @Builder
    private Member(@NonNull String name, @NonNull MemberRole role, @NonNull String email,
                  @NonNull String phoneNumber, @NonNull String password) {
        this.name = name;
        this.role = role;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.salt = Crypto.getSalt();
        this.encryptedPassword = getEncryptValue(password, this.salt);
    }

    public Member addPayment(Payment payment) {
        this.payments.add(payment);
        return this;
    }

    public boolean validatePassword(String password) throws NoSuchAlgorithmException {
        if (!Crypto.encrypt(password, this.salt).equals(this.encryptedPassword)) {
            return false;
        };
        return true;
    }

    public static String getEncryptValue(String password, String salt) {
        try {
            return Crypto.encrypt(password, salt);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("암호화에 실패했습니다.");
        }
    }
}
