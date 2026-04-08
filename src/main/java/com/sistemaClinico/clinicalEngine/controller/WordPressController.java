package com.sistemaClinico.clinicalEngine.controller;

import com.sistemaClinico.clinicalEngine.dto.ApiResponse;
import com.sistemaClinico.clinicalEngine.dto.CreatePostRequest;
import com.sistemaClinico.clinicalEngine.dto.PostDto;
import com.sistemaClinico.clinicalEngine.service.WordPressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@Tag(name = "Posts", description = "Operaciones para gestionar posts en WordPress")
public class WordPressController {

    private final WordPressService wordPressService;

    public WordPressController(WordPressService wordPressService) {
        this.wordPressService = wordPressService;
    }

    @GetMapping()
    @Operation(
        summary = "Obtener posts paginados",
        description = "Retorna una lista paginada de posts de WordPress"
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Posts obtenidos exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Parámetros de paginación inválidos"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error al conectar con WordPress")
    })
    public ResponseEntity<ApiResponse<Page<PostDto>>> getPostsPaged(
            @Parameter(description = "Número de página (comienza en 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Cantidad de items por página (máximo 20)", example = "10")
            @RequestParam(defaultValue = "10") int size
    ) {

        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 20);

        Page<PostDto> result =
                wordPressService.getPostsPaged(safePage, safeSize);

        return ResponseEntity.ok(ApiResponse.success("Posts obtenidos", result));
    }

    @PostMapping()
    @Operation(
        summary = "Crear nuevo post",
        description = "Crea un nuevo post en WordPress"
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Post creado exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error al crear post en WordPress")
    })
    public ResponseEntity<ApiResponse<PostDto>> createPost(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Datos del post a crear",
                required = true,
                content = @Content(schema = @Schema(implementation = CreatePostRequest.class))
            )
            @RequestBody CreatePostRequest request) {

        PostDto created = wordPressService.createPost(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Post creado exitosamente", created));
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Actualizar post",
        description = "Actualiza un post existente en WordPress"
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Post actualizado exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Post no encontrado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error al actualizar post")
    })
    public ResponseEntity<ApiResponse<PostDto>> updatePost(
            @Parameter(description = "ID del post a actualizar", example = "1", required = true)
            @PathVariable Long id,
            
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Datos actualizados del post",
                required = true,
                content = @Content(schema = @Schema(implementation = CreatePostRequest.class))
            )
            @RequestBody CreatePostRequest request) {

        PostDto updated = wordPressService.updatePost(id, request);
        return ResponseEntity.ok(ApiResponse.success("Post actualizado correctamente", updated));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Eliminar post",
        description = "Elimina permanentemente un post de WordPress"
    )
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Post eliminado exitosamente"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Post no encontrado"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error al eliminar post")
    })
    public ResponseEntity<ApiResponse<Void>> deletePost(
            @Parameter(description = "ID del post a eliminar", example = "1", required = true)
            @PathVariable Long id) {
        wordPressService.deletePost(id);
        return ResponseEntity.ok(ApiResponse.success("Post eliminado (permanente)", null));
    }
}
