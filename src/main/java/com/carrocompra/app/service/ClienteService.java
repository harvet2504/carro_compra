package com.carrocompra.app.service;

import com.carrocompra.app.dto.ClienteDto;
import rx.Single;

import java.util.List;

/**
 * Service interface for the Client entity.
 */
public interface ClienteService {

    ClienteDto crear(ClienteDto clientDto);

    List<ClienteDto> findAll();

    Single<Object> findAllById(Long id);
}
