package cowin.appointment.finder.config;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

@Configuration
public class RestTemplateConfig {

    @Value("${rest.template.http.max.idle:50}")
    private int httpMaxIdle;
    @Value("${rest.template.http.keep.alive:30}")
    private int httpKeepAlive;
    @Value("${rest.template.http.connection.timeout:20}")
    private int httpConnectionTimeout;

    @Bean
    public RestTemplate okhttp3Template() {
        RestTemplate restTemplate = new RestTemplate();

        // create the okhttp client builder
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        ConnectionPool okHttpConnectionPool = new ConnectionPool(httpMaxIdle, httpKeepAlive,
                TimeUnit.SECONDS);
        builder.connectionPool(okHttpConnectionPool);
        builder.connectTimeout(httpConnectionTimeout, TimeUnit.SECONDS);
        builder.retryOnConnectionFailure(false);

        // embed the created okhttp client to a spring rest template
        restTemplate.setRequestFactory(new OkHttp3ClientHttpRequestFactory(builder.build()));

        return restTemplate;
    }

}
