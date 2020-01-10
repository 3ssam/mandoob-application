package com.mandob.domain;

import com.mandob.base.domain.MasterFile;
import com.mandob.domain.lookup.City;
import com.mandob.domain.lookup.Government;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "route")
public class Route extends MasterFile {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "government_id", referencedColumnName = "id")
    private Government government;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", referencedColumnName = "id")
    private City city;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "salesforce_id", referencedColumnName = "id")
    private Salesforce salesforce;

    @Override
    public String toString() {
        return "Route{" +
                "id='" + id + '\'' +
                ", arName='" + arName + '\'' +
                ", enName='" + enName + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                "}";
    }
}
