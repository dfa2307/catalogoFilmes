package br.com.catalogoFilmes.catalogoFilmes.util;

public interface IJsonConverter {
    <T> T jsonConverter(String json, Class<T> classe);
}
