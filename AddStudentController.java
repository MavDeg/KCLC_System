package kclc;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;
import kclc.common.Common;
import kclc.database.DBConfig;
import kclc.database.DBHandler;
import kclc.model.Student;
import kclc.model.Teacher;
import kclc.view.FadeInAnimationHelper;
import kclc.view.TransitionHelper;

import javax.imageio.ImageIO;
import javax.swing.*;
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
import java.util.Random;
import java.util.ResourceBundle;

public class AddStudentController {

    DBHandler dbStudent = new DBHandler();
    DBHandler dbTeacher = new DBHandler();
    Student student = new Student();;
    Teacher teacher = new Teacher();
    ResultSet resultSet_Student = null;
    ResultSet resultSet_Teacher = null;
    ArrayList<String> teachersName = new ArrayList<String>();

    boolean isExist = true;

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
    private TextField txtGuardian;

    @FXML
    private TextField txtPhone;

    @FXML
    private TextArea txtAddress;

    @FXML
    private JFXComboBox<String> cmbTeacher;

    @FXML
    private TextArea txtRemark;

    @FXML
    private JFXComboBox<String> cmbPaymentStatus;

    @FXML
    private TextField txtPayment;

    @FXML
    private JFXButton addNewStudent_btn_Cancel;

    @FXML
    private JFXButton addNewStudent_btn_Add;

    @FXML
    private DatePicker startDate;

    @FXML
    private DatePicker endDate;

    @FXML
    private JFXComboBox<String> cmbTimeStart;

    @FXML
    private JFXComboBox<String> cmbTimeEnd;

    @FXML
    private JFXComboBox<String> cmbAge;

    @FXML
    private JFXComboBox<String> cmbGradeLevel;

    @FXML
    private Label lblPleaseCompleteForm;

    @FXML
    private ImageView imgProfilePic;

    Image profilePic_toSave;
    String profilePic_toSave_Path = "";  // save in the relative path of the application. This will also be stored in database

