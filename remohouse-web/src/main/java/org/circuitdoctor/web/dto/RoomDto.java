package org.circuitdoctor.web.dto;

import lombok.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
public class RoomDto extends BaseDto{
    private final String NAME_REGEX="^[A-Za-z][A-Za-z0-9]*$";
    private Long id;
    private Long locationID;
    @NotBlank(message = "name is mandatory")
    @Size(min = 2)
    private String name;
}
