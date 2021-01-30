package kclc.common;

import javafx.scene.control.Alert;
import javafx.util.converter.IntegerStringConverter;

import java.sql.SQLException;

public class CustomIntegerStringConverter extends IntegerStringConverter {
    private final IntegerStringConverter converter = new IntegerStringConverter();

    @Override
    public String toString(Integer object) {
        try {
            return converter.toString(object);
        } catch (NumberFormatException e) {
            showAlert();
        }
        return null;
    }

    @Override
    public Integer fromString(String string) {
        try {
            return converter.fromString(string);
        } catch (NumberFormatException e) {
            showAlert();
        }
        return -1;
    }

    private void showAlert(){
        Alert dlg = new Alert(Alert.AlertType.INFORMATION);
        dlg.setTitle("Successfully added.");
        dlg.setContentText("Please enter a valid number.");
        //dlg.setHeaderText("");
        dlg.show();

        dlg.setOnCloseRequest(event -> {

        });
    }
}
