package com.example.novel.novel.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.novel.ai.domain.response.AiDescriptionDTO;
import com.example.novel.novel.dto.NovelDTO;
import com.example.novel.novel.dto.PageRequestDTO;
import com.example.novel.novel.dto.PageResultDTO;
import com.example.novel.novel.service.NovelService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Response Novels", description = "Response Nevel API")
@RequestMapping("/api/novels")
@RequiredArgsConstructor
@Log4j2
@RestController
public class NovelController {
    // /api/novels/1 + GET : 상세조회

    private final NovelService novelService;

    @Operation(summary = "novel 상세조회", description = "novel 상세 조회 API")
    @GetMapping("/{id}")
    public NovelDTO getRow(
            @Parameter(description = "novel id 값", example = "1", required = true) @PathVariable Long id) {
        log.info("novel 요청 {}", id);
        NovelDTO dto = novelService.getRow(id);
        return dto;
    }

    // /api/novels + GET : 전체조회
    @Operation(summary = "novel 전체 조회", description = "novel 전체 조회 API")
    @GetMapping("")
    public PageResultDTO<NovelDTO> getRows(PageRequestDTO dto) {
        log.info("novel 리스트 요청 {}", dto);

        PageResultDTO<NovelDTO> result = novelService.getList(dto);

        return result;
    }

    // /api/novels/add + POST : 추가
    @Operation(summary = "novel 추가", description = "novel 추가 조회 API")
    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ROLE_MEMBER','ROLE_ADMIN')")
    public Long postRow(@RequestBody NovelDTO dto) {
        log.info("novel 추가 요청 {}", dto);

        return novelService.create(dto);
    }

    // /api/novels/available/1 + PUT: 수정
    @Operation(summary = "novel 수정", description = "novel 수정 API - 이용 가능, 장르 변경")
    @PutMapping("/available/{id}")
    public Long putRow(@PathVariable Long id, @RequestBody NovelDTO dto) {
        log.info("novel 수정 요청 {} {}", id, dto);

        dto.setId(id);

        return novelService.updateAvailable(dto);
    }

    // /api/novels/edit/1 + PUT: 수정 장르변경
    @PutMapping("/edit/{id}")
    public Long putRow2(@PathVariable Long id, @RequestBody NovelDTO dto) {
        log.info("row 수정 {} {}", id, dto);

        dto.setId(id);
        return novelService.update(dto);
    }

    // /api/novels/1 + DELETE: 삭제
    @Operation(summary = "novel 삭제", description = "novel 삭제 API")
    @DeleteMapping("/{id}")
    public String deleteRow(@PathVariable Long id) {
        log.info("novel 삭제 요청 {}", id);
        novelService.delete(id);
        return "success";
    }

    // ai 소개글 생성
    @Operation(summary = "novel ai 소개글 생성", description = "novel ai 소개글 생성 API")
    @GetMapping("/ai-description/{id}")
    public AiDescriptionDTO getAiDescription(@PathVariable Long id) {
        log.info("novel ai 소개글 요청 {}", id);
        return novelService.generateAiDescription(id);
    }

}
