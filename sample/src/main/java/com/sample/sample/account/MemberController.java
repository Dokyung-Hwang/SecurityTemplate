package com.sample.sample.account;


import com.sample.sample.account.dto.MemberDto;
import com.sample.sample.account.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/v1/members")
@RequiredArgsConstructor
public class MemberController {
    private final static String DEFAULT_URI = "/v1/members";
    private final MemberService memberService;

    @PostMapping()
    public ResponseEntity<?> signUp(@RequestBody MemberDto memberDto) {
        long memberId = memberService.createMember(memberDto);

        URI location =  UriComponentsBuilder
                .newInstance()
                .path(DEFAULT_URI + "/{resource-memberId}")
                .buildAndExpand(memberId)
                .toUri();

        return ResponseEntity.created(location).build();
    }
}
