package org.circuitdoctor.core.model;

import lombok.*;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Table(name="location")
public class Location extends BaseEntity<Long> implements Serializable {
    @Column(nullable = false, unique = true)
    private String coordinates;
    @Column(nullable = true)
    private String image; //to be updated later
    @Column(nullable = false)
    private String name;
}
