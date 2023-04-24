package hr.algebra.cars_frontend_fx.api;

import hr.algebra.cars_frontend_fx.converter.JsonToCarDTOListConverter;
import hr.algebra.cars_frontend_fx.model.CarDTO;
import lombok.AllArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class CarAPI {

    private final JsonToCarDTOListConverter carDTOListConverter;


    public List<CarDTO> getAllCars() throws IOException {
        final HttpURLConnection getRequest = prepareConnection("http://localhost:8080/cars/all", "GET");

        if (getRequest.getResponseCode() == HttpURLConnection.HTTP_OK) {
            final String response = getResponseBody(getRequest);
            System.out.println(response);

            final Optional<List<CarDTO>> cars = carDTOListConverter.convert(response);
            return cars.orElseThrow(() -> new IOException("Deserialization failed..."));
        } else {
            throw new IOException("GET Request failed...");
        }
    }

    private HttpURLConnection prepareConnection(final String host, final String httpMethod) throws IOException {
        final URL url = new URL(host);
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(httpMethod);
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        return connection;
    }

    private String getResponseBody(final HttpURLConnection request) throws IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
        String inputLine;
        final StringBuilder response = new StringBuilder();

        while ((inputLine = reader.readLine()) != null) {
            response.append(inputLine);
        }
        reader.close();
        return response.toString();
    }
}

//Converter: https://www.javatpoint.com/convert-json-to-map-in-java
//HTTP Methods: https://www.javaguides.net/2019/07/java-http-getpost-request-example.html
