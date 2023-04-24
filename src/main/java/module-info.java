module hr.algebra.cars_frontend_fx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires lombok;
    requires com.fasterxml.jackson.databind;

    opens hr.algebra.cars_frontend_fx to javafx.fxml;
    exports hr.algebra.cars_frontend_fx;
    exports hr.algebra.cars_frontend_fx.controller;
    exports hr.algebra.cars_frontend_fx.model;
    opens hr.algebra.cars_frontend_fx.controller to javafx.fxml;
}