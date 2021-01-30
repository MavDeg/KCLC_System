package kclc.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXNodesList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import kclc.database.DBConfig;
import kclc.database.DBHandler;
import kclc.model.Admin;
import kclc.model.Student;
import kclc.model.Student_Singleton;
import kclc.model.Teacher;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Calendar;
import java.util.Locale;
import java.util.Optional;

public class Student_ListItem extends JFXListCell<Student> {

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
  private Label lblAgeTag = new Label("Age:");
  private Label lblGradeTag = new Label("Grade:");
  private Label lblDateTag = new Label("Date Enrolled:");
  private Label lblTimeTag = new Label("Time:");
  private Label lblTeacherTag = new Label("Teacher:");
  private Label lblRemarkTag = new Label("Remark");
  private Label lblName = new Label("");
  private Label lblAge = new Label("");
  private Label lblGrade = new Label("");
  private Label lblDate = new Label("");
  private Label lblTime = new Label("");
  private Label lblTeacher = new Label("");
  private Label lblRemark = new Label("");

  private Label lblGuardianTag = new Label("Guardian:");
  private Label lblPhoneTag = new Label("Phone:");
  private Label lblAddressTag = new Label("Address:");
  private Label lblPaymentStatusTag = new Label("Payment:");
  private Label lblPaymentTag = new Label("Amount:");
  private Label lblGuardian = new Label();
  private Label lblPhone = new Label();
  private Label lblAddress = new Label();
  private Label lblPaymentStatus = new Label();
  private Label lblPayment = new Label();


