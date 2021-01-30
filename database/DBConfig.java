package kclc.database;

public class DBConfig {

    /* database path and table names */
    public final String DB_FILE = "jdbc:sqlite:src/kclc/database/kclc.db";
    //public final String DB_FILE = "jdbc:sqlite:kclc.db";
    public final String DB_ADMINS = "Admins";
    public final String DB_STUDENTS = "Students";
    public final String DB_TEACHERS = "Teachers";
    public final String DB_CASHBOOK= "Cashbook";
    public final String DB_INVENTORY= "Inventory";
    public final String DB_SCHOOLS = "Schools";


    /* database column names */
    // ADMINS column names
    public final String DB_ADMINS_ID_COL = "adminId";
    public final String DB_ADMINS_USERNAME_COL = "userName";
    public final String DB_ADMINS_PASSWORD_COL = "password";
    public final String DB_ADMINS_FNAME_COL = "firstname";
    public final String DB_ADMINS_LNAME_COL = "lastName";
    public final String DB_ADMINS_PHONE_COL = "phone";
    public final String DB_ADMINS_PROFILEPIC_COL = "profilePic";
    public final String DB_ADMINS_ADDRESS_COL = "address";

    //STUDENTS column names
    public final String DB_STUDENTS_ID_COL = "studentId";
    public final String DB_STUDENTS_FNAME_COL = "firstName";
    public final String DB_STUDENTS_MNAME_COL = "middleName";
    public final String DB_STUDENTS_LNAME_COL = "lastName";
    public final String DB_STUDENTS_AGE_COL = "age";
    public final String DB_STUDENTS_GRADELEVEL_COL = "gradeLevel";
    public final String DB_STUDENTS_GUARDIAN_COL = "guardian";
    public final String DB_STUDENTS_PHONE_COL = "phone";
    public final String DB_STUDENTS_ADDRESS_COL = "address";
    public final String DB_STUDENTS_REMARK_COL = "remark";
    public final String DB_STUDENTS_STARTDATE_COL = "startDate";
    public final String DB_STUDENTS_ENDDATE_COL = "endDate";
    public final String DB_STUDENTS_STARTTIME_COL = "startTime";
    public final String DB_STUDENTS_ENDTIME_COL = "endTime";
    public final String DB_STUDENTS_TEACHERID_COL = "teacherId";
    public final String DB_STUDENTS_PAYMENTSTATUS_COL = "paymentStatus";
    public final String DB_STUDENTS_PROFILEPIC_COL = "profilePic";
    public final String DB_STUDENTS_PAYMENT_COL = "payment";

    //TEACHERS column names
    public final String DB_TEACHERS_TEACHERID_COL = "teacherId";
    public final String DB_TEACHERS_STUDENTID_COL = "studentId";
    public final String DB_TEACHERS_FNAME_COL = "firstName";
    public final String DB_TEACHERS_MNAME_COL = "middleName";
    public final String DB_TEACHERS_LNAME_COL = "lastName";
    public final String DB_TEACHERS_PHONE_COL = "phone";
    public final String DB_TEACHERS_ADDRESS_COL = "address";
    public final String DB_TEACHERS_EMPLOYMENTDATE_COL = "employmentDate";
    public final String DB_TEACHERS_SALARY_COL = "salary";
    public final String DB_TEACHERS_PROFILEPIC_COL = "profilePic";
    public final String DB_TEACHERS_REMARK_COL = "remark";

    //CASHBOOK column names
    public final String DB_CASHBOOK_REFID_COL = "refId";
    public final String DB_CASHBOOK_DATE_COL = "date";
    public final String DB_CASHBOOK_DESCRIPTION_COL = "description";
    public final String DB_CASHBOOK_INCOME_COL = "income";
    public final String DB_CASHBOOK_EXPENSE_COL = "expense";
    public final String DB_CASHBOOK_BANKBALANCE_COL = "bankBalance";

    //INVENTORY column names
    public final String DB_INVENTORY_ID_COL = "refId";
    public final String DB_INVENTORY_QTY_COL = "quantity";
    public final String DB_INVENTORY_ITEM_COL = "item";
    public final String DB_INVENTORY_DATE_COL = "purchaseDate";

    // COMMON
    public final String DB_PAYMENT_STATUS_FULL = "Full";
    public final String DB_PAYMENT_STATUS_PARTIAL = "Partial";
    public final String DB_PAYMENT_STATUS_UNPAID = "Unpaid";

    /* database table operations */

    // ADMINS
    public final String DB_ADMINS_INSERT = "INSERT INTO " + DB_ADMINS + "(" + DB_ADMINS_USERNAME_COL + ", " + DB_ADMINS_PASSWORD_COL + ", " + DB_ADMINS_FNAME_COL + ", "
            + DB_ADMINS_LNAME_COL + ", " + DB_ADMINS_PHONE_COL + ", " + DB_ADMINS_ADDRESS_COL + ") VALUES (?,?,?,?,?,?)";

