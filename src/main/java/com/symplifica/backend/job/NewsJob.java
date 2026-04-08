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

@Component
public class NewsJob {

    private final RssService rssService;
    private final LlmService llmService;
    private final EmailService emailService;
    private final NewsLogRepository newsLogRepository;

    private static final String RSS_URL = "http://portafolio.co/rss/tendencias/entretenimiento.xml";
    private static final String NOTIFY_EMAIL = "admin@symplifica.test";

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
        System.out.println("Comenzando el job de noticias RSS...");
        
        try {
            // 1. Consume RSS
            List<String> titles = rssService.fetchLatestNewsTitles(RSS_URL);
            
            if (titles.isEmpty()) {
                logEntry.setStatus("SKIPPED");
                logEntry.setSummary("No se encontraron noticias en el RSS.");
                newsLogRepository.save(logEntry);
                return;
            }

            // 2. Resumir con LLM
            String summary = llmService.summarizeNews(titles);

            // 3. Enviar Email
            emailService.sendNewsSummaryEmail(NOTIFY_EMAIL, summary);

            // 4. Guardar Log Exitoso
            logEntry.setStatus("SUCCESS");
            logEntry.setSummary("Se procesaron " + titles.size() + " noticias. Resumen generado y enviado correctamente.");
            
        } catch (Exception e) {
            logEntry.setStatus("FAILED");
            logEntry.setErrorMessage(e.getMessage());
            System.err.println("Error en el NewsJob: " + e.getMessage());
            throw e; // Lanza el error para que JobRunr o Spring lo maneje/vuelva a intentar
        } finally {
            logEntry.setExecutionTime(LocalDateTime.now());
            newsLogRepository.save(logEntry);
        }
    }
}
