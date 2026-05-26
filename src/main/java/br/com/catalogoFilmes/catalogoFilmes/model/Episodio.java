package br.com.catalogoFilmes.catalogoFilmes.model;

import br.com.catalogoFilmes.catalogoFilmes.dto.EpisodioDTO;

import java.time.LocalDate;

public class Episodio {
    private String titulo;
    private int numeroEpisodio;
    private double avaliacao;
    private int numeroTemporada;
    private LocalDate dataLancamento;

    public Episodio(int numeroTemporada, EpisodioDTO episodioDTO) {
        this.titulo = episodioDTO.title();
        this.numeroEpisodio = Integer.parseInt(episodioDTO.episode());
        try{
            this.avaliacao = Double.parseDouble(episodioDTO.imdbRating());
        } catch (NumberFormatException e) {
            this.avaliacao = 0.0;
        }

        this.numeroTemporada = numeroTemporada;
        this.dataLancamento = LocalDate.parse(episodioDTO.released());
    }

    public String getTitulo() {
        return titulo;
    }

    public int getNumeroEpisodio() {
        return numeroEpisodio;
    }

    public double getAvaliacao() {
        return avaliacao;
    }

    public int getNumeroTemporada() {
        return numeroTemporada;
    }

    public LocalDate getDataLancamento() {
        return dataLancamento;
    }

    @Override
    public String toString() {
        return "Temporada: " + getNumeroTemporada()
                +" - Episódio: " + getNumeroEpisodio() + " - "
                + getTitulo()
                +" - Lançamento: " + getDataLancamento()
                +" - Avaliação: " + getAvaliacao();
    }
}
