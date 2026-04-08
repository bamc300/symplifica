package com.symplifica.backend.controller;

import com.symplifica.backend.entity.NewsLog;
import com.symplifica.backend.repository.NewsLogRepository;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class NewsLogController {

    private final NewsLogRepository newsLogRepository;

    public NewsLogController(NewsLogRepository newsLogRepository) {
        this.newsLogRepository = newsLogRepository;
    }

    @GetMapping
    public List<NewsLog> getAllLogs() {
        return newsLogRepository.findAll(Sort.by(Sort.Direction.DESC, "executionTime"));
    }
}
