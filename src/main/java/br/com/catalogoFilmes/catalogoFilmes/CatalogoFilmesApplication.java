package br.com.catalogoFilmes.catalogoFilmes;

import br.com.catalogoFilmes.catalogoFilmes.client.ApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CatalogoFilmesApplication  implements CommandLineRunner{

    @Value("${omdb.api.key}")
    private String apiKey;

	public static void main(String[] args) {
		SpringApplication.run(CatalogoFilmesApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {
        ApiClient apiClient = new ApiClient();

        String url = "http://www.omdbapi.com/?t=Minions&apikey=" + apiKey ;

        System.out.println(apiClient.sendRequest(url));
    }

}
