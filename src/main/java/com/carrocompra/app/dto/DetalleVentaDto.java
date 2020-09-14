package com.carrocompra.app.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class DetalleVentaDto {
    private Long idDetalleVenta;
    private Long idVenta;
    private Long idProducto;
}
