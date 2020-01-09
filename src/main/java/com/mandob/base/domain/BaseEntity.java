package com.mandob.base.domain;

import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@Getter
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    @Id
    @Column(name = "id",nullable = false)
    @GeneratedValue(generator = "mandob-genertor")
    @GenericGenerator(name = "mandob-genertor",strategy = "com.mandob.base.Utils.GenerateID")
    protected String id;

}
