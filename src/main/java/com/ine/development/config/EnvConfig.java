package com.ine.development.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Clase de configuración para la gestión de variables de entorno utilizando la biblioteca Dotenv.
 * Proporciona un bean para acceder a las variables de entorno y un método estático para cargarlas
 * en las propiedades del sistema.
 */
@Configuration
public class EnvConfig {

    /**
     * Define un bean de Spring para Dotenv, que permite cargar variables de entorno desde un archivo `.env`.
     *
     * @return una instancia configurada de Dotenv.
     */
    @Bean
    public Dotenv dotenv(){
        return Dotenv.configure()
                .filename(".env")
                .ignoreIfMissing()
                .load();
    }

    /**
     * Método estático para cargar las variables de entorno desde un archivo `.env` y establecerlas
     * como propiedades del sistema. Esto permite que las variables estén disponibles globalmente
     * en la aplicación.
     */
    public static void loadEnv() {
        Dotenv dotenv = Dotenv.configure()
                .filename(".env")
                .directory("./")
                .ignoreIfMissing()
                .load();

        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
    }
}