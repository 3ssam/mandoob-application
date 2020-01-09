package com.mandob.domain;

import com.mandob.base.domain.MasterFile;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Setter
@Getter
@Table(name = "company")
public class Company extends MasterFile {

    @Column(nullable = false)
    private Boolean suspended;

    @Override
    public String toString() {
        return "Company{" +
                "id='" + id + '\'' +
                ", arName='" + arName + '\'' +
                ", enName='" + enName + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
