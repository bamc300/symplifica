package com.symplifica.backend.job;

import com.symplifica.backend.entity.NewsLog;
import com.symplifica.backend.repository.NewsLogRepository;
import com.symplifica.backend.service.EmailService;
import com.symplifica.backend.service.LlmService;
import com.symplifica.backend.service.RssService;
import org.jobrunr.jobs.annotations.Job;
import org.jobrunr.jobs.annotations.Recurring;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import com.symplifica.backend.utils.Constants;
import com.symplifica.backend.utils.Messages;

@Component
public class NewsJob {

    private final RssService rssService;
    private final LlmService llmService;
    private final EmailService emailService;
    private final NewsLogRepository newsLogRepository;

    public NewsJob(RssService rssService, LlmService llmService, EmailService emailService, NewsLogRepository newsLogRepository) {
        this.rssService = rssService;
        this.llmService = llmService;
        this.emailService = emailService;
        this.newsLogRepository = newsLogRepository;
    }

    @Recurring(id = "daily-news-summary", cron = "0 * * * *")
    @Job(name = "Daily News Summary Workflow")
    public void executeNewsWorkflow() {
        NewsLog logEntry = new NewsLog();
        System.out.println(Messages.MSG_JOB_STARTING);
        
        try {
            // 1. Consume RSS
            List<String> titles = rssService.fetchLatestNewsTitles(Constants.RSS_ENTERTAINMENT_URL);
            
            if (titles.isEmpty()) {
                logEntry.setStatus(Constants.STATUS_SKIPPED);
                logEntry.setSummary(Messages.MSG_JOB_SKIPPED_NO_NEWS);
                newsLogRepository.save(logEntry);
                return;
            }

            // 2. Resumir con LLM
            String summary = llmService.summarizeNews(titles);

            // 3. Enviar Email
            emailService.sendNewsSummaryEmail(Constants.DEFAULT_ADMIN_EMAIL, summary);

            // 4. Guardar Log Exitoso
            logEntry.setStatus(Constants.STATUS_SUCCESS);
            logEntry.setSummary(Messages.MSG_JOB_SUCCESS(titles.size()));
            
        } catch (Exception e) {
            logEntry.setStatus(Constants.STATUS_FAILED);
            logEntry.setErrorMessage(e.getMessage());
            System.err.println(Messages.MSG_JOB_ERROR + e.getMessage());
            throw e; // Lanza el error para que JobRunr o Spring lo maneje/vuelva a intentar
        } finally {
            logEntry.setExecutionTime(LocalDateTime.now());
            newsLogRepository.save(logEntry);
        }
    }
}