    public final String DB_ADMINS_SELECT = "SELECT * FROM " + DB_ADMINS;

    public final String DB_ADMINS_UPDATE = "UPDATE "+ DB_ADMINS + " SET " + DB_ADMINS_USERNAME_COL + " = ?, " + DB_ADMINS_PASSWORD_COL + " = ?, "
            + DB_ADMINS_FNAME_COL + " = ?, " + DB_ADMINS_LNAME_COL + " = ?, " + DB_ADMINS_PHONE_COL + " = ?, " +
            DB_ADMINS_PROFILEPIC_COL + " = ?, " + DB_ADMINS_ADDRESS_COL + " = ? WHERE " +
            DB_ADMINS_ID_COL + " = ?";

    public final String DB_ADMINS_DELETE = "DELETE FROM " + DB_ADMINS + " WHERE " + DB_ADMINS_ID_COL + " = ? ";


    // STUDENTS
    public final String DB_STUDENTS_INSERT = "INSERT INTO " + DB_STUDENTS + "(" + DB_STUDENTS_FNAME_COL + ", " + DB_STUDENTS_MNAME_COL + ", " + DB_STUDENTS_LNAME_COL + ", "
            + DB_STUDENTS_AGE_COL + ", " + DB_STUDENTS_GRADELEVEL_COL + ", " + DB_STUDENTS_GUARDIAN_COL + ", " + DB_STUDENTS_PHONE_COL +
            ", " + DB_STUDENTS_ADDRESS_COL + ", " + DB_STUDENTS_STARTDATE_COL + ", " + DB_STUDENTS_ENDDATE_COL + ", " + DB_STUDENTS_STARTTIME_COL + ", " +
            DB_STUDENTS_ENDTIME_COL + ", "  + DB_STUDENTS_TEACHERID_COL + ", " + DB_STUDENTS_PAYMENTSTATUS_COL + ", " + DB_STUDENTS_PAYMENT_COL + ", " +
            DB_STUDENTS_PROFILEPIC_COL + ", " + DB_STUDENTS_REMARK_COL + ") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    //public final String DB_STUDENTS_SELECT = "SELECT * FROM " + DB_STUDENTS;
    public final String DB_STUDENTS_SELECT = "SELECT * FROM " + DB_STUDENTS + " ORDER BY " + DB_STUDENTS_FNAME_COL + " COLLATE NOCASE ASC";

    //public final String DB_STUDENTS_SEARCH = "SELECT * FROM " + DB_STUDENTS + " WHERE " + DB_STUDENTS_FNAME_COL + " LIKE ?";
    public final String DB_STUDENTS_SEARCH = "SELECT * FROM " + DB_STUDENTS + " WHERE " + DB_STUDENTS_FNAME_COL + " LIKE ?" + " OR " +
            DB_STUDENTS_MNAME_COL + " LIKE ? OR " + DB_STUDENTS_LNAME_COL + " LIKE ?";


    public final String DB_STUDENTS_UPDATE = "UPDATE "+ DB_STUDENTS + " SET " + DB_STUDENTS_FNAME_COL + "=?, " + DB_STUDENTS_MNAME_COL + "=?, "
            + DB_STUDENTS_LNAME_COL + "=?, " + DB_STUDENTS_AGE_COL + "=?, " + DB_STUDENTS_GRADELEVEL_COL + "=?, " + DB_STUDENTS_GUARDIAN_COL + "=?, "
            + DB_STUDENTS_PHONE_COL + "=?, " + DB_STUDENTS_ADDRESS_COL + "=?, " + DB_STUDENTS_STARTDATE_COL + "=?, " + DB_STUDENTS_ENDDATE_COL + "=?, "
            + DB_STUDENTS_STARTTIME_COL + "=?, " + DB_STUDENTS_ENDTIME_COL + "=?, " + DB_STUDENTS_TEACHERID_COL + "=?, " + DB_STUDENTS_PAYMENTSTATUS_COL + "=?, "
            + DB_STUDENTS_PAYMENT_COL + "=?, " + DB_STUDENTS_PROFILEPIC_COL + "=?, " + DB_STUDENTS_REMARK_COL + "=? WHERE " + DB_STUDENTS_ID_COL + "=?";

    public final String DB_STUDENTS_DELETE = "DELETE FROM " + DB_STUDENTS + " WHERE " + DB_STUDENTS_ID_COL + " = ? ";

    // TEACHERS
    public final String DB_TEACHERS_INSERT = "INSERT INTO " + DB_TEACHERS + "(" + DB_TEACHERS_FNAME_COL + ", " + DB_TEACHERS_MNAME_COL + ", "
            + DB_TEACHERS_LNAME_COL + ", " + DB_TEACHERS_PHONE_COL + ", " + DB_TEACHERS_ADDRESS_COL + ", " + DB_TEACHERS_EMPLOYMENTDATE_COL + ", " +
            DB_TEACHERS_PROFILEPIC_COL + ", " + DB_TEACHERS_REMARK_COL + ") VALUES (?,?,?,?,?,?,?,?)";

