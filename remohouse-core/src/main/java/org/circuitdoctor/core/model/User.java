package org.circuitdoctor.core.model;

import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
@Builder
@ToString
@Table(name="user_account")
public class User extends BaseEntity<Long> implements Serializable {

    private final String EMAIL_REGEX="^[A-Za-z0-9+_.-]+@(.+)\\.[A-Za-z]{2,6}$";//matches all kinds of emails
    private final String PHONE_NUMBER_REGEX="[0-9]{10}";///to be apdated later
    private final String NAME_SURNAME_REGEX="^[A-Za-z][A-Za-z'\\-]+";//matches names like O'Sullival or Ana-Maria
    @Column(nullable = false)
    @NotBlank(message = "name is mandatory")
    @Pattern(regexp =NAME_SURNAME_REGEX)
    private String name;
    @Column(nullable = false)
    @NotBlank(message = "surname is mandatory")
    @Pattern(regexp =NAME_SURNAME_REGEX)
    private String surname;
    @Column(nullable = false,unique = true)
    @NotBlank(message = "phoneNumber is mandatory")
    @Pattern(regexp = PHONE_NUMBER_REGEX)
    private String phoneNumber;
    @Column(nullable = false)
    @NotBlank(message = "password is mandatory")
    @Size(min = 7)
    private String password;

    @Column(nullable = false,unique = true)
    @NotBlank(message = "email is mandatory")
    @Pattern(regexp = EMAIL_REGEX)
    private String email;

    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    Set<Location> locations;
}
