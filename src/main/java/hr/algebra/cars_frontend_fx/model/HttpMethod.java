package hr.algebra.cars_frontend_fx.model;

public enum HttpMethod {
    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE");

    private final String method;

    HttpMethod(final String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return method;
    }
}
