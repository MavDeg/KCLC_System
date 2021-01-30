package kclc;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import kclc.database.DBHandler;
import kclc.model.CashBook;
import kclc.view.TransitionHelper;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AddCashbookItemController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private ImageView logo;

    @FXML
    private Label lblAddNewStudent;

    @FXML
    private TextArea txtDescription = new TextArea();

    @FXML
    private JFXButton addNewCashBookItem_btn_Cancel;

    @FXML
    private JFXButton addNewCashBookItem_btn_Add;

    @FXML
    private Label lblPleaseCompleteForm;

    @FXML
    private JFXRadioButton radioButton_Income;

    @FXML
    private JFXRadioButton radioButton_Expense;

    ToggleGroup radioBtnGroup = new ToggleGroup();


    @FXML
    private TextField txtAmount = new TextField();

    CashBook cashbook = new CashBook();
    DBHandler cashBookDB = new DBHandler();
    ResultSet resultSet_CashBook = null;

    @FXML
    void initialize() {

        /*Initialize fields*/
        radioButton_Income.setSelectedColor(Color.web("ff7d00ff"));
        radioButton_Income.setSelected(true);
        radioButton_Expense.setSelectedColor(Color.web("ff7d00ff"));

        /* group radio buttons */
        radioButton_Income.setToggleGroup(radioBtnGroup);
        radioButton_Expense.setToggleGroup(radioBtnGroup);

        addNewCashBookItem_btn_Add.setOnAction(addEvent ->{

            // retrieve accounts DB
            if(cashBookDB.getConnection()) {
                try {
                    if (prepareData()) {  // all info OK?
                        try {
                            cashBookDB.writeToDB(cashbook);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        cashBookDB.closeConnection();
                        // go bak to main window
                        //addAnotherItem();
                        goBacktoMain(); //todo: show a dialog box that says Tasks has been added and an "OK" button will send back to MainWindow
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

    }

    private boolean prepareData() throws SQLException {

        Double amount  = 0.0;
        boolean isInfoComplete = true;
        Double newBankBalance = 0.0;

        /* get the last row to get the latest bank balance.
        this is going to be either add/minus the income/expense part*/
        resultSet_CashBook = cashBookDB.readLastRowDB(cashbook);

        while(resultSet_CashBook.next()){
            newBankBalance = resultSet_CashBook.getDouble(1);
        }

        cashbook.setDate(LocalDate.now());
        if(txtDescription.getText() != ""){
            cashbook.setDescription(txtDescription.getText());
        }else{
            lblPleaseCompleteForm.setText("* Please put description");
        }

        if(txtAmount.getText() != "") {
            //check if numbers (either Int or Double will do)
            try{
                amount = Double.parseDouble(txtAmount.getText().replace(",", ""));  // remove also if there is "," separator
            }catch(Exception e){
                lblPleaseCompleteForm.setText("* Please a valid amount");
                return isInfoComplete = false;
            }
        } else {
            lblPleaseCompleteForm.setText("* Please enter amount");
            return isInfoComplete = false;
        }

        if(radioButton_Income.isSelected()) {
            cashbook.setIncome(amount);
            cashbook.setBankBalance(newBankBalance + amount);
            cashbook.setExpense(0.0);
        }else if(radioButton_Expense.isSelected()){
            cashbook.setIncome(0.0);
            cashbook.setExpense(amount);
            cashbook.setBankBalance(newBankBalance - amount);
        }else{
            lblPleaseCompleteForm.setText("* Please select: Income or Expense");
            return isInfoComplete = false;
        }

        return isInfoComplete;
    }

    private void goBacktoMain(){
        TransitionHelper.windowFadeOutTransition(addNewCashBookItem_btn_Add.getScene().getWindow()); // fade then hide
    }
}
