package com.carrocompra.app.service;

import com.carrocompra.app.dto.ProductoDto;
import com.carrocompra.app.dto.VentaDto;
import rx.Single;

import java.util.List;
import java.util.Optional;

public interface VentaService {

    VentaDto crear(VentaDto clientDto);

    List<VentaDto> findAll();

    Optional<VentaDto> findOne(Long id);

    Single<List<VentaDto>> findAllSalesById(Long id);

    VentaDto crearDetalleVenta(Long clientId, List<ProductoDto> products);
}
