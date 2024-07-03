package com.acer.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

// es连接配置

@Configuration
@Component
public class EastElasticsearchConfig {

    @Value("${east-es.ip}")
    private String esIp;

    @Value("${east-es.es-port}")
    private Integer port;

    @Value("${east-es.user-name}")
    private String userName;

    @Value("${east-es.password}")
    private String password;


    @Bean
    public RestHighLevelClient client() {
        final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(this.userName, this.password));

        RestClientBuilder builder = RestClient.builder(new HttpHost(this.esIp, this.port, "http"))
                .setHttpClientConfigCallback(httpClientBuilder -> {
                    httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    httpClientBuilder.setDefaultIOReactorConfig(IOReactorConfig.custom()
                            .setIoThreadCount(Runtime.getRuntime().availableProcessors())
                            .build());
                    httpClientBuilder.setMaxConnTotal(100);
                    httpClientBuilder.setMaxConnPerRoute(100);
                    return httpClientBuilder;
                });

        return new RestHighLevelClient(builder);
    }
}
