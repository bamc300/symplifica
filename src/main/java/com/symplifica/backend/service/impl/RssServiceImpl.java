package com.symplifica.backend.service.impl;

import com.symplifica.backend.service.RssService;
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

import com.symplifica.backend.utils.Constants;
import com.symplifica.backend.utils.Messages;

@Service
public class RssServiceImpl implements RssService {

    private final RestTemplate restTemplate;

    public RssServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
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

            NodeList itemNodes = doc.getElementsByTagName(Constants.TAG_ITEM);
            for (int i = 0; i < itemNodes.getLength(); i++) {
                Element itemElement = (Element) itemNodes.item(i);
                NodeList titleNodes = itemElement.getElementsByTagName(Constants.TAG_TITLE);
                if (titleNodes.getLength() > 0) {
                    titles.add(titleNodes.item(0).getTextContent().trim());
                }
            }
        } catch (Exception e) {
            System.err.println(Messages.MSG_RSS_FETCH_ERROR + e.getMessage());
        }
        return titles;
    }
}
