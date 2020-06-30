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
@Table(name="room")
public class Room extends BaseEntity<Long> implements Serializable {


    @ManyToOne(fetch = FetchType.EAGER)
    private Location location;
    @Column
    @NotBlank(message = "name is mandatory")
    @Size(min = 2)
    private String name;
}
