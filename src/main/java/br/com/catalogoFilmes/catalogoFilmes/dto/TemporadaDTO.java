package br.com.catalogoFilmes.catalogoFilmes.dto;

import java.util.List;

public record TemporadaDTO(
        String season,
        List<EpisodioDTO> episodes
) {
}
