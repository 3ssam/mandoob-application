package com.mandob.domain;

import com.mandob.base.domain.MasterFile;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "user")
//TODO Search About Using @Inheritance
@Inheritance(strategy = InheritanceType.JOINED)
public class User extends MasterFile {
    @Column(nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean activated;

    @Column(nullable = false)
    private Boolean suspended;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;


    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", activated=" + activated +
                ", suspended=" + suspended +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }


}
