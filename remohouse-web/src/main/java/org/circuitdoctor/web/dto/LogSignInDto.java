package org.circuitdoctor.web.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
public class LogSignInDto extends BaseDto{
    private Long id;
    private Long userID;
    private String ip;
    private String browser;
    private String browserVersion;
    private String device;
    private String os;
    private String osVersion;
    private String dateTime;
}
