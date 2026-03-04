package com.example.novel.novel.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.novel.novel.entity.Novel;

public interface NovelRepository extends JpaRepository<Novel, Long>, SearchNovelRepository {

}
