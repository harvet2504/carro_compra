package com.carrocompra.app.controller;

import com.carrocompra.app.dto.ProductoDto;
import com.carrocompra.app.exceptions.ProductException;
import com.carrocompra.app.service.ProductoService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductoService productService;

    public ProductController(ProductoService productService) {
        this.productService = productService;
    }

    public static void writeLog(String text) {
        log.error(text);
    }

    /**
     * POST  /products : Create a new product.
     *
     * @param productDTO the productDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new productDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/products")
    public ResponseEntity<ProductoDto> createProduct(@RequestBody ProductoDto productDTO) throws URISyntaxException, ProductException {
        log.debug("REST request to save Product : {}", productDTO);

        ProductoDto result = productService.crear(productDTO);
        return ResponseEntity.created(new URI("/api/products/" + result.getIdProducto()))
                .body(result);
    }

    /**
     * PUT  /products : Updates an existing product.
     *
     * @param productDTO the productDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated productDTO
     */
    @PutMapping("/products")
    public ResponseEntity<ProductoDto> updateProduct(@RequestBody ProductoDto productDTO) throws ProductException {
        log.debug("REST request to update Product : {}", productDTO);
        ProductoDto result = productService.crear(productDTO);
        return ResponseEntity.ok()
                .body(result);
    }

    /**
     * GET  /products : get all the products.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of products in body
     */
    @GetMapping("/products")
    public List<ProductoDto> getAllProducts() throws ProductException {
        log.debug("REST request to get all Products");
        return productService.findAll();
    }

    /**
     * GET  /products/:id : get the "id" product.
     *
     * @param id the id of the productDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the productDTO
     */
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductoDto> getProduct(@PathVariable Long id) throws ProductException {
        log.debug("REST request to get Product : {}", id);

        ProductoDto productDTO = productService.findOne(id);
        return new ResponseEntity<>(productDTO,HttpStatus.OK);
    }

    /**
     * DELETE  /products/:id : delete the "id" product.
     *
     * @param id the id of the productDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) throws ProductException {
        log.debug("REST request to delete Product : {}", id);
        productService.eliminar(id);
        return ResponseEntity.ok().build();
    }

}
