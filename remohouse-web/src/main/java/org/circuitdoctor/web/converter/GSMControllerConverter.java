package org.circuitdoctor.web.converter;

import org.circuitdoctor.core.model.GSMController;
import org.circuitdoctor.core.repository.RoomRepository;
import org.circuitdoctor.web.dto.GSMControllerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GSMControllerConverter extends BaseConverter<GSMController, GSMControllerDto> {
    @Autowired
    private RoomRepository roomRepository;
    @Override
    public GSMController convertDtoToModel(GSMControllerDto dto) {
        GSMController gsmController=GSMController.builder()
                .phoneNumber(dto.getPhoneNumber())
                .room(roomRepository.findById(dto.getRoomID()).get())
                .status(dto.getStatus())
                .type(dto.getType())
                .build();
        gsmController.setId(dto.getId());
        return gsmController;

    }

    @Override
    public GSMControllerDto convertModelToDto(GSMController gsmController) {
        GSMControllerDto dto=GSMControllerDto.builder()
                .id(gsmController.getId())
                .phoneNumber(gsmController.getPhoneNumber())
                .roomID(gsmController.getRoom().getId())
                .status(gsmController.getStatus())
                .type(gsmController.getType())
                .build();
        return dto;
    }
}
