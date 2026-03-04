package com.example.novel.novel.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString(exclude = "genre")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Novel extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private boolean available;

    @Column(nullable = false)
    private LocalDate publishedDate;

    @Column(length = 2000)
    private String plot;

    @Column(length = 2000)
    private String aiDescription; // ai 생성 소개글

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "genre_id")
    private Genre genre;

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public void setAiDescription(String aiDescription) {
        this.aiDescription = aiDescription;
    }
}
