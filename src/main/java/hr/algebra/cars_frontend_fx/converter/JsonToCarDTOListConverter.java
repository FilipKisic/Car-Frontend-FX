package hr.algebra.cars_frontend_fx.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hr.algebra.cars_frontend_fx.model.CarDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JsonToCarDTOListConverter implements Converter<String, Optional<List<CarDTO>>> {
    @Override
    public Optional<List<CarDTO>> convert(String json) {
        Optional<List<CarDTO>> carsList;
        try {
            carsList = Optional.of(new ObjectMapper().readValue(json, new TypeReference<ArrayList<CarDTO>>() {
            }));
        } catch (Exception e) {
            carsList = Optional.empty();
        }
        return carsList;
    }
}
