package hr.algebra.cars_frontend_fx.converter;

public interface Converter<S,T>{
    T convert(S source);
}
