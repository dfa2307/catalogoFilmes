package br.com.catalogoFilmes.catalogoFilmes.services;

import br.com.catalogoFilmes.catalogoFilmes.client.ApiClient;
import br.com.catalogoFilmes.catalogoFilmes.dto.EpisodioDTO;
import br.com.catalogoFilmes.catalogoFilmes.dto.SerieDTO;
import br.com.catalogoFilmes.catalogoFilmes.dto.TemporadaDTO;
import br.com.catalogoFilmes.catalogoFilmes.model.Episodio;
import br.com.catalogoFilmes.catalogoFilmes.util.JsonConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import static java.util.stream.Collectors.toList;

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

        System.out.println("####################################################################################");

        //Lista de episódios
        List<Episodio> listaEpisodios = listaTemporadas.stream()
                        .flatMap(t -> t.episodes().stream()
                                .map(e -> new Episodio(Integer.parseInt(t.season()), e)))
                        .toList();

        listaEpisodios.forEach(System.out::println);

        System.out.println("####################################################################################");

        System.out.println("TOP 5 Episódios: ");

        List<Episodio> listaEpisodiosFiltrados = listaEpisodios.stream()
                .sorted(Comparator.comparing(Episodio::getAvaliacao).reversed())
                .limit(5)
                .toList();

        listaEpisodiosFiltrados.forEach(System.out::println);

        System.out.println("####################################################################################");
        System.out.println("Pesquise episódios por data de lançamento:");

        System.out.println("Digite a partir do ano que deseja os episódio: ");
        int ano = scanner.nextInt();
        scanner.nextLine();

        //Cria uma variável e armazena no ano no formato de data
        //Para pegar os episodios que tenham lançado a partir do 01/01/ano(que o usuário digitar)
        LocalDate dataLancamento = LocalDate.of(ano, 1, 1);

        //Formatar data par ao padrão que usamos
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");


        listaEpisodios.stream()
                .filter(e-> e.getDataLancamento().isAfter(dataLancamento))
                .forEach(e -> {
                    System.out.println("Temporada: " + e.getNumeroTemporada()
                    + " - Episódio: " + e.getNumeroEpisodio()
                    + " - " + e.getTitulo()
                    + " - " + e.getDataLancamento().format(formatter));
                });
    }

}
