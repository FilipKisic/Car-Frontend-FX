package hr.algebra.cars_frontend_fx.controller;

import hr.algebra.cars_frontend_fx.api.CarAPI;
import hr.algebra.cars_frontend_fx.converter.JsonToCarDTOListConverter;
import hr.algebra.cars_frontend_fx.model.CarDTO;
import hr.algebra.cars_frontend_fx.model.CarModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;

import java.io.IOException;

public class MainScreenController {
    private final CarAPI carApi;
    @FXML
    public TableView tvCars;
    @FXML
    public TableColumn tcId;
    @FXML
    public TableColumn tcBrand;
    @FXML
    public TableColumn tcModel;
    @FXML
    public TableColumn tcColor;
    @FXML
    public TableColumn tcPower;
    @FXML
    public TextField tfId;
    @FXML
    public TextField tfBrand;
    @FXML
    public TextField tfModel;
    @FXML
    public TextField tfPower;
    @FXML
    public TextField tfColor;
    @FXML
    public Button btnClear;
    @FXML
    public Button btnCreate;
    @FXML
    public Button btnUpdate;
    @FXML
    public Button btnDelete;

    public MainScreenController() {
        final JsonToCarDTOListConverter converter = new JsonToCarDTOListConverter();
        this.carApi = new CarAPI(converter);
    }

    @FXML
    public void initialize() {
        refreshDataInTable();
    }

    @FXML
    public void onBtnClearPressed() {
        tvCars.getSelectionModel().clearSelection();
        clearAllFields();
    }

    @FXML
    public void onBtnCreatePressed() {
        try {
            carApi.createCar(getCarModelFromUser());
            tvCars.refresh();
            clearAllFields();
            refreshDataInTable();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    @FXML
    public void onBtnUpdatePressed() {
    }

    @FXML
    public void onBtnDeletePressed() {
    }

    private void refreshDataInTable() {
        try {
            final ObservableList<CarDTO> observableListOfCars = FXCollections.observableList(carApi.getAllCars());
            populateTableWithInitialData(observableListOfCars);
            setOnClickListenerOnRow();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).showAndWait();
        }
    }

    private void populateTableWithInitialData(final ObservableList<CarDTO> observableListOfCars) {
        tcId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        tcModel.setCellValueFactory(new PropertyValueFactory<>("model"));
        tcColor.setCellValueFactory(new PropertyValueFactory<>("color"));
        tcPower.setCellValueFactory(new PropertyValueFactory<>("powerInHp"));

        tvCars.setItems(observableListOfCars);
    }

    private void setOnClickListenerOnRow() {
        tvCars.setRowFactory(tableView -> {
            final TableRow<CarDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY) {
                    fillTextFieldsWithData(row.getItem());
                }
            });
            return row;
        });
    }

    private void fillTextFieldsWithData(final CarDTO carDTO) {
        tfId.setText(carDTO.getId().toString());
        tfBrand.setText(carDTO.getBrand());
        tfModel.setText(carDTO.getModel());
        tfColor.setText(carDTO.getColor());
        tfPower.setText(carDTO.getPowerInHp().toString());
    }

    private CarModel getCarModelFromUser() {
        return CarModel.builder()
                .brand(tfBrand.getText())
                .model(tfModel.getText())
                .color(tfColor.getText())
                .powerInHp(Integer.parseInt(tfPower.getText()))
                .build();
    }

    private void clearAllFields() {
        tfId.clear();
        tfBrand.clear();
        tfModel.clear();
        tfColor.clear();
        tfPower.clear();
    }
}
