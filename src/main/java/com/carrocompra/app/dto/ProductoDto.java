package com.carrocompra.app.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ProductoDto {
    private Long idProducto;
    private String nombre;
    private Float precio;
}
