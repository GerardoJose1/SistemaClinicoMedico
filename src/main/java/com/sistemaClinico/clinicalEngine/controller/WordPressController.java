package com.sistemaClinico.clinicalEngine.controller;

import com.sistemaClinico.clinicalEngine.dto.ApiResponse;
import com.sistemaClinico.clinicalEngine.dto.CreatePostRequest;
import com.sistemaClinico.clinicalEngine.dto.PostDto;
import com.sistemaClinico.clinicalEngine.service.WordPressService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
public class WordPressController {

    private final WordPressService wordPressService;

    public WordPressController(WordPressService wordPressService) {
        this.wordPressService = wordPressService;
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<Page<PostDto>>> getPostsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 20);

        Page<PostDto> result =
                wordPressService.getPostsPaged(safePage, safeSize);

        return ResponseEntity.ok(ApiResponse.success("Posts obtenidos", result));
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<PostDto>> createPost(@RequestBody CreatePostRequest request) {

        PostDto created = wordPressService.createPost(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Post creado exitosamente", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PostDto>> updatePost(
            @PathVariable Long id,
            @RequestBody CreatePostRequest request) {

        PostDto updated = wordPressService.updatePost(id, request);
        return ResponseEntity.ok(ApiResponse.success("Post actualizado correctamente", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable Long id) {
        wordPressService.deletePost(id);
        return ResponseEntity.ok(ApiResponse.success("Post eliminado (permanente)", null));
    }
}
