package org.circuitdoctor.core.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
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
    @Column(nullable = false, unique = true)
    @NotBlank(message = "Latitude is mandatory")
    @Pattern(regexp = "^(\\+|-)?(?:90(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-8][0-9])(?:(?:\\.[0-9]{1,6})?))$")
    private String latitude;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Longitude is mandatory")
    @Pattern(regexp = "^(\\+|-)?(?:180(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,6})?))$")
    private String longitude;

    @Column(nullable = true)
    private String image; //to be updated later
    @Column(nullable = false)
    @NotBlank(message = "Name is mandatory")
    @Min(2)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    User user;
}
