package com.carrocompra.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder(builderMethodName = "newInstance")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "producto"
)
public class Producto {

    @Id
    @Column(name = "idProducto")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProducto;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "precio")
    private Float precio;

}
