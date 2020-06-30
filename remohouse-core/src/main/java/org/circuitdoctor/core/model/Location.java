package org.circuitdoctor.core.model;

import lombok.*;
import javax.persistence.*;
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
    private final String LAT_LONG_REGEX = "^-?[0-9]{1,3}(?:\\.[0-9]{1,10})?$";
    @Column(nullable = false, unique = true)
    @NotBlank(message = "Latitude is mandatory")
    @Pattern(regexp = LAT_LONG_REGEX,message = "Latitude has invalid format")
    private String latitude;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Longitude is mandatory")
    @Pattern(regexp = LAT_LONG_REGEX,message = "Longitude has invalid format")
    private String longitude;

    @Column(nullable = true)
    private String image; //to be updated later
    @Column(nullable = false)
    @NotBlank(message = "Name is mandatory")
    @Size(min=2,message = "The name should have at least 2 characters.")
    private String name;
    @Column(nullable = false)
    private String city;
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

}
