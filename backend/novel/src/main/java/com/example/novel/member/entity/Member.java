package com.example.novel.member.entity;

import java.util.HashSet;
import java.util.Set;

import com.example.novel.member.entity.constant.MemberRole;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Table(name = "novel_member")
public class Member {

    @Id
    private String email;

    @Column(nullable = false)
    private String password;

    private String nickname;

    private boolean fromSocial;

    @ElementCollection(fetch = FetchType.LAZY)
    @Builder.Default
    private Set<MemberRole> roles = new HashSet<>();

    public void addRole(MemberRole role) {
        roles.add(role);
    }

    public void clearRole(MemberRole role) {
        roles.clear();
    }
}
