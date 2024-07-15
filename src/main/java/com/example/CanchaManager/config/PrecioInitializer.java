package com.example.CanchaManager.config;

import com.example.CanchaManager.model.Precio;
import com.example.CanchaManager.model.tiposEnum;
import com.example.CanchaManager.repository.PrecioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class PrecioInitializer implements CommandLineRunner {

    private final PrecioRepository precioRepository;

    @Autowired
    public PrecioInitializer(PrecioRepository precioRepository) {
        this.precioRepository = precioRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        initializePrecios();
    }

    private void initializePrecios() {
        createPrecioIfNotFound(tiposEnum.DIA, 10000.0);
        createPrecioIfNotFound(tiposEnum.NOCHE, 20000.0);
    }

    private void createPrecioIfNotFound(tiposEnum tiposEnum, Double monto) {
        Precio precio = precioRepository.findByTipoPrecio(tiposEnum)
                .orElse(null);

        if (precio == null) {
            precio = new Precio(tiposEnum, monto);
            precioRepository.save(precio);
        }
    }
}
