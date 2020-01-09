package com.mandob.domain.lookup;


import com.mandob.base.domain.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(name = "city")
public class City extends BaseEntity {

    private String ar_Name;
    private String en_Name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "government_id", referencedColumnName = "id")
    private Government government;

    @Override
    public String toString() {
        return "City{" +
                "id='" + id + '\'' +
                "ar_Name='" + ar_Name + '\'' +
                ", en_Name='" + en_Name + '\'' +
                '}';
    }
}
