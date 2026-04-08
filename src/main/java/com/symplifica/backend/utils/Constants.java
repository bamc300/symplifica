package com.symplifica.backend.utils;

public final class Constants {

    private Constants() {
        // Prevent instantiation
    }

    public static final String RSS_ENTERTAINMENT_URL = "http://portafolio.co/rss/tendencias/entretenimiento.xml";
    public static final String DEFAULT_ADMIN_EMAIL = "admin@symplifica.test";
    public static final String SENDER_EMAIL = "newsletter@symplifica.com.co";
    
    // Statuses
    public static final String STATUS_SUCCESS = "SUCCESS";
    public static final String STATUS_SKIPPED = "SKIPPED";
    public static final String STATUS_FAILED = "FAILED";

    // Tags & Keys
    public static final String TAG_ITEM = "item";
    public static final String TAG_TITLE = "title";
    public static final String KEY_SUMMARY_CONTENT = "{{SUMMARY_CONTENT}}";
}
