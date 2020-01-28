package com.mandob.domain;

import com.mandob.base.domain.MasterFile;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "category")
public class Category extends MasterFile {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    private Category parent;

    @Override
    public String toString() {
        return "Category{" +
                "id='" + arName + '\'' +
                ", arName='" + enName + '\'' +
                ", enName='" + id + '\'' +
                '}';
    }
}