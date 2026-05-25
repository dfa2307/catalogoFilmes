package br.com.catalogoFilmes.catalogoFilmes.dto;

import com.google.gson.annotations.SerializedName;

public record EpisodioDTO(
        String title,
        String episode,
        @SerializedName("imdbRating")
        String imdbRating,
        @SerializedName("Season")
        String season
) {
}
