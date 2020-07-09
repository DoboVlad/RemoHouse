package org.circuitdoctor.web.converter;

import org.circuitdoctor.core.model.ActionLogGSM;
import org.circuitdoctor.core.model.GSMController;
import org.circuitdoctor.core.repository.GSMControllerRepository;
import org.circuitdoctor.core.repository.UserRepository;
import org.circuitdoctor.web.dto.ActionLogGSMDto;
import org.circuitdoctor.web.dto.GSMControllerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ActionLogGSMConverter extends BaseConverter<ActionLogGSM, ActionLogGSMDto> {
    @Autowired
    private GSMControllerRepository gsmControllerRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public ActionLogGSM convertDtoToModel(ActionLogGSMDto dto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ActionLogGSM actionLogGSM= ActionLogGSM.builder()
                .operationType(dto.getOperationType())
                .dateTime(LocalDateTime.parse(dto.getDateTime(),formatter))
                .user(userRepository.findById(dto.getUserID()).get())
                .gsmController(gsmControllerRepository.findById(dto.getGsmControllerID()).get())
                .build();
        actionLogGSM.setId(dto.getId());
        return actionLogGSM;
    }

    @Override
    public ActionLogGSMDto convertModelToDto(ActionLogGSM actionLogGSM) {
        ActionLogGSMDto dto= ActionLogGSMDto.builder()
                .id(actionLogGSM.getId())
                .operationType(actionLogGSM.getOperationType())
                .dateTime(actionLogGSM.getDateTime().toString().replace("T"," "))
                .gsmControllerID(actionLogGSM.getGsmController().getId())
                .userID(actionLogGSM.getUser().getId())
                .gsmControllerType(actionLogGSM.getGsmController().getType())
                .locationName(actionLogGSM.getGsmController().getRoom().getLocation().getName())
                .roomName(actionLogGSM.getGsmController().getRoom().getName())
                .build();
        return dto;
    }
}
