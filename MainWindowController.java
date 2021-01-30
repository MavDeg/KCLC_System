package kclc;

import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.IntegerStringConverter;
import kclc.common.CustomIntegerStringConverter;
import kclc.database.DBHandler;
import kclc.model.*;
import kclc.view.FadeInAnimationHelper;
import kclc.view.Student_ListItem;
import kclc.view.Teacher_ListItem;
import kclc.view.TransitionHelper;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainWindowController {

    /** todo: need some hack how to update the database of the MainWindow.
    maybe need to modify a none noticable increment in either height or width of the mainRootPane to trigger refresh of children
     */

    /** Currently displayed window Flags*/
    private int activeBoard = 0;
    enum Board {
        DASHBOARD,
        STUDENTS,
        TEACHERS,
        CASHBOOK,
        INVENTORY,
        REPORT
    }

    public int getActiveBoard() {
        return activeBoard;
    }

    public void setActiveBoard(int activeBoard) {
        this.activeBoard = activeBoard;
    }

    // admin welcome message
    Admin admin = Admin.getInstance();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane mainRootPane;

    @FXML
    private ImageView mainWindow_adminProfile;

    @FXML
    private AnchorPane mainMenuLeftPane;

    @FXML
    private Label mainWindow_lblWelcome;

    @FXML
    private JFXButton mainMenuLeftPane_btnDashBoard;

    @FXML
    private JFXButton mainMenuLeftPane_btnStudents;

    @FXML
    private JFXButton mainMenuLeftPane_btnTeachers;

    @FXML
    private JFXButton mainMenuLeftPane_btnCashbook;

    @FXML
    private JFXButton mainMenuLeftPane_btnInventory;

    @FXML
    private JFXButton mainMenuLeftPane_btnSchedule;

    @FXML
    private JFXNodesList mainMenuLeftPane_nodeListMenuAdd;

    // node list (for expandable menu item
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

    public JFXButton mainMenuLeftPane_nodeListMenuAdd_Menu = new JFXButton();
    public JFXButton mainMenuLeftPane_nodeListMenuAdd_Student = new JFXButton();
    public JFXButton mainMenuLeftPane_nodeListMenuAdd_Teacher = new JFXButton();
    //public JFXButton dashBoardBtnAddAccount = new JFXButton();
    public JFXButton mainMenuLeftPane_nodeListMenuAdd_HideMenu = new JFXButton();

    @FXML
    private ImageView mainWindow_imgLogo;

    @FXML
    private AnchorPane dashboardAnchorPane;
    @FXML
    private JFXButton dashboard_btnStudent;
    @FXML
    private JFXButton dashboard_btnTeacher;
    @FXML
    private JFXButton dashboard_btnCashbook;
    @FXML
    private JFXButton dashboard_btnInventory;
    @FXML
    private JFXButton dashboard_btnReport;
    @FXML
    private JFXButton dashboard_btnLogout;


    @FXML
    private AnchorPane studentAnchorPane;

    @FXML
    private JFXListView<Student> student_listStudent;
    private ObservableList<Student> student_studentsList;

    @FXML
    private AnchorPane teacherAnchorPane;

    @FXML
    private JFXListView<Teacher> teacher_listTeacher;
    private ObservableList<Teacher> teacher_teachersList;

    @FXML
    private TextField mainMenuLeftPane_txtSearch;

    @FXML
    private JFXButton mainMenuLeftPane_btnSearch;

    @FXML
    private JFXButton mainMenuLeftPane_btnRefresh;


    @FXML
    private AnchorPane cashbookAnchorPane;

    @FXML
    private Label lblPrevMonth_BankBalance;

    @FXML
    private Label lblThisMonth_BankBalance;

    @FXML
    private Label cashbookLabel_Desc;

    @FXML
    private TextField cashbook_txtDescription;

    @FXML
    private TextField cashbook_txtAmount;

    @FXML
    private Label casgbookLabel_Amount;

    @FXML
    private JFXRadioButton cashbook_radioExpense;

    @FXML
    private JFXRadioButton cashbook_radioIncome;


    ToggleGroup cashbook_radioBtnGroup = new ToggleGroup();

    @FXML
    private JFXButton cashbook_btnAdd;

    @FXML
    private Label cashbook_lblPleaseComplete;

    @FXML
    private TableView<CashBook> cashbook_tableView;
    private ObservableList<CashBook> cashbook_tableViewList;

    private TableColumn cashbook_tableView_Col_ID =  new TableColumn<CashBook, Integer>("ID");
    private TableColumn cashbook_tableView_Col_Date = new TableColumn<CashBook, LocalDate>("Date");
    private TableColumn cashbook_tableView_Col_Description = new TableColumn<CashBook, String>("Description");
    private TableColumn cashbook_tableView_Col_Income = new TableColumn<CashBook, Double>("Income");
    private TableColumn cashbook_tableView_Col_Expenses = new TableColumn<CashBook, Double>("Expenses");
    private TableColumn cashbook_tableView_Col_BankBalance = new TableColumn<CashBook, Double>("Bank Balance");

    CashBook cashbook = new CashBook();
    DBHandler cashBookDB = new DBHandler();
    ResultSet resultSet_CashBook = null;

    @FXML
    private AnchorPane inventoryAnchorPane;

    @FXML
    private TableView<Inventory> inventory_tableView;
    private ObservableList<Inventory> inventory_tableViewList;

    private TableColumn inventory_tableView_Col_quantity =  new TableColumn<Inventory, Integer>("quantity");
    private TableColumn inventory_tableView_Col_item = new TableColumn<Inventory, String>("item");
    private TableColumn inventory_tableView_Col_purchaseDate = new TableColumn<Inventory, LocalDate>("Date of Purchase");

    Inventory inventory = new Inventory();
    DBHandler inventoryDB = new DBHandler();
    ResultSet resultSet_Inventory = null;

    @FXML
    private Label inventory_lblQty;

    @FXML
    private Label inventory_lblItem;

    @FXML
    private Label inventory_lblDate;

    @FXML
    private TextField inventory_txtQty;

    @FXML
    private DatePicker inventory_datePicker;

    @FXML
    private JFXButton inventory_btnAdd;

    @FXML
    private JFXButton inventory_btnDelete;

    @FXML
    private TextField inventory_txtItem;

    @FXML
    private Label inventory_lblPleaseComplete;

    @FXML
    void initialize() {

        // some more configuration and formatting
        internalInitialization();
/*
        1. Populate List Menu (left side panel)
        2. show the default AnchorPane view on the right (Dashboard Panel is shown)

        3. Primary Behaviors

            DashBoard Panel:
            - Student button pressed: show Student Panel, hide Others
            - Teachers button pressed: show Teachers Panel, hide Others
            - Accounts button pressed: show Accounts Panel, hide Others
            - Inventory button pressed: show Inventory Panel, hide Others

            Students and Teachers Panel:
            - initial display will be Students/Teachers List view
            - inside each entry, there are "update" and "delete" buttons
                    - update button press: show update panel, hide list view
                    - delete button press: show "Confirmation Dialog"
                        - OK: delete the item and show List view
                        - Cancel: show list view

            - Add student/teacher pressed: show Add Students/Teachers Panel, hide java.util.List View
            - Inside the "Add Student/Teacher" Panel:
                    - Cancel: go back to List View
                    - OK: Information dialog "Student/Teacher successfully added"

        */

        /**** Important: These 2 methods will be the starting point of the mainWindow
         * all the succeeding views will be handled inside
         */
        mainMenuLeftPane_configureMenuButtons(); //1
        show_dashboardAnchorPane(); //2

    }

    private void internalInitialization(){

        //admin picture and name
        //System.out.println(admin.getProfilePic());
       File fileImg = new File(admin.getProfilePic());  // get the stored image
       Image image = new Image(fileImg.toURI().toString());
       mainWindow_adminProfile.setImage(image);

        mainWindow_lblWelcome.setText("Welcome " + admin.getFirstName());

        // configure add and cancel button format
        File fileLogoutIcon = new File("src/kclc/view/assets/login_36px.png");  // nodelist expand menu button
        //File fileLogoutIcon = new File("login_36px.png");  // nodelist expand menu button
        Image imageLogout = new Image(fileLogoutIcon.toURI().toString());
        ImageView imgViewLogoutIcon= new ImageView(imageLogout);
        imgViewLogoutIcon.setRotate(180.0);
        dashboard_btnLogout.setGraphic(imgViewLogoutIcon);

        // configure add and cancel button format
        File fileReportIcon = new File("src/kclc/view/assets/file_copy.png");  // nodelist expand menu button
        Image imageReport = new Image(fileReportIcon.toURI().toString());
        ImageView imgViewReportIcon= new ImageView(imageReport);
        dashboard_btnReport.setGraphic(imgViewReportIcon);

        // Cashbook "add" and "add" button
        File fileCashbookAddIcon = new File("src/kclc/view/assets/add_white_icon_18px.png");  // nodelist expand menu button
        Image imageCashbookAdd = new Image(fileCashbookAddIcon.toURI().toString());
        ImageView imgViewCashbookAddIcon= new ImageView(imageCashbookAdd);
        cashbook_btnAdd.setGraphic(imgViewCashbookAddIcon);

        // Inventory "add" and "delete" button
        File fileInventoryAddIcon = new File("src/kclc/view/assets/add_white_icon_18px.png");  // nodelist expand menu button
        Image imageInventoryAdd = new Image(fileInventoryAddIcon.toURI().toString());
        ImageView imgViewInventoryAddIcon= new ImageView(imageInventoryAdd);
        inventory_btnAdd.setGraphic(imgViewInventoryAddIcon);

        File fileInventoryDeleteIcon = new File("src/kclc/view/assets/cancel_white_18px.png");  // nodelist expand menu button
        Image imageInventoryDelete = new Image(fileInventoryDeleteIcon.toURI().toString());
        ImageView imgViewInventoryDeleteIcon = new ImageView(imageInventoryDelete);
        inventory_btnDelete.setGraphic(imgViewInventoryDeleteIcon);
    }


    /**
    * - Configure all the mainMenuLeftPane buttons.
     * Calls the button behaviors
    * **/
    private void mainMenuLeftPane_configureMenuButtons(){
        File fileImgDashBoard  = new File("src/kclc/view/assets/dashboard.png");  // get the default image resource
        Image imageDashBoard = new Image(fileImgDashBoard.toURI().toString());
        ImageView imageViewDashBoard = new ImageView(imageDashBoard);

        File fileImgStudents  = new File("src/kclc/view/assets/student.png");  // get the default image resource
        Image imageStudents = new Image(fileImgStudents.toURI().toString());
        ImageView imageViewStudents = new ImageView(imageStudents);

        File fileImgTeachers  = new File("src/kclc/view/assets/teacher.png");  // get the default image resource
        Image imageTeachers = new Image(fileImgTeachers.toURI().toString());
        ImageView imageViewTeachers= new ImageView(imageTeachers);

        File fileImgAccounts  = new File("src/kclc/view/assets/account.png");  // get the default image resource
        Image imageAccounts = new Image(fileImgAccounts.toURI().toString());
        ImageView imageViewAccounts  = new ImageView(imageAccounts);

        File fileImgInventory = new File("src/kclc/view/assets/inventory.png");  // get the default image resource
        Image imageInventory = new Image(fileImgInventory.toURI().toString());
        ImageView imageViewInventory  = new ImageView(imageInventory);

        File fileImgSchedule = new File("src/kclc/view/assets/sched_24px.png");  // get the default image resource
        Image imageSchedule = new Image(fileImgSchedule.toURI().toString());
        ImageView imageViewSchedule  = new ImageView(imageSchedule);

        File fileImgAdd = new File("src/kclc/view/assets/add_white.png");  // get the default image resource
        Image imageAdd = new Image(fileImgAdd.toURI().toString());
        ImageView imageViewAdd   = new ImageView(imageAdd);

        File fileImgSearch = new File("src/kclc/view/assets/search_white_24dp.png");  // get the default image resource
        Image imageSearch = new Image(fileImgSearch.toURI().toString());
        ImageView imageViewSearch  = new ImageView(imageSearch);

        File fileImgRefresh = new File("src/kclc/view/assets/update_white.png");  // get the default image resource
        Image imageRefresh= new Image(fileImgRefresh.toURI().toString());
        ImageView imageViewRefresh  = new ImageView(imageRefresh);

        mainMenuLeftPane_btnDashBoard.setGraphic(imageViewDashBoard);
        mainMenuLeftPane_btnStudents.setGraphic(imageViewStudents);
        mainMenuLeftPane_btnTeachers.setGraphic(imageViewTeachers);
        mainMenuLeftPane_btnCashbook.setGraphic(imageViewAccounts);
        mainMenuLeftPane_btnInventory.setGraphic(imageViewInventory);
        mainMenuLeftPane_btnSchedule.setGraphic(imageViewSchedule);
        //btnMenuCommonAdd.setGraphic(imageViewAdd);
        mainMenuLeftPane_btnSearch.setGraphic(imageViewSearch);
        mainMenuLeftPane_btnRefresh.setGraphic(imageViewRefresh);

        mainMenuLeftPane_btnBehaviors();

    }

    /**
     * - Handle all the button behaviors of mainMenuLeft Panel
     * - Controls which window to be shown
     * **/
    private void mainMenuLeftPane_btnBehaviors(){

    // update admin profile pic
    mainWindow_adminProfile.addEventHandler(MouseEvent.MOUSE_CLICKED, event ->{
            if(event.getClickCount() == 2) {  // double clicked
                TransitionHelper.buttonPresstoOtherWindowTransition("updateAdmin.fxml");
            }
        });

        mainMenuLeftPane_btnDashBoard.setOnAction(showDashboardEvent ->{
            show_dashboardAnchorPane();
        });

        mainMenuLeftPane_btnStudents.setOnAction(showStudentsEvent ->{
            try {
                show_studentAnchorPane();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        mainMenuLeftPane_btnTeachers.setOnAction(showTeachersEvent ->{
            try {
                show_teachersAnchorPane();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        });

        mainMenuLeftPane_btnCashbook.setOnAction(showAccountsEvent ->{
            try {
                show_cashbookAnchorPane();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        mainMenuLeftPane_btnInventory.setOnAction(showInventoryEvent ->{
            try {
                show_inventoryAnchorPane();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        });

        /* SEARCH BEHAVIORS: can either use the button search or keyboard enter to SEARCH */
        mainMenuLeftPane_btnSearch.setOnAction(searchListEvent ->{

            // behavior depends on which one is the active view
            try {
                if(this.activeBoard == Board.STUDENTS.ordinal()) {
                    displaySearchResult_Students();  // repopulate list
                }else if(this.activeBoard == Board.TEACHERS.ordinal()) {
                    displaySearchResult_Teachers();
                }else if(this.activeBoard == Board.CASHBOOK.ordinal()) {
                    displaySearchResult_Cashbook();
                }else if(this.activeBoard == Board.INVENTORY.ordinal()) {
                    displaySearchResult_Inventory();
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        mainMenuLeftPane_txtSearch.setOnKeyPressed(searchListEvent ->{
            if(searchListEvent.getCode().equals(KeyCode.ENTER)) {
                // behavior depends on which one is the active view
                try {
                    if (this.activeBoard == Board.STUDENTS.ordinal()) {

                        displaySearchResult_Students();  // repopulate list
                    }else if(this.activeBoard == Board.TEACHERS.ordinal()) {
                        displaySearchResult_Teachers();
                    }else if(this.activeBoard == Board.CASHBOOK.ordinal()) {
                        displaySearchResult_Cashbook();
                    }else if(this.activeBoard == Board.INVENTORY.ordinal()) {
                        displaySearchResult_Inventory();
                    }

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        mainMenuLeftPane_btnRefresh.setOnAction(refreshListEvent ->{

            // behavior depends on which one is the active view
            try {
                if (this.activeBoard == Board.STUDENTS.ordinal()) {
                    populatestudent_listStudent();  // repopulate list
                }else if(this.activeBoard == Board.TEACHERS.ordinal()) {
                    populateteacher_listTeacher();
                }else if(this.activeBoard == Board.CASHBOOK.ordinal()) {
                    populateListCashbook();
                }else if(this.activeBoard == Board.INVENTORY.ordinal()) {
                    populateListInventory();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }


            //common refresh
            //e.g. admin picture and name
            File fileImg = new File(admin.getProfilePic());  // get the stored image
            Image image = new Image(fileImg.toURI().toString());
            mainWindow_adminProfile.setImage(image);
            mainWindow_lblWelcome.setText("Welcome " + admin.getFirstName());

        });


        // add NodeList menu buttons
        final double btnPrefWidth = 197.0;
        final double btnPrefHeight= 35.0;
        final Pos btnAlignment = Pos.BASELINE_LEFT;

        final Font fontStyle = new Font("", 18);
        final Color fontColor = Color.WHITE;
        final double graphic_text_gap = 10.0;


        mainMenuLeftPane_nodeListMenuAdd_Menu.setPrefWidth(btnPrefWidth);
        mainMenuLeftPane_nodeListMenuAdd_Menu.setPrefHeight(btnPrefHeight);
        mainMenuLeftPane_nodeListMenuAdd_Menu.setAlignment(btnAlignment);
        mainMenuLeftPane_nodeListMenuAdd_Menu.setTooltip(new Tooltip("Expand Menu"));
        mainMenuLeftPane_nodeListMenuAdd_Menu.setGraphic(imgViewAddIcon);
        mainMenuLeftPane_nodeListMenuAdd_Menu.setText(" Add");
        mainMenuLeftPane_nodeListMenuAdd_Menu.setGraphicTextGap(graphic_text_gap);
        mainMenuLeftPane_nodeListMenuAdd_Menu.setFont(fontStyle);
        mainMenuLeftPane_nodeListMenuAdd_Menu.setTextFill(fontColor);
        mainMenuLeftPane_nodeListMenuAdd_Menu.setOnAction(e->{      // toggle expanded and collapse
            if(mainMenuLeftPane_nodeListMenuAdd.isExpanded()) {
                //btnCollapseMenu.setGraphic(imgViewMenuIcon);
                mainMenuLeftPane_nodeListMenuAdd.animateList(true);
            }else{
                //btnCollapseMenu.setGraphic(imgViewMenuCollapseIcon);
                mainMenuLeftPane_nodeListMenuAdd.animateList(false);
            }
        });

        mainMenuLeftPane_nodeListMenuAdd.setOnMouseExited(e->{mainMenuLeftPane_nodeListMenuAdd.animateList(false);});  // auto collapse menu


        mainMenuLeftPane_nodeListMenuAdd_Student.setPrefWidth(btnPrefWidth);
        mainMenuLeftPane_nodeListMenuAdd_Student.setPrefHeight(btnPrefHeight);
        //mainMenuLeftPane_nodeListMenuAdd_Student.setStyle(btnStyle_BGColor);
        mainMenuLeftPane_nodeListMenuAdd_Student.setAlignment(btnAlignment);
        mainMenuLeftPane_nodeListMenuAdd_Student.setGraphicTextGap(graphic_text_gap);
        mainMenuLeftPane_nodeListMenuAdd_Student.setFont(fontStyle);
        mainMenuLeftPane_nodeListMenuAdd_Student.setTextFill(fontColor);
        mainMenuLeftPane_nodeListMenuAdd_Student.setTooltip(new Tooltip("Add new student"));
        //mainMenuLeftPane_nodeListMenuAdd_Student.setGraphic(imgViewAddIcon);
        mainMenuLeftPane_nodeListMenuAdd_Student.setText("        Student");


        mainMenuLeftPane_nodeListMenuAdd_Teacher.setPrefWidth(btnPrefWidth);
        mainMenuLeftPane_nodeListMenuAdd_Teacher.setPrefHeight(btnPrefHeight);
        //mainMenuLeftPane_nodeListMenuAdd_Student.setStyle(btnStyle_BGColor);
        mainMenuLeftPane_nodeListMenuAdd_Teacher.setAlignment(btnAlignment);
        mainMenuLeftPane_nodeListMenuAdd_Teacher.setGraphicTextGap(graphic_text_gap);
        mainMenuLeftPane_nodeListMenuAdd_Teacher.setFont(fontStyle);
        mainMenuLeftPane_nodeListMenuAdd_Teacher.setTextFill(fontColor);
        mainMenuLeftPane_nodeListMenuAdd_Teacher.setTooltip(new Tooltip("Add new teacher"));
        //mainMenuLeftPane_nodeListMenuAdd_Student.setGraphic(imgViewAddIcon);
        mainMenuLeftPane_nodeListMenuAdd_Teacher.setText("        Teacher");


        //dashBoardBtnAddAccount.setPrefWidth(btnPrefWidth);
        //dashBoardBtnAddAccount.setPrefHeight(btnPrefHeight);
        //mainMenuLeftPane_nodeListMenuAdd_Student.setStyle(btnStyle_BGColor);
        //dashBoardBtnAddAccount.setAlignment(btnAlignment);
        //dashBoardBtnAddAccount.setGraphicTextGap(graphic_text_gap);
        //dashBoardBtnAddAccount.setFont(fontStyle);
        //dashBoardBtnAddAccount.setTextFill(fontColor);
        //dashBoardBtnAddAccount.setTooltip(new Tooltip("Add cashbook item"));
        //mainMenuLeftPane_nodeListMenuAdd_Student.setGraphic(imgViewAddIcon);
        //dashBoardBtnAddAccount.setText("        Cashbook Item");

        mainMenuLeftPane_nodeListMenuAdd_HideMenu.setPrefWidth(btnPrefWidth);
        mainMenuLeftPane_nodeListMenuAdd_HideMenu.setPrefHeight(btnPrefHeight);
        //mainMenuLeftPane_nodeListMenuAdd_Student.setStyle(btnStyle_BGColor);
        mainMenuLeftPane_nodeListMenuAdd_HideMenu.setAlignment(btnAlignment);
        mainMenuLeftPane_nodeListMenuAdd_HideMenu.setGraphicTextGap(graphic_text_gap);
        mainMenuLeftPane_nodeListMenuAdd_HideMenu.setFont(fontStyle);
        mainMenuLeftPane_nodeListMenuAdd_HideMenu.setTextFill(fontColor);
        mainMenuLeftPane_nodeListMenuAdd_HideMenu.setTooltip(new Tooltip("Hide add menu"));
        //mainMenuLeftPane_nodeListMenuAdd_Student.setGraphic(imgViewAddIcon);
        mainMenuLeftPane_nodeListMenuAdd_HideMenu.setText("Hide");

        mainMenuLeftPane_nodeListMenuAdd.setAlignment(Pos.BASELINE_LEFT);
        mainMenuLeftPane_nodeListMenuAdd.addAnimatedNode(mainMenuLeftPane_nodeListMenuAdd_Menu);
        mainMenuLeftPane_nodeListMenuAdd.addAnimatedNode(mainMenuLeftPane_nodeListMenuAdd_Student);
        mainMenuLeftPane_nodeListMenuAdd.addAnimatedNode(mainMenuLeftPane_nodeListMenuAdd_Teacher);
        //mainMenuLeftPane_nodeListMenuAdd.addAnimatedNode(dashBoardBtnAddAccount);
        //mainMenuLeftPane_nodeListMenuAdd.addAnimatedNode(mainMenuLeftPane_nodeListMenuAdd_HideMenu);


        //* Some minor adjustments for the account table format
        cashbook_tableView.setStyle("-fx-background-color:  #80DEEA;");
        cashbook_tableView.setStyle("-fx-font-size: 16;");

        cashbook_tableView_Col_ID.setPrefWidth(50.0);
        //cashbook_tableView_Col_ID.setStyle("-fx-text-fill: red;");
        cashbook_tableView_Col_Date.setPrefWidth(150.0);
        cashbook_tableView_Col_Description.setPrefWidth(410.0);

        cashbook_tableView_Col_Income.setPrefWidth(140.0);
        cashbook_tableView_Col_Income.setStyle("-fx-text-fill: blue;");

        cashbook_tableView_Col_Expenses.setPrefWidth(140.0);
        cashbook_tableView_Col_Expenses.setStyle("-fx-text-fill: red;");

        cashbook_tableView_Col_BankBalance.setPrefWidth(185.0);
        cashbook_tableView_Col_BankBalance.setStyle("-fx-font-weight: bold;");


        //* Some minor adjustments for the inventory table format
        inventory_tableView.setStyle("-fx-background-color:  #80DEEA;");
        inventory_tableView.setStyle("-fx-font-size: 16;");
        inventory_tableView_Col_quantity.setPrefWidth(100.0);
        inventory_tableView_Col_item.setPrefWidth(780.0);
        inventory_tableView_Col_purchaseDate.setPrefWidth(200.0);

        mainMenuLeftPane_nodeListMenuAdd_Student.setOnAction(addAction ->{
            TransitionHelper.buttonPresstoOtherWindowTransition("addStudent.fxml");
        });

        mainMenuLeftPane_nodeListMenuAdd_Teacher.setOnAction(addAction ->{
            TransitionHelper.buttonPresstoOtherWindowTransition("addTeacher.fxml");
        });

        //dashBoardBtnAddAccount.setOnAction(addAction ->{
        //    TransitionHelper.buttonPresstoOtherWindowTransition("addCashbookItem.fxml");
        //});
    }


    /**
     * - show_dashboardAnchorPane
     * **/
    private void show_dashboardAnchorPane(){
        dashboardAnchorPane.setVisible(true); // show only this
        studentAnchorPane.setVisible(false);
        teacherAnchorPane.setVisible(false);
        cashbookAnchorPane.setVisible(false);
        inventoryAnchorPane.setVisible(false);
        this.setActiveBoard(Board.DASHBOARD.ordinal());
        //animate
        FadeInAnimationHelper fadeIn = new FadeInAnimationHelper(dashboardAnchorPane);
        fadeIn.play();

        dashboard_btnBehaviors();

    }

    private void dashboard_btnBehaviors(){

        //student panel to be shown
        dashboard_btnStudent.setOnAction(gotoStudentAnchorPaneEvent ->{
            try {
                show_studentAnchorPane();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        });

        // teacher panel to be shown
        dashboard_btnTeacher.setOnAction(gotoTeacherAnchorPaneEvent ->{
            try {
                show_teachersAnchorPane();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        });

        // account panel to be shown
        dashboard_btnCashbook.setOnAction(gotoAccountsAnchorPaneEvent ->{
            try {
                show_cashbookAnchorPane();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        // inventory panel to be shown
        dashboard_btnInventory.setOnAction(gotoInventoryAnchorPaneEvent ->{
            try {
                show_inventoryAnchorPane();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });


        dashboard_btnReport.setOnAction(gotoReportEvent->{
            // go to Report screen
            TransitionHelper.buttonPresstoOtherWindowTransition("generateReport.fxml");
        });

        dashboard_btnLogout.setOnAction(gotoLogoutAnchorPaneEvent ->{
            // go to Login window
            dashboard_btnLogout.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view/login.fxml"));

            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene((root)));
            stage.setTitle("KCLC School Management System");
            //stage.setResizable(false);
            stage.setResizable(true);
            stage.show();
        });

    }

    private void show_studentAnchorPane() throws SQLException {

        dashboardAnchorPane.setVisible(false); //hide all
        studentAnchorPane.setVisible(true);// show only this
        teacherAnchorPane.setVisible(false);
        cashbookAnchorPane.setVisible(false);
        inventoryAnchorPane.setVisible(false);
        this.setActiveBoard(Board.STUDENTS.ordinal());
        // animate
        FadeInAnimationHelper fadeIn = new FadeInAnimationHelper(studentAnchorPane);
        fadeIn.play();

        populatestudent_listStudent();

    }

    private void show_teachersAnchorPane() throws SQLException {
        dashboardAnchorPane.setVisible(false); //hide all
        studentAnchorPane.setVisible(false);
        teacherAnchorPane.setVisible(true); // show only this
        cashbookAnchorPane.setVisible(false);
        inventoryAnchorPane.setVisible(false);
        this.setActiveBoard(Board.TEACHERS.ordinal());
        //animate
        FadeInAnimationHelper fadeIn = new FadeInAnimationHelper(teacherAnchorPane);
        fadeIn.play();

        populateteacher_listTeacher();
    }

    private void show_cashbookAnchorPane() throws SQLException {
        dashboardAnchorPane.setVisible(false); //hide all
        studentAnchorPane.setVisible(false);
        teacherAnchorPane.setVisible(false);
        cashbookAnchorPane.setVisible(true); // show only this
        inventoryAnchorPane.setVisible(false);
        this.setActiveBoard(Board.CASHBOOK.ordinal());
        //animate
        FadeInAnimationHelper fadeIn = new FadeInAnimationHelper(cashbookAnchorPane);
        fadeIn.play();

        populateListCashbook();

        /*add cashbook item*/
        cashbook_btnAdd.setOnAction(addCashbookItem->{

                // retrieve accounts DB
                if(cashBookDB.getConnection()) {
                    try {
                        if (addCashbookItem()) {  // all info OK?
                            try {
                                cashBookDB.writeToDB(cashbook);

                                addCashbookDialog();

                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                            cashBookDB.closeConnection();
                            // go bak to main window
                            //addAnotherItem();
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }

        });
    }

    private void addCashbookDialog(){
        Alert dlg = new Alert(Alert.AlertType.INFORMATION);
        dlg.setTitle("Successfully added.");
        dlg.setContentText("Item " + this.cashbook_txtDescription.getText() + " is successfully added.");
        //dlg.setHeaderText("");
        dlg.show();

        dlg.setOnCloseRequest(event -> {
            //clear the textfields
            cashbook_txtDescription.setText("");
            cashbook_txtAmount.setText("");
            cashbook_radioIncome.setSelected(false);
            cashbook_radioExpense.setSelected(false);
            cashbook_lblPleaseComplete.setText("");

            //refresh the list
            try {
                populateListCashbook();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

        private void show_inventoryAnchorPane() throws SQLException {
        dashboardAnchorPane.setVisible(false); //hide all
        studentAnchorPane.setVisible(false);
        teacherAnchorPane.setVisible(false);
        cashbookAnchorPane.setVisible(false);
        inventoryAnchorPane.setVisible(true); // show only this
        this.setActiveBoard(Board.INVENTORY.ordinal());
        //animate
        FadeInAnimationHelper fadeIn = new FadeInAnimationHelper(inventoryAnchorPane);
        fadeIn.play();

        populateListInventory();

           /*add inventory item*/
        inventory_btnAdd.setOnAction(addInventoryItem->{

            // retrieve accounts DB
            if(inventoryDB.getConnection()) {
                try {
                    if (addInventoryItem()) {  // all info OK?
                        try {
                            inventoryDB.writeToDB(inventory);

                            addInventoryDialog();

                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        // go bak to main window
                        //addAnotherItem();
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            inventoryDB.closeConnection();

        });

        inventory_btnDelete.setOnAction(deleteInventoryItem->{

           // inventory_tableView.getSelectionModel().getSelectedItems();
            if(inventoryDB.getConnection()){
                try {
                    resultSet_Inventory= inventoryDB.readFromDB(inventory);
                    int refId = 0;
                    ObservableList<Inventory> inventoryList_toBeDeleted = FXCollections.observableArrayList();
                    while(resultSet_Inventory.next()){
                        refId = resultSet_Inventory.getInt(1);

                        inventoryList_toBeDeleted = inventory_tableView.getSelectionModel().getSelectedItems();

                    }
                    deleteInventoryItemDialog(inventoryList_toBeDeleted);

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                inventoryDB.closeConnection();

            }

        });


    }

    /**
     * Responsible for showing the List of Students.
     * This will call the Student_ListItem to format each of the row
     *
     * **/
    private void populatestudent_listStudent(){
        student_studentsList = FXCollections.observableArrayList();
        DBHandler studentsDB = new DBHandler();
        ResultSet resultSet_Students = null;
        // retrieve students DB
        try {
            if (studentsDB.getConnection()) {
                resultSet_Students = studentsDB.readFromDB(new Student());

                while (resultSet_Students.next()) {
                    Student student = new Student();

                    student.setStudentId(resultSet_Students.getInt(1));
                    student.setFirstName(resultSet_Students.getString(2));
                    student.setMiddleName(resultSet_Students.getString(3));
                    student.setLastName(resultSet_Students.getString(4));
                    student.setAge(resultSet_Students.getInt(5));
                    student.setGradeLevel(resultSet_Students.getInt(6));
                    student.setGuardian(resultSet_Students.getString(7));
                    student.setPhone(resultSet_Students.getString(8));
                    student.setAddress(resultSet_Students.getString(9));

                    student.setStartDate(LocalDate.parse(resultSet_Students.getString(10))); // check if also need format???
                    student.setEndDate(LocalDate.parse(resultSet_Students.getString(11))); // check if also need format???

                    student.setStartTime(LocalTime.parse(resultSet_Students.getString(12))); // check if also need format???
                    student.setEndTime(LocalTime.parse(resultSet_Students.getString(13))); // check if also need format???

                    student.setTeacherId(resultSet_Students.getInt(14));

                    student.setPaymentStatus(resultSet_Students.getString(15));
                    student.setPayment(resultSet_Students.getDouble(16));
                    student.setProfilePic(resultSet_Students.getString(17));
                    student.setRemark(resultSet_Students.getString(18));

                    student_studentsList.add(student);
                }
                student_listStudent.setItems(student_studentsList);
                student_listStudent.setCellFactory(param -> new Student_ListItem(new Student()));

                student_listStudent.setVisible(true);
                /* hide the other lists */
                //addStudentAnchorPane.setVisible(false);
            }
            studentsDB.closeConnection();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Responsible for showing the List of Teachers.
     * This will call the Teacher_ListItem to format each of the row
     *
     * **/
    private void populateteacher_listTeacher() throws SQLException {
        teacher_teachersList = FXCollections.observableArrayList();
        DBHandler teachersDB = new DBHandler();
        ResultSet resultSet_Teachers = null;
        // retrieve students DB
        if(teachersDB.getConnection()){
            resultSet_Teachers = teachersDB.readFromDB(new Teacher());

            while(resultSet_Teachers.next()) {
                Teacher teacher = new Teacher();

                teacher.setTeacherId(resultSet_Teachers.getInt(1));
                teacher.setFirstName(resultSet_Teachers.getString(2));
                teacher.setMiddleName(resultSet_Teachers.getString(3));
                teacher.setLastName(resultSet_Teachers.getString(4));
                teacher.setPhone(resultSet_Teachers.getString(5));
                teacher.setAddress(resultSet_Teachers.getString(6));
                teacher.setEmploymentDate(LocalDate.parse(resultSet_Teachers.getString(7)));
                teacher.setProfilePic(resultSet_Teachers.getString(8));
                teacher.setRemark(resultSet_Teachers.getString(9));

                teacher_teachersList.add(teacher);
            }
            teacher_listTeacher.setItems(teacher_teachersList);
            teacher_listTeacher.setCellFactory(param -> new Teacher_ListItem(new Teacher()));

            teacher_listTeacher.setVisible(true);
            /* hide the other lists */
            //addStudentAnchorPane.setVisible(false);
        }
        teachersDB.closeConnection();
    }

    /**
     * Populate the cashbook list.
     * Call the add item method
     *
     * **/
    private void populateListCashbook() throws SQLException {

        /*Initialize fields*/
        cashbook_radioIncome.setSelectedColor(Color.web("ff7d00ff"));
        cashbook_radioIncome.setSelected(false);
        cashbook_radioExpense.setSelectedColor(Color.web("ff7d00ff"));
        cashbook_radioExpense.setSelected(false);
        /* group radio buttons */
        cashbook_radioIncome.setToggleGroup(cashbook_radioBtnGroup);
        cashbook_radioExpense.setToggleGroup(cashbook_radioBtnGroup);

        cashbook_tableViewList = FXCollections.observableArrayList();
        DBHandler cashBookDB = new DBHandler();
        ResultSet resultSet_CashBook = null;

        cashbook_tableView_Col_ID.setCellValueFactory(new PropertyValueFactory<CashBook, Integer>("refId"));
        cashbook_tableView_Col_Date.setCellValueFactory(new PropertyValueFactory<CashBook, LocalDate>("date"));
        cashbook_tableView_Col_Description.setCellValueFactory(new PropertyValueFactory<CashBook, String>("description"));
        cashbook_tableView_Col_Income.setCellValueFactory(new PropertyValueFactory<CashBook, Double>("income"));
        cashbook_tableView_Col_Expenses.setCellValueFactory(new PropertyValueFactory<CashBook, Double>("expense"));
        cashbook_tableView_Col_BankBalance.setCellValueFactory(new PropertyValueFactory<CashBook, Double>("bankBalance"));


        //format the income, expense, bank balance to 2 decimal places as string
        cashbook_tableView_Col_Income.setCellFactory(tc -> new TableCell<CashBook, Double>() {
            //DecimalFormat df = new DecimalFormat("0.00");
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "PH"));
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty) ;
                if (empty) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(value.doubleValue()));
                }
            }
        });

        cashbook_tableView_Col_Expenses.setCellFactory(tc -> new TableCell<CashBook, Double>() {
            //DecimalFormat df = new DecimalFormat("0.00");
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "PH"));
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty) ;
                if (empty) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(value.doubleValue()));
                }
            }
        });

        cashbook_tableView_Col_BankBalance.setCellFactory(tc -> new TableCell<CashBook, Double>() {
            //DecimalFormat df = new DecimalFormat("0.00");
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "PH"));
            @Override
            protected void updateItem(Double value, boolean empty) {
                super.updateItem(value, empty) ;
                if (empty) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(value.doubleValue()));
                }
            }
        });

        // retrieve cashbook DB
        if(cashBookDB.getConnection()){
            resultSet_CashBook = cashBookDB.readFromDB(new CashBook());

            while(resultSet_CashBook.next()) {
                CashBook cashbook = new CashBook();

                cashbook.setRefId(resultSet_CashBook.getInt(1));
                cashbook.setDate(LocalDate.parse(resultSet_CashBook.getString(2)));
                cashbook.setDescription(resultSet_CashBook.getString(3));
                cashbook.setIncome(resultSet_CashBook.getDouble(4));
                cashbook.setExpense(resultSet_CashBook.getDouble(5));
                cashbook.setBankBalance(resultSet_CashBook.getDouble(6));

                cashbook_tableViewList.add(cashbook);
            }
            cashbook_tableView.setItems(cashbook_tableViewList);
            cashbook_tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

            //teacher_listTeacher.setVisible(true);
            /* hide the other lists */
            //addStudentAnchorPane.setVisible(false);
        }
        cashBookDB.closeConnection();

        //set_accountTable_Editable();
        cashbook_tableView.getColumns().setAll(cashbook_tableView_Col_ID, cashbook_tableView_Col_Date, cashbook_tableView_Col_Description,
                cashbook_tableView_Col_Income, cashbook_tableView_Col_Expenses, cashbook_tableView_Col_BankBalance);

    }

    /** adding new cashbook item **/
    private boolean addCashbookItem() throws SQLException {


            boolean isInfoComplete = true;
            Double newBankBalance = 0.0;
            Double amount  = 0.0;
            String description = "";
            boolean income = true; // income = true, expense = false

        /** prepare data**/

        /* get the last row to get the latest bank balance.
        this is going to be either add/minus the income/expense part*/
            resultSet_CashBook = cashBookDB.readLastRowDB(cashbook);
            while(resultSet_CashBook.next()){
                newBankBalance = resultSet_CashBook.getDouble(1);
            }

            if(cashbook_txtDescription.getText() != ""){
                description = cashbook_txtDescription.getText();

            }else{
               cashbook_lblPleaseComplete.setText("* Please put description");
                return isInfoComplete = false;
            }

            if(cashbook_txtAmount.getText() != "") {
                //check if numbers (either Int or Double will do)
                try{
                    amount = Double.parseDouble(cashbook_txtAmount.getText().replace(",", ""));  // remove also if there is "," separator
                }catch(Exception e){
                    cashbook_lblPleaseComplete.setText("* Please a valid amount");
                    return isInfoComplete = false;
                }
            } else {
                cashbook_lblPleaseComplete.setText("* Please enter amount");
                return isInfoComplete = false;
            }

            if(cashbook_radioIncome.isSelected()) {
                income = true;
            }else if(cashbook_radioExpense.isSelected()){
                income = false;
            }else{
                cashbook_lblPleaseComplete.setText("* Please select: Income or Expense");
                return isInfoComplete = false;
            }

            // all info is good,proceed putting in the cashbook
            cashbook.setDate(LocalDate.now());
            cashbook.setDescription(description);
            if(income) { // income
                cashbook.setIncome(amount);
                cashbook.setBankBalance(newBankBalance + amount);
                cashbook.setExpense(0.0);
            }else{ // expense
                cashbook.setIncome(0.0);
                cashbook.setExpense(amount);
                cashbook.setBankBalance(newBankBalance - amount);
            }

            return isInfoComplete;
        }


    /** Only description can be edited, this is to keep the value intact **/
/*  Will not be used for the cashbook table. Cashbook table is intended not to be editable

    private void set_accountTable_Editable(){
        cashbook_tableView.setEditable(true);
        cashbook_tableView_Col_Description.setEditable(true);
        cashbook_tableView_Col_Description.setCellFactory(TextFieldTableCell.forTableColumn());

        cashbook_tableView_Col_Description.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<CashBook, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<CashBook, String> cellEditEvent) {
                CashBook cashbookItemToUpdate = cellEditEvent.getRowValue();
                cashbookItemToUpdate.setDescription(cellEditEvent.getNewValue());

                System.out.println(cashbookItemToUpdate.getDescription());
            }
        });

    }
*/
    /**
     * Populate the inventory list.
     * Call the add inventory item method
     *
     * **/
    private void populateListInventory() throws SQLException {

        inventory_tableViewList = FXCollections.observableArrayList();
        DBHandler inventoryDB = new DBHandler();
        ResultSet resultSet_Inventory = null;

        inventory_tableView_Col_quantity.setCellValueFactory(new PropertyValueFactory<Inventory, Integer>("quantity"));
        inventory_tableView_Col_item.setCellValueFactory(new PropertyValueFactory<Inventory, String>("item"));
        inventory_tableView_Col_purchaseDate.setCellValueFactory(new PropertyValueFactory<Inventory, LocalDate>("purchaseDate"));
        inventory_tableView.getColumns().setAll(inventory_tableView_Col_quantity, inventory_tableView_Col_item, inventory_tableView_Col_purchaseDate);


        // retrieve inventory DB
        if(inventoryDB.getConnection()) {
            resultSet_Inventory = inventoryDB.readFromDB(new Inventory());

            while (resultSet_Inventory.next()) {
                Inventory inventory = new Inventory();
                inventory.setRefId(resultSet_Inventory.getInt(1));
                inventory.setQuantity(resultSet_Inventory.getInt(2));
                inventory.setItem(resultSet_Inventory.getString(3));
                inventory.setPurchaseDate(LocalDate.parse(resultSet_Inventory.getString(4)));
                inventory_tableViewList.add(inventory);
            }

            inventory_tableView.setItems(inventory_tableViewList);
            inventory_tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        }

        // adding new item
        //addInventoryItem();

        //updatable row
        set_inventoryTable_Editable();

    }

    private boolean addInventoryItem() throws SQLException {

        boolean isInfoComplete = true;
        int quantity = 0;
        String item = "";
        LocalDate date = LocalDate.now();

        inventory_lblPleaseComplete.setText("");

        /** prepare data**/
        if(inventory_txtQty.getText() != "") {
            //check if numbers (either Int or Double will do)
            try{
                quantity = Integer.parseInt(inventory_txtQty.getText().trim().replace(",", "")); // remove also if there is "," separator
            }catch(Exception e){
                inventory_lblPleaseComplete.setText("* Please a valid quantity");
                return isInfoComplete = false;
            }
        } else {
            inventory_lblPleaseComplete.setText("* Please enter quantity");
            return isInfoComplete = false;
        }

        if(inventory_txtItem.getText() != ""){
            item = inventory_txtItem.getText();
        }else{
            inventory_lblPleaseComplete.setText("* Please enter item name");
            return isInfoComplete = false;
        }

        if(inventory_datePicker.getValue() != null){
            date = inventory_datePicker.getValue();  // returns local date
        }
        else{
            inventory_lblPleaseComplete.setText("* Please enter date of purchase");
            return isInfoComplete = false;
        }

        // all is good, proceed putting to the inventory
        inventory.setQuantity(quantity);
        inventory.setItem(item);
        inventory.setPurchaseDate(date);

        return isInfoComplete;
    }

    private void addInventoryDialog(){
        Alert dlg = new Alert(Alert.AlertType.INFORMATION);
        dlg.setTitle("Successfully added.");
        dlg.setContentText("Item " + this.inventory_txtItem.getText() + " is successfully added.");
        //dlg.setHeaderText("");
        dlg.show();

        dlg.setOnCloseRequest(event -> {
            //clear the textfields
            inventory_txtQty.setText("");
            inventory_txtItem.setText("");
            inventory_datePicker.setValue(null);
            inventory_lblPleaseComplete.setText("");

            //refresh the list
            try {
                populateListInventory();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }

    /**
     * Confirm deletion inventory item deletion
     * */
    private void deleteInventoryItemDialog(ObservableList<Inventory> inventoryList) throws SQLException {

        Alert dlg = new Alert(Alert.AlertType.CONFIRMATION);
        dlg.setTitle("Confirm deletion.");
        //dlg.setContentText("Please confirm if " + inventory.getItem() + " is to be deleted.");
        //dlg.setHeaderText("");
        dlg.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> result = dlg.showAndWait();
        if(result.get() == ButtonType.OK){ // proceed on deleting the item

            for(int i=0; i < inventoryList.size(); i++){
                inventoryDB.deleteFromDB(inventoryList.get(i));
            }

            //refresh list
            populateListInventory();
        }else {
            // nothing to do
        }
    }

    /**The item quantity table must be editable directly. So that it can be easily updated**/
    private void set_inventoryTable_Editable(){
        inventory_tableView.setEditable(true);
        inventory_tableView_Col_quantity.setEditable(true);

        inventory_tableView_Col_quantity.setCellFactory(TextFieldTableCell.forTableColumn(new CustomIntegerStringConverter()));
        inventory_tableView_Col_quantity.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<Inventory, Integer>>) inventoryIntegerCellEditEvent -> {
            Inventory itemToUpdate = inventoryIntegerCellEditEvent.getRowValue();
            itemToUpdate.setQuantity(inventoryIntegerCellEditEvent.getNewValue());
            // don't save in database if the input is invalid
            if(inventoryIntegerCellEditEvent.getNewValue() != -1) { // CustomIntegerStringConverter will return -1 when invalid number was inputted

                if (inventoryDB.getConnection()) {
                    try {
                        inventoryDB.updateDB(itemToUpdate);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            }
            else{
                // refresh list
                try {
                    populateListInventory();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }

        });
    }

    //todo: for future use
    private void addInventoryItemDoubleClick(){
        inventory_tableView.setRowFactory(new Callback<TableView<Inventory>, TableRow<Inventory>>(){

            @Override
            public TableRow<Inventory> call(TableView<Inventory> inventoryTableView) {
                TableRow<Inventory> row = new TableRow<Inventory>();
                row.setOnMouseClicked(new EventHandler<MouseEvent>(){

                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if(mouseEvent.getClickCount() == 2){   // double-clicked
                            //create a new item and initialize it
                            Inventory inventory = new Inventory();
                            inventory_tableView.getItems().add(inventory);
                        }
                    }
                });

                return row;
            }
        });
    }


    /***************** Handle Search Results. Results depends on the current window being shown  **********************/

    /** SEARCH RESULT STUDENTS **/
    private void displaySearchResult_Students() throws SQLException {

        String strToSearch = "%"+ mainMenuLeftPane_txtSearch.getText() + "%";
        student_studentsList = FXCollections.observableArrayList();
        DBHandler studentsDB = new DBHandler();
        ResultSet resultSet_Students = null;

        // retrieve students DB
        if(studentsDB.getConnection()){
            resultSet_Students = studentsDB.searchFromDB(new Student(), strToSearch);

            while(resultSet_Students.next()) {
                Student student = new Student();

                student.setStudentId(resultSet_Students.getInt(1));
                student.setFirstName(resultSet_Students.getString(2));
                student.setMiddleName(resultSet_Students.getString(3));
                student.setLastName(resultSet_Students.getString(4));
                student.setAge(resultSet_Students.getInt(5));
                student.setGradeLevel(resultSet_Students.getInt(6));
                student.setGuardian(resultSet_Students.getString(7));
                student.setPhone(resultSet_Students.getString(8));
                student.setAddress(resultSet_Students.getString(9));

                student.setStartDate(LocalDate.parse(resultSet_Students.getString(10))); // check if also need format???
                student.setEndDate(LocalDate.parse(resultSet_Students.getString(11))); // check if also need format???

                student.setStartTime(LocalTime.parse(resultSet_Students.getString(12))); // check if also need format???
                student.setEndTime(LocalTime.parse(resultSet_Students.getString(13))); // check if also need format???

                student.setTeacherId(resultSet_Students.getInt(14));

                student.setPaymentStatus(resultSet_Students.getString(15));
                student.setPayment(resultSet_Students.getDouble(16));
                student.setProfilePic(resultSet_Students.getString(17));
                student.setRemark(resultSet_Students.getString(18));

                student_studentsList.add(student);
            }
            student_listStudent.setItems(student_studentsList);
            student_listStudent.setCellFactory(param -> new Student_ListItem(new Student()));

            student_listStudent.setVisible(true);
            /* hide the other lists */
            //addStudentAnchorPane.setVisible(false);
        }
        studentsDB.closeConnection();
    }


    /** SEARCH RESULT TEACHERS **/
    private void displaySearchResult_Teachers() throws SQLException {

        String strToSearch = "%"+ mainMenuLeftPane_txtSearch.getText() + "%";
        teacher_teachersList = FXCollections.observableArrayList();
        DBHandler teachersDB = new DBHandler();
        ResultSet resultSet_Teachers= null;

        // retrieve teacher DB
        if(teachersDB.getConnection()){
            resultSet_Teachers = teachersDB.searchFromDB(new Teacher(), strToSearch);

            while(resultSet_Teachers.next()) {
                Teacher teacher = new Teacher();

                teacher.setTeacherId(resultSet_Teachers.getInt(1));
                teacher.setFirstName(resultSet_Teachers.getString(2));
                teacher.setMiddleName(resultSet_Teachers.getString(3));
                teacher.setLastName(resultSet_Teachers.getString(4));
                teacher.setPhone(resultSet_Teachers.getString(5));
                teacher.setAddress(resultSet_Teachers.getString(6));
                teacher.setEmploymentDate(LocalDate.parse(resultSet_Teachers.getString(7)));
                teacher.setProfilePic(resultSet_Teachers.getString(8));
                teacher.setRemark(resultSet_Teachers.getString(9));

                teacher_teachersList.add(teacher);
            }
            teacher_listTeacher.setItems(teacher_teachersList);
            teacher_listTeacher.setCellFactory(param -> new Teacher_ListItem(new Teacher()));

            teacher_listTeacher.setVisible(true);
        }
        teachersDB.closeConnection();
    }

    /** SEARCH RESULT CASHBOOK **/
    private void displaySearchResult_Cashbook() throws SQLException {

        String strToSearch = "%"+ mainMenuLeftPane_txtSearch.getText() + "%";
        cashbook_tableViewList = FXCollections.observableArrayList();
        DBHandler cashbookDB = new DBHandler();
        ResultSet resultSet_CashbookSearch= null;

        // retrieve cashbook DB
        if(cashbookDB.getConnection()){
            resultSet_CashbookSearch = cashbookDB.searchFromDB(new CashBook(), strToSearch);

            while(resultSet_CashbookSearch.next()) {
                CashBook cashbook = new CashBook();
                cashbook.setRefId(resultSet_CashbookSearch.getInt(1));
                cashbook.setDate(LocalDate.parse(resultSet_CashbookSearch.getString(2)));
                cashbook.setDescription(resultSet_CashbookSearch.getString(3));
                cashbook.setIncome(resultSet_CashbookSearch.getDouble(4));
                cashbook.setExpense(resultSet_CashbookSearch.getDouble(5));
                cashbook.setBankBalance(resultSet_CashbookSearch.getDouble(6));

                cashbook_tableViewList.add(cashbook);
            }
            cashbook_tableView.setItems(cashbook_tableViewList);
            cashbook_tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            cashbook_tableView.setVisible(true);
        }
        cashbookDB.closeConnection();
    }

    private void displaySearchResult_Inventory() throws SQLException {

        String strToSearch = "%"+ mainMenuLeftPane_txtSearch.getText() + "%";
        inventory_tableViewList = FXCollections.observableArrayList();
        DBHandler inventoryDB = new DBHandler();
        ResultSet resultSet_InventorySearch= null;

        // retrieve inventory DB
        if(inventoryDB.getConnection()){
            resultSet_InventorySearch = inventoryDB.searchFromDB(new Inventory(), strToSearch);

            while(resultSet_InventorySearch.next()) {
                Inventory inventory = new Inventory();
                inventory.setRefId(resultSet_InventorySearch.getInt(1));
                inventory.setQuantity(resultSet_InventorySearch.getInt(2));
                inventory.setItem(resultSet_InventorySearch.getString(3));
                inventory.setPurchaseDate(LocalDate.parse(resultSet_InventorySearch.getString(4)));
                inventory_tableViewList.add(inventory);
            }
            inventory_tableView.setItems(inventory_tableViewList);
            inventory_tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            inventory_tableView.setVisible(true);
        }
        inventoryDB.closeConnection();
    }
}
