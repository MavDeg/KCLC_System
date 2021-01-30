package kclc;

import com.jfoenix.controls.JFXButton;
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
import kclc.database.DBHandler;
import kclc.model.Student;
import kclc.model.Teacher;
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

public class AddTeacherController {

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
    private Label lblAddNewStudent;

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
    private JFXButton addNewTeacher_btn_Cancel;

    @FXML
    private JFXButton addNewTeacher_btn_Add;

    @FXML
    private DatePicker employmentDate;

    @FXML
    private Label lblPleaseCompleteForm;

    @FXML
    private ImageView teacher_profilePic;

    Image profilePic_toSave;
    String profilePic_toSave_Path = "";  // save in the relative path of the application. This will also be stored in database

    @FXML
    void initialize() {

        // configure add and cancel button format
        File fileAddIcon = new File("src/kclc/view/assets/add_circle_white_24px.png");  // nodelist expand menu button
        Image imageAdd = new Image(fileAddIcon.toURI().toString());
        ImageView imgViewAddIcon= new ImageView(imageAdd);
        addNewTeacher_btn_Add.setGraphic(imgViewAddIcon);

        // configure add and cancel button format
        File fileCancelIcon = new File("src/kclc/view/assets/cancel_circle_white_24px.png");  // nodelist expand menu button
        Image imageCancel = new Image(fileCancelIcon.toURI().toString());
        ImageView imgViewCancelIcon= new ImageView(imageCancel);
        addNewTeacher_btn_Cancel.setGraphic(imgViewCancelIcon);

        /**********************************************************************************
         * Add button
         *  - check all information is correct
         *  - check if already exist in DB, if yes, then provide hint to the user that the Teacher already exist
         *  - if not yet exist, then proceed to writing to database
         *  - notify the user if the new student has been added via a dialog box
         *  - return to main window
         **********************************************************************************/
        addNewTeacher_btn_Add.setOnAction(actionEvent -> {
            try {
                if(getAllInfo()){  // all info OK?
                    if(!checkIfExist()){ // new student?
                        dbTeacher.writeToDB(teacher);
                        dbTeacher.closeConnection();
                        // go bak to main window
                        addTeacherDialog();
                        //goBacktoMain(); //todo: show a dialog box that says Tasks has been added and an "OK" button will send back to MainWindow
                    }else{
                        lblPleaseCompleteForm.setText("Add Failed. \"" + teacher.getFirstName() + " " + teacher.getLastName() + "\" already exist.");
                    }
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
        addNewTeacher_btn_Cancel.setOnAction(actionEvent -> {
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
        LocalDate startDate = LocalDate.now();
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
            startDate = employmentDate.getValue();  //local date
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
        this.teacher.setFirstName(fName);
        this.teacher.setMiddleName(mName);
        this.teacher.setLastName(lName);
        this.teacher.setPhone(phone);
        this.teacher.setAddress(address);
        this.teacher.setEmploymentDate(startDate);
        this.teacher.setProfilePic(profilePic);
        this.teacher.setRemark(remark);

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
            profilePic_toSave_Path = "src/kclc/database/images/teachers/" + Common.randomAlphanumeric() + ".png";
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
     *  - check if key information like "first name" and "last name already exist *
     **********************************************************************************/
    private boolean checkIfExist() throws SQLException {
        String name = "";
        if(dbTeacher.getConnection()) {
            this.resultSet_Teacher = dbTeacher.readFromDB(teacher);
        }
        while(resultSet_Teacher.next()){
            name = resultSet_Teacher.getString(2) + " " + resultSet_Teacher.getString(4);
            if(name.equalsIgnoreCase(teacher.getFirstName() + " " + teacher.getLastName())){
                return true;
            }
        }
        // remember to close the db connection after used
        //dbStudent.closeConnection();
        return false;
    }

    /**********************************************************************************
     *  - will prompt that the new student has been added in the database
     *  - go back to mainWindow after closing
     **********************************************************************************/
    private void addTeacherDialog() throws SQLException {
        Alert dlg = new Alert(Alert.AlertType.INFORMATION);
        dlg.setTitle("Successfully added.");
        dlg.setContentText("Teacher " + this.teacher.getFirstName() + " is successfully added.");
        //dlg.setHeaderText("");
        dlg.show();

        dlg.setOnCloseRequest(event -> goBacktoMain()); // go back to main view

    }

    private void goBacktoMain(){
        TransitionHelper.windowFadeOutTransition(addNewTeacher_btn_Add.getScene().getWindow()); // fade then hide
    }
}
