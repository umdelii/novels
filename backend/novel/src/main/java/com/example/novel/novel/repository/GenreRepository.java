package com.example.novel.novel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.novel.novel.entity.Genre;

public interface GenreRepository extends JpaRepository<Genre, Long> {

}
