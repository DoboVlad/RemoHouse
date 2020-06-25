package org.circuitdoctor.web.dto;

import lombok.*;
import org.circuitdoctor.core.model.GSMStatus;

import javax.print.DocFlavor;
import javax.validation.constraints.*;


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
    @Builder.Default
    private GSMStatus status=OFF;
    private String type;
}
