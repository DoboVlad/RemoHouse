package org.circuitdoctor.web.dto;

import lombok.*;
import org.circuitdoctor.core.model.GSMController;
import org.circuitdoctor.core.model.User;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
public class ActionLogGSMDto extends BaseDto {
    private Long id;
    @NonNull
    @NotBlank(message = "operationType is mandatory")
    private String operationType;

    @NonNull
    @NotBlank(message = "date is mandatory")
    private LocalDateTime dateTime;

    private Long userID;

    private Long gsmControllerID;
}
