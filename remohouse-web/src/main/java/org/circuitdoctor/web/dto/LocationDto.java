package org.circuitdoctor.web.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
public class LocationDto extends BaseDto {
    private final String LAT_LONG_REGEX = "^-?[0-9]{1,3}(?:\\.[0-9]{1,10})?$";
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
    private String city;
    private Long userID;
}
