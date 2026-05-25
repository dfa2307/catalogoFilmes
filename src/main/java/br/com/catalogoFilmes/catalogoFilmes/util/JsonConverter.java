package br.com.catalogoFilmes.catalogoFilmes.util;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonConverter implements IJsonConverter{

    //GsonBuilder().setFieldNamingPolicy -> Usado para converter o padrão de nomenclatura Java para nomenclatura JSON
    private Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();

    @Override
    public <T> T jsonConverter(String json, Class<T> classe) {
        return gson.fromJson(json, classe);
    }
}
