package org.circuitdoctor.web.dto;

import lombok.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
public class RoomDto extends BaseDto{
    private final String NAME_REGEX="^[A-Za-z][A-Za-z0-9]*$";
    private Long id;
    private Long locationID;
    @NotBlank(message = "name is mandatory")
    @Pattern(regexp = NAME_REGEX)
    private String name;
}
