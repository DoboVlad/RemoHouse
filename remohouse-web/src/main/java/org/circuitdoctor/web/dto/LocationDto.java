package org.circuitdoctor.web.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
public class LocationDto extends BaseDto {
    private final String LAT_LONG_REGEX = "^(\\()([-+]?)([\\d]{1,2})(((\\.)(\\d+)(,)))(\\s*)(([-+]?)([\\d]{1,3})((\\.)(\\d+))?(\\)))$";
    private Long id;
    @NotBlank(message = "Longitude is mandatory")
    @Pattern(regexp = LAT_LONG_REGEX)
    private String latitude;
    @NotBlank(message = "Longitude is mandatory")
    @Pattern(regexp = LAT_LONG_REGEX)
    private String longitude;
    private String image; //to be updated later
    @NotBlank(message = "Name is mandatory")
    @Size(min=2)
    private String name;
    private Long userID;
}
