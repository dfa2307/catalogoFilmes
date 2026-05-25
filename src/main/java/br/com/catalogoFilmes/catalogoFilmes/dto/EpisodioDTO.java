package br.com.catalogoFilmes.catalogoFilmes.dto;

import com.google.gson.annotations.SerializedName;

public record EpisodioDTO(
        String title,
        Integer episode,
        @SerializedName("imdbRating")
        String imdbRating
) {
}
