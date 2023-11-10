package com.sample.sample.auth;


import com.sample.sample.account.entity.Member;
import com.sample.sample.account.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;


// Custom

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;


    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username).orElseThrow(EntityNotFoundException::new);
        return new CustomUserDetails(member);
    }
}
