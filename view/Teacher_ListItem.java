package kclc.view;

import com.jfoenix.controls.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import kclc.database.DBHandler;
import kclc.model.*;
import org.sqlite.core.DB;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Optional;

public class Teacher_ListItem extends JFXListCell<Teacher> {

    DBHandler teacherDB = new DBHandler();
    DBHandler studentDB = new DBHandler();
    ResultSet resultSet_Teacher = null;
    ResultSet resultSet_Student = null;

    // new format
    private AnchorPane rootAnchorPane = new AnchorPane();

    File fileImg = new File("src/kclc/view/assets/profile_generic_icon_96px.png");  // get the default image resource
    private Image imageDefaultProfilePic = new Image(fileImg.toURI().toString());
    private ImageView imgProfile = new ImageView(imageDefaultProfilePic);


    // node list (for expandable menu item
    private JFXNodesList nodelist = new JFXNodesList();

    File fileMenuIcon = new File("src/kclc/view/assets/menu_white.png");  // nodelist expand menu button
    private Image imageMenu = new Image(fileMenuIcon.toURI().toString());
    private ImageView imgViewMenuIcon= new ImageView(imageMenu);

    File fileAddIcon = new File("src/kclc/view/assets/add_white.png");  // nodelist expand menu button
    private Image imageAdd = new Image(fileAddIcon.toURI().toString());
    private ImageView imgViewAddIcon= new ImageView(imageAdd);

    File fileUpdateIcon = new File("src/kclc/view/assets/update_white.png");  // nodelist expand menu button
    private Image imageUpdate= new Image(fileUpdateIcon.toURI().toString());
    private ImageView imgViewUpdateIcon= new ImageView(imageUpdate);

    File fileDeleteIcon = new File("src/kclc/view/assets/remove_white.png");  // nodelist expand menu button
    private Image imageDelete = new Image(fileDeleteIcon.toURI().toString());
    private ImageView imgViewDeleteIcon= new ImageView(imageDelete);

    File fileMenuCollapseIcon = new File("src/kclc/view/assets/menu_open_white.png");  // nodelist expand menu button
    private Image imageMenuCollapse = new Image(fileMenuCollapseIcon.toURI().toString());
    private ImageView imgViewMenuCollapseIcon= new ImageView(imageMenuCollapse);


    public JFXButton btnMenu = new JFXButton();
    public JFXButton btnAdd = new JFXButton();
    public JFXButton btnUpdate = new JFXButton();
    public JFXButton btnDelete = new JFXButton();
    public JFXButton btnCollapseMenu = new JFXButton();


    private Label lblNameTag = new Label("Name:");
    private Label lblName = new Label("");
    private Label lblPhoneTag = new Label("Phone:");
    private Label lblPhone = new Label("");
    private Label lblAddressTag = new Label("Address:");
    private Label lblAddress = new Label("");
    private Label lblEmploymentDateTag = new Label("Joined since:");
    private Label lblEmploymentDate = new Label("");
    private Label lblRemarkTag = new Label("Remark:");
    private Label lblRemark = new Label("");
    private Label lblStudentsTag = new Label("Students:");
    //private Label lblStudents = new Label("");


    //private HBox studentsPanel = new HBox();
   // private JFXMasonryPane assignedStudents = new JFXMasonryPane();
    private JFXListView<Label> listAssignedStudents = new JFXListView();
    private ObservableList<Label> assignedStudents;


