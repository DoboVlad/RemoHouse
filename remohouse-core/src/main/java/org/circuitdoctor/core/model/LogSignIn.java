package org.circuitdoctor.core.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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
@Table(name="log_signin")
public class LogSignIn extends BaseEntity<Long>{
    @ManyToOne
    private User user;
    @Column(nullable = false)
    private String ip;
    @Column(nullable = false)
    private String browser;
    @Column(nullable = false)
    private String browserVersion;
    @Column(nullable = false)
    private String device;
    @Column(nullable = false)
    private String os;
    @Column(nullable = false)
    private String osVersion;
    @Column(nullable = false)
    private LocalDateTime dateTime;


}
