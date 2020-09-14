package com.carrocompra.app.service.impl;

import com.carrocompra.app.dto.ProductoDto;
import com.carrocompra.app.dto.VentaDto;
import com.carrocompra.app.model.Venta;
import com.carrocompra.app.repository.DetalleVentaRepository;
import com.carrocompra.app.repository.VentaRepository;
import com.carrocompra.app.service.VentaService;
import com.carrocompra.app.EntityNotFoundException;
import com.carrocompra.app.model.DetalleVentas;
import com.carrocompra.app.mapper.ProductoMapper;
import com.carrocompra.app.mapper.VentaMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import rx.Single;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;

    private VentaMapper ventaMapper;
    private ProductoMapper productMapper;

    public VentaServiceImpl(VentaRepository ventaRepository, DetalleVentaRepository detalleVentaRepository, VentaMapper ventaMapper, ProductoMapper productMapper) {
        this.ventaRepository = ventaRepository;
        this.detalleVentaRepository = detalleVentaRepository;
        this.ventaMapper = ventaMapper;
        this.productMapper = productMapper;
    }

    public VentaDto crear(VentaDto ventaDto) {
        log.debug("Venta : {}", ventaDto);
        Venta venta = ventaMapper.toEntidad(ventaDto);
        venta = ventaRepository.save(venta);
        return ventaMapper.toDto(venta);
    }

    @Transactional(readOnly = true)
    public List<VentaDto> findAll() {
        log.debug("Lista de ventas");
        return ventaRepository.findAll().stream()
                .map(ventaMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public Optional<VentaDto> findOne(Long id) {
        log.debug("Obtener venta por id : {}", id);
        return ventaRepository.findById(id)
                .map(ventaMapper::toDto);
    }

    @Override
    public VentaDto crearDetalleVenta(Long clientId, List<ProductoDto> products) {
        VentaDto ventaDto = crear(VentaDto.builder().idCliente(clientId).fecha(new Date()).build());
        for (ProductoDto product : products) {
            DetalleVentas detalleVenta =  new DetalleVentas();
            detalleVenta.setProducto(productMapper.toEntidad(product));
            detalleVenta.setVenta(ventaMapper.toEntidad(ventaDto));
            detalleVentaRepository.save(detalleVenta);
        }
        return ventaDto;
    }


    @Transactional(readOnly = true)
    public Single<List<VentaDto>> findAllSalesById(Long id) {
        log.debug("Obtener ventas por user id : {}", id);
        return findAllSalesInRepository(id)
                .map(this::toBookResponseList);
    }

    private Single<List<Venta>> findAllSalesInRepository(Long id) {
        return Single.create(singleSubscriber -> {
            List<Venta> ventas = ventaRepository.findAllByCliente_IdCliente(id);

            if (!ObjectUtils.isEmpty(ventas)) {
                singleSubscriber.onSuccess(ventas);
            } else {
                singleSubscriber.onError(new EntityNotFoundException(Venta.class));
            }
        });
    }

    private List<VentaDto> toBookResponseList(List<Venta> bookList) {
        return bookList
                .stream()
                .map(ventaMapper::toDto)
                .collect(Collectors.toList());
    }
}
