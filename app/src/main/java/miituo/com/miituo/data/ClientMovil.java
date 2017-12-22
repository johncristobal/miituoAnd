package miituo.com.miituo.data;

/**
 * Created by john.cristobal on 12/04/17.
 */

public class ClientMovil
{
    private int Id;
    private String Name;
    private String LastName;
    private String MotherName;
    private String Celphone;
    private String Token;

    public String getEmail() {
        return Email;
    }

    public void setEmail(String correo) {
        this.Email = correo;
    }

    private String Email;

    public ClientMovil(){

    }

    public String getCelphone() {
        return Celphone;
    }

    public void setCelphone(String celphone) {
        Celphone = celphone;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public ClientMovil(int id, String lastName, String motherName, String name) {
        Id = id;
        LastName = lastName;
        MotherName = motherName;
        Name = name;
        Celphone="";
        Token="";
        Email = "";
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getMotherName() {
        return MotherName;
    }

    public void setMotherName(String motherName) {
        MotherName = motherName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}