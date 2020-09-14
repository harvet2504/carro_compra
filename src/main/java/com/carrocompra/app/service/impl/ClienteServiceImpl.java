package com.carrocompra.app.service.impl;


import com.carrocompra.app.customException.CustomError;
import com.carrocompra.app.dto.ClienteDto;
import com.carrocompra.app.mapper.ClienteMapper;
import com.carrocompra.app.model.Cliente;
import com.carrocompra.app.repository.ClienteRepository;
import com.carrocompra.app.service.ClienteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rx.Single;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clientRepository;

    private ClienteMapper clientMapper;

    public ClienteDto crear(ClienteDto clientDto) {
        log.debug("Crear cliente : {}", clientDto);
        Cliente client = clientMapper.toEntidad(clientDto);
        client = clientRepository.save(client);
        return clientMapper.toDto(client);
    }

    @Transactional(readOnly = true)
    public List<ClienteDto> findAll() {
        log.debug("Lista de clientes");
        return clientRepository.findAll().stream()
                .map(clientMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public ClienteServiceImpl(ClienteRepository clientRepository, ClienteMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public Single<Object> findAllById(Long id) {
        log.debug("Request to get all clients by Id");
        return Single.create(singleSubscriber -> {
            final Optional<ClienteDto> optionalClient = clientRepository.findById(id).map(clientMapper::toDto);
            if (!optionalClient.isPresent()) {
                singleSubscriber.onError(new CustomError(HttpStatus.NO_CONTENT,
                        204,
                        "no se encontro ese id",
                        ""));
            } else {
                singleSubscriber.onSuccess(optionalClient);
            }
        });
    }

}



