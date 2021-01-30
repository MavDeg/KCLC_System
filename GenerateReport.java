package kclc;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import kclc.database.DBConfig;
import kclc.database.DBHandler;
import kclc.model.CashBook;
import kclc.model.Inventory;
import kclc.model.Student;
import kclc.model.Teacher;
import kclc.view.TransitionHelper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ResourceBundle;

public class GenerateReport {

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
    private JFXRadioButton radio_students;

    @FXML
    private JFXRadioButton radio_teachers;

    @FXML
    private JFXRadioButton radio_cashbook;

    @FXML
    private JFXRadioButton radio_inventory;

    @FXML
    private JFXButton btnExport;

    @FXML
    private JFXRadioButton radio_all;

    @FXML
    private JFXButton btnCancel;

    ToggleGroup radioButtonGroup = new ToggleGroup();

    DBConfig dbconfig = new DBConfig();

    @FXML
    void initialize() {

        radio_students.setToggleGroup(radioButtonGroup);
        radio_teachers.setToggleGroup(radioButtonGroup);
        radio_cashbook.setToggleGroup(radioButtonGroup);
        radio_inventory.setToggleGroup(radioButtonGroup);
        radio_all.setToggleGroup(radioButtonGroup);

        radio_students.setSelectedColor(Color.web("ff7d00ff"));
        radio_teachers.setSelectedColor(Color.web("ff7d00ff"));
        radio_cashbook.setSelectedColor(Color.web("ff7d00ff"));
        radio_inventory.setSelectedColor(Color.web("ff7d00ff"));
        radio_all.setSelectedColor(Color.web("ff7d00ff"));

        radio_students.setSelected(true); // default selected item

        btnExport.setOnAction(exportEvent ->{
            // open file location
            //generate report
            try {
                if(radio_students.isSelected()) {
                    exportStudentReport();
                }
                else if(radio_teachers.isSelected()){
                    exportTeacherReport();
                }
                else if(radio_cashbook.isSelected()){
                    exportCashbookReport();
                }
                else if(radio_inventory.isSelected()){
                    exportInventoryReport();
                }
                else if(radio_all.isSelected()){
                    exportAllReport();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        btnCancel.setOnAction(gotoMainEvent ->{
            TransitionHelper.windowFadeOutTransition(btnCancel.getScene().getWindow()); // fade then hide
        });

    }

    /**
     * STUDENTS REPORT
     *
     * ***/
    private void exportStudentReport() throws SQLException, IOException {
        DBHandler studentDB = new DBHandler();
        ResultSet resultResultSet_Student = null;

        String fileName = "Students_" + LocalDate.now().toString() + ".xlsx";

        if(studentDB.getConnection()){
            resultResultSet_Student = studentDB.readFromDB(new Student());

            //while (resultResultSet_Student.next()) {
                //Student student = new Student();
                XSSFWorkbook workbook = new XSSFWorkbook();
                XSSFSheet sheet = workbook.createSheet("Students");

            exportStudentReport_Header(sheet);
            exportStudentReport_Rows(resultResultSet_Student, workbook, sheet);

                //FileOutputStream outputStream = new FileOutputStream(excelFilePath);
                //workbook.write(outputStream);
                //workbook.close();
                saveFile(workbook, fileName); // saveDialogBox

            //}

            studentDB.closeConnection();
        }
    }

    private void exportStudentReport_Header(XSSFSheet sheet){
        Row headerRow = sheet.createRow(0);

        Cell headerCell = headerRow.createCell(0);
        headerCell.setCellValue(dbconfig.DB_STUDENTS_ID_COL);
        headerCell = headerRow.createCell(1);
        headerCell.setCellValue(dbconfig.DB_STUDENTS_FNAME_COL);
        headerCell = headerRow.createCell(2);
        headerCell.setCellValue(dbconfig.DB_STUDENTS_MNAME_COL);
        headerCell = headerRow.createCell(3);
        headerCell.setCellValue(dbconfig.DB_STUDENTS_LNAME_COL);
        headerCell = headerRow.createCell(4);
        headerCell.setCellValue(dbconfig.DB_STUDENTS_AGE_COL);
        headerCell = headerRow.createCell(5);
        headerCell.setCellValue(dbconfig.DB_STUDENTS_GRADELEVEL_COL);
        headerCell = headerRow.createCell(6);
        headerCell.setCellValue(dbconfig.DB_STUDENTS_GUARDIAN_COL);
        headerCell = headerRow.createCell(7);
        headerCell.setCellValue(dbconfig.DB_STUDENTS_PHONE_COL);
        headerCell = headerRow.createCell(8);
        headerCell.setCellValue(dbconfig.DB_STUDENTS_ADDRESS_COL);
        headerCell = headerRow.createCell(9);
        headerCell.setCellValue(dbconfig.DB_STUDENTS_STARTDATE_COL);
        headerCell = headerRow.createCell(10);
        headerCell.setCellValue(dbconfig.DB_STUDENTS_ENDDATE_COL);
        headerCell = headerRow.createCell(11);
        headerCell.setCellValue(dbconfig.DB_STUDENTS_STARTTIME_COL);
        headerCell = headerRow.createCell(12);
        headerCell.setCellValue(dbconfig.DB_STUDENTS_ENDTIME_COL);
        headerCell = headerRow.createCell(13);
        headerCell.setCellValue(dbconfig.DB_STUDENTS_TEACHERID_COL);
        headerCell = headerRow.createCell(14);
        headerCell.setCellValue(dbconfig.DB_STUDENTS_PAYMENTSTATUS_COL);
        headerCell = headerRow.createCell(15);
        headerCell.setCellValue(dbconfig.DB_STUDENTS_PAYMENT_COL);
        headerCell = headerRow.createCell(16);
        headerCell.setCellValue(dbconfig.DB_STUDENTS_PROFILEPIC_COL);
        headerCell = headerRow.createCell(17);
        headerCell.setCellValue(dbconfig.DB_STUDENTS_REMARK_COL);
    }

    private void exportStudentReport_Rows(ResultSet result, XSSFWorkbook workbook, XSSFSheet sheet) throws SQLException {
            int rowCount = 1;

        while (result.next()) {

            Row row = sheet.createRow(rowCount++);

            int columnCount = 0;
            Cell cell = row.createCell(columnCount++);
            cell.setCellValue(result.getString(dbconfig.DB_STUDENTS_ID_COL));

            cell = row.createCell(columnCount++);
            cell.setCellValue(result.getString(dbconfig.DB_STUDENTS_FNAME_COL));

            cell = row.createCell(columnCount++);
            cell.setCellValue(result.getString(dbconfig.DB_STUDENTS_MNAME_COL));

            cell = row.createCell(columnCount++);
            cell.setCellValue(result.getString(dbconfig.DB_STUDENTS_LNAME_COL));

            cell = row.createCell(columnCount++);
            cell.setCellValue(result.getString(dbconfig.DB_STUDENTS_AGE_COL));

            cell = row.createCell(columnCount++);
            cell.setCellValue(result.getString(dbconfig.DB_STUDENTS_GRADELEVEL_COL));

            cell = row.createCell(columnCount++);
            cell.setCellValue(result.getString(dbconfig.DB_STUDENTS_GUARDIAN_COL));

            cell = row.createCell(columnCount++);
            cell.setCellValue(result.getString(dbconfig.DB_STUDENTS_PHONE_COL));

            cell = row.createCell(columnCount++);
            cell.setCellValue(result.getString(dbconfig.DB_STUDENTS_ADDRESS_COL));

            cell = row.createCell(columnCount++);
            cell.setCellValue(result.getString(dbconfig.DB_STUDENTS_STARTDATE_COL));

            cell = row.createCell(columnCount++);
            cell.setCellValue(result.getString(dbconfig.DB_STUDENTS_ENDDATE_COL));

            cell = row.createCell(columnCount++);
            cell.setCellValue(result.getString(dbconfig.DB_STUDENTS_STARTTIME_COL));

            cell = row.createCell(columnCount++);
            cell.setCellValue(result.getString(dbconfig.DB_STUDENTS_ENDTIME_COL));

            cell = row.createCell(columnCount++);
            cell.setCellValue(result.getString(dbconfig.DB_STUDENTS_TEACHERID_COL));

            cell = row.createCell(columnCount++);
            cell.setCellValue(result.getString(dbconfig.DB_STUDENTS_LNAME_COL));

            cell = row.createCell(columnCount++);
            cell.setCellValue(result.getString(dbconfig.DB_STUDENTS_LNAME_COL));

            cell = row.createCell(columnCount++);
            cell.setCellValue(result.getString(dbconfig.DB_STUDENTS_LNAME_COL));

            cell = row.createCell(columnCount++);
            cell.setCellValue(result.getString(dbconfig.DB_STUDENTS_LNAME_COL));
        }

    }

    /**
     * TEACHERS REPORT
     *
     * ***/
    private void exportTeacherReport() throws SQLException, IOException {
        DBHandler teacherDB = new DBHandler();
        ResultSet resultResultSet_Teacher= null;

        String fileName = "Teachers_" + LocalDate.now().toString() + ".xlsx";

        if(teacherDB.getConnection()){
            resultResultSet_Teacher = teacherDB.readFromDB(new Teacher());

            //while (resultResultSet_Student.next()) {
            //Student student = new Student();
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Teachers");

            exportTeacherReport_Header(sheet);
            exportTeacherReport_Rows(resultResultSet_Teacher, workbook, sheet);

            // todo: add open file dialog first
            //FileOutputStream outputStream = new FileOutputStream(excelFilePath);
            //workbook.write(outputStream);
            //workbook.close();
            saveFile(workbook, fileName); // saveDialogBox

            //}

            teacherDB.closeConnection();
        }
    }

    private void exportTeacherReport_Header(XSSFSheet sheet){
        Row headerRow = sheet.createRow(0);

        Cell headerCell = headerRow.createCell(0);
        headerCell.setCellValue(dbconfig.DB_TEACHERS_TEACHERID_COL);
        headerCell = headerRow.createCell(1);
        headerCell.setCellValue(dbconfig.DB_TEACHERS_FNAME_COL);
        headerCell = headerRow.createCell(2);
        headerCell.setCellValue(dbconfig.DB_TEACHERS_MNAME_COL);
        headerCell = headerRow.createCell(3);
        headerCell.setCellValue(dbconfig.DB_TEACHERS_LNAME_COL);
        headerCell = headerRow.createCell(4);
        headerCell.setCellValue(dbconfig.DB_TEACHERS_PHONE_COL);
        headerCell = headerRow.createCell(5);
        headerCell.setCellValue(dbconfig.DB_TEACHERS_ADDRESS_COL);
        headerCell = headerRow.createCell(6);
        headerCell.setCellValue(dbconfig.DB_TEACHERS_EMPLOYMENTDATE_COL);
        headerCell = headerRow.createCell(7);
        headerCell.setCellValue(dbconfig.DB_TEACHERS_PROFILEPIC_COL);
        headerCell = headerRow.createCell(8);
        headerCell.setCellValue(dbconfig.DB_TEACHERS_REMARK_COL);
    }

    private void exportTeacherReport_Rows(ResultSet result, XSSFWorkbook workbook, XSSFSheet sheet) throws SQLException {
        int rowCount = 1;

        while (result.next()) {

            Row row = sheet.createRow(rowCount++);

            int columnCount = 0;
            Cell cell = row.createCell(columnCount++);
            cell.setCellValue(result.getString(dbconfig.DB_TEACHERS_TEACHERID_COL));

            cell = row.createCell(columnCount++);
            cell.setCellValue(result.getString(dbconfig.DB_TEACHERS_FNAME_COL));

            cell = row.createCell(columnCount++);
            cell.setCellValue(result.getString(dbconfig.DB_TEACHERS_MNAME_COL));

            cell = row.createCell(columnCount++);
            cell.setCellValue(result.getString(dbconfig.DB_TEACHERS_LNAME_COL));

            cell = row.createCell(columnCount++);
            cell.setCellValue(result.getString(dbconfig.DB_TEACHERS_PHONE_COL));

            cell = row.createCell(columnCount++);
            cell.setCellValue(result.getString(dbconfig.DB_TEACHERS_ADDRESS_COL));

            cell = row.createCell(columnCount++);
            cell.setCellValue(result.getString(dbconfig.DB_TEACHERS_EMPLOYMENTDATE_COL));

            cell = row.createCell(columnCount++);
            cell.setCellValue(result.getString(dbconfig.DB_TEACHERS_PROFILEPIC_COL));

            cell = row.createCell(columnCount++);
            cell.setCellValue(result.getString(dbconfig.DB_TEACHERS_REMARK_COL));
        }
    }


    /**
     * CASHBOOK REPORT
     *
     * ***/
    private void exportCashbookReport() throws SQLException, IOException {
        DBHandler cashbookDB = new DBHandler();
        ResultSet resultResultSet_CashBook= null;

        String fileName = "Cashbook_" + LocalDate.now().toString() + ".xlsx";

        if(cashbookDB.getConnection()){
            resultResultSet_CashBook = cashbookDB.readFromDB(new CashBook());

            //while (resultResultSet_Student.next()) {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Cashbook");

            exportCashbookReport_Header(sheet);
            exportCashbookReport_Rows(resultResultSet_CashBook, workbook, sheet);

            // todo: add open file dialog first
            //FileOutputStream outputStream = new FileOutputStream(excelFilePath);
            //workbook.write(outputStream);
            //workbook.close();
            saveFile(workbook, fileName); // saveDialogBox

            //}

            cashbookDB.closeConnection();
        }
    }

    private void exportCashbookReport_Header(XSSFSheet sheet){
        Row headerRow = sheet.createRow(0);

        Cell headerCell = headerRow.createCell(0);
        headerCell.setCellValue(dbconfig.DB_CASHBOOK_REFID_COL);
        headerCell = headerRow.createCell(1);
        headerCell.setCellValue(dbconfig.DB_CASHBOOK_DATE_COL);
        headerCell = headerRow.createCell(2);
        headerCell.setCellValue(dbconfig.DB_CASHBOOK_DESCRIPTION_COL);
        headerCell = headerRow.createCell(3);
        headerCell.setCellValue(dbconfig.DB_CASHBOOK_INCOME_COL);
        headerCell = headerRow.createCell(4);
        headerCell.setCellValue(dbconfig.DB_CASHBOOK_EXPENSE_COL);
        headerCell = headerRow.createCell(5);
        headerCell.setCellValue(dbconfig.DB_CASHBOOK_BANKBALANCE_COL);
    }

    private void exportCashbookReport_Rows(ResultSet result, XSSFWorkbook workbook, XSSFSheet sheet) throws SQLException {
        int rowCount = 1;

        while (result.next()) {

            Row row = sheet.createRow(rowCount++);

            int columnCount = 0;
            Cell cell = row.createCell(columnCount++);
            cell.setCellValue(result.getString(dbconfig.DB_CASHBOOK_REFID_COL));

            cell = row.createCell(columnCount++);
            cell.setCellValue(result.getString(dbconfig.DB_CASHBOOK_DATE_COL));

            cell = row.createCell(columnCount++);
            cell.setCellValue(result.getString(dbconfig.DB_CASHBOOK_DESCRIPTION_COL));

            cell = row.createCell(columnCount++);
            cell.setCellValue(result.getString(dbconfig.DB_CASHBOOK_INCOME_COL));

            cell = row.createCell(columnCount++);
            cell.setCellValue(result.getString(dbconfig.DB_CASHBOOK_EXPENSE_COL));

            cell = row.createCell(columnCount++);
            cell.setCellValue(result.getString(dbconfig.DB_CASHBOOK_BANKBALANCE_COL));
        }
    }


    /**
     * INVENTORY REPORT
     *
     * ***/
    private void exportInventoryReport() throws SQLException, IOException {
        DBHandler inventoryDB = new DBHandler();
        ResultSet resultResultSet_Inventory= null;

        String fileName = "Inventory_" + LocalDate.now().toString() + ".xlsx";

        if(inventoryDB.getConnection()){
            resultResultSet_Inventory = inventoryDB.readFromDB(new Inventory());

            //while (resultResultSet_Student.next()) {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Inventory");

            exportInventoryReport_Header(sheet);
            exportInventoryReport_Rows(resultResultSet_Inventory, workbook, sheet);

            // todo: add open file dialog first
            //FileOutputStream outputStream = new FileOutputStream(excelFilePath);
            //workbook.write(outputStream);
            //workbook.close();
            saveFile(workbook, fileName); // saveDialogBox

            //}

            inventoryDB.closeConnection();
        }
    }

    private void exportInventoryReport_Header(XSSFSheet sheet){
        Row headerRow = sheet.createRow(0);

        Cell headerCell = headerRow.createCell(0);
        headerCell.setCellValue(dbconfig.DB_INVENTORY_ID_COL);
        headerCell = headerRow.createCell(1);
        headerCell.setCellValue(dbconfig.DB_INVENTORY_QTY_COL);
        headerCell = headerRow.createCell(2);
        headerCell.setCellValue(dbconfig.DB_INVENTORY_ITEM_COL);
        headerCell = headerRow.createCell(3);
        headerCell.setCellValue(dbconfig.DB_INVENTORY_DATE_COL);
    }

    private void exportInventoryReport_Rows(ResultSet result, XSSFWorkbook workbook, XSSFSheet sheet) throws SQLException {
        int rowCount = 1;

        while (result.next()) {

            Row row = sheet.createRow(rowCount++);

            int columnCount = 0;
            Cell cell = row.createCell(columnCount++);
            cell.setCellValue(result.getString(dbconfig.DB_INVENTORY_ID_COL));

            cell = row.createCell(columnCount++);
            cell.setCellValue(result.getString(dbconfig.DB_INVENTORY_QTY_COL));

            cell = row.createCell(columnCount++);
            cell.setCellValue(result.getString(dbconfig.DB_INVENTORY_ITEM_COL));

            cell = row.createCell(columnCount++);
            cell.setCellValue(result.getString(dbconfig.DB_INVENTORY_DATE_COL));
        }
    }

    /**
     * ALL REPORT
     *
     * ***/
    private void exportAllReport() throws SQLException, IOException {
        DBHandler studentDB = new DBHandler();
        DBHandler teachersDB = new DBHandler();
        DBHandler cashbookDB = new DBHandler();
        DBHandler inventoryDB = new DBHandler();
        ResultSet resultResultSet_Students= null;
        ResultSet resultResultSet_Teachers= null;
        ResultSet resultResultSet_CashBook= null;
        ResultSet resultResultSet_Inventory= null;

        String fileName = "KCLC_Database_Backup_" + LocalDate.now().toString() + ".xlsx";

        if(studentDB.getConnection()){ // open db connection, common
            resultResultSet_Students = studentDB.readFromDB(new Student());
            resultResultSet_Teachers = teachersDB.readFromDB(new Teacher());
            resultResultSet_CashBook = cashbookDB.readFromDB(new CashBook());
            resultResultSet_Inventory = inventoryDB.readFromDB(new Inventory());

            //while (resultResultSet_Student.next()) {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet student_sheet = workbook.createSheet("Students");
            XSSFSheet teacher_sheet = workbook.createSheet("Teachers");
            XSSFSheet cashbook_sheet = workbook.createSheet("Cashbook");
            XSSFSheet inventory_sheet = workbook.createSheet("Inventory");

            exportStudentReport_Header(student_sheet);
            exportStudentReport_Rows(resultResultSet_Students, workbook, student_sheet);

            exportTeacherReport_Header(teacher_sheet);
            exportTeacherReport_Rows(resultResultSet_Teachers, workbook, teacher_sheet);

            exportCashbookReport_Header(cashbook_sheet);
            exportCashbookReport_Rows(resultResultSet_CashBook, workbook, cashbook_sheet);

            exportInventoryReport_Header(inventory_sheet);
            exportInventoryReport_Rows(resultResultSet_Inventory, workbook, inventory_sheet);

            // todo: add open file dialog first
            //FileOutputStream outputStream = new FileOutputStream(excelFilePath);
            //workbook.write(outputStream);
            //workbook.close();
            saveFile(workbook, fileName); // saveDialogBox

            //}

            studentDB.closeConnection();
        }
    }



    /**
     * SAVE DIALOG
     *
     * ***/
    private void saveFile(XSSFWorkbook workbook, String fileName) throws IOException {
        Stage stage = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Files", "*.*"));
        fileChooser.setInitialFileName(fileName);
        File file = fileChooser.showSaveDialog(stage);

        if(file != null){
            FileOutputStream outputStream = new FileOutputStream(file);
            System.out.println(file);
            workbook.write(outputStream);
            workbook.close();
        }

    }
}
