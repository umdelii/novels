package com.example.novel.member.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.example.novel.member.entity.constant.MemberRole;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class MemberDTO extends User /* implements OAuth2User */ {

    private String email;

    private String password;

    private String nickname;

    private boolean fromSocial;

    private List<String> roles = new ArrayList<>();

    // OAuth2User가 넘겨주는 attr 받기
    private Map<String, Object> attr;

    // extends User
    public MemberDTO(String username, String password, String nickname, boolean fromSocial,
            List<String> roles) {
        super(username, password,
                roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(Collectors.toList()));
        this.email = username; // username을 email로 받았기에
        this.password = password;
        this.nickname = nickname;
        this.fromSocial = fromSocial;
        this.roles = roles;
    }

    public Map<String, Object> getClaims() {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("email", email);
        dataMap.put("password", password);
        dataMap.put("nickname", nickname);
        dataMap.put("social", fromSocial);
        dataMap.put("roles", roles);

        return dataMap;
    }

    // // Oauth2User
    // public MemberDTO(String username, String password, String name, String
    // nickname, boolean fromSocial,
    // Collection<? extends GrantedAuthority> authorities, Map<String, Object> attr)
    // {
    // this(username, password, nickname, fromSocial, authorities);
    // this.attr = attr;
    // }

    // // social login (Oauth2User)
    // @Override
    // public Map<String, Object> getAttributes() {
    // return this.attr;
    // }
}
