package com.galsie.gcs.microservicecommon.lib.gcsrequests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancedExchangeFilterFunction;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Defines a {@link RestTemplate} and a {@link GCSRemoteRequests} bean so that they can be conveniently used to request to other microservices
 * - Note that the {@link RestTemplate} is required since {@link GCSRemoteRequests} depends on it
 *
 * Note: {@link com.galsie.gcs.microservicecommon.config.gcsrequests.GCSRemoteRequestsConfig} is a configuration bean that's a child of this class
 */
public abstract class GCSRemoteRequestsConfiguration {

    @Autowired
    private LoadBalancedExchangeFilterFunction lbFunction;

    @LoadBalanced
    @Bean
    WebClient.Builder webClientBuilder() {
        return WebClient.builder().filter(lbFunction).clientConnector(new ReactorClientHttpConnector());
    }

    @Bean
    public GCSRemoteRequests gcsRemoteRequests(){
        return new GCSRemoteRequests();
    }



}
