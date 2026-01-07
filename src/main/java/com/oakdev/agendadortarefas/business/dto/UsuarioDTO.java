package com.oakdev.agendadortarefas.business.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UsuarioDTO {
    private String email;
    private String senha;
}