package hr.algebra.cars_frontend_fx.api;

import hr.algebra.cars_frontend_fx.converter.JsonToCarDTOListConverter;
import hr.algebra.cars_frontend_fx.model.CarDTO;
import hr.algebra.cars_frontend_fx.model.CarModel;
import hr.algebra.cars_frontend_fx.model.HttpMethod;
import lombok.AllArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class CarAPI {

    private final JsonToCarDTOListConverter carDTOListConverter;


    public List<CarDTO> getAllCars() throws IOException {
        final HttpURLConnection getRequest = prepareConnection("http://localhost:8080/cars/all", HttpMethod.GET);

        if (getRequest.getResponseCode() == HttpURLConnection.HTTP_OK) {
            final String response = getResponseBody(getRequest);
            final Optional<List<CarDTO>> cars = carDTOListConverter.convert(response);
            return cars.orElseThrow(() -> new IOException("Deserialization failed..."));
        } else {
            throw new IOException("GET Request failed...");
        }
    }

    public void createCar(CarModel carToCreate) throws IOException {
        final HttpURLConnection postRequest = prepareConnection("http://localhost:8080/cars", HttpMethod.POST);
        final String jsonBody = carToCreate.toJson();

        try (OutputStream os = postRequest.getOutputStream()) {
            byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(postRequest.getInputStream(), StandardCharsets.UTF_8))) {
            final StringBuilder response = new StringBuilder();
            String responseLine;
            while ((responseLine = bufferedReader.readLine()) != null) {
                response.append(responseLine.trim());
            }
            if (postRequest.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException("There is an error with endpoint");
            }
        }
    }

    private HttpURLConnection prepareConnection(final String host, final HttpMethod httpMethod) throws IOException {
        final URL url = new URL(host);
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(httpMethod.toString());
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");

        if (httpMethod == HttpMethod.POST) {
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
        }

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
