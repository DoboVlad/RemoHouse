package org.circuitdoctor.web.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
public class LocationDto extends BaseDto {
    private Long id;
    private String latitude;
    private String longitude;
    private String image; //to be updated later
    private String name;
}
