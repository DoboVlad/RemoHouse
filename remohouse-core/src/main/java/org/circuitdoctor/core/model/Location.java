package org.circuitdoctor.core.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
@Builder
@ToString
@Table(name="location")
public class Location extends BaseEntity<Long> implements Serializable {
    /*
    Latitude and longitude type:
    (80.0123, -34.034)
     */
    private final String LAT_LONG_REGEX = "^(\\()([-+]?)([\\d]{1,2})(((\\.)(\\d+)(,)))(\\s*)(([-+]?)([\\d]{1,3})((\\.)(\\d+))?(\\)))$";
    @Column(nullable = false, unique = true)
    @NotBlank(message = "Latitude is mandatory")
    @Pattern(regexp = LAT_LONG_REGEX)
    private String latitude;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Longitude is mandatory")
    @Pattern(regexp = LAT_LONG_REGEX)
    private String longitude;

    @Column(nullable = true)
    private String image; //to be updated later
    @Column(nullable = false)
    @NotBlank(message = "Name is mandatory")
    @Size(min=2)
    private String name;

}
