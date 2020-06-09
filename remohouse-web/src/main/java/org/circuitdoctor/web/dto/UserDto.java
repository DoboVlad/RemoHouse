package org.circuitdoctor.web.dto;

import lombok.*;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
public class UserDto extends BaseDto {
    private Long id;
    private String name;
    private String surname;
    private String phoneNumber;
    private String password;
    private String email;
    private Set<Long> locations;
}


