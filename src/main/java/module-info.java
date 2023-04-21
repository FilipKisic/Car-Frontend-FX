module hr.algebra.cars_frontend_fx {
    requires javafx.controls;
    requires javafx.fxml;
            
                        requires org.kordamp.bootstrapfx.core;
            
    opens hr.algebra.cars_frontend_fx to javafx.fxml;
    exports hr.algebra.cars_frontend_fx;
    exports hr.algebra.cars_frontend_fx.controller;
    opens hr.algebra.cars_frontend_fx.controller to javafx.fxml;
}