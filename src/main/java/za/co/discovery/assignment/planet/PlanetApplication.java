package za.co.discovery.assignment.planet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"za.co.discovery.assignment.planet"})
public class PlanetApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlanetApplication.class, args);
	}
}
