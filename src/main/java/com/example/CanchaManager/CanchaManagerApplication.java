package com.example.CanchaManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@SpringBootApplication
@ComponentScan(basePackages = "com.example.CanchaManager")

public class CanchaManagerApplication {

	public static void main(String[] args) throws IOException {
		// Obtener la ruta de application.properties basada en el sistema operativo
		String applicationPropertiesPath = getApplicationPropertiesPath();

		// Cargar el archivo de propiedades
		Properties properties = new Properties();
		try (FileInputStream fis = new FileInputStream(applicationPropertiesPath)){
			properties.load(fis);
		}

		// Iniciar la aplicación Spring Boot con las propiedades cargadas
		SpringApplication app = new SpringApplication(CanchaManagerApplication.class);
		app.setDefaultProperties(properties);
		app.run(args);
	}

	private static String getApplicationPropertiesPath() {
		String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("win")) {
			return "C:/application.properties";
		} else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
			return "/home/ignacio/Escritorio/canchamanager/src/main/resources/application.properties";
		}
		throw new UnsupportedOperationException("Unsupported operating system: " + os);
	}
}
