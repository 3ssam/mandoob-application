package com.mandob.base.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
@Setter
public class MasterFile extends EntityAudit {

    @Column(nullable = false)
    protected String arName;

    @Column(nullable = false)
    protected String enName;
}
