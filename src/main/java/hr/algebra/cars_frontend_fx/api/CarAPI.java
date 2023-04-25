package hr.algebra.cars_frontend_fx.api;

import hr.algebra.cars_frontend_fx.converter.JsonToCarDTOListConverter;
import hr.algebra.cars_frontend_fx.model.CarDTO;
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

    public void createCar(final CarDTO carToCreate) throws IOException {
        final HttpURLConnection postRequest = prepareConnection("http://localhost:8080/cars", HttpMethod.POST);
        sendDataToRestApi(carToCreate, postRequest);
    }

    public void updateCar(final CarDTO carToUpdate) throws IOException {
        final HttpURLConnection putRequest = prepareConnection("http://localhost:8080/cars", HttpMethod.PUT);
        sendDataToRestApi(carToUpdate, putRequest);
    }

    public void deleteCar(final CarDTO carToDelete) throws IOException {
        final HttpURLConnection deleteRequest = prepareConnection(
                "http://localhost:8080/cars/" + carToDelete.getId(),
                HttpMethod.DELETE
        );
        if (deleteRequest.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException("DELETE request failed...");
        }
    }

    private HttpURLConnection prepareConnection(final String host, final HttpMethod httpMethod) throws IOException {
        final URL url = new URL(host);
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(httpMethod.toString());
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");

        if (httpMethod == HttpMethod.POST || httpMethod == HttpMethod.PUT) {
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

    private void sendDataToRestApi(final CarDTO carModel, final HttpURLConnection httpRequest) throws IOException {
        final String jsonBody = carModel.toJson();

        try (OutputStream os = httpRequest.getOutputStream()) {
            byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        if (httpRequest.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new IOException("There is an error with sending data to the endpoint...");
        }
    }
}
