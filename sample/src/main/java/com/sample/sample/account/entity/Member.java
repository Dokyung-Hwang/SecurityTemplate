package com.sample.sample.account.entity;

import com.sample.sample.account.constants.Authority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    private String email;

    private String password;

    @Enumerated(value = EnumType.STRING)
    private Authority authority;

//    public static Account createMember(String email, String password, String nickname) {
//        return Account.builder()
//                .email(email)
//                .password(password)
//                .nickname(nickname)
//                .authority(Authority.ROLE_USER)
//                .build();
//    }
}
