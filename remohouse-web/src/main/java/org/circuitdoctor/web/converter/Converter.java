package org.circuitdoctor.web.converter;


import org.circuitdoctor.core.model.BaseEntity;
import org.circuitdoctor.web.dto.BaseDto;


public interface Converter<Model extends BaseEntity<Long>, Dto extends BaseDto> {

    Model convertDtoToModel(Dto dto);

    Dto convertModelToDto(Model model);

}

