package es.psp.sutil_mesa_unidad2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Launcher Class
 */
@SpringBootApplication
@ComponentScan(basePackages = "es.psp.sutil_mesa_unidad2")
public class SutilMesaUnidad2Application
{
    /**
     * The main entry point for the application.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args)
    {
        SpringApplication.run(SutilMesaUnidad2Application.class, args);
    }

}
