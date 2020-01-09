package com.mandob.base.Projection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.context.i18n.LocaleContextHolder;

@JsonPropertyOrder({"id", "name"})

public interface LookupProjection extends BaseProjection{
    @JsonIgnore
    String getArName();

    @JsonIgnore
    String getEnName();

    default String getName() {
        String name = LocaleContextHolder.getLocale().toString().equals("ar") ? getArName() : getEnName();
        return ObjectUtils.firstNonNull(name, getArName(), getEnName());
    }

}
