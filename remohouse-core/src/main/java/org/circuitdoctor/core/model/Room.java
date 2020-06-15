package org.circuitdoctor.core.model;


import lombok.*;

import javax.persistence.*;
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
@Table(name="room")
public class Room extends BaseEntity<Long> implements Serializable {
    private final String NAME_REGEX="^[A-Za-z][A-Za-z0-9]*$";

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Location location;
    @Column
    @NotBlank(message = "name is mandatory")
    @Pattern(regexp = NAME_REGEX)
    private String name;
}
