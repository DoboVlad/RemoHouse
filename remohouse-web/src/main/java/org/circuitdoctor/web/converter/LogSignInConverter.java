package org.circuitdoctor.web.converter;

import org.circuitdoctor.core.model.LogSignIn;
import org.circuitdoctor.core.repository.UserRepository;
import org.circuitdoctor.web.dto.LogSignInDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class LogSignInConverter extends BaseConverter<LogSignIn, LogSignInDto>  {
    @Autowired
    private UserRepository userRepository;
    @Override
    public LogSignIn convertDtoToModel(LogSignInDto dto) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LogSignIn logSignIn = LogSignIn.builder()
                .browser(dto.getBrowser())
                .browserVersion(dto.getBrowserVersion())
                .dateTime(LocalDateTime.parse(dto.getDateTime().replace("T"," "),formatter))
                .device(dto.getDevice())
                .ip(dto.getIp())
                .os(dto.getOs())
                .osVersion(dto.getOsVersion())
                .user(userRepository.findById(dto.getUserID()).get())
                .build();
        logSignIn.setId(dto.getId());
        return logSignIn;
    }

    @Override
    public LogSignInDto convertModelToDto(LogSignIn logSignIn) {
        LogSignInDto logSignInDto = LogSignInDto.builder()
                .browser(logSignIn.getBrowser())
                .browserVersion(logSignIn.getBrowserVersion())
                .dateTime(logSignIn.getDateTime().toString().replace("T"," "))
                .device(logSignIn.getDevice())
                .ip(logSignIn.getIp())
                .os(logSignIn.getOs())
                .osVersion(logSignIn.getOsVersion())
                .userID(logSignIn.getUser().getId())
                .id(logSignIn.getId())
                .build();
        return logSignInDto;
    }
}