    /**********************************************************************************
     *  - Format has been hardcoded but used view/Reference_Student_ListItem.fxml as
     *  reference.
     ***********************************************************************************/
    public Teacher_ListItem(Teacher teacher){
        super();

        rootAnchorPane.setPrefHeight(200.0);
        rootAnchorPane.setPrefWidth(900.0);
        rootAnchorPane.setStyle("-fx-border-width: 3; -fx-border-color: #00b8d4;");

        imgProfile.setImage(imageDefaultProfilePic);
        imgProfile.setLayoutX(20.0);
        imgProfile.setLayoutY(25.0);
        imgProfile.setFitHeight(140.0);
        imgProfile.setFitWidth(140.0);
        imgProfile.setPickOnBounds(true);
        //imgProfile.setPreserveRatio(true);

        //btnUpdate.setPrefHeight(34.0);
        //btnUpdate.setPrefWidth(81.0);
        //btnUpdate.setLayoutX(900.0);
        //btnUpdate.setLayoutY(15.0);
        //btnUpdate.setTextFill(Color.WHITE);
        //btnUpdate.setStyle("-fx-background-color: #90a4ae;");
        //btnDelete.setPrefHeight(34.0);
        //btnDelete.setPrefWidth(81.0);
        //btnDelete.setLayoutX(900.0);
        //btnDelete.setLayoutY(53.0);
        //btnDelete.setTextFill(Color.WHITE);
        //btnDelete.setStyle("-fx-background-color: #90a4ae;");

        final String btnStyle_BGColor = "-fx-background-color: #13DEF0;";
        final double btn_preWidth = 100.0;
        final Pos btnAlignment = Pos.BASELINE_LEFT;

        nodelist.setLayoutX(950.0);
        nodelist.setLayoutY(35.0);

        //btnMenu.setPrefWidth(btn_preWidth);
        btnMenu.setStyle(btnStyle_BGColor);
        btnMenu.setTooltip(new Tooltip("Expand menu"));
        btnMenu.setGraphic(imgViewMenuIcon);
        //btnMenu.setOnAction(e->nodelist.animateList(true));
        btnMenu.setOnAction(e->{      // toggle expanded and collapse
            if(nodelist.isExpanded()) {
                //btnCollapseMenu.setGraphic(imgViewMenuIcon);
                nodelist.animateList(true);
            }else{
                //btnCollapseMenu.setGraphic(imgViewMenuCollapseIcon);
                nodelist.animateList(false);
            }
        });

        nodelist.setOnMouseExited(e->{nodelist.animateList(false);});  // auto collapse menu

        btnAdd.setPrefWidth(btn_preWidth);
        btnAdd.setStyle(btnStyle_BGColor);
        btnAdd.setAlignment(btnAlignment);
        btnAdd.setTooltip(new Tooltip("Add new student"));
        btnAdd.setGraphic(imgViewAddIcon);
        btnAdd.setText("Add");

        btnUpdate.setPrefWidth(btn_preWidth);
        btnUpdate.setStyle(btnStyle_BGColor);
        btnUpdate.setAlignment(btnAlignment);
        btnUpdate.setTooltip(new Tooltip("Update student information"));
        btnUpdate.setGraphic(imgViewUpdateIcon);
        btnUpdate.setText("Update");

        btnDelete.setPrefWidth(btn_preWidth);
        btnDelete.setStyle(btnStyle_BGColor);
        btnDelete.setAlignment(btnAlignment);
        btnDelete.setTooltip(new Tooltip("Delete student information"));
        btnDelete.setGraphic(imgViewDeleteIcon);
        btnDelete.setText("Delete");

        btnCollapseMenu.setPrefWidth(btn_preWidth);
        btnCollapseMenu.setStyle(btnStyle_BGColor);
        btnCollapseMenu.setAlignment(btnAlignment);
        btnCollapseMenu.setTooltip(new Tooltip("Collapse menu"));
        btnCollapseMenu.setGraphic(imgViewMenuCollapseIcon);
        btnCollapseMenu.setOnAction(e->nodelist.animateList(false));
        btnCollapseMenu.setText("Hide Menu");

        nodelist.setAlignment(Pos.BASELINE_LEFT);
        nodelist.addAnimatedNode(btnMenu);
        nodelist.addAnimatedNode(btnAdd);
        nodelist.addAnimatedNode(btnUpdate);
        nodelist.addAnimatedNode(btnDelete);
        //nodelist.addAnimatedNode(btnCollapseMenu);



        final Color lblTagTextFill =  Color.web("#757575");
        final Font lblTagFont = (Font.font("", FontWeight.BOLD, 14));
        final Color lblTextFill =  Color.BLACK;
        final Font lblFont =  (Font.font("", FontWeight.NORMAL, 14));

        final Double lblTagHeight = 20.0;
        final Double lblTagWidth = 110.0;
        final Double lblHeight = 20.0;
        final Double lblWidth = 300.0;//450.0;

        lblNameTag.setPrefHeight(lblTagHeight);
        lblNameTag.setPrefWidth(lblTagWidth);
        lblNameTag.setTextFill(lblTagTextFill);
        lblNameTag.setFont(lblTagFont);
        lblNameTag.setLayoutX(180.0);
        lblNameTag.setLayoutY(20.0);
        lblName.setPrefHeight(lblHeight);
        lblName.setPrefWidth(lblWidth);
        lblName.setTextFill(lblTextFill);
        lblName.setFont(lblFont);
        lblName.setLayoutX(285.0);
        lblName.setLayoutY(20.0);

        lblPhoneTag.setPrefHeight(lblTagHeight);
        lblPhoneTag.setPrefWidth(lblTagWidth);
        lblPhoneTag.setTextFill(lblTagTextFill);
        lblPhoneTag.setFont(lblTagFont);
        lblPhoneTag.setLayoutX(180.0);
        lblPhoneTag.setLayoutY(55.0);
        lblPhone.setPrefWidth(lblWidth);
        lblPhone.setTextFill(lblTextFill);
        lblPhone.setFont(lblFont);
        lblPhone.setLayoutX(285.0);
        lblPhone.setLayoutY(55.0);


        lblEmploymentDateTag.setPrefHeight(lblTagHeight);
        lblEmploymentDateTag.setPrefWidth(lblTagWidth);
        lblEmploymentDateTag.setTextFill(lblTagTextFill);
        lblEmploymentDateTag.setFont(lblTagFont);
        lblEmploymentDateTag.setLayoutX(180.0);
        lblEmploymentDateTag.setLayoutY(80.0);
        lblEmploymentDate.setPrefHeight(lblHeight);
        lblEmploymentDate.setPrefWidth(lblWidth);
        lblEmploymentDate.setTextFill(lblTextFill);
        lblEmploymentDate.setFont(lblFont);
        lblEmploymentDate.setLayoutX(285.0);
        lblEmploymentDate.setLayoutY(80.0);

        lblAddressTag.setPrefHeight(lblTagHeight);
        lblAddressTag.setPrefWidth(lblTagWidth);
        lblAddressTag.setTextFill(lblTagTextFill);
        lblAddressTag.setFont(lblTagFont);
        lblAddressTag.setLayoutX(180.0);
        lblAddressTag.setLayoutY(105.0);
        lblAddress.setPrefHeight(lblHeight);
        lblAddress.setPrefWidth(lblWidth);
        lblAddress.setTextFill(lblTextFill);
        lblAddress.setFont(lblFont);
        lblAddress.setLayoutX(285.0);
        lblAddress.setLayoutY(105.0);

        lblRemarkTag.setPrefHeight(lblTagHeight);
        lblRemarkTag.setPrefWidth(lblTagWidth);
        lblRemarkTag.setTextFill(lblTagTextFill);
        lblRemarkTag.setFont(lblTagFont);
        lblRemarkTag.setLayoutX(180.0);
        lblRemarkTag.setLayoutY(130.0);
        lblRemark.setPrefHeight(lblHeight+20);
        lblRemark.setAlignment(Pos.TOP_LEFT);
        lblRemark.setTextAlignment(TextAlignment.LEFT);
        lblRemark.setWrapText(true);
        lblRemark.setPrefWidth(lblWidth);
        lblRemark.setTextFill(lblTextFill);
        lblRemark.setFont(lblFont);
        lblRemark.setLayoutX(285.0);
        lblRemark.setLayoutY(130.0);

        // todo: check if can put links to each of those name
        lblStudentsTag.setPrefHeight(lblTagHeight);
        lblStudentsTag.setPrefWidth(lblTagWidth);
        lblStudentsTag.setTextFill(lblTagTextFill);
        lblStudentsTag.setFont(lblTagFont);
        lblStudentsTag.setLayoutX(600.0);
        lblStudentsTag.setLayoutY(20.0);


        //assignedStudents.setPrefWidth(lblWidth);
        //assignedStudents.setMaxWidth(lblWidth);
        //assignedStudents.setMaxHeight(50.0);
        //assignedStudents.setPrefHeight(50.0);
        //assignedStudents.setCellHeight(14);
        //assignedStudents.setLayoutX(600.0);
        //assignedStudents.setLayoutY(40.0);

        listAssignedStudents.setPrefWidth(lblWidth - 10.0);
        listAssignedStudents.setMaxWidth(lblWidth - 10.0);
        listAssignedStudents.setMaxHeight(130.0);
        listAssignedStudents.setPrefHeight(130.0);
        listAssignedStudents.setLayoutX(600.0);
        listAssignedStudents.setLayoutY(50.0);


        //assignedStudents.getChildren().addAll(new Label("something"), new Label("just like this"));


        rootAnchorPane.getChildren().addAll(imgProfile, nodelist,
                lblNameTag, lblName, lblPhoneTag, lblPhone, lblEmploymentDateTag, lblEmploymentDate,
                lblAddressTag,lblAddress, lblRemarkTag, lblRemark,lblStudentsTag,listAssignedStudents);//,lblStudents);

    }

