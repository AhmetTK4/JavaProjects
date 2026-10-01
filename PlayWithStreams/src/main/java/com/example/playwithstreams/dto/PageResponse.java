package com.example.playwithstreams.dto;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Stable JSON shape for a page of results. Spring Data's {@code PageImpl} is not meant to be
 * serialised directly; its JSON structure is not guaranteed between versions.
 */
public record PageResponse<T>(List<T> content, int page, int size, long totalElements, int totalPages) {

    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(page.getContent(), page.getNumber(), page.getSize(),
                page.getTotalElements(), page.getTotalPages());
    }
}
