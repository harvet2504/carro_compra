package com.carrocompra.app.mapper;

import com.carrocompra.app.dto.ClienteDto;
import com.carrocompra.app.model.Cliente;
import org.mapstruct.Mapper;

@Mapper( componentModel = "spring")
public interface ClienteMapper extends EntityMapper<ClienteDto, Cliente> {

}
