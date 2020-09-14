package com.carrocompra.app.service.impl;

import com.carrocompra.app.CarroCompraApplication;
import com.carrocompra.app.exceptions.ProductException;
import com.carrocompra.app.model.Producto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CarroCompraApplication.class)
public class ProductoServiceImplTest implements TestConstants {

    @Autowired
    private ProductoServiceImpl service;

    @Before
    public void setUp() throws Exception {

    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void findOneById() throws ProductException {
        Producto prod =  service.findById(null);
    }
}