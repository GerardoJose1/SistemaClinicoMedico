package com.sistemaClinico.clinicalEngine.service.impl;

import com.sistemaClinico.clinicalEngine.dto.CreatePostRequest;
import com.sistemaClinico.clinicalEngine.dto.PostDto;
import com.sistemaClinico.clinicalEngine.service.WordPressService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class WordPressServiceImpl implements WordPressService {

    private final WebClient webClient;

    public WordPressServiceImpl(@Qualifier("wordpressWebClient") WebClient webClient) {
        this.webClient = webClient;
    }


    @Override
    public PostDto createPost(CreatePostRequest request) {
        return webClient.post()
                .uri("/wp-json/wp/v2/posts")
                .bodyValue(Map.of(
                        "title", request.getTitle(),
                        "content", request.getContent(),
                        "status", request.getStatus() != null ? request.getStatus() : "draft"
                ))
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response -> response.bodyToMono(String.class)
                        .map(body -> new RuntimeException("WordPress error: " + body)))
                .bodyToMono(PostDto.class)
                .block();
    }

    @Override
    public Page<PostDto> getPostsPaged(int page, int size) {
        int wpPage = page + 1;
        Pageable pageable = PageRequest.of(page, size);

        ResponseEntity<List<PostDto>> response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/wp-json/wp/v2/posts")
                        .queryParam("per_page", size)
                        .queryParam("page", wpPage)
                        .build()
                )
                .retrieve()
                .onStatus(s -> s.is4xxClientError(),
                        r -> r.bodyToMono(String.class).map(RuntimeException::new))
                .toEntityList(PostDto.class)
                .block();

        List<PostDto> posts = (response != null && response.getBody() != null)
                ? response.getBody()
                : Collections.emptyList();

        String totalHeader = response != null ? response.getHeaders().getFirst("X-WP-Total") : "0";
        long total = totalHeader != null ? Long.parseLong(totalHeader) : 0L;
        return new PageImpl<>(posts, pageable, total);
    }

    @Override
    public PostDto updatePost(Long id, CreatePostRequest request) {
        return webClient.put()
                .uri("/wp-json/wp/v2/posts/{id}", id)
                .bodyValue(Map.of(
                        "title", request.getTitle(),
                        "content", request.getContent(),
                        "status", request.getStatus() != null ? request.getStatus() : "publish"
                ))
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response -> response.bodyToMono(String.class)
                        .map(body -> new RuntimeException("Error actualizando en WordPress: " + body)))
                .bodyToMono(PostDto.class)
                .block();
    }

    @Override
    public void deletePost(Long id) {
        webClient.delete()
                .uri(uriBuilder -> uriBuilder
                        .path("/wp-json/wp/v2/posts/{id}")
                        .queryParam("force", true)
                        .build(id))
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response -> response.bodyToMono(String.class)
                        .map(body -> new RuntimeException("Error eliminando en WordPress: " + body)))
                .toBodilessEntity()
                .block();
    }
}