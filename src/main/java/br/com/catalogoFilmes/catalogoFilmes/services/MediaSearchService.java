package br.com.catalogoFilmes.catalogoFilmes.services;

import br.com.catalogoFilmes.catalogoFilmes.client.ApiClient;
import br.com.catalogoFilmes.catalogoFilmes.dto.EpisodioDTO;
import br.com.catalogoFilmes.catalogoFilmes.dto.SerieDTO;
import br.com.catalogoFilmes.catalogoFilmes.dto.TemporadaDTO;
import br.com.catalogoFilmes.catalogoFilmes.util.JsonConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
public class MediaSearchService {
    private Scanner scanner = new Scanner(System.in);
    private ApiClient apiClient = new ApiClient();
    private JsonConverter jsonConverter = new JsonConverter();
    private final String ENDERECO = "http://www.omdbapi.com/?t=";
    private final String API_KEY;

    public MediaSearchService(@Value("${omdb.api.key}") String apiKey) {
        this.API_KEY = "&apikey=" + apiKey;
    }

    public void search(){
        System.out.println("Digite o nome da série: ");
        var nameSerie = scanner.nextLine();

        var json = apiClient.sendRequest(ENDERECO + nameSerie.replace(" ", "+") + API_KEY);

        //Instância um novo objeto SerieDTO e converte o json nesse objto
        SerieDTO serieDTO = jsonConverter.jsonConverter(json, SerieDTO.class);

        System.out.println(serieDTO);

        //Lista de temporadas
        List<TemporadaDTO> listaTemporadas = new ArrayList<>();

        for(int i = 1; i <= serieDTO.totalSeasons(); i++){
            json = apiClient.sendRequest(ENDERECO + nameSerie.replace(" ", "+") + "&season=" + i + API_KEY);
            TemporadaDTO temporadaDTO = jsonConverter.jsonConverter(json, TemporadaDTO.class);
            listaTemporadas.add(temporadaDTO);
        }

        //Para cada temporada na lista dê um System.out.println = t -> System.out.println(t)
        listaTemporadas.forEach(System.out::println);


        listaTemporadas.forEach(t -> {
            System.out.println("Temporada " + t.season());

            t.episodes().forEach(e-> System.out.println("Episódio " + e.episode() + " : " + e.title()));
        });
    }

}
