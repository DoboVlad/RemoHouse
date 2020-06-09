package org.circuitdoctor.web.converter;

import org.circuitdoctor.core.model.BaseEntity;
import org.circuitdoctor.core.repository.Repository;
import org.circuitdoctor.web.dto.BaseDto;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class BaseConverter<Model extends BaseEntity<Long>, Dto extends BaseDto> implements Converter<Model, Dto> {
    @Autowired
    private Repository<Model,Long> repository;
    public Set<Long> convertModelsToIDs(Set<Model> models) {
        return models.stream()
                .map(model -> model.getId())
                .collect(Collectors.toSet());
    }

    public Set<Long> convertDTOsToIDs(Set<Dto> dtos) {
        return dtos.stream()
                .map(dto -> dto.getId())
                .collect(Collectors.toSet());
    }

    public Set<Dto> convertModelsToDtos(Collection<Model> models) {
        return models.stream()
                .map(model -> convertModelToDto(model))
                .collect(Collectors.toSet());
    }

    public Set<Model> convertIDstoModel(Collection<Long> ids){
        //see how to implement with optional
        return ids.stream()
                .map(id -> repository.findById(id).get())
                .collect(Collectors.toSet());
    }
}
