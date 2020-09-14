package com.carrocompra.app.mapper;

import java.util.List;

public interface EntityMapper<D, E> {

    E toEntidad(D dto);

    D toDto(E entity);

    List <D> toDto(List<E> entityList);
}
