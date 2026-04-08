package com.symplifica.backend.service;

import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LlmService {

    private final ChatLanguageModel chatLanguageModel;

    public LlmService(ChatLanguageModel chatLanguageModel) {
        this.chatLanguageModel = chatLanguageModel;
    }

    public String summarizeNews(List<String> newsTitles) {
        if (newsTitles == null || newsTitles.isEmpty()) {
            return "No hay noticias relevantes para resumir.";
        }

        String prompt = "Resume las siguientes noticias de entretenimiento en máximo 3 párrafos en español, " +
                "destacando los puntos más relevantes:\n\n";
        prompt += String.join("\n", newsTitles);

        try {
            return chatLanguageModel.generate(prompt);
        } catch (Exception e) {
            System.err.println("Error calling Ollama LLM: " + e.getMessage());
            return "Ocurrió un error al generar el resumen de las noticias con el proveedor LLM.";
        }
    }
}
