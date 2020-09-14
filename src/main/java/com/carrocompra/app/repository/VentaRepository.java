package com.carrocompra.app.repository;

import com.carrocompra.app.model.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Sale entity.
 */
@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {

    List<Venta> findAllByCliente_IdCliente(Long id);
}
