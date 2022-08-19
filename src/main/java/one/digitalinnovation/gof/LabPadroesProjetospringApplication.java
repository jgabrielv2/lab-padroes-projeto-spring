package one.digitalinnovation.gof;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class LabPadroesProjetospringApplication {

	public static void main(String[] args) {
		SpringApplication.run(LabPadroesProjetospringApplication.class, args);
	}

}
