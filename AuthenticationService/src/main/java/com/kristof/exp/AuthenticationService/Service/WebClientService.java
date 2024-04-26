package com.kristof.exp.AuthenticationService.Service;

import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class WebClientService {
    private final WebClient webClient = WebClient.create();
    /**
     * generalized way to send post requests
     * @param requestUrl the request url
     * @param requestPayload payload in JSON format
     * @return a client response
     */
    public Mono<ClientResponse> sendPostRequest(String requestUrl, JSONObject requestPayload) {
        return webClient.post()
                .uri(requestUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(requestPayload.toString()))
                .exchangeToMono(Mono::just);
    }
}
