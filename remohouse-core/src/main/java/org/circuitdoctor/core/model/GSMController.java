package org.circuitdoctor.core.model;

import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import javax.print.DocFlavor;
import javax.validation.constraints.*;

import static org.circuitdoctor.core.model.GSMStatus.OFF;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
@Builder
@ToString
@Table(name="gsm_controller")
public class GSMController extends BaseEntity<Long> implements Serializable {
    @Column(nullable = false,unique = true)
    @NonNull
    @NotBlank(message = "phoneNumber is mandatory")
    @Digits(fraction=0,integer=10)
    private String phoneNumber;
    @Column(nullable = false)
    @Builder.Default
    private GSMStatus status=OFF;
    @Column(nullable = false)
    private String type;
    @ManyToOne(fetch = FetchType.EAGER)
    private Room room;
}
