package com.example.novel.repository;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import com.example.novel.member.entity.Member;
import com.example.novel.member.entity.constant.MemberRole;
import com.example.novel.member.repository.MemberRepository;
import com.example.novel.novel.entity.Grade;
import com.example.novel.novel.entity.Novel;
import com.example.novel.novel.repository.GenreRepository;
import com.example.novel.novel.repository.GradeRepository;
import com.example.novel.novel.repository.NovelRepository;

@SpringBootTest
@Transactional
public class NovelRepositoryTest {

    @Autowired
    private NovelRepository novelRepository;
    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @Commit
    public void memberInsertTest() {

        IntStream.rangeClosed(11, 20).forEach(i -> {
            Member member = Member.builder()
                    .email("user" + i + "@gmail.com")
                    .password(passwordEncoder.encode("1111"))
                    .nickname("user" + i)
                    .build();

            member.addRole(MemberRole.MEMBER);

            if (i > 8) {
                member.addRole(MemberRole.ADMIN);
            }

            memberRepository.save(member);
        });
    }

    @Test
    public void gradeInsertTest() {

        IntStream.rangeClosed(1, 50).forEach(i -> {

            long no = (long) (Math.random() * 18) + 1;
            Novel novel = Novel.builder().id(no).build();

            // 평점 임의 생성(1~5)
            int rating = (int) (Math.random() * 5) + 1;
            // member 임의 선택
            int j = (int) (Math.random() * 10) + 1;
            Member member = Member.builder().email("user" + j + "@gmail.com").build();

            Grade grade = Grade.builder().novel(novel).rating(rating).member(member).build();
            gradeRepository.save(grade);
        });
    }

    @Test
    public void readTest() {
        Object[] row = novelRepository.getNovelById(1L);

        System.out.println(row[0]);
        System.out.println(row[1]);
        System.out.println(row[2]);

    }

    @Test
    public void readTest2() {

        Pageable pageable = PageRequest.of(1, 10, Sort.by("id").descending());

        Page<Object[]> result = novelRepository.list(null, null, pageable);

        for (Object[] objects : result) {
            System.out.println(Arrays.toString(objects));
        }

    }

    @Test
    public void memberRead() {
        Optional<Member> result = memberRepository.findByEmailAndFromSocial("user11@gmail.com", false);
        System.out.println(result);
    }
}