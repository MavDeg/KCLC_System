package kclc;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import kclc.database.DBHandler;
import kclc.model.Admin;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane loginAnchorPane2;

    @FXML
    private ImageView loginImageLogo;

    @FXML
    private JFXButton loginButton;

    @FXML
    private TextField loginTextFieldUsername;

    @FXML
    private PasswordField loginPasswordField;

    @FXML
    private ImageView loginUsernameIcon;

    @FXML
    private ImageView loginPasswordIcon;

    @FXML
    private Label loginLabel_FailedMessage;

    /* internal object declaration (not coming from sceneBuilder) */
    DBHandler dbAdmin = new DBHandler();

    // configure add and cancel button format
    File fileLogoutIcon = new File("src/kclc/view/assets/login_36px.png");  // nodelist expand menu button
    Image imageLogout = new Image(fileLogoutIcon.toURI().toString());
    ImageView imgViewLogoutIcon= new ImageView(imageLogout);


    @FXML
    void initialize() {


        /*todo: for now, the New Admin Signup will not be implemented yet. Thus, make the signup link invisible. */
        //loginCreateNewUser.setVisible(false); not implemented for now

        /* put icon in the login button */
        //loginButton.setGraphic(imgViewLogoutIcon);

        /*****************************************************************************
         * - get the username and password in the textFields
         *  - call the loginAdmin where checking between the values in the textFields
         * and Admin database matches
         *****************************************************************************/
        loginButton.setOnAction(actionEvent -> {
            String loginText = loginTextFieldUsername.getText().trim();
            String loginPassword = loginPasswordField.getText().trim();

            if(!loginText.equals("") && !loginPassword.equals((""))){
                try {
                    loginAdmin(loginText, loginPassword);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }else{
                //System.out.println("Error login");
                loginLabel_FailedMessage.setText("Login Failed. Please try again.");
            }

        });

        loginPasswordField.setOnKeyPressed(searchListEvent ->{
            if(searchListEvent.getCode().equals(KeyCode.ENTER)) {
                String loginText = loginTextFieldUsername.getText().trim();
                String loginPassword = loginPasswordField.getText().trim();

                if(!loginText.equals("") && !loginPassword.equals((""))){
                    try {
                        loginAdmin(loginText, loginPassword);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }else{
                    //System.out.println("Error login");
                    loginLabel_FailedMessage.setText("Login Failed. Please try again.");
                }
            }
        });

        // signup new Admin (todo later: only the Company Owners can add new Admin)
        /*****************************************************************************
         *
         *****************************************************************************/
   /*
        loginCreateNewUser.setOnAction(actionEvent -> {
            // go to signup screen
            loginCreateNewUser.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/signup.fxml"));

            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene((root)));
            stage.showAndWait();

        });
*/
    }

    /*****************************************************************************
     - open database connection for the Admin
     *  - retrieve all the Admin data
     *  - if one of the Admin data matches, Login will be successful
     *  - Notify the User  with "red" text if login failed
     *****************************************************************************/
    private void loginAdmin(String userName, String password) throws SQLException {
        // check in the database if the admin exist
        // if successful, we take them to the main Window
        // this will also know who among the Admins is currently logged in

        boolean isFound = false;
        Admin admin = Admin.getInstance();

        ResultSet resultSet = null;

        if(dbAdmin.getConnection()) {
            resultSet = dbAdmin.readFromDB(admin);

            // check if the userName and password are valid, if yes, put it in the "admin" object
            while(resultSet.next()){
                if(userName.equals(resultSet.getString("userName")) && password.equals(resultSet.getString("password"))){
                    System.out.println("Welcome " + resultSet.getString("userName"));

                    admin.setAdminId(resultSet.getInt("adminId"));
                    admin.setUserName(resultSet.getString("userName"));
                    admin.setPassword(resultSet.getString("password"));
                    admin.setFirstName(resultSet.getString("firstName"));
                    admin.setLastName(resultSet.getString("lastName"));
                    admin.setPhone(resultSet.getString("phone"));
                    admin.setProfilePic(resultSet.getString("profilePic"));
                    admin.setAddress(resultSet.getString("address"));


                    gotoMainWindow();

                    isFound = true;
                    break;
                }
            }
            if(!isFound){
                // notify the User if login failed.
                loginLabel_FailedMessage.setText("Login Failed. Please try again.");
            }
        }
        // remember to close the db connection after used
        dbAdmin.closeConnection();
        //return  admin;

    }

    /*****************************************************************************
     * - If login is successful, let's proceed to go to the Main Window
     *   - goal: Hide the loginWindow and Show the MainWindow
     *****************************************************************************/
    private void gotoMainWindow(){
        // go to Main screen
        loginButton.getScene().getWindow().hide();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("view/mainWindow.fxml"));

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.setTitle("KCLC School Management System");
        stage.setResizable(false);
        stage.setScene(new Scene((root)));
        stage.show();
    }



}
