package com.carrocompra.app.repository;

import com.carrocompra.app.model.DetalleVentas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetalleVentaRepository extends JpaRepository<DetalleVentas, Long> {

}
