package org.circuitdoctor.core.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
@Builder
@ToString
@Table(name="action_log_gsm")
public class ActionLogGSM extends BaseEntity<Long> implements Serializable {
    @Column(nullable = false)
    @NonNull
    @NotBlank(message = "operationType is mandatory")
    private String operationType;

    @Column(nullable = false,unique = true)
    @NonNull
    @NotBlank(message = "date is mandatory")
    private LocalDateTime dateTime;
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    private GSMController gsmController;
}
