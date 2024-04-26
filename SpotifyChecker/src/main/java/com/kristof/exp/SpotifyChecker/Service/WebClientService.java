package com.kristof.exp.SpotifyChecker.Service;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class WebClientService {
    private final WebClient webClient = WebClient.create();
    /**
     *
     * @param requestUrl
     * @return
     */
    public Mono<ClientResponse> sendGetRequest(String requestUrl) {
        return webClient.get()
                .uri(requestUrl)
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(Mono::just);
    }
}
