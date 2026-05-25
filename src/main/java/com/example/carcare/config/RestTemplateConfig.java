package com.example.carcare.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

@Slf4j
@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() throws NoSuchAlgorithmException, KeyManagementException {

        // Solo desarrollo: desactiva verificación SSL para APIs externas con certificados no reconocidos
        // Eliminar esta llamada antes de desplegar en producción
        configurarSslDev();

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(10_000);  // 10 s conexión
        factory.setReadTimeout(25_000);     // 25 s lectura (la API de estaciones es lenta)

        RestTemplate restTemplate = new RestTemplate(factory);

        // Forzar UTF-8 en todos los conversores (campos acentuados del JSON MINETUR)
        restTemplate.getMessageConverters().forEach(converter -> {
            if (converter instanceof MappingJackson2HttpMessageConverter json) {
                json.setDefaultCharset(StandardCharsets.UTF_8);
            }
            if (converter instanceof StringHttpMessageConverter str) {
                str.setDefaultCharset(StandardCharsets.UTF_8);
            }
        });

        return restTemplate;
    }

    private void configurarSslDev() throws NoSuchAlgorithmException, KeyManagementException {
        log.warn("============================================================");
        log.warn("  ⚠  SSL VERIFICATION DISABLED — modo desarrollo");
        log.warn("  No usar en producción. Ver RestTemplateConfig#configurarSslDev");
        log.warn("============================================================");

        TrustManager[] aceptarTodo = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                    // sin validación — solo desarrollo
                }
                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                    // sin validación — solo desarrollo
                }
            }
        };

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, aceptarTodo, new SecureRandom());

        // Aplica a todas las conexiones HTTPS de la JVM, incluyendo RestTemplate
        SSLContext.setDefault(sslContext);
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
    }
}
