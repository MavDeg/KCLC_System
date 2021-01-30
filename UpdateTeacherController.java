package kclc;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import kclc.common.Common;
import kclc.database.DBConfig;
import kclc.database.DBHandler;
import kclc.model.Student;
import kclc.model.Student_Singleton;
import kclc.model.Teacher;
import kclc.model.Teacher_Singleton;
import kclc.view.TransitionHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class UpdateTeacherController {
    Teacher_Singleton teacher_singleton = Teacher_Singleton.getInstance();
    Teacher teacher_to_update =  teacher_singleton.getTeacher();

    DBHandler dbTeacher = new DBHandler();
    Teacher teacher = new Teacher();
    ResultSet resultSet_Teacher = null;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane rootAnchorPane;

    @FXML
    private ImageView logo;

    @FXML
    private Label lblUpdateTeacher;

    @FXML
    private TextField txtFirstName;

    @FXML
    private TextField txtMiddleName;

    @FXML
    private TextField txtLastName;

    @FXML
    private TextField txtPhone;

    @FXML
    private TextArea txtAddress;

    @FXML
    private TextArea txtRemark;

    @FXML
    private JFXButton updateTeacher_btn_Cancel;

    @FXML
    private JFXButton updateTeacher_btn_Update;

    @FXML
    private DatePicker employmentDate;

    @FXML
    private Label lblPleaseCompleteForm;

    @FXML
    private ImageView teacher_profilePic;
    Image profilePic_toSave;
    String profilePic_toSave_Path = "";  // save in the relative path of the application. This will also be stored in databse

    @FXML
    void initialize() throws SQLException {

        // Populate the form with the current value from the Teacher object
        populateWithCurrentData();

        // Recopy the new update back to the Teacher database (use DB UPDATE)
        /**********************************************************************************
         * Update button
         *  - check all information is correct
         *  - if all good, then proceed to writing to database
         *  - notify the user if the new student has been added via a dialog box
         *  - return to main window
         **********************************************************************************/
        updateTeacher_btn_Update.setOnAction(actionEvent -> {
            try {
                if(getAllInfo()){  // all info OK?
                    dbTeacher.getConnection(); // open a connection again
                    dbTeacher.updateDB(teacher_to_update);
                    dbTeacher.closeConnection();
                    updateTeacherDialog();
                        //goBacktoMain(); //todo: show a dialog box that says Tasks has been added and an "OK" button will send back to MainWindow
                    dbTeacher.closeConnection();
                }

            } catch (SQLException | IOException throwables) {
                throwables.printStackTrace();
            } finally {
                // remember to close the db connection after used
                dbTeacher.closeConnection();
            }
        });

        //Cancel button
        updateTeacher_btn_Cancel.setOnAction(actionEvent -> {
            // just go back to MainWindow
            goBacktoMain();
        });

        /* upload profile pic */
        teacher_profilePic.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->{
            if(event.getClickCount() == 2) {  // double clicked
                try {
                    getProfilePicture();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("image is pressed");
            }
        });
    }

    /**********************************************************************************
     *  - get the current data of the Teacher to be updated
     *
     **********************************************************************************/
    private void populateWithCurrentData() throws SQLException {
        txtFirstName.setText(teacher_to_update.getFirstName());
        txtMiddleName.setText(teacher_to_update.getMiddleName());
        txtLastName.setText(teacher_to_update.getLastName());
        // Phone
        txtPhone.setText(teacher_to_update.getPhone());
        // Address
        txtAddress.setText(teacher_to_update.getAddress());
        // Remark
        txtRemark.setText(teacher_to_update.getRemark());
        /* Employment Date */
        employmentDate.setValue(teacher_to_update.getEmploymentDate());

        File fileImg = new File(teacher_to_update.getProfilePic());  // get the stored image
        Image image = new Image(fileImg.toURI().toString());
        profilePic_toSave_Path = teacher_to_update.getProfilePic();
        profilePic_toSave = image;
        teacher_profilePic.setImage(image);


    }

    /**********************************************************************************
     *  - retrieve all the inputs from the user
     *  - check if everything is ok, otherwise provide hints to the user about the incorrect/incomplete data
     *  - if everything is OK, copy all the information to the student objectin preparation to writing to database
     *  - Notify the User  with "red" text if login failed
     **********************************************************************************/
    private boolean getAllInfo() throws SQLException, IOException {

        boolean isInfoComplete = true;
        String fName ="";
        String mName = "";
        String lName = "";
        String phone = "";
        String address = "";
        LocalDate employmentDateLocal = LocalDate.now();
        String profilePic = "";
        String remark = "";


        if(txtFirstName.getText() != ""){
            fName = txtFirstName.getText().trim();
        } else {
            lblPleaseCompleteForm.setText("* Please enter first name");
            return isInfoComplete = false;
        }

        if(txtMiddleName.getText() != "") mName = txtMiddleName.getText().trim(); // nullable

        if(txtLastName.getText() != "") {
            lName = txtLastName.getText().trim();
        } else {
            lblPleaseCompleteForm.setText("* Please enter last name");
            return isInfoComplete = false;
        }

        if(txtPhone.getText() != ""){
            phone = txtPhone.getText().trim();
        } else {
            lblPleaseCompleteForm.setText("* Please enter phone number");
            return isInfoComplete = false;
        }

        if(txtAddress.getText() != "") {
            address = txtAddress.getText().trim();
        }else {
            lblPleaseCompleteForm.setText("* Please enter address");
            return isInfoComplete = false;
        }

        if(employmentDate.getValue() != null){
            employmentDateLocal = employmentDate.getValue();  //local date
        }else {
            lblPleaseCompleteForm.setText("* Please enter date.");
            return isInfoComplete = false;
        }

        if(profilePic_toSave_Path != ""){
            saveResizedPicture();
            profilePic = profilePic_toSave_Path;
            System.out.println(profilePic);
        }else{
            profilePic = "src/kclc/view/assets/profile_generic_icon_96px.png";
        }

        if(txtRemark.getText() != "") {
            //remark = String.valueOf(txtRemark.getParagraphs()).trim(); // nullable
            remark = txtRemark.getText().trim();
        }

        lblPleaseCompleteForm.setText(""); // information is complete, empty the label

        // if all info is OK, then build the student object with those values
        teacher_to_update.setFirstName(fName);
        teacher_to_update.setMiddleName(mName);
        teacher_to_update.setLastName(lName);
        teacher_to_update.setPhone(phone);
        teacher_to_update.setAddress(address);
        teacher_to_update.setEmploymentDate(employmentDateLocal);
        teacher_to_update.setProfilePic(profilePic);
        teacher_to_update.setRemark(remark);


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
            profilePic_toSave_Path = "src/kclc/database/images/students/" + Common.randomAlphanumeric() + ".png";
            teacher_profilePic.setImage(profilePic_toSave);  // show in the ImageView
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
    private void updateTeacherDialog() throws SQLException {
        Alert dlg = new Alert(Alert.AlertType.INFORMATION);
        dlg.setTitle("Successfully updated.");
        dlg.setContentText("Teacher " + teacher_to_update.getFirstName() + " is successfully updated.");
        //dlg.setHeaderText("");
        dlg.show();

        dlg.setOnCloseRequest(event -> goBacktoMain()); // go back to main view

    }

    private void goBacktoMain(){
        TransitionHelper.windowFadeOutTransition(updateTeacher_btn_Update.getScene().getWindow()); // fade then hide
    }
}