    /**********************************************************************************
     *  - Format has been hardcoded but used view/Reference_Student_ListItem.fxml as
     *  reference.
     ***********************************************************************************/
  public Student_ListItem(Student student){
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

      final Font lblDateFont = (Font.font("", FontWeight.BOLD, FontPosture.ITALIC, 14));
      final Font lblPaymentFont = (Font.font("", FontWeight.BOLD, FontPosture.ITALIC, 14));

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

      lblAgeTag.setPrefHeight(lblTagHeight);
      lblAgeTag.setPrefWidth(lblTagWidth);
      lblAgeTag.setTextFill(lblTagTextFill);
      lblAgeTag.setFont(lblTagFont);
      lblAgeTag.setLayoutX(180.0);
      lblAgeTag.setLayoutY(45.0);
      lblAge.setPrefHeight(lblHeight);
      lblAge.setPrefWidth(lblWidth);
      lblAge.setTextFill(lblTextFill);
      lblAge.setFont(lblFont);
      lblAge.setLayoutX(285.0);
      lblAge.setLayoutY(45.0);

      lblGradeTag.setPrefHeight(lblTagHeight);
      lblGradeTag.setPrefWidth(lblTagWidth);
      lblGradeTag.setTextFill(lblTagTextFill);
      lblGradeTag.setFont(lblTagFont);
      lblGradeTag.setLayoutX(180.0);
      lblGradeTag.setLayoutY(70.0);
      lblGrade.setPrefHeight(lblHeight);
      lblGrade.setPrefWidth(lblWidth);
      lblGrade.setTextFill(lblTextFill);
      lblGrade.setFont(lblFont);
      lblGrade.setLayoutX(285.0);
      lblGrade.setLayoutY(70.0);

      lblDateTag.setPrefHeight(lblTagHeight);
      lblDateTag.setPrefWidth(lblTagWidth);
      lblDateTag.setTextFill(lblTagTextFill);
      lblDateTag.setFont(lblTagFont);
      lblDateTag.setLayoutX(180.0);
      lblDateTag.setLayoutY(95.0);
      lblDate.setPrefHeight(lblHeight);
      lblDate.setPrefWidth(lblWidth);
      //lblDate.setTextFill(lblTextFill);
      lblDate.setFont(lblDateFont);
      lblDate.setLayoutX(285.0);
      lblDate.setLayoutY(95.0);

      lblTimeTag.setPrefHeight(lblTagHeight);
      lblTimeTag.setPrefWidth(lblTagWidth);
      lblTimeTag.setTextFill(lblTagTextFill);
      lblTimeTag.setFont(lblTagFont);
      lblTimeTag.setLayoutX(180.0);
      lblTimeTag.setLayoutY(120.0);
      lblTime.setPrefHeight(lblHeight);
      lblTime.setPrefWidth(lblWidth);
      lblTime.setTextFill(lblTextFill);
      lblTime.setFont(lblFont);
      lblTime.setLayoutX(285.0);
      lblTime.setLayoutY(120.0);

      lblTeacherTag.setPrefHeight(lblTagHeight);
      lblTeacherTag.setPrefWidth(lblTagWidth);
      lblTeacherTag.setTextFill(lblTagTextFill);
      lblTeacherTag.setFont(lblTagFont);
      lblTeacherTag.setLayoutX(180.0);
      lblTeacherTag.setLayoutY(145.0);
      lblTeacher.setPrefHeight(lblHeight);
      lblTeacher.setPrefWidth(lblWidth);
      lblTeacher.setTextFill(lblTextFill);
      lblTeacher.setFont(lblFont);
      lblTeacher.setLayoutX(285.0);
      lblTeacher.setLayoutY(145.0);

      lblGuardianTag.setPrefHeight(lblTagHeight);
      lblGuardianTag.setPrefWidth(lblTagWidth);
      lblGuardianTag.setTextFill(lblTagTextFill);
      lblGuardianTag.setFont(lblTagFont);
      lblGuardianTag.setLayoutX(600.0);
      lblGuardianTag.setLayoutY(20.0);
      lblGuardian.setPrefHeight(lblHeight);
      lblGuardian.setPrefWidth(lblWidth-60);
      lblGuardian.setTextFill(lblTextFill);
      lblGuardian.setFont(lblFont);
      lblGuardian.setLayoutX(700.0);
      lblGuardian.setLayoutY(20.0);

      lblPhoneTag.setPrefHeight(lblTagHeight);
      lblPhoneTag.setPrefWidth(lblTagWidth);
      lblPhoneTag.setTextFill(lblTagTextFill);
      lblPhoneTag.setFont(lblTagFont);
      lblPhoneTag.setLayoutX(600.0);
      lblPhoneTag.setLayoutY(45.0);
      lblPhone.setPrefHeight(lblHeight);
      lblPhone.setPrefWidth(lblWidth-60);
      lblPhone.setTextFill(lblTextFill);
      lblPhone.setFont(lblFont);
      lblPhone.setLayoutX(700.0);
      lblPhone.setLayoutY(45.0);

      lblAddressTag.setPrefHeight(lblTagHeight);
      lblAddressTag.setPrefWidth(lblTagWidth);
      lblAddressTag.setTextFill(lblTagTextFill);
      lblAddressTag.setFont(lblTagFont);
      lblAddressTag.setLayoutX(600.0);
      lblAddressTag.setLayoutY(70.0);
      lblAddress.setPrefHeight(lblHeight);
      lblAddress.setPrefWidth(lblWidth-60);
      lblAddress.setTextFill(lblTextFill);
      lblAddress.setWrapText(true);  // wrap for multiline text
      lblAddress.setFont(lblFont);
      lblAddress.setLayoutX(700.0);
      lblAddress.setLayoutY(70.0);

      lblPaymentStatusTag.setPrefHeight(lblTagHeight);
      lblPaymentStatusTag.setPrefWidth(lblTagWidth+10);
      lblPaymentStatusTag.setTextFill(lblTagTextFill);
      lblPaymentStatusTag.setFont(lblTagFont);
      lblPaymentStatusTag.setLayoutX(600.0);
      lblPaymentStatusTag.setLayoutY(110.0);
      lblPaymentStatus.setPrefHeight(lblHeight);
      lblPaymentStatus.setPrefWidth(lblWidth-60);
      //lblPaymentStatus.setTextFill(lblTextFill);
      lblPaymentStatus.setFont(lblPaymentFont);
      lblPaymentStatus.setLayoutX(700.0);
      lblPaymentStatus.setLayoutY(110.0);

      lblPaymentTag.setPrefHeight(lblTagHeight);
      lblPaymentTag.setPrefWidth(lblTagWidth);
      lblPaymentTag.setTextFill(lblTagTextFill);
      lblPaymentTag.setFont(lblTagFont);
      lblPaymentTag.setLayoutX(600.0);
      lblPaymentTag.setLayoutY(135.0);
      lblPayment.setPrefHeight(lblHeight);
      lblPayment.setPrefWidth(lblWidth-60);
      //lblPayment.setTextFill(lblTextFill);
      lblPayment.setFont(lblPaymentFont);
      lblPayment.setLayoutX(700.0);
      lblPayment.setLayoutY(135.0);

      lblRemarkTag.setPrefHeight(lblTagHeight);
      lblRemarkTag.setPrefWidth(lblTagWidth);
      lblRemarkTag.setTextFill(lblTagTextFill);
      lblRemarkTag.setFont(lblTagFont);
      lblRemarkTag.setLayoutX(600.0);
      lblRemarkTag.setLayoutY(160.0);
      lblRemark.setPrefHeight(lblHeight);
      lblRemark.setPrefWidth(lblWidth-60);
      lblRemark.setTextFill(lblTextFill);
      lblRemark.setWrapText(true);  // wrap for multiline text
      lblRemark.setFont(lblFont);
      lblRemark.setLayoutX(700.0);
      lblRemark.setLayoutY(160.0);


/*
      rootAnchorPane.getChildren().addAll(imgProfile, btnUpdate, btnDelete,
              lblNameTag, lblName, lblAgeTag, lblAge, lblGradeTag, lblGrade, lblDateTag, lblDate,
              lblTimeTag, lblTime, lblTeacherTag, lblTeacher, lblRemarkTag, lblRemark,
              lblGuardianTag, lblGuardian, lblPhoneTag, lblPhone, lblAddressTag, lblAddress,
              lblPaymentStatusTag, lblPaymentStatus, lblPaymentTag, lblPayment);
*/
      rootAnchorPane.getChildren().addAll(imgProfile, nodelist,
              lblNameTag, lblName, lblAgeTag, lblAge, lblGradeTag, lblGrade, lblDateTag, lblDate,
              lblTimeTag, lblTime, lblTeacherTag, lblTeacher, lblRemarkTag, lblRemark,
              lblGuardianTag, lblGuardian, lblPhoneTag, lblPhone, lblAddressTag, lblAddress,
              lblPaymentStatusTag, lblPaymentStatus, lblPaymentTag, lblPayment);

    }

