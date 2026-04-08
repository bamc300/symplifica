package com.symplifica.backend.service;

import java.util.List;

public interface RssService {
    List<String> fetchLatestNewsTitles(String url);
}
