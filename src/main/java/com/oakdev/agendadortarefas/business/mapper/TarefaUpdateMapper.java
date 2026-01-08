package com.oakdev.agendadortarefas.business.mapper;


import com.oakdev.agendadortarefas.business.dto.TarefasDTO;
import com.oakdev.agendadortarefas.infrastructure.entity.TarefasEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TarefaUpdateMapper {

    void updateTarefas(TarefasDTO dto, @MappingTarget TarefasEntity entity);
}
