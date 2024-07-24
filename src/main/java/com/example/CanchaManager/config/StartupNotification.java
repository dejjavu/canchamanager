package com.example.CanchaManager.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import static com.example.CanchaManager.caja.service.CajaService.log;

@Component
@Slf4j
public class StartupNotification implements CommandLineRunner {


    @Override
    public void run(String... args) {

        log.info("Esto es un log tipo info.");

    }
}
