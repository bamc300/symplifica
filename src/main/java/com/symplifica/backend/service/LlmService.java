package com.symplifica.backend.service;

import java.util.List;

public interface LlmService {
    String summarizeNews(List<String> newsTitles);
}
