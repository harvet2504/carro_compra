package com.carrocompra.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Builder(builderMethodName = "newInstance")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "venta")
public class Venta {

    @Id
    @Column(name = "idVenta")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVenta;

    @Column(name = "fecha", nullable = false)
    private Date fecha;

    @ManyToOne(optional = false)
    @JsonIgnoreProperties("ventas")
    private Cliente cliente;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<DetalleVentas> detalleVentas = new ArrayList<>();




}
