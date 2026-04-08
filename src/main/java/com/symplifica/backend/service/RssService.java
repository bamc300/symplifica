package com.symplifica.backend.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class RssService {

    private final RestTemplate restTemplate;

    public RssService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<String> fetchLatestNewsTitles(String url) {
        List<String> titles = new ArrayList<>();
        try {
            String xmlContent = restTemplate.getForObject(url, String.class);
            if (xmlContent == null || xmlContent.trim().isEmpty()) {
                return titles;
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlContent)));

            NodeList itemNodes = doc.getElementsByTagName("item");
            for (int i = 0; i < itemNodes.getLength(); i++) {
                Element itemElement = (Element) itemNodes.item(i);
                NodeList titleNodes = itemElement.getElementsByTagName("title");
                if (titleNodes.getLength() > 0) {
                    titles.add(titleNodes.item(0).getTextContent().trim());
                }
            }
        } catch (Exception e) {
            // Log error, and potentially throw a custom exception
            System.err.println("Error fecthing RSS: " + e.getMessage());
        }
        return titles;
    }
}
