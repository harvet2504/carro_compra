package com.carrocompra.app.mapper;

import com.carrocompra.app.dto.VentaDto;
import com.carrocompra.app.model.Venta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper( componentModel = "spring")
public interface VentaMapper extends EntityMapper<VentaDto, Venta> {

    @Mapping(source = "cliente.idCliente", target = "idCliente")
    @Mapping(source = "cliente.nombre", target = "nombreCliente")
    VentaDto toDto(Venta venta);

    @Mapping(source = "idCliente", target = "cliente.idCliente")
    Venta toEntidad(VentaDto ventaDto);

}