    @Override
    public void updateItem(Student student, boolean empty){
        super.updateItem(student, empty);
        setText(null);
        setGraphic(null);

        if(student != null && !empty){
            try {
                populateRow(student);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            setGraphic(rootAnchorPane);

            //button clicks
            // ADD
            btnAdd.setOnAction(addAction ->{
                TransitionHelper.buttonPresstoOtherWindowTransition("addStudent.fxml");
            });
            // UPDATE
            btnUpdate.setOnAction(updateAction ->{
             System.out.println("button update of each cell is clicked");
             //go to the Update student window, passing the particular student object
             if(studentDB.getConnection()){
                 try {
                     resultSet_Student = studentDB.readFromDB(student);
                     while(resultSet_Student.next()){
                             // compare the studentId to delete
                             if(resultSet_Student.getInt(1) == student.getStudentId()){
                                 System.out.println("button update is called?");
                                 //studentDB.deleteFromDB(student);
                                 Student_Singleton student_singleton = Student_Singleton.getInstance();
                                 student_singleton.setStudent(student);
                                 TransitionHelper.buttonPresstoOtherWindowTransition("updateStudent.fxml");
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

                 if(studentDB.getConnection()){
                     try {
                         resultSet_Student = studentDB.readFromDB(student);
                         while(resultSet_Student.next()){
                             // compare the studentId to delete
                             if(resultSet_Student.getInt(1) == student.getStudentId()){
                                 System.out.println("button delete is called?");
                                 //studentDB.deleteFromDB(student);
                                 deleteStudentDialog(student);
                             }
                         }
                     } catch (SQLException throwables) {
                         throwables.printStackTrace();
                     }

                 }
                 //getListView().getItems().remove(getItem());
                 System.out.println("button delete of each cell is clicked");
             });

        }
    }


    /**********************************************************************************
     *  - will provide all the details needed to fill a row
     ***********************************************************************************/
    private void populateRow(Student student) throws SQLException {

        if (student.getMiddleName() != "") {  // Full Name
            lblName.setText(student.getFirstName() + " " + student.getMiddleName() + " " + student.getLastName());
        } else {
            lblName.setText(student.getFirstName() + " " + student.getLastName());
        }

        lblAge.setText(String.valueOf(student.getAge())); // age

        if (String.valueOf(student.getGradeLevel()) != "") {  // grade level, nullable info
            lblGrade.setText(String.valueOf(student.getGradeLevel()));
        }

        LocalDate startDate = student.getStartDate();
        LocalDate endDate = student.getEndDate();
        String startDateFormat = startDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));
        String endDateFormat = endDate.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));

        /* style differently if the End Date is about to expire (e.g. 1 week or less */
        LocalDate aWeekFromNow = LocalDate.now().plusWeeks(1);
        LocalDate twoWeeksFromNow = LocalDate.now().plusWeeks(2);
        Period daysBeforeEnrollmentEnds = Period.between(LocalDate.now(), endDate);

        System.out.println("start date: " + startDateFormat);
        System.out.println("end date: " + endDateFormat);
        System.out.println(daysBeforeEnrollmentEnds.getDays() );

        if(daysBeforeEnrollmentEnds.getDays() <= 0){  // enrollment ends
            lblDate.setTextFill(Color.RED);
        }
        else if( (daysBeforeEnrollmentEnds.getDays() > 0) && (daysBeforeEnrollmentEnds.getDays() <= 7) ) { // a week before enrollment ends
            lblDate.setTextFill(Color.DARKORANGE);
        } else if ((daysBeforeEnrollmentEnds.getDays() > 7) && (daysBeforeEnrollmentEnds.getDays() <= 14)){  // 2 weeks from now
            lblDate.setTextFill(Color.ORANGE);
        }
        lblDate.setText(startDateFormat + " - " + endDateFormat);  // date enrolled

        LocalTime startTime = student.getStartTime();
        LocalTime endTime = student.getEndTime();
        String startTimeFormat = startTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));

