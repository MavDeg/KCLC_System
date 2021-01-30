package kclc.database;
import kclc.model.*;

import java.sql.*;

public class DBHandler extends DBConfig{

    private boolean isConnected = false;
    private static Connection connection;
    private static PreparedStatement preparedStatement;

    public boolean getConnection(){
        try {
            Class.forName("org.sqlite.JDBC");
            _setConnected(true);

            System.out.println("database connection successful");
            connection = DriverManager.getConnection(DB_FILE);
        }
        catch(Exception e){
            _setConnected(false);
            e.printStackTrace();
        }

        return this.isConnected;
    }

    public void closeConnection(){
        try {
            connection.close();
            _setConnected(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** READ
     * Overloaded methods depending on which table is being pass
     * return: all the information from database
     */
    public ResultSet readFromDB(Admin admin) throws SQLException{
        preparedStatement = (PreparedStatement) connection.prepareStatement(DB_ADMINS_SELECT);

        ResultSet resultSet = preparedStatement.executeQuery();
        //preparedStatement.close();
        return resultSet;

    }

    public ResultSet readFromDB(Student student) throws SQLException{
        preparedStatement = (PreparedStatement) connection.prepareStatement(DB_STUDENTS_SELECT);

        ResultSet resultSet = preparedStatement.executeQuery();
        //preparedStatement.close();
        return resultSet;
    }

    public ResultSet readFromDB(Teacher teacher) throws SQLException{
        preparedStatement = (PreparedStatement) connection.prepareStatement(DB_TEACHERS_SELECT);

        ResultSet resultSet = preparedStatement.executeQuery();
        //preparedStatement.close();
        return resultSet;
    }

    public ResultSet readFromDB(CashBook cashbook) throws SQLException{
        preparedStatement = (PreparedStatement) connection.prepareStatement(DB_CASHBOOK_SELECT);

        ResultSet resultSet = preparedStatement.executeQuery();
        //preparedStatement.close();
        return resultSet;
    }

    public ResultSet readLastRowDB(CashBook cashbook) throws SQLException{
        preparedStatement = (PreparedStatement) connection.prepareStatement(DB_CASHBOOK_SEARCH_LAST_ROW);

        ResultSet resultSet = preparedStatement.executeQuery();
        //preparedStatement.close();
        return resultSet;
    }

    public ResultSet readFromDB(Inventory inventory) throws SQLException{
        preparedStatement = (PreparedStatement) connection.prepareStatement(DB_INVENTORY_SELECT);

        ResultSet resultSet = preparedStatement.executeQuery();
        //preparedStatement.close();
        return resultSet;
    }

    /** WRITE
     * Overloaded methods depending on which table is being pass
     */

    public void writeToDB(Student student) throws SQLException{

        System.out.println(DB_STUDENTS_INSERT);

        preparedStatement = (PreparedStatement) connection.prepareStatement(DB_STUDENTS_INSERT);
        preparedStatement.setString(1, student.getFirstName());
        preparedStatement.setString(2, student.getMiddleName());
        preparedStatement.setString(3, student.getLastName());
        preparedStatement.setInt(4, student.getAge());
        preparedStatement.setInt(5, student.getGradeLevel());
        preparedStatement.setString(6, student.getGuardian());
        preparedStatement.setString(7, student.getPhone());
        preparedStatement.setString(8, student.getAddress());
        preparedStatement.setString(9, String.valueOf(student.getStartDate()));  // convert to string
        preparedStatement.setString(10, String.valueOf(student.getEndDate()));  // convert to string
        preparedStatement.setString(11, String.valueOf(student.getStartTime()));  // convert to string
        preparedStatement.setString(12, String.valueOf(student.getEndTime()));  // convert to string
        preparedStatement.setInt(13, student.getTeacherId());
        preparedStatement.setString(14, student.getPaymentStatus());
        preparedStatement.setDouble(15, student.getPayment());
        preparedStatement.setString(16, student.getProfilePic());
        preparedStatement.setString(17, student.getRemark());
        preparedStatement.executeUpdate();
        preparedStatement.close();
        System.out.println("witeToDB executed");
    }

    public void writeToDB(Teacher teacher) throws SQLException {
        System.out.println(DB_TEACHERS_INSERT);

        preparedStatement = (PreparedStatement) connection.prepareStatement(DB_TEACHERS_INSERT);
        preparedStatement.setString(1, teacher.getFirstName());
        preparedStatement.setString(2, teacher.getMiddleName());
        preparedStatement.setString(3, teacher.getLastName());
        preparedStatement.setString(4, teacher.getPhone());
        preparedStatement.setString(5, teacher.getAddress());
        preparedStatement.setString(6, String.valueOf(teacher.getEmploymentDate())); // convert to string
        preparedStatement.setString(7, teacher.getProfilePic());
        preparedStatement.setString(8, teacher.getRemark());
        preparedStatement.executeUpdate();
        preparedStatement.close();
        System.out.println("witeToDB executed");
    }

    public void writeToDB(CashBook cashbook) throws SQLException {

        preparedStatement = (PreparedStatement) connection.prepareStatement(DB_CASHBOOK_INSERT);
        preparedStatement.setString(1, String.valueOf(cashbook.getDate())); // convert to string
        preparedStatement.setString(2, cashbook.getDescription());
        preparedStatement.setDouble(3, cashbook.getIncome());
        preparedStatement.setDouble(4, cashbook.getExpense());
        preparedStatement.setDouble(5, cashbook.getBankBalance());// need some calculation
        preparedStatement.executeUpdate();
        preparedStatement.close();
        System.out.println("witeToDB executed");
    }

    public void writeToDB(Inventory inventory) throws SQLException {

        preparedStatement = (PreparedStatement) connection.prepareStatement(DB_INVENTORY_INSERT);
        preparedStatement.setInt(1 , inventory.getQuantity());
        preparedStatement.setString(2, inventory.getItem());
        preparedStatement.setString(3, String.valueOf(inventory.getPurchaseDate())); // convert to string
        preparedStatement.executeUpdate();
        preparedStatement.close();
        System.out.println("witeToDB executed");
    }

    /** UPDATE
     * Overloaded methods depending on which table is being pass
     */
    public void updateDB(Admin admin) throws SQLException {
        preparedStatement = (PreparedStatement) connection.prepareStatement(DB_ADMINS_UPDATE);
        preparedStatement.setString(1, admin.getUserName());
        preparedStatement.setString(2, admin.getPassword());
        preparedStatement.setString(3, admin.getFirstName());
        preparedStatement.setString(4, admin.getLastName());
        preparedStatement.setString(5, admin.getPhone());
        preparedStatement.setString(6, admin.getProfilePic());
        preparedStatement.setString(7, admin.getAddress());
        preparedStatement.setInt(8, admin.getAdminId());

        preparedStatement.executeUpdate();
        preparedStatement.close();

        System.out.println(DB_ADMINS_UPDATE);
        //preparedStatement.close();
    }

    public void updateDB(Student student) throws SQLException {
        preparedStatement = (PreparedStatement) connection.prepareStatement(DB_STUDENTS_UPDATE);
        preparedStatement.setString(1, student.getFirstName());
        preparedStatement.setString(2, student.getMiddleName());
        preparedStatement.setString(3, student.getLastName());
        preparedStatement.setInt(4, student.getAge());
        preparedStatement.setInt(5, student.getGradeLevel());
        preparedStatement.setString(6, student.getGuardian());
        preparedStatement.setString(7, student.getPhone());
        preparedStatement.setString(8, student.getAddress());
        preparedStatement.setString(9, String.valueOf(student.getStartDate()));  // convert to string
        preparedStatement.setString(10, String.valueOf(student.getEndDate()));  // convert to string
        preparedStatement.setString(11, String.valueOf(student.getStartTime()));  // convert to string
        preparedStatement.setString(12, String.valueOf(student.getEndTime()));  // convert to string
        preparedStatement.setInt(13, student.getTeacherId());
        preparedStatement.setString(14, student.getPaymentStatus());
        preparedStatement.setDouble(15, student.getPayment());
        preparedStatement.setString(16, student.getProfilePic());
        preparedStatement.setString(17, student.getRemark());
        preparedStatement.setInt(18, student.getStudentId());
        preparedStatement.executeUpdate();
        preparedStatement.close();

        System.out.println(DB_STUDENTS_UPDATE);
        //preparedStatement.close();
    }

    public void updateDB(Teacher teacher) throws SQLException {
        preparedStatement = (PreparedStatement) connection.prepareStatement(DB_TEACHERS_UPDATE);
        preparedStatement.setString(1, teacher.getFirstName());
        preparedStatement.setString(2, teacher.getMiddleName());
        preparedStatement.setString(3, teacher.getLastName());
        preparedStatement.setString(4, teacher.getPhone());
        preparedStatement.setString(5, teacher.getAddress());
        preparedStatement.setString(6, String.valueOf(teacher.getEmploymentDate()));  // convert to string
        preparedStatement.setString(7, teacher.getProfilePic());
        preparedStatement.setString(8, teacher.getRemark());
        preparedStatement.setInt(9, teacher.getTeacherId());

        preparedStatement.executeUpdate();
        preparedStatement.close();

        System.out.println(DB_STUDENTS_UPDATE);
        //preparedStatement.close();
    }

    public void updateDB(Inventory inventory) throws SQLException {
        System.out.println(DB_INVENTORY_UPDATE);
        preparedStatement = (PreparedStatement) connection.prepareStatement(DB_INVENTORY_UPDATE);
        preparedStatement.setInt(1 , inventory.getQuantity());
        preparedStatement.setString(2, inventory.getItem());
        preparedStatement.setString(3, String.valueOf(inventory.getPurchaseDate())); // convert to string
        preparedStatement.setInt(4, inventory.getRefId());
        preparedStatement.executeUpdate();
        preparedStatement.close();

        System.out.println(DB_INVENTORY_UPDATE);
        //preparedStatement.close();
    }

    /** DELETE
     * Overloaded methods depending on which table is being pass
     */
    public void deleteFromDB(Student student) throws SQLException {
        preparedStatement = (PreparedStatement) connection.prepareStatement(DB_STUDENTS_DELETE);

        preparedStatement.setInt(1, student.getStudentId());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void deleteFromDB(Teacher teacher) throws SQLException {
        preparedStatement = (PreparedStatement) connection.prepareStatement(DB_TEACHERS_DELETE);

        preparedStatement.setInt(1, teacher.getTeacherId());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void deleteFromDB(Inventory inventory) throws SQLException {
        preparedStatement = (PreparedStatement) connection.prepareStatement(DB_INVENTORY_DELETE);

        preparedStatement.setInt(1, inventory.getRefId());
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    /** SEARCH
     * Overloaded methods depending on which table is being pass
     */
    public ResultSet searchFromDB(Student student, String toSearch) throws SQLException{
        preparedStatement = (PreparedStatement) connection.prepareStatement(DB_STUDENTS_SEARCH);

        //preparedStatement.setString(1, toSearch);
        preparedStatement.setString(1, toSearch);
        preparedStatement.setString(2, toSearch);
        preparedStatement.setString(3, toSearch);
        ResultSet resultSet = preparedStatement.executeQuery();
        //preparedStatement.close();
        return resultSet;
    }


    public ResultSet searchFromDB(Teacher teacher, String toSearch) throws SQLException{
        preparedStatement = (PreparedStatement) connection.prepareStatement(DB_TEACHERS_SEARCH);

        //preparedStatement.setString(1, toSearch);
        preparedStatement.setString(1, toSearch);
        preparedStatement.setString(2, toSearch);
        preparedStatement.setString(3, toSearch);
        ResultSet resultSet = preparedStatement.executeQuery();
        //preparedStatement.close();
        return resultSet;
    }

    public ResultSet searchFromDB(CashBook cashbook, String toSearch) throws SQLException{
        preparedStatement = (PreparedStatement) connection.prepareStatement(DB_CASHBOOK_SEARCH);

        //preparedStatement.setString(1, toSearch);
        preparedStatement.setString(1, toSearch);
        ResultSet resultSet = preparedStatement.executeQuery();
        //preparedStatement.close();
        System.out.println(DB_CASHBOOK_SEARCH);
        return resultSet;
    }

    public ResultSet searchFromDB(Inventory inventory, String toSearch) throws SQLException{
        preparedStatement = (PreparedStatement) connection.prepareStatement(DB_INVENTORY_SEARCH);

        //preparedStatement.setString(1, toSearch);
        preparedStatement.setString(1, toSearch);
        ResultSet resultSet = preparedStatement.executeQuery();
        //preparedStatement.close();
        System.out.println(DB_INVENTORY_SEARCH);
        return resultSet;
    }
    /*
    public void writeToDB(String dbTable, PreparedStatement preparedStatement) throws SQLException {

        //select in which table to write
        switch (dbTable) {
            case DB_ADMINS:
            case DB_SCHOOLS:
            case DB_TEACHERS:
            case DB_STUDENTS:
            default:
                break;
        }
    }
    */



    private void _setConnected(boolean connected) {
        this.isConnected = connected;
    }

    public boolean isConnected() {
        return this.isConnected;

    }


}
