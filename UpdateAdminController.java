package kclc;

import com.jfoenix.controls.JFXButton;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import kclc.common.Common;
import kclc.database.DBHandler;
import kclc.model.Admin;
import kclc.model.Student;
import kclc.model.Teacher;
import kclc.view.TransitionHelper;

import javax.imageio.ImageIO;


public class UpdateAdminController {
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
    private JFXButton cancel;

    @FXML
    private JFXButton update;

    @FXML
    private Label lblPleaseCompleteForm;

    @FXML
    private ImageView profilePic;
    Image profilePic_toSave;
    String profilePic_toSave_Path = "";  // save in the relative path of the application. This will also be stored in database

    @FXML
    private TextField txtUsername;


    @FXML
    private TextField txtFirstname;

    @FXML
    private TextField txtLastname;

    @FXML
    private TextField txtPhone;

    @FXML
    private TextField txtAddress;

    @FXML
    private PasswordField txtNewPassword;

    @FXML
    private PasswordField txtRetypeNewPassword;


    DBHandler dbAdmin = new DBHandler();
    ResultSet resultSet_Admin = null;
    Admin admin = Admin.getInstance();

    @FXML
    void initialize() {


        // initialize with current values
        txtUsername.setText(admin.getUserName());
        txtNewPassword.setText(admin.getPassword());
        txtRetypeNewPassword.setText(""); // will be used if password is changed
        txtFirstname.setText(admin.getFirstName());
        txtLastname.setText(admin.getLastName());
        txtPhone.setText(admin.getPhone());
        txtAddress.setText(admin.getAddress());
        File fileImg = new File(admin.getProfilePic());  // get the stored image
        Image image = new Image(fileImg.toURI().toString());
        profilePic_toSave_Path = admin.getProfilePic();
        profilePic_toSave = image;
        profilePic.setImage(image);

        /* upload profile pic */
        profilePic.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->{
            if(event.getClickCount() == 2) {  // double clicked
                try {
                    getProfilePicture();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("image is pressed");
            }
        });


        update.setOnAction(updateEvent ->{

            if(dbAdmin.getConnection()){
                try {
                    if(getAllInfo()){
                        dbAdmin.updateDB(admin);
                        dbAdmin.closeConnection();
                        updateAdminDialog();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            dbAdmin.closeConnection();
        });

        cancel.setOnAction(actionEvent ->{
            goBacktoMain();
        });
    }


    private boolean getAllInfo() throws IOException {

        boolean isInfoComplete = true;
        String userName = "";
        String newPassword = "";
        String retypeNewPassword = "";
        String fName ="";
        String lName = "";
        String phone = "";
        String address = "";
        String profilePic = "";

        lblPleaseCompleteForm.setText("");

        if(txtUsername.getText() != ""){
            userName = txtUsername.getText();
        }
        else{
            lblPleaseCompleteForm.setText("Username is empty");
            return isInfoComplete = false;
        }

        // check if the txtNewPassword field contains atleast 6 characters
        if(txtNewPassword.getText().length() < 6){
            lblPleaseCompleteForm.setText("Password must be atleast 6 characters long");
            return isInfoComplete = false;
        }else{
            // check first if the currentPassword in DB is the same as the current one in the newPassword field
            if(admin.getPassword().equals(txtNewPassword.getText().trim())){
                // admin don't want to change password, so just maintain the existing password
                newPassword = admin.getPassword();
            }else{
                // check the retype password, they should be identical
                if(txtNewPassword.getText().trim().equals(txtRetypeNewPassword.getText().trim())){
                    newPassword = txtNewPassword.getText().trim();
                }else{
                    lblPleaseCompleteForm.setText("Password doesn't match");
                    return isInfoComplete = false;
                }
            }
        }

        if(txtFirstname.getText() != ""){
            fName = txtFirstname.getText();
        }
        else{
            lblPleaseCompleteForm.setText("Firstname is empty");
            return isInfoComplete = false;
        }

        if(txtLastname.getText() != ""){
            lName = txtLastname.getText();
        }
        else{
            lblPleaseCompleteForm.setText("Lastname is empty");
            return isInfoComplete = false;
        }

        if(txtPhone.getText() != ""){
            phone = txtPhone.getText();
        }
        else{
            lblPleaseCompleteForm.setText("Phone is empty");
            return isInfoComplete = false;
        }


        if(profilePic_toSave_Path != ""){
            saveResizedPicture();
            profilePic = profilePic_toSave_Path;
            System.out.println(profilePic);
        }else{
            profilePic = "src/kclc/view/assets/profile_generic_icon_96px.png";
        }

        if(txtAddress.getText() != ""){
            address = txtAddress.getText();
        }
        else{
            lblPleaseCompleteForm.setText("Address is empty");
            return isInfoComplete = false;
        }

        //if all good, proceed on updating the admin (singleton) value

        admin.setUserName(userName);
        admin.setPassword(newPassword);
        admin.setFirstName(fName);
        admin.setLastName(lName);
        admin.setPhone(phone);
        admin.setAddress(address);
        admin.setProfilePic(profilePic);

        return isInfoComplete;
    }


    /**********************************************************************************
     ** Adding profile pic in the the student information
     * - the actual image will be resized and save in the application path "images/students" folder
     * - the path then will be copied to the database "picture" column
     **********************************************************************************/

    private void getProfilePicture() throws IOException {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose an image");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"));

        File selectedFile = fileChooser.showOpenDialog(stage);
        if(selectedFile != null) {
            profilePic_toSave = new Image(selectedFile.toURI().toString(), 150.0, 150.0, false, false); // resize image
            profilePic_toSave_Path = "src/kclc/database/images/admins/" + Common.randomAlphanumeric() + ".png";
            profilePic.setImage(profilePic_toSave);  // show in the ImageView
            //saveResizedPicture(profilePic_toSave);  <-- call this in the add button
        }

    }

    private void saveResizedPicture() throws IOException {
        /*copy also the path and write in database*/
        File fileoutput = new File(profilePic_toSave_Path);
        BufferedImage bufImage = SwingFXUtils.fromFXImage(profilePic_toSave, null);

        ImageIO.write(bufImage, "png",fileoutput); // save the image in the given path
    }

    /**********************************************************************************
     *  - will prompt that the new student has been added in the database
     *  - go back to mainWindow after closing
     **********************************************************************************/
    private void updateAdminDialog() throws SQLException {
        Alert dlg = new Alert(Alert.AlertType.INFORMATION);
        dlg.setTitle("Successfully updated.");
        dlg.setContentText("Admin " + admin.getUserName() + " information is successfully updated.");
        //dlg.setHeaderText("");
        dlg.show();

        dlg.setOnCloseRequest(event -> goBacktoMain()); // go back to main view

    }
    private void goBacktoMain(){
        TransitionHelper.windowFadeOutTransition(update.getScene().getWindow()); // fade then hide
    }
}
