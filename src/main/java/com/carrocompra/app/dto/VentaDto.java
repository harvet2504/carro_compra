package com.carrocompra.app.dto;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class VentaDto {
    private Long idVenta;
    private Date fecha;
    private Long idCliente;
    private String nombreCliente;

}
