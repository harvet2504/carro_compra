package com.carrocompra.app.service.impl;

import com.carrocompra.app.dto.ProductoDto;
import com.carrocompra.app.mapper.ProductoMapper;
import com.carrocompra.app.repository.ProductoRepository;
import com.carrocompra.app.service.ProductoService;
import com.carrocompra.app.model.Producto;
import com.carrocompra.app.exceptions.ProductException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service implementation for the Product entity.
 */
@Service
@Transactional
@Log4j2
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productRepository;
    private final ProductoMapper productMapper;

    public ProductoServiceImpl(ProductoMapper productoMapper, ProductoRepository productRepository) {
        this.productMapper = productoMapper;
        this.productRepository = productRepository;
    }

    public ProductoDto crear(ProductoDto productDto) throws ProductException {
        log.debug("Crear producto : {}", productDto);

        try{
            Producto product = productMapper.toEntidad(productDto);
            product = productRepository.save(product);
            return productMapper.toDto(product);
        } catch (Exception ex) {
            throw new ProductException(String.format("An error occurred while persisting a product: %s", ex.getMessage()));
        }
    }

    @Transactional(readOnly = true)
    public ProductoDto findOne(Long id) throws ProductException {
        log.debug("findOne {} " + "idProducto: " + id);

        try{
            return productMapper.toDto(this.findById(id));
        } catch (ProductException e) {
            throw e;
        } catch (Exception ex) {
            throw new ProductException(String.format("Ocurrio un error: %s", ex.getMessage()));
        }
    }

    @Transactional(readOnly = true)
    public Producto findById(Long id) throws ProductException {
        log.debug("[ProductService] - findById {} " + "productId: " + id);

        try{
            return productRepository.findById(id)
                    .orElseThrow(() -> new ProductException(String.format("No hay producto con id %s", id)));
        } catch (Exception ex) {
            throw ex;
        }
    }

    @Transactional(readOnly = true)
    public List<ProductoDto> findAll() throws ProductException {
        log.debug("findAll {}");

        try{
            return productMapper.toDto(productRepository.findAll());
        } catch (Exception ex) {
            throw new ProductException(String.format("Ocurrio un error: %s", ex.getMessage()));
        }
    }

    public void eliminar(Long id) throws ProductException {
        log.info("delete {} " + "idProducto: " + id);

        try{
            productRepository.deleteById(id);
        } catch (Exception ex) {
            throw new ProductException(String.format("Ocurrio un error: %s", ex.getMessage()));
        }
    }
}
