package com.carrocompra.app.dto;

import lombok.*;

@Builder(builderMethodName = "newInstance")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ClienteDto {
    private Long idCliente;
    private String nombre;
    private String apellido;
    private String dni;
    private String telefono;
    private String email;
}
