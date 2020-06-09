package org.circuitdoctor.core.model;


import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Data
@ToString
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Builder
@Table(name="user")
public class User extends BaseEntity<Long> implements Serializable {
    @Column(nullable = false)
    @NotBlank(message = "name is mandatory")
    @Pattern(regexp ="^[A-Za-z][A-Za-z'\\-]+")
    private String name;
    @Column(nullable = false)
    @NotBlank(message = "surname is mandatory")
    @Pattern(regexp ="[A-Z][a-z]+")
    private String surname;
    @Column(nullable = false,unique = true)
    @NotBlank(message = "phoneNumber is mandatory")
            @Pattern(regexp = "[0-9]{10}")
    private String phoneNumber;
    @Column(nullable = false)
    @NotBlank(message = "password is mandatory")
    private String password;

    @Column(nullable = false,unique = true)
    @NotBlank(message = "email is mandatory")
    @Pattern(regexp = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$")
    private String email;

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    Set<Location> locations;



}
