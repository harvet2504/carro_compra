package com.carrocompra.app.mapper;

import com.carrocompra.app.dto.ProductoDto;
import com.carrocompra.app.model.Producto;
import org.mapstruct.Mapper;

@Mapper( componentModel = "spring")
public interface ProductoMapper extends EntityMapper<ProductoDto, Producto> {

}
