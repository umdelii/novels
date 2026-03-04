package com.example.novel.novel.service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.novel.ai.domain.response.AiDescriptionDTO;
import com.example.novel.novel.dto.NovelDTO;
import com.example.novel.novel.dto.PageRequestDTO;
import com.example.novel.novel.dto.PageResultDTO;
import com.example.novel.novel.entity.Genre;
import com.example.novel.novel.entity.Novel;
import com.example.novel.novel.repository.GradeRepository;
import com.example.novel.novel.repository.NovelRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Transactional
@Log4j2
@Service
@RequiredArgsConstructor
public class NovelService {

    private final NovelRepository novelRepository;
    private final GradeRepository gradeRepository;
    private final ChatClient chatClient;

    // CRUD
    public Long create(NovelDTO dto) {

        Novel novel = Novel.builder()
                .author(dto.getAuthor())
                .title(dto.getTitle())
                .publishedDate(dto.getPublishedDate())
                .genre(Genre.builder().id(dto.getGid()).build())
                .available(dto.isAvailable())
                .plot(dto.getPlot())
                .build();

        return novelRepository.save(novel).getId();
    }

    @Transactional(readOnly = true)
    public NovelDTO getRow(Long id) {
        Object[] row = novelRepository.getNovelById(id);
        return entityToDto((Novel) row[0], (Genre) row[1], (Double) row[2]);
    }

    @Transactional(readOnly = true)
    public PageResultDTO<NovelDTO> getList(PageRequestDTO dto) {
        Pageable pageable = PageRequest.of(dto.getPage() - 1, dto.getSize(), Sort.by("id").descending());
        Page<Object[]> result = novelRepository.list(dto.getGenre(), dto.getKeyword(), pageable);

        Function<Object[], NovelDTO> function = en -> entityToDto((Novel) en[0], (Genre) en[1], (Double) en[2]);

        List<NovelDTO> dtoList = result.get().map(function).collect(Collectors.toList());
        long totalCount = result.getTotalElements();

        return PageResultDTO.<NovelDTO>withAll().dtoList(dtoList).pageRequestDTO(dto).totalCount(totalCount).build();
    }

    // available 변경
    public Long updateAvailable(NovelDTO dto) {
        Novel novel = novelRepository.findById(dto.getId()).get();
        novel.setAvailable(dto.isAvailable());
        return novel.getId();
    }

    // available + 장르 변경
    public Long update(NovelDTO dto) {
        Novel novel = novelRepository.findById(dto.getId()).get();
        novel.setAvailable(dto.isAvailable());
        novel.setGenre(Genre.builder().id(dto.getGid()).build());
        return novel.getId();
    }

    public void delete(Long id) {
        // 평점 삭제
        gradeRepository.deleteByNovel(id);

        // 도서 삭제
        novelRepository.deleteById(id);
    }

    // ai
    public AiDescriptionDTO generateAiDescription(Long id) {
        // description이 없는 경우 ai 소개글 작성
        Novel novel = novelRepository.findById(id).get();
        if (novel.getAiDescription() != null && !novel.getAiDescription().isEmpty()) {
            return new AiDescriptionDTO(id, novel.getAiDescription());
        }

        // ai 요청
        // system prompt
        String systemPrompt = """
                당신은 소설 소개글을 작성하는 작가입니다. 프로마케터이기도 하죠.
                입력으로 제공된 정보(도서명, 작가, 장르, 줄거리)만 활용하여 흥미롭고 재밌는 소개글을 작성해주세요.
                줄거리/등장인물/세계관/수상경력 등 제공되지 않은 사실을 사용하지 말 것.
                대신 장르의 매력, 독서 경험, 기대 포인트, 추천 독자를 중심으로 소개문을 작성
                """.stripIndent();
        String userPrompt = """
                도서명: %s
                작가: %s
                장르: %s
                줄거리: %s

                출력 규칙:
                1. 한국어 3~5문장으로 작성
                2. 첫 문장을 훅(흥미 유발)으로 작성
                3. 마지막 문장은 '이런 독자에게 추천:' 으로 시작하는 한 문장
                4. 과장된 표현 금지, 자연스럽게 작성
                """.formatted(novel.getTitle(), novel.getAuthor(), novel.getGenre().getName(), novel.getPlot());
        String aiText = chatClient.prompt().system(systemPrompt).user(userPrompt).call().content();
        novel.setAiDescription(aiText);

        return new AiDescriptionDTO(id, aiText);
    }

    private NovelDTO entityToDto(Novel novel, Genre genre, Double rating) {
        NovelDTO novelDTO = NovelDTO.builder()
                .id(novel.getId())
                .title(novel.getTitle())
                .author(novel.getAuthor())
                .available(novel.isAvailable())
                .publishedDate(novel.getPublishedDate())
                .gid(genre.getId())
                .genreName(genre.getName())
                .rating(rating != null ? rating.intValue() : 0)
                .plot(novel.getPlot())
                .aiDescription(novel.getAiDescription())
                .build();
        return novelDTO;
    }

}