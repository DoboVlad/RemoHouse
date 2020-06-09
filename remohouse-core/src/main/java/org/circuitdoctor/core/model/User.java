package org.circuitdoctor.core.model;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;


@Data
@ToString
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Builder
@Table(name="user")
public class User extends BaseEntity<Long> implements Serializable {
    @Column(nullable = false)
    String name;
    @Column(nullable = false)
    String surname;
    @Column(nullable = false,unique = true)
    String phoneNumber;
    @Column(nullable = false)
    String password;
    @Column(nullable = false,unique = true)
    String email;

    @OneToMany(fetch = FetchType.EAGER)
    Set<Location> locations;



}
