package kclc;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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

public class UpdateStudentController {
    Student_Singleton student_singleton = Student_Singleton.getInstance();
    Student student_to_update =  student_singleton.getStudent();

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

    DBHandler dbTeacher = new DBHandler();
    DBHandler dbStudent = new DBHandler();

    ResultSet resultSet_Teacher = null;
    ResultSet resultSet_Student= null;

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

        // Open databases
        if(dbStudent.getConnection()) {
            resultSet_Student = dbStudent.readFromDB(new Student());
        }
        dbStudent.closeConnection();

        // Populate the form with the current value from the Student object
        populateWithCurrentData();

        // Recopy the new update back to the Student database (use DB UPDATE)
        addNewStudent_btn_Add.setOnAction(actionEvent -> {
            try {
                if(getAllInfo()){  // all info OK?
                    dbStudent.getConnection(); // open a connection again
                    dbStudent.updateDB(student_to_update);
                    dbStudent.closeConnection();
                    updateStudentDialog();
                }else{
                    //lblPleaseCompleteForm.setText("Add Failed. \"" + student_to_update.getFirstName() + " " + student_to_update.getLastName() + "\" already exist.");
                }

            } catch (SQLException | IOException throwables) {
                throwables.printStackTrace();
            } finally {
                // remember to close the db connection after used
                dbStudent.closeConnection();
            }
        });

