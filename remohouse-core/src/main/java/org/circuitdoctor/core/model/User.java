package org.circuitdoctor.core.model;

import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;
import javax.validation.constraints.*;

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


    private final String NAME_SURNAME_REGEX="^[A-Za-z][A-Za-z'\\-]+";//matches names like O'Sullival or Ana-Maria
    @Column(nullable = false)
    @NonNull
    @NotBlank(message = "name is mandatory")
    @Pattern(regexp =NAME_SURNAME_REGEX)
    private String name;
    @Column(nullable = false)
    @NonNull
    @NotBlank(message = "surname is mandatory")
    @Pattern(regexp =NAME_SURNAME_REGEX)
    private String surname;
    @Column(nullable = false,unique = true)
    @NonNull
    @NotBlank(message = "phoneNumber is mandatory")
    @Digits(fraction=0,integer=10)
    private String phoneNumber;
    @Column(nullable = false)
    @NonNull
    @NotBlank(message = "password is mandatory")
    @Size(min = 7)
    private String password;

    @Column(nullable = false,unique = true)
    @NonNull
    @NotBlank(message = "email is mandatory")
    @Email
    private String email;
}
