package com.example.novel.novel.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NovelDTO {
    // novel_id, author, title, 대여여부, 장르명(+장르아이디), 평점(평균)
    private Long id;
    private String title;
    private String author;
    private boolean available;
    private LocalDate publishedDate;
    private String plot;
    private String aiDescription;

    private Long gid; // 장르 아이디
    private String genreName; // 장르명

    private Integer rating; // 평점

    private String email;
}