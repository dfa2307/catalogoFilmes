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
import java.util.*;
import java.util.stream.Collectors;

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

        try {
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
            System.out.println("####################################################################################");
            System.out.println("BUSCA DE EPISÓDIO: ");
            System.out.println("Digite o nome do episódio");
            String nomeEpisodio = scanner.nextLine();

            Optional<Episodio> episodioSearch = listaEpisodios.stream()
                    .filter(e -> e.getTitulo().toUpperCase().contains(nomeEpisodio.toUpperCase()))
                    .findFirst();

            if(episodioSearch.isPresent()){
                System.out.println("Episódio encontrado!");
                System.out.println("Temporada: " + episodioSearch.get().getNumeroTemporada()
                + " - Episódio: " + episodioSearch.get().getNumeroEpisodio()
                + " - " + episodioSearch.get().getTitulo());
            }else {
                System.out.println("Episódio não encontrado!");
            }
            System.out.println("####################################################################################");

            //Cria um map(key, value) com numero da temporada(key), media das avaliações(value)
            Map<Integer, Double> avaliacaoTemporadas = listaEpisodios.stream()
                    .filter(e -> e.getAvaliacao() > 0.0)
                    .collect(Collectors.groupingBy(Episodio::getNumeroTemporada, Collectors.averagingDouble(Episodio::getAvaliacao)));
                    //collect retorna para o map a chave pegando o Collectors(Episodio::getNumeroTemporada (CHAVE DO MAP), Collectors.averagingDouble(Episodio::getAvaliacao) (VALOR DO MAP)

            for (int i = 1; i <= serieDTO.totalSeasons(); i++){
                Double media = avaliacaoTemporadas.get(i);

                System.out.println("Temporada: " + i + " - Avaliação: " + String.format("%.1f", media));
            }

            System.out.println("####################################################################################");

            //Cria estatísticas, media, maior valor, menos valor, media com base na lista de episódios
            DoubleSummaryStatistics est = listaEpisodios.stream()
                    .filter(e -> e.getAvaliacao() > 0.0)
                    .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));

            Double mediaNotaEpisodio = est.getAverage();

            System.out.println("Episódios avaliados: " + est.getCount());
            System.out.println("Maior nota de episódio: " + est.getMax());
            System.out.println("Menor nota de episódio: " + est.getMin());
            System.out.println("Média das notas: " + String.format("%.1f", mediaNotaEpisodio));

        }catch (NullPointerException e){
            System.out.println("Nome digitado errado");
        }

    }

}
