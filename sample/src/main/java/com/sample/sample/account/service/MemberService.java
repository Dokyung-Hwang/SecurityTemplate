package com.sample.sample.account.service;


import com.sample.sample.account.constants.Authority;
import com.sample.sample.account.dto.MemberDto;
import com.sample.sample.account.entity.Member;
import com.sample.sample.account.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Long createMember(MemberDto memberDto) {
        Member member = Member.builder()
                .email(memberDto.getEmail())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .authority(Authority.ROLE_USER)
                .build();

        return memberRepository.save(member).getMemberId();
    }
}
