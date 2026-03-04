package com.example.novel.member.service;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.novel.member.dto.MemberDTO;
import com.example.novel.member.dto.RegisterDTO;
import com.example.novel.member.entity.Member;
import com.example.novel.member.entity.constant.MemberRole;
import com.example.novel.member.repository.MemberRepository;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
@Data
public class NovelMemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("novel service username : {}", username);

        Member member = memberRepository.findByEmailAndFromSocial(username, false)
                .orElseThrow(() -> new UsernameNotFoundException("메일 확인"));

        MemberDTO dto = new MemberDTO(member.getEmail(), member.getPassword(),
                member.getNickname(), member.isFromSocial(),
                member.getRoles().stream().map(role -> role.name()).collect(Collectors.toList()));

        dto.setNickname(member.getNickname());

        return dto;
    }

    public void register(RegisterDTO dto) throws IllegalStateException {
        Optional<Member> result = memberRepository.findById(dto.getEmail());
        if (result.isPresent()) {
            throw new IllegalStateException("이미 등록된 회원입니다");
        }

        Member member = Member.builder()
                .email(dto.getEmail())
                .nickname(dto.getNickname())
                .fromSocial(false)
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();

        member.addRole(MemberRole.MEMBER);
        memberRepository.save(member);

    }

}
