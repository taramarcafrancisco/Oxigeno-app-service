package com.oxigeno.portal.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Component
public class LoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);
    private static final int MAX_LOG_CHARS = 10_000;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        log.info("➡️ {} {} desde {}",
                request.getMethod(),
                request.getRequestURI(),
                request.getRemoteAddr()
        );

        String uri = request.getRequestURI();
        boolean isUnstructured = (uri != null && uri.startsWith("/api/query/unstructured"));

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);
        } finally {
            try {
                // ✅ Loguear BODY solo para métodos que tienen body
                String method = request.getMethod();
                if ("POST".equalsIgnoreCase(method) || "PUT".equalsIgnoreCase(method) || "PATCH".equalsIgnoreCase(method)) {
                    String requestBody = readBodySafely(
                            wrappedRequest.getContentAsByteArray(),
                            resolveCharset(wrappedRequest.getCharacterEncoding())
                    );
                    log.info("📥 Request Body: {}", requestBody);
                }

                // ✅ Response body: si querés, podés saltearlo SOLO para este endpoint (por tamaño)
                String responseBody = readBodySafely(
                        wrappedResponse.getContentAsByteArray(),
                        resolveCharset(wrappedResponse.getCharacterEncoding())
                );

                log.info("📤 Response {} Body: {}", wrappedResponse.getStatus(), responseBody);


            } catch (Exception e) {
                log.warn("LoggingFilter failed to log body: {}", e.toString());
            } finally {
                wrappedResponse.copyBodyToResponse();
            }
        }
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private Charset resolveCharset(String enc) {
        if (isBlank(enc)) return StandardCharsets.UTF_8;
        try { return Charset.forName(enc); }
        catch (Exception ignored) { return StandardCharsets.UTF_8; }
    }

    private String readBodySafely(byte[] bodyBytes, Charset charset) {
        if (bodyBytes == null || bodyBytes.length == 0) return "<empty>";
        String s = new String(bodyBytes, charset).replace("\r", "").trim();
        if (s.isEmpty()) return "<empty>";
        if (s.length() > MAX_LOG_CHARS) return s.substring(0, MAX_LOG_CHARS) + "... <truncated>";
        return s;
    }
}
