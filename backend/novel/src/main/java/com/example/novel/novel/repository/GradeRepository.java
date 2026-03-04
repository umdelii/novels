package com.example.novel.novel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.novel.novel.entity.Grade;

public interface GradeRepository extends JpaRepository<Grade, Long> {

    @Modifying
    @Query("DELETE FROM Grade g WHERE g.novel.id = :id")
    void deleteByNovel(Long id);
}
