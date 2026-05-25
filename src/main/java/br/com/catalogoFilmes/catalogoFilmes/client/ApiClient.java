package br.com.catalogoFilmes.catalogoFilmes.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiClient {

    public String sendRequest(String url){
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();
        HttpResponse<String> response = null;

        try{
            //Converte a o corpo da resposta em HttpResponse.BodyHandlers.ofString())
            response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());
        }catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }// IOException - Erros de Rede, servidor fora do ar
         // InterruptedException - A thread foi interrompida enquanto esperava a resposta

        String json = response.body();
        return json;
    }
}
