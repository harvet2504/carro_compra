package com.carrocompra.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "detalleVenta"
)
public class DetalleVentas {

    @Id
    @Column(name = "idDetalleVenta")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalleVenta;

    @ManyToOne(optional = false)
    @JsonIgnoreProperties("venta")
    private Venta venta;

    @ManyToOne(optional = false)
    @JsonIgnoreProperties("detalleVentas")
    private Producto producto;
}