    @FXML
    void initialize() throws SQLException {

        /* some minor adjustments in the gui */
        final String setStyle = "-fx-font-size: 14px;";
        startDate.setStyle(setStyle);
        endDate.setStyle(setStyle);
        cmbTimeStart.setStyle(setStyle);
        cmbTimeEnd.setStyle(setStyle);
        cmbAge.setStyle(setStyle);
        cmbGradeLevel.setStyle(setStyle);
        cmbPaymentStatus.setStyle(setStyle);

        // configure add and cancel button format
        File fileAddIcon = new File("src/kclc/view/assets/add_circle_white_24px.png");  // nodelist expand menu button
        Image imageAdd = new Image(fileAddIcon.toURI().toString());
        ImageView imgViewAddIcon= new ImageView(imageAdd);
        addNewStudent_btn_Add.setGraphic(imgViewAddIcon);

        // configure add and cancel button format
        File fileCancelIcon = new File("src/kclc/view/assets/cancel_circle_white_24px.png");  // nodelist expand menu button
        Image imageCancel = new Image(fileCancelIcon.toURI().toString());
        ImageView imgViewCancelIcon= new ImageView(imageCancel);
        addNewStudent_btn_Cancel.setGraphic(imgViewCancelIcon);


        /* some initialization settings */
        //lblPleaseCompleteForm.setText(""); // this label will be used to prompt for incorrect/incomplete form
        populateCmbBox();

        /**********************************************************************************
         * Add button
         *  - check all information is correct
         *  - check if already exist in DB, if yes, then provide hint to the user that tthe Student already exist
         *  - if not yet exist, then proceed to writing to database
         *  - notify the user if the new student has been added via a dialog box
         *  - return to main window
         **********************************************************************************/
        addNewStudent_btn_Add.setOnAction(actionEvent -> {
            try {
                if(getAllInfo()){  // all info OK?
                    if(!checkIfExist()){ // new student?
                        dbStudent.writeToDB(student);
                        dbStudent.closeConnection();
                        // go bak to main window
                        addStudentDialog();
                        //goBacktoMain(); //todo: show a dialog box that says Tasks has been added and an "OK" button will send back to MainWindow
                    }else{
                        lblPleaseCompleteForm.setText("Add Failed. \"" + student.getFirstName() + " " + student.getLastName() + "\" already exist.");
                    }
                    dbStudent.closeConnection();
                }

            } catch (SQLException | IOException throwables) {
                throwables.printStackTrace();
            } finally {
                // remember to close the db connection after used
                dbStudent.closeConnection();
            }
        });

        //Cancel button
        addNewStudent_btn_Cancel.setOnAction(actionEvent -> {
            // just go back to MainWindow
            goBacktoMain();
        });

        /* upload profile pic */
        imgProfilePic.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->{
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
     *  - Populate the value for all the comboBoxes: Time, Teacher, Adge, Grade Level
     **********************************************************************************/
    private void populateCmbBox() throws SQLException {

        // read Teacher DB, needed to populate the teacher comboBox
        if(dbTeacher.getConnection()) {
            this.resultSet_Teacher = dbTeacher.readFromDB(teacher);
        }
        while(resultSet_Teacher.next()){
            //System.out.println(resultSet_Teacher.getString(3));
            teachersName.add(resultSet_Teacher.getString(2) + " " + resultSet_Teacher.getString(4));
        }

        // remember to close the db connection after used
        dbTeacher.closeConnection();

        /* Age */
        ObservableList<String> age = FXCollections.observableArrayList();
        for(int i = 0; i <=20; i++){
            age.add(new String(Integer.toString(i)));
        }
        cmbAge.setItems(age);
        //cmbAge.getSelectionModel().select(2);  // default value

        /* Grade Level */
        ObservableList<String> gradeLevel = FXCollections.observableArrayList();
        for(int i = 0; i <=12; i++){
            gradeLevel.add(new String(Integer.toString(i)));
        }
        cmbGradeLevel.setItems(gradeLevel);
        //cmbGradeLevel.getSelectionModel().select(2);  // default value

        /* Time */
       DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        ObservableList<String> timeOfDay = FXCollections.observableArrayList();
        for(int h = 7; h <=20; h++){              //hour, selection is just from 7AM to 8PM
            for(int m = 0; m <= 30; m = m+30 ) {  // minutes, selection is just either 00 or 30
                LocalTime localTime = LocalTime.of(h,m);
                timeOfDay.add(localTime.format(timeFormatter));
            }
        }
        cmbTimeStart.setItems(timeOfDay);
        //cmbTimeStart.getSelectionModel().select(0); // default value
        cmbTimeEnd.setItems(timeOfDay);
        //cmbTimeEnd.getSelectionModel().select(2); // default value
        /* Teachers */

        ObservableList<String> teachers = FXCollections.observableArrayList(teachersName);  // todo: must get from Teacher DB
        cmbTeacher.setItems(teachers);

        //cmbTeacher.getSelectionModel().select(0); // default value
        /* Payment Status: Unpaid, Partial, Full */

        DBConfig  dbConfig = new DBConfig();
        ObservableList<String> paymentStatus = FXCollections.observableArrayList(
                dbConfig.DB_PAYMENT_STATUS_UNPAID, dbConfig.DB_PAYMENT_STATUS_PARTIAL, dbConfig.DB_PAYMENT_STATUS_FULL);
        cmbPaymentStatus.setItems(paymentStatus);
        //cmbPaymentStatus.getSelectionModel().select(0); // default value
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
        int age = 0;
        int gradeLevel = 0;
        String guardian = "";
        String phone = "";
        String address = "";
        LocalDate startDateLocal = LocalDate.now();
        LocalDate endDateLocal = LocalDate.now();
        LocalTime startTime = LocalTime.of(6, 0);
        LocalTime endTime = LocalTime.of(7,0);
        String paymentStatus = "Unpaid";
        Double payment = 0.0;
        int teacherId = 0;
        String remark = "";
        String profilePic = "";
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");


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

        if(cmbAge.getValue() != null) {
            age = Integer.valueOf(cmbAge.getValue().trim());
        } else {
            lblPleaseCompleteForm.setText("* Please enter age");
            return isInfoComplete = false;
        }

        if(cmbGradeLevel.getValue() != null) {
            gradeLevel = Integer.valueOf(cmbGradeLevel.getValue().trim()); // nullable
        }

        if(txtGuardian.getText() != "") {
            guardian = txtGuardian.getText().trim();
        }else{
            lblPleaseCompleteForm.setText("* Please enter parent/guardian");
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

        if( (startDate.getValue() != null) && (endDate.getValue() != null)){

            if(endDate.getValue().isEqual(startDate.getValue())){
                lblPleaseCompleteForm.setText("* Invalid. Start and End dates are the same.");
                return isInfoComplete = false;
            }else if(endDate.getValue().isBefore(startDate.getValue())) {
                lblPleaseCompleteForm.setText("* Invalid. End date is earlier than Start date.");
                return isInfoComplete = false;
            }else {
                startDateLocal = startDate.getValue();  // returns local date
                endDateLocal = endDate.getValue();      // returns local date
            }
        }else {
            lblPleaseCompleteForm.setText("* Please enter start and end dates.");
            return isInfoComplete = false;
        }

        if((cmbTimeStart.getValue() != null) && (cmbTimeEnd.getValue() !=null)){
            startTime = LocalTime.parse(cmbTimeStart.getValue(), DateTimeFormatter.ofPattern("hh:mm a"));  // this is the format used in the comboBox, String back to LocalTime
            endTime = LocalTime.parse(cmbTimeEnd.getValue(), DateTimeFormatter.ofPattern("hh:mm a"));  // this is the format used in the comboBox, String back to LocalTime

            if(endTime.compareTo(startTime) == 0){
                lblPleaseCompleteForm.setText("* Invalid. Start and End time are the same.");
                return isInfoComplete = false;
            }else if(endTime.compareTo(startTime) < 0){
                lblPleaseCompleteForm.setText("* Invalid. End time is earlier than Start time.");
                return isInfoComplete = false;
            }

        } else {
            lblPleaseCompleteForm.setText("* Please select schedule");
            return isInfoComplete = false;
        }


        if(cmbTeacher.getValue() != null) {
            // get the teacher ID based on the comboBox
            if(dbTeacher.getConnection()) {
                this.resultSet_Teacher = dbTeacher.readFromDB(teacher);
            }
            while(resultSet_Teacher.next()){
                //System.out.println(resultSet_Teacher.getString(3));
                //teachersName.add(resultSet_Teacher.getString(3) + " " + resultSet_Teacher.getString(5));
                String str_cmbTeacher = cmbTeacher.getValue().trim();
                String str_resultSet_Teacher = resultSet_Teacher.getString(2) + " " + resultSet_Teacher.getString(4);

                //System.out.println(str_cmbTeacher);
                //System.out.println(str_resultSet_Teacher);

                if(str_cmbTeacher.equals(str_resultSet_Teacher)){
                    teacherId = resultSet_Teacher.getInt(1); // get the ID of the teacher
                    break;
                }

            }
            // remember to close the db connection after used
            dbTeacher.closeConnection();
        } else{
            lblPleaseCompleteForm.setText("* Please select Teacher");
            return isInfoComplete = false;
        }

        if(cmbPaymentStatus.getValue() != null) {
            paymentStatus = cmbPaymentStatus.getValue().trim();
        } else{
            lblPleaseCompleteForm.setText("* Please select payment status");
            return isInfoComplete = false;
        }

        if(txtPayment.getText() != "") {
            //check if numbers (either Int or Double will do)
            try{
                payment = Double.parseDouble(txtPayment.getText().replace(",", "")); // remove also if there is "," separator
            }catch(Exception e){
                lblPleaseCompleteForm.setText("* Please a valid amount");
                return isInfoComplete = false;
            }

           // payment = Double.valueOf(txtPayment.getText().trim());
        } else {
            lblPleaseCompleteForm.setText("* Please enter payment");
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
        this.student.setFirstName(fName);
        this.student.setMiddleName(mName);
        this.student.setLastName(lName);
        this.student.setAge(age);
        this.student.setGradeLevel(gradeLevel);
        this.student.setGuardian(guardian);
        this.student.setPhone(phone);
        this.student.setAddress(address);
        this.student.setStartDate(startDateLocal);
        this.student.setEndDate(endDateLocal);
        this.student.setStartTime(startTime);
        this.student.setEndTime(endTime);
        this.student.setTeacherId(teacherId);
        this.student.setPaymentStatus(paymentStatus);
        this.student.setPayment(payment);
        this.student.setProfilePic(profilePic);
        this.student.setRemark(remark);

        // testing
        /* System.out.println("fName: " + student.getFirstName());
        System.out.println("mName: " + student.getMiddleName());
        System.out.println("lName: " + student.getLastName());
        System.out.println("age: " + student.getAge());
        System.out.println("gradeLevel: " + student.getGradeLevel());
        System.out.println("guardian: " + student.getGuardian());
        System.out.println("phone: " + student.getPhone());
        System.out.println("address: " + student.getAddress());
        System.out.println("startDateLocal: " + student.getStartDate());
        System.out.println("endDateLocal: " + student.getEndDate());
        System.out.println("schedule: " + student.getStartTime() + " " + student.getEndTime());
        System.out.println("teacherId: " + student.getTeacherId());
        System.out.println("paymentStatus: " + student.getPaymentStatus());
        System.out.println("payment: " + student.getPayment());
        System.out.println("remark: " + student.getRemark());*/

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
            imgProfilePic.setImage(profilePic_toSave);  // show in the ImageView
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
        if(dbStudent.getConnection()) {
            this.resultSet_Student = dbStudent.readFromDB(student);
        }
        while(resultSet_Student.next()){
            name = resultSet_Student.getString(2) + " " + resultSet_Student.getString(4);
            if(name.equalsIgnoreCase(student.getFirstName() + " " + student.getLastName())){
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
    private void addStudentDialog() throws SQLException {
        Alert dlg = new Alert(Alert.AlertType.INFORMATION);
        dlg.setTitle("Successfully added.");
        dlg.setContentText("Student " + this.student.getFirstName() + " is successfully added.");
        //dlg.setHeaderText("");
        dlg.show();

        dlg.setOnCloseRequest(event -> goBacktoMain()); // go back to main view

    }

    private void goBacktoMain(){
        TransitionHelper.windowFadeOutTransition(addNewStudent_btn_Add.getScene().getWindow()); // fade then hide
    }
}
