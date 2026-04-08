package com.sistemaClinico.clinicalEngine.dto;

public record PostDto(
        Long id,
        RenderedDto title,
        String status,
        String date,
        Long author,
        RenderedDto content) {
        public record RenderedDto(String rendered) {}


}
