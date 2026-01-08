package com.oakdev.agendadortarefas.business.service;

import com.oakdev.agendadortarefas.business.dto.TarefasDTO;
import com.oakdev.agendadortarefas.business.mapper.TarefaUpdateMapper;
import com.oakdev.agendadortarefas.business.mapper.TarefasMapper;
import com.oakdev.agendadortarefas.infrastructure.entity.TarefasEntity;
import com.oakdev.agendadortarefas.infrastructure.enums.StatusNotificacaoEnum;
import com.oakdev.agendadortarefas.infrastructure.exceptions.ResourceNotFoundException;
import com.oakdev.agendadortarefas.infrastructure.repository.TarefasRepository;
import com.oakdev.agendadortarefas.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TarefasService {

    private final TarefasRepository tarefasRepository;
    private final TarefasMapper tarefasMapper;
    private final JwtUtil jwtUtil;
    private final TarefaUpdateMapper tarefaUpdateMapper;

    public TarefasDTO gravarTarefa(String token, TarefasDTO dto) {
        String email = jwtUtil.extrairEmailToken(token.substring(7));
        dto.setDataCriacao(LocalDateTime.now());
        dto.setStatusNotificacaoEnum(StatusNotificacaoEnum.PENDENTE);
        dto.setEmailUsuario(email);
        TarefasEntity entity = tarefasMapper.paraTarefaEntity(dto);
        return tarefasMapper.paraTarefaDTO(tarefasRepository.save(entity));
    }

    public List<TarefasDTO> buscaTarefasAgendadasPorPeriodo(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        return tarefasMapper.paraListaTarefasDTO(
                tarefasRepository.findByDataEventoBetween(dataInicial, dataFinal)
        );
    }

    public List<TarefasDTO> buscaTarefasPorEmail(String token) {
        String email = jwtUtil.extrairEmailToken(token.substring(7));
        return tarefasMapper.paraListaTarefasDTO(
                tarefasRepository.findByEmailUsuario(email)
        );
    }

    public void deletaTarefaPorId(String id) {
        if (!tarefasRepository.existsById(id)) {
            throw new ResourceNotFoundException("Erro ao deletar tarefa por ID, ID inexistente: " + id);
        }
        tarefasRepository.deleteById(id);
    }

    public TarefasDTO alteraStatus(StatusNotificacaoEnum status, String id) {
        TarefasEntity entity = tarefasRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Tarefa não encontrada" + id));
        entity.setStatusNotificacaoEnum(status);
        return tarefasMapper.paraTarefaDTO(tarefasRepository.save(entity));

    }

    public TarefasDTO updateTarefas(TarefasDTO dto, String id) {
        TarefasEntity entity = tarefasRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Tarefa não encontrada" + id));

        tarefaUpdateMapper.updateTarefas(dto, entity);

        return tarefasMapper.paraTarefaDTO(tarefasRepository.save(entity));
    }
}
