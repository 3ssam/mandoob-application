package com.mandob.base.Utils;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@JsonPropertyOrder({"content", "page", "last", "first", "totalPages", "totalElements"})
public class ApiPageResponse<T> {
    private List<T> content;

    private long page;

    private boolean last;

    private boolean first;

    private long totalPages;

    private long totalElements;

    private long numberOfElements;

    public ApiPageResponse(Page<T> page) {
        this.last = page.isLast();
        this.first = page.isFirst();
        this.page = page.getNumber() + 1;
        this.content = page.getContent();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.numberOfElements = page.getNumberOfElements();
    }

    public static <T> ApiPageResponse<T> of(Page<T> page) {
        return new ApiPageResponse<>(page);
    }
}
