package hr.algebra.cars_frontend_fx.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarModel {
    private String brand;
    private String model;
    private String color;
    private Integer powerInHp;

    public String toJson() {
        return "{"
                + "\"brand\":\"" + brand + "\","
                + "\"model\":\"" + model + "\","
                + "\"color\":\"" + color + "\","
                + "\"powerInHp\":\"" + powerInHp
                + "\"}";
    }
}