        String endTimeFormat = endTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT));
        lblTime.setText(startTimeFormat + " - " + endTimeFormat);  // time

        // get the teacher's name based on the teacherId provided , todo
        //ResultSet resultSet_Teacher =  null;
        if (teacherDB.getConnection()) { // teacher
            resultSet_Teacher = teacherDB.readFromDB(new Teacher());
            while (resultSet_Teacher.next()) {
                if (resultSet_Teacher.getInt(1) == student.getTeacherId()) {
                    lblTeacher.setText(resultSet_Teacher.getString(2)); // firstname is fine
                    break;
                }
            }
        }
        // remember to close the db connection after use
        teacherDB.closeConnection();

        // show profile pic
        if(student.getProfilePic() != null){
            File fileImg = new File(student.getProfilePic());  // get the stored image
            Image image = new Image(fileImg.toURI().toString());
            imgProfile.setImage(image);
        }

        if (student.getRemark() != "") {
            lblRemark.setText(student.getRemark());
        }

        lblGuardian.setText(student.getGuardian()); // guardian
        lblPhone.setText(student.getPhone()); // phone
        lblAddress.setText(student.getAddress()); // address
        DBConfig dbConfig = new DBConfig();
        //Color lblPaymentTextFill =  Color.web("#2196f3");
        if (student.getPaymentStatus().equalsIgnoreCase(dbConfig.DB_PAYMENT_STATUS_UNPAID)){
            lblPaymentStatus.setTextFill(Color.RED);
        }else if (student.getPaymentStatus().equalsIgnoreCase(dbConfig.DB_PAYMENT_STATUS_PARTIAL)){
            //lblPaymentStatus.setTextFill(lblPaymentTextFill);
            lblPaymentStatus.setTextFill(Color.ORANGE);
        }else if (student.getPaymentStatus().equalsIgnoreCase(dbConfig.DB_PAYMENT_STATUS_FULL)) {
            // no change
            //lblPaymentStatus.setTextFill(Color.BLACK);
        }
        lblPaymentStatus.setText(student.getPaymentStatus()); // payment status
        //lblPayment.setText(String.valueOf(student.getPayment().intValue())); // payment, convert to integer. I don't think the decimal is necessary. Also, a bit confusing to look at
        //DecimalFormat df = new DecimalFormat("0,000.00");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "PH"));
        lblPayment.setText(currencyFormat.format(student.getPayment()));

    }

    /**********************************************************************************
     *  - An OK confirmation is needed before removing the item in the database
     ***********************************************************************************/
    private void deleteStudentDialog(Student student) throws SQLException {

        Alert dlg = new Alert(Alert.AlertType.CONFIRMATION);
        dlg.setTitle("Confirm deletion.");
        dlg.setContentText("Please confirm if " + student.getFirstName() + " is to be deleted.");
        //dlg.setHeaderText("");
        dlg.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> result = dlg.showAndWait();
        if(result.get() == ButtonType.OK){ // proceed on deleting the item
            studentDB.deleteFromDB(student);

            //todo: delete the picture as well

            getListView().getItems().remove(getItem());
        }else {
            // nothing to do
        }
    }
}
