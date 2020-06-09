package org.circuitdoctor.web.dto;

import lombok.*;

import javax.persistence.Entity;
import java.util.Set;

@Data
@ToString
@AllArgsConstructor
@Entity
@NoArgsConstructor
@Builder
public class UserDto extends BaseDto {
    private String phoneNumber;
    private String email;
    private String password;
    private Set<Long> locations;
}
