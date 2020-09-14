package com.carrocompra.app.service.impl;

import com.carrocompra.app.dto.ClienteDto;
import com.carrocompra.app.mapper.ClienteMapper;
import com.carrocompra.app.model.Cliente;
import com.carrocompra.app.repository.ClienteRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static com.carrocompra.app.service.impl.TestConstants.CLIENT_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ClienteServiceImplTest {

    @Rule
    public ExpectedException fails = ExpectedException.none();

    @Mock
    ClienteRepository clientRepository;
    @Mock
    ClienteMapper clientMapper;
    @InjectMocks
    private ClienteServiceImpl service;

    Cliente client = Cliente.newInstance()
            .idCliente(CLIENT_ID)
            .email("correo@gmail.com")
            .nombre("correo")
            .apellido("de cuba")
            .dni("dni")
            .telefono("000-00000")
            .build();

    ClienteDto clientDto = ClienteDto.newInstance()
            .idCliente(CLIENT_ID)
            .email("correo@gmail.com")
            .nombre("correo")
            .apellido("de cuba")
            .dni("dni")
            .telefono("000-00000")
            .build();

    @Before
    public void setUp() throws Exception {

        when(clientMapper.toEntidad(clientDto)).thenReturn(client);
        when(clientMapper.toDto(client)).thenReturn(clientDto);

        when(clientRepository.save(client)).thenReturn(client);
        when(clientRepository.findAll()).thenReturn(Arrays.asList(client));
    }

    @Test
    public void whenSaveProductAllParamsSet() {

        ClienteDto clientdto = service.crear(clientDto);

        verify(clientRepository, times(1))
                .save(client);
        verify(clientMapper, times(1))
                .toEntidad(clientDto);
        verify(clientMapper, times(1))
                .toDto(client);

        assertEquals(clientdto.getIdCliente(), CLIENT_ID);
    }

    @Test
    public void findAll() {

        List<ClienteDto> clientDtoList = service.findAll();

        verify(clientRepository, times(1))
                .findAll();

        verify(clientMapper, times(1))
                .toDto(client);

        assertTrue(!clientDtoList.isEmpty());
        assertTrue(clientDtoList.size() > 0);
    }

    @Test
    public void findAllById() {

    }
}