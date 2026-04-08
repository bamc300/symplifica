package com.symplifica.backend.service.impl;

import com.symplifica.backend.service.LlmService;
import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.stereotype.Service;

import java.util.List;

import com.symplifica.backend.utils.Messages;

@Service
public class LlmServiceImpl implements LlmService {

    private final ChatLanguageModel chatLanguageModel;

    public LlmServiceImpl(ChatLanguageModel chatLanguageModel) {
        this.chatLanguageModel = chatLanguageModel;
    }

    @Override
    public String summarizeNews(List<String> newsTitles) {
        if (newsTitles == null || newsTitles.isEmpty()) {
            return Messages.MSG_LLM_NO_NEWS;
        }

        String prompt = Messages.MSG_LLM_PROMPT + String.join("\n", newsTitles);

        try {
            return chatLanguageModel.generate(prompt);
        } catch (Exception e) {
            System.err.println(Messages.MSG_LLM_ERROR_CALLING + e.getMessage());
            return Messages.MSG_LLM_ERROR_GENERATING;
        }
    }
}