    public final String DB_TEACHERS_SELECT = "SELECT * FROM " + DB_TEACHERS;

    public final String DB_TEACHERS_SEARCH = "SELECT * FROM " + DB_TEACHERS + " WHERE " + DB_TEACHERS_FNAME_COL + " LIKE ?" + " OR " +
            DB_TEACHERS_MNAME_COL + " LIKE ? OR " + DB_TEACHERS_LNAME_COL + " LIKE ?";

    public final String DB_TEACHERS_UPDATE = "UPDATE "+ DB_TEACHERS + " SET " + DB_TEACHERS_FNAME_COL + " = ?, "
            + DB_TEACHERS_MNAME_COL + " = ?, " + DB_TEACHERS_LNAME_COL + " = ?, " + DB_TEACHERS_PHONE_COL + " = ?, " + DB_TEACHERS_ADDRESS_COL + " = ?, "
            + DB_TEACHERS_EMPLOYMENTDATE_COL + " = ?, " + DB_TEACHERS_PROFILEPIC_COL + " = ?, " + DB_TEACHERS_REMARK_COL + " = ? WHERE " + DB_TEACHERS_TEACHERID_COL + " = ?";

    public final String DB_TEACHERS_DELETE = "DELETE FROM " + DB_TEACHERS + " WHERE " + DB_TEACHERS_TEACHERID_COL + " = ? ";

    // CASHBOOK
    public final String DB_CASHBOOK_INSERT = "INSERT INTO " + DB_CASHBOOK + "(" + DB_CASHBOOK_DATE_COL + ", " + DB_CASHBOOK_DESCRIPTION_COL + ", "
            + DB_CASHBOOK_INCOME_COL + ", " + DB_CASHBOOK_EXPENSE_COL + ", " + DB_CASHBOOK_BANKBALANCE_COL+ ") VALUES (?,?,?,?,?)";

    public final String DB_CASHBOOK_SELECT = "SELECT * FROM " + DB_CASHBOOK + " ORDER BY " + DB_CASHBOOK_REFID_COL + " DESC";

    public final String DB_CASHBOOK_SEARCH = "SELECT * FROM " + DB_CASHBOOK + " WHERE " + DB_CASHBOOK_DESCRIPTION_COL + " LIKE ?" + " ORDER BY " + DB_CASHBOOK_REFID_COL + " DESC";

    public final String DB_CASHBOOK_SEARCH_LAST_ROW = "SELECT " + DB_CASHBOOK_BANKBALANCE_COL + " FROM " + DB_CASHBOOK + " ORDER BY " + DB_CASHBOOK_REFID_COL + " DESC LIMIT 1";


    public final String DB_CASHBOOK_UPDATE = "UPDATE "+ DB_CASHBOOK + " SET " + DB_CASHBOOK_DATE_COL + " = ?, "
            + DB_CASHBOOK_DESCRIPTION_COL + " = ?, " + DB_CASHBOOK_INCOME_COL + " = ?, " + DB_CASHBOOK_EXPENSE_COL + " = ?, " + DB_CASHBOOK_BANKBALANCE_COL + " = ?" +
            " WHERE " + DB_CASHBOOK_REFID_COL + " = ?";

    public final String DB_CASHBOOK_DELETE = "DELETE FROM " + DB_CASHBOOK + " WHERE " + DB_CASHBOOK_REFID_COL + " = ? ";


    // INVENTORY
    public final String DB_INVENTORY_INSERT = "INSERT INTO " + DB_INVENTORY + "(" + DB_INVENTORY_QTY_COL + ", " + DB_INVENTORY_ITEM_COL + ", "
            + DB_INVENTORY_DATE_COL + ") VALUES (?,?,?)";

    public final String DB_INVENTORY_SELECT = "SELECT * FROM " + DB_INVENTORY + " ORDER BY " + DB_INVENTORY_ID_COL + " DESC";

    public final String DB_INVENTORY_SEARCH = "SELECT * FROM " + DB_INVENTORY + " WHERE " + DB_INVENTORY_ITEM_COL + " LIKE ?" + " ORDER BY " + DB_INVENTORY_ID_COL + " DESC";

    public final String DB_INVENTORY_UPDATE = "UPDATE " + DB_INVENTORY + " SET " + DB_INVENTORY_QTY_COL + " = ?, "
            + DB_INVENTORY_ITEM_COL + " = ?, " + DB_INVENTORY_DATE_COL + " = ? " + "WHERE " + DB_INVENTORY_ID_COL + " = ?";

    public final String DB_INVENTORY_DELETE = "DELETE FROM " + DB_INVENTORY + " WHERE " + DB_INVENTORY_ID_COL + " = ? ";
}
