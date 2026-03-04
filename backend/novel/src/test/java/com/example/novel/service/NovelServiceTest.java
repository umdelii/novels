package com.example.novel.service;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import com.example.novel.novel.dto.NovelDTO;
import com.example.novel.novel.service.NovelService;

@SpringBootTest
@Transactional
public class NovelServiceTest {
    @Autowired
    private NovelService novelService;

    @Test
    @Commit
    public void testGenerateAiDescription() {
        NovelDTO dto = NovelDTO.builder()
                .title("사랑하기 때문에")
                .author("기욤 뮈소")
                .available(true)
                .publishedDate(LocalDate.of(2024, 5, 27))
                .plot("""
                        기욤 뮈소의 소설 《사랑하기 때문에》는 4살 딸 라일라를 잃고 슬픔 속에 살던 마크가 5년 후,
                        기적적으로 다시 찾은 딸과 함께 겪는 미스터리하고 따뜻한 사랑과 치유의 이야기를 다룬 작품입니다.
                        상처받은 세 명의 주인공(마크, 앨리슨, 에비)이 과거의 트라우마를 극복하는 과정과 패자부활전을 감동적으로 그려냈습니다.
                        """)
                .gid(5L)
                .genreName("Romance")
                .rating(5)
                .build();
        Long id = novelService.create(dto);
        // novelService.generateAiDescription(id);
    }
}
