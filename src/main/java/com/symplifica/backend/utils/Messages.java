package com.symplifica.backend.utils;

public final class Messages {

    private Messages() {
        // Prevent instantiation
    }

    // Job Messages
    public static final String MSG_JOB_STARTING = "Comenzando el job de noticias RSS...";
    public static final String MSG_JOB_SKIPPED_NO_NEWS = "No se encontraron noticias en el RSS.";
    public static final String MSG_JOB_SUCCESS(int size) {
        return "Se procesaron " + size + " noticias. Resumen generado y enviado correctamente.";
    }
    public static final String MSG_JOB_ERROR = "Error en el NewsJob: ";

    // LLM Messages
    public static final String MSG_LLM_NO_NEWS = "No hay noticias relevantes para resumir.";
    public static final String MSG_LLM_PROMPT = "Resume las siguientes noticias de entretenimiento en máximo 3 párrafos en español, destacando los puntos más relevantes:\n\n";
    public static final String MSG_LLM_ERROR_CALLING = "Error calling Ollama LLM: ";
    public static final String MSG_LLM_ERROR_GENERATING = "Ocurrió un error al generar el resumen de las noticias con el proveedor LLM.";

    // RSS Messages
    public static final String MSG_RSS_FETCH_ERROR = "Error fecthing RSS: ";

    // Email Messages
    public static final String MSG_EMAIL_SUBJECT = "Tu Resumen de Noticias Principal - Symplifica";
    public static final String MSG_EMAIL_FALLBACK_HTML(String summary) {
        return "<html><body><h1>Resumen de Noticias</h1><p>" + summary + "</p></body></html>";
    }
    public static final String MSG_EMAIL_SEND_ERROR = "Error enviando email: ";
}
