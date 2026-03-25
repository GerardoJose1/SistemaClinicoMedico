package com.sistemaClinico.clinicalEngine.service;

import com.sistemaClinico.clinicalEngine.dto.CreatePostRequest;
import com.sistemaClinico.clinicalEngine.dto.PostDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface WordPressService {
    PostDto createPost(CreatePostRequest request);
    Page<PostDto> getPostsPaged(int page, int size);
    PostDto updatePost(Long id, CreatePostRequest request);
    void deletePost(Long id);
}
