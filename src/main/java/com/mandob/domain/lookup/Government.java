package com.mandob.domain.lookup;

import com.mandob.base.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Setter
@Getter
@Table(name = "government")
public class Government extends BaseEntity {

    private String ar_Name;
    private String en_Name;

    @Override
    public String toString() {
        return "Government{" +
                "id='" + id + '\'' +
                "ar_Name='" + ar_Name + '\'' +
                ", en_Name='" + en_Name + '\'' +
                '}';
    }

}