        addNewStudent_btn_Cancel.setOnAction(actionEvent ->{
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
     *  - get the current data of the Student to be updated
     *
     **********************************************************************************/
    private void populateWithCurrentData() throws SQLException {
        txtFirstName.setText(student_to_update.getFirstName());
        txtMiddleName.setText(student_to_update.getMiddleName());
        txtLastName.setText(student_to_update.getLastName());

        /* Age */
        // populate the combobox and then set the selected item based on the value of the singleton
        int age_index = 0;
        ObservableList<String> age = FXCollections.observableArrayList();
        for(int i = 0; i <=20; i++){
            age.add(new String(Integer.toString(i)));

        }
        cmbAge.setItems(age);
        cmbAge.getSelectionModel().select(student_to_update.getAge());  // get the age to update


        // Grade Level
        ObservableList<String> gradeLevel = FXCollections.observableArrayList();
        for(int i = 0; i <=12; i++){
            gradeLevel.add(new String(Integer.toString(i)));
        }
        cmbGradeLevel.setItems(gradeLevel);

        if(student_to_update.getGradeLevel()  >= 0) {
            cmbGradeLevel.getSelectionModel().select(student_to_update.getGradeLevel());  // get the gradeLevel to update
        }

        // Guardian
        txtGuardian.setText(student_to_update.getGuardian());

        // Phone
        txtPhone.setText(student_to_update.getPhone());

        // Address
        txtAddress.setText(student_to_update.getAddress());

        // profile picture
        File fileImg = new File(student_to_update.getProfilePic());  // get the stored image
        Image image = new Image(fileImg.toURI().toString());
        profilePic_toSave_Path = student_to_update.getProfilePic();
        profilePic_toSave = image;
        imgProfilePic.setImage(image);

        // Remark
        txtRemark.setText(student_to_update.getRemark());


        /* Date */
        startDate.setValue(student_to_update.getStartDate());
        endDate.setValue(student_to_update.getEndDate());


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
        // retrieve the current start time and show it to the user
        AtomicInteger currentTime_Index = new AtomicInteger();
        timeOfDay.forEach((time) -> {
            LocalTime currentStartTime =student_to_update.getStartTime();
            String currentStartTime_toString = currentStartTime.format(timeFormatter);
            if(time.equalsIgnoreCase(currentStartTime_toString)){
                cmbTimeStart.getSelectionModel().select(currentTime_Index.get()); // default value
            }
            currentTime_Index.getAndIncrement();

        });
        cmbTimeStart.setItems(timeOfDay);;


        cmbTimeEnd.setItems(timeOfDay);
        // retrieve the current end time and show it to the user
        AtomicInteger currentEndTime_Index = new AtomicInteger();
        timeOfDay.forEach((time) -> {
            LocalTime currentEndTime =student_to_update.getEndTime();
            String currentEndTime_toString = currentEndTime.format(timeFormatter);
            if(time.equalsIgnoreCase(currentEndTime_toString)){
                cmbTimeEnd.getSelectionModel().select(currentEndTime_Index.get()); // default value
            }
            currentEndTime_Index.getAndIncrement();

        });
        cmbTimeEnd.setItems(timeOfDay);;


        /* Teacher */
        ArrayList<String> teachersName = new ArrayList<String>();
        if(dbTeacher.getConnection()) {
            resultSet_Teacher = dbTeacher.readFromDB(new Teacher());
            while(resultSet_Teacher.next()){
                // get firstname and last name
                teachersName.add(resultSet_Teacher.getString(2) + " " + resultSet_Teacher.getString(4));
            }
        }
        // get the list of names, to be used to populate the comboBox
        ObservableList<String> teachers = FXCollections.observableArrayList(teachersName);
        cmbTeacher.setItems(teachers);
        dbTeacher.closeConnection();



        if(dbTeacher.getConnection()) {
            resultSet_Teacher = dbTeacher.readFromDB(new Teacher());
            // get now the index where the teacherId matches
            int index = 0;// this will hold the count where the teacherID matches
            while(resultSet_Teacher.next()) {
                System.out.println(resultSet_Teacher.getInt(1) );
                // get firstname and last name
                if (resultSet_Teacher.getInt(1) == student_to_update.getTeacherId()) {
                    System.out.println("index is: " + index);
                    break;

                }
                index++;
            }
            cmbTeacher.getSelectionModel().select(index); // show current value
            dbTeacher.closeConnection();
        }

        /* Payment Status */
        ArrayList<String> str_paymentStatus = new ArrayList<String>();
        DBConfig dbConfig = new DBConfig();
        str_paymentStatus.add(dbConfig.DB_PAYMENT_STATUS_UNPAID);
        str_paymentStatus.add(dbConfig.DB_PAYMENT_STATUS_PARTIAL);
        str_paymentStatus.add(dbConfig.DB_PAYMENT_STATUS_FULL);
        ObservableList<String> observableList_paymentStatus = FXCollections.observableArrayList(str_paymentStatus);

        cmbPaymentStatus.setItems(observableList_paymentStatus);

        AtomicInteger i = new AtomicInteger();
        observableList_paymentStatus.forEach((result)->{
            if(result.equals(student_to_update.getPaymentStatus())){
                cmbPaymentStatus.getSelectionModel().select(i.get()); // get the current value
            }
            i.getAndIncrement();
        });


        /* Payment */
        txtPayment.setText(String.valueOf(student_to_update.getPayment()));

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
                resultSet_Teacher = dbTeacher.readFromDB(new Teacher());
                while(resultSet_Teacher.next()) {

                    String str_cmbTeacher = cmbTeacher.getValue().trim();
                    String str_resultSet_Teacher = resultSet_Teacher.getString(2) + " " + resultSet_Teacher.getString(4);

                    System.out.println(str_cmbTeacher);
                    System.out.println(str_resultSet_Teacher);
                    System.out.println(resultSet_Teacher.getInt(1));
                    if (str_cmbTeacher.equals(str_resultSet_Teacher)) {

                        teacherId = resultSet_Teacher.getInt(1); // get the ID of the teacher
                        break;
                    }
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
                payment = Double.parseDouble(txtPayment.getText());
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
        student_to_update.setFirstName(fName);
        student_to_update.setMiddleName(mName);
        student_to_update.setLastName(lName);
        student_to_update.setAge(age);
        student_to_update.setGradeLevel(gradeLevel);
        student_to_update.setGuardian(guardian);
        student_to_update.setPhone(phone);
        student_to_update.setAddress(address);
        student_to_update.setStartDate(startDateLocal);
        student_to_update.setEndDate(endDateLocal);
        student_to_update.setStartTime(startTime);
        student_to_update.setEndTime(endTime);
        student_to_update.setTeacherId(teacherId);
        student_to_update.setPaymentStatus(paymentStatus);
        student_to_update.setPayment(payment);
        student_to_update.setProfilePic(profilePic);
        student_to_update.setRemark(remark);

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
     *  - will prompt that the new student has been added in the database
     *  - go back to mainWindow after closing
     **********************************************************************************/
    private void updateStudentDialog() throws SQLException {
        Alert dlg = new Alert(Alert.AlertType.INFORMATION);
        dlg.setTitle("Successfully updated.");
        dlg.setContentText("Student " + student_to_update.getFirstName() + " information is successfully updated.");
        //dlg.setHeaderText("");
        dlg.show();

        dlg.setOnCloseRequest(event -> goBacktoMain()); // go back to main view

    }
    private void goBacktoMain(){
        TransitionHelper.windowFadeOutTransition(addNewStudent_btn_Add.getScene().getWindow()); // fade then hide
    }
}
