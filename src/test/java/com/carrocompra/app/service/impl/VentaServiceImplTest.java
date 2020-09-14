package com.carrocompra.app.service.impl;

import com.carrocompra.app.dto.VentaDto;
import com.carrocompra.app.model.Venta;
import com.carrocompra.app.mapper.VentaMapper;
import com.carrocompra.app.repository.VentaRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class VentaServiceImplTest {

    @Rule
    public ExpectedException fails = ExpectedException.none();

    @Mock
    VentaRepository ventaRepository;
    @Spy
    VentaMapper saleMapper;
    @InjectMocks
    private VentaServiceImpl service;

    Venta sale = Venta.newInstance().build();
    VentaDto saleDto = VentaDto.builder().build();

    @Before
    public void setUp() throws Exception {

        when(ventaRepository.save(any()))
                .thenReturn(null);
    }

    @Test
    public void saveNullEtityExpectException() {

        service.crear(null);

        verify(ventaRepository, times(1))
                .save(null);


    }

}