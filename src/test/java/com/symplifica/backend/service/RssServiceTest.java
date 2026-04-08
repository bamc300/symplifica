package com.symplifica.backend.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import com.symplifica.backend.service.impl.RssServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RssServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private RssService rssService;

    @BeforeEach
    void setUp() {
        rssService = new RssServiceImpl(restTemplate);
    }

    @Test
    void shouldExtractTitlesFromValidRss() {
        String mockXml = "<?xml version=\"1.0\"?><rss version=\"2.0\"><channel>" +
                "<item><title>Noticia 1</title></item>" +
                "<item><title>Noticia 2</title></item>" +
                "</channel></rss>";
                
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(mockXml);

        List<String> titles = rssService.fetchLatestNewsTitles("http://test.url");

        assertEquals(2, titles.size());
        assertEquals("Noticia 1", titles.get(0));
        assertEquals("Noticia 2", titles.get(1));
    }

    @Test
    void shouldReturnEmptyListWhenXmlIsEmpty() {
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn("");

        List<String> titles = rssService.fetchLatestNewsTitles("http://test.url");

        assertTrue(titles.isEmpty());
    }

    @Test
    void shouldReturnEmptyListWhenXmlIsInvalid() {
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn("<test>");

        List<String> titles = rssService.fetchLatestNewsTitles("http://test.url");

        assertTrue(titles.isEmpty());
    }
}
