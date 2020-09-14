package com.carrocompra.app.service;

import com.carrocompra.app.dto.ProductoDto;
import com.carrocompra.app.exceptions.ProductException;

import java.util.List;

/**
 * Service interface for the Client entity.
 */
public interface ProductoService {

    ProductoDto crear(ProductoDto productDto) throws ProductException;

    List<ProductoDto> findAll() throws ProductException;

    ProductoDto findOne(Long id) throws ProductException;

    void eliminar(Long id) throws ProductException;
}
