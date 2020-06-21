package org.circuitdoctor.web.dto;

import lombok.*;
import org.circuitdoctor.core.model.GSMStatus;

import javax.persistence.Column;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;

import static org.circuitdoctor.core.model.GSMStatus.OFF;
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
public class GSMControllerDto extends BaseDto {

    private Long id;
    private Long roomID;
    @NonNull
    @NotBlank(message = "phoneNumber is mandatory")
    @Digits(fraction=0,integer=10)
    private String phoneNumber;

    private GSMStatus status=OFF;
}
