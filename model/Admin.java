package kclc.model;

/*
* THIS CLASS MUST BE IMPLEMENTED AS SINGLETON.
* Only 1 admin at a time can login
* */
public class Admin {

    private static Admin admin_instance = null;

    private int adminId;
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private String profilePic;
    private String address;

    private Admin() {
    }

    private Admin(int adminId, String userName, String password, String firstName, String lastName, String phone, String address) {
        this.adminId = adminId;
        this.userName = userName;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.address = address;
    }

    public static Admin getInstance(){
        if(admin_instance == null){
            admin_instance = new Admin();
        }
        return admin_instance;
    }

    public int getAdminId() {
        return adminId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
