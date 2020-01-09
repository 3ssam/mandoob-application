package com.mandob.domain;

import com.mandob.base.domain.MasterFile;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "role")
public class Role extends MasterFile {

    @Column(nullable = false)
    private String arName;

    @Column(nullable = false)
    private String enName;

    @Column(nullable = false)
    private Boolean system_permission;

    @Override
    public String toString() {
        return "Role{" +
                "id='" + id + '\'' +
                ", arName='" + arName + '\'' +
                ", enName='" + enName + '\'' +
                ", system=" + system_permission +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