    @Override
    public void updateItem(Teacher teacher, boolean empty){
        super.updateItem(teacher, empty);
        setText(null);
        setGraphic(null);

        if(teacher != null && !empty){
            try {
                populateRow(teacher);
                setGraphic(rootAnchorPane);

                //button clicks
                // ADD
                btnAdd.setOnAction(addAction ->{
                    TransitionHelper.buttonPresstoOtherWindowTransition("addTeacher.fxml");
                });
                // UPDATE
                btnUpdate.setOnAction(updateAction ->{
                    System.out.println("button update of each cell is clicked");
                    //go to the Update student window, passing the particular student object
                    if(teacherDB.getConnection()){
                        try {
                            resultSet_Teacher = teacherDB.readFromDB(teacher);
                            while(resultSet_Teacher.next()){
                                // compare the studentId to delete
                                if(resultSet_Teacher.getInt(1) == teacher.getTeacherId()){
                                    System.out.println("button update is called?");
                                    Teacher_Singleton teacher_singleton = Teacher_Singleton.getInstance();
                                    teacher_singleton.setTeacher(teacher);
                                    TransitionHelper.buttonPresstoOtherWindowTransition("updateTeacher.fxml");
                                }
                            }
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }

                    }
                    //getListView().getItems().remove(getItem());
                    System.out.println("button update of each cell is clicked");
                });

                // DELETE
                btnDelete.setOnAction(deleteAction ->{

                    if(teacherDB.getConnection()){
                        try {
                            resultSet_Teacher = teacherDB.readFromDB(teacher);
                            while(resultSet_Teacher.next()){
                                // compare the studentId to delete
                                if(resultSet_Teacher.getInt(1) == teacher.getTeacherId()){
                                    System.out.println("button delete is called?");
                                    //studentDB.deleteFromDB(student);
                                    deleteTeacherDialog(teacher);
                                }
                            }
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }

                    }
                    //getListView().getItems().remove(getItem());
                    System.out.println("button delete of each cell is clicked");
                });

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
    }


    /**********************************************************************************
     *  - will provide all the details needed to fill a row
     ***********************************************************************************/
    private void populateRow(Teacher teacher) throws SQLException {

        if(teacher.getMiddleName() != "") {  // Full Name
            lblName.setText(teacher.getFirstName() + " " + teacher.getMiddleName() + " " + teacher.getLastName());
        }else{
            lblName.setText(teacher.getFirstName() + " " + teacher.getLastName());
        }

        lblPhone.setText(teacher.getPhone()); // phone
        lblAddress.setText(teacher.getAddress()); // address

        LocalDate employmentDate = teacher.getEmploymentDate();
        String employmentDateFormat = employmentDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
        lblEmploymentDate.setText(employmentDateFormat);  // date employed

        // show profile pic
        if(teacher.getProfilePic() != null){
            File fileImg = new File(teacher.getProfilePic());  // get the stored image
            Image image = new Image(fileImg.toURI().toString());
            imgProfile.setImage(image);
        }

        if(teacher.getRemark() != ""){
            lblRemark.setText(teacher.getRemark());
        }

        // get all the students assigned to the teacher
        //ResultSet resultSet_Teacher =  null;
        if(studentDB.getConnection()) { // student

            assignedStudents = FXCollections.observableArrayList();
            Font lblStudentFont = (Font.font("", FontWeight.SEMI_BOLD, 14));

            resultSet_Student = studentDB.readFromDB(new Student());
            while(resultSet_Student .next()){
                if(resultSet_Student.getInt(14) == teacher.getTeacherId()){
                    //Hyperlink assignedStudent_Name = new Hyperlink();
                    Label assignedStudent_Name = new Label();
                    assignedStudent_Name.setFont(lblStudentFont);

                    String name = resultSet_Student.getString(2);
                    LocalTime startTime = LocalTime.parse(resultSet_Student.getString(12));
                    LocalTime endTime = LocalTime.parse(resultSet_Student.getString(13));
                    String startTimeFormat = startTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));
                    String endTimeFormat = endTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));

                    assignedStudent_Name.setText(name + " ( sched: " + startTimeFormat + " - " + endTimeFormat + " )");
                    assignedStudents.add(assignedStudent_Name);

                }
            }

