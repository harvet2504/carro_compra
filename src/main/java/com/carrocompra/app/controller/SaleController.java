package com.carrocompra.app.controller;

import com.carrocompra.app.dto.ProductoDto;
import com.carrocompra.app.dto.VentaDto;
import com.carrocompra.app.service.VentaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rx.Single;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j
public class SaleController {

	private final VentaService ventaService;

	public SaleController(VentaService ventaService) {
		this.ventaService = ventaService;
	}

    public static void writeLog(String text) {
        log.error(text);
    }

    /**
     * POST  /sales : Create a new sale.
     *
     * @param saleDTO the saleDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new saleDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/sales")
    public ResponseEntity<VentaDto> createSale(@RequestBody VentaDto saleDTO) throws URISyntaxException {
        log.debug("REST request to save Sale : {}", saleDTO);

        VentaDto result = ventaService.crear(saleDTO);
        return ResponseEntity.created(new URI("/api/sales/" + result.getIdVenta()))
                .body(result);
    }

	/**
     * GET  /sales : get all the sales.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of sales in body
     */
    @GetMapping("/sales")
    public List<VentaDto> getAllSales() {
        log.debug("REST request to get all sales");
        return ventaService.findAll();
    }


    @GetMapping("/sales/{id}")
    public Single<List<VentaDto>> getAllSalesByUserId(@PathVariable Long id) {
        log.debug("Request to get all sales from userId : {}", id);
        return ventaService.findAllSalesById(id);
    }

    /**
     * POST  /sales : Create a new saleDetail.
     *
     * @param products the product list of sale to create
     * @param clientId the id of client
     * @return the ResponseEntity with status 201 (Created) and with body the new saleDTO
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */

    @PostMapping("/sales/detail/{clientId}")
    public ResponseEntity<VentaDto> createSaleDetail(@RequestBody List<ProductoDto> products, @PathVariable Long clientId) throws URISyntaxException {
        log.debug("REST request to save SaleDetail : {}", clientId);
        VentaDto result = ventaService.crearDetalleVenta(clientId, products);
        return ResponseEntity.created(new URI("/api/sales/detail/" + result.getIdVenta()))
                .body(result);
    }
}
