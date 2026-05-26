package br.com.catalogoFilmes.catalogoFilmes;

import br.com.catalogoFilmes.catalogoFilmes.client.ApiClient;
import br.com.catalogoFilmes.catalogoFilmes.dto.EpisodioDTO;
import br.com.catalogoFilmes.catalogoFilmes.dto.TemporadaDTO;
import br.com.catalogoFilmes.catalogoFilmes.services.MediaSearchService;
import br.com.catalogoFilmes.catalogoFilmes.util.JsonConverter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CatalogoFilmesApplication  implements CommandLineRunner{

    private final MediaSearchService service;

    public CatalogoFilmesApplication(MediaSearchService service) {
        this.service = service;
    }

    public static void main(String[] args) {
		SpringApplication.run(CatalogoFilmesApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {
        service.search();
    }

}
