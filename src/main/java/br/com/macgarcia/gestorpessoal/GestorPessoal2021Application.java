package br.com.macgarcia.gestorpessoal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(enableDefaultTransactions = false)
public class GestorPessoal2021Application {

	public static void main(String[] args) {
		SpringApplication.run(GestorPessoal2021Application.class, args);
	}

}
