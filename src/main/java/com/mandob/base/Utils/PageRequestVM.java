package com.mandob.base.Utils;

import lombok.Value;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;

@Value
public class PageRequestVM {
    String page;

    public PageRequest build() {
        int pageNumber = StringUtils.isNumeric(page) ? Integer.parseInt(page) : 0;
        return PageRequest.of(Math.max(0, --pageNumber), 20);
    }

    public PageRequest buildWithPage(String page_Number) {
        int pageNumber = StringUtils.isNumeric(page_Number) ? Integer.parseInt(page_Number) : 0;
        return PageRequest.of(Math.max(0, --pageNumber), 20);
    }
}