            listAssignedStudents.setItems(assignedStudents);

            //getListView().refresh();
           // assignedStudents = assignedStudents_local;
            //Platform.runLater(()->assignedStudents_local.requestLayout());
        }
        // remember to close the db connection after use
        studentDB.closeConnection();
/*
        lblAge.setText(String.valueOf(student.getAge())); // age

        if(String.valueOf(student.getGradeLevel()) != ""){  // grade level, nullable info
            lblGrade.setText(String.valueOf(student.getGradeLevel()));
        }

        LocalDate startDate = student.getStartDate();
        LocalDate endDate = student.getEndDate();
        String startDateFormat = startDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
        String endDateFormat = endDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));

        lblDate.setText(startDateFormat + " - " + endDateFormat);  // date enrolled

        LocalTime startTime = student.getStartTime();
        LocalTime endTime = student.getEndTime();
        String startTimeFormat = startTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));

        String endTimeFormat = endTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));
        lblTime.setText(startTimeFormat + " - " + endTimeFormat);  // time

        // get the teacher's name based on the teacherId provided , todo
        //ResultSet resultSet_Teacher =  null;
        if(teacherDB.getConnection()) { // teacher
            resultSet_Teacher = teacherDB.readFromDB(new Teacher());
            while(resultSet_Teacher.next()){
                if(resultSet_Teacher.getInt(1) == student.getTeacherId()){
                    lblTeacher.setText(resultSet_Teacher.getString(2)); // firstname is fine
                    break;
                }
            }
        }
        // remember to close the db connection after use
        teacherDB.closeConnection();

        if(student.getRemark() != ""){
            lblRemark.setText(student.getRemark());
        }

        lblGuardian.setText(student.getGuardian()); // guardian
        lblPhone.setText(student.getPhone()); // phone
        lblAddress.setText(student.getAddress()); // address
        lblPaymentStatus.setText(student.getPaymentStatus()); // payment status
        lblPayment.setText(String.valueOf(student.getPayment().intValue())); // payment, convert to integer. I don't think the decimal is necessary. Also, a bit confusing to look at
 */
    }

    /**********************************************************************************
     *  - An OK confirmation is needed before removing the item in the database
     ***********************************************************************************/
    private void deleteTeacherDialog(Teacher teacher) throws SQLException {

        Alert dlg = new Alert(Alert.AlertType.CONFIRMATION);
        dlg.setTitle("Confirm deletion.");
        dlg.setContentText("Please confirm if " + teacher.getFirstName() + " is to be deleted.");
        //dlg.setHeaderText("");
        dlg.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> result = dlg.showAndWait();
        if(result.get() == ButtonType.OK){ // proceed on deleting the item
            teacherDB.deleteFromDB(teacher);
            getListView().getItems().remove(getItem());
        }else {
            // nothing to do
        }
    }
}
