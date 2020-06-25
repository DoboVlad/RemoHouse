package org.circuitdoctor.web.dto;

import lombok.*;
import javax.persistence.Column;
import javax.validation.constraints.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
public class UserDto extends BaseDto {
    private Long id;
    @NotBlank(message = "name is mandatory")
    @Pattern(regexp ="^[A-Za-z][A-Za-z'\\-]+")
    private String name;
    @NotBlank(message = "surname is mandatory")
    @Pattern(regexp ="[A-Za-z][A-Za-z'\\-]+")
    private String surname;
    @NotBlank(message = "phoneNumber is mandatory")
    @Digits(fraction=0,integer=10)
    private String phoneNumber;
    @NotBlank(message = "password is mandatory")
    @Size(min=7)
    private String password;
    @NotBlank(message = "email is mandatory")
    @Email
    private String email;
}


