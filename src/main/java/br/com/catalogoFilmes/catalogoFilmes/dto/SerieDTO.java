package br.com.catalogoFilmes.catalogoFilmes.dto;

public record SerieDTO(
        String title,
        String year,
        String genre,
        String totalSeasons,
        String imdbRating
) {
}
