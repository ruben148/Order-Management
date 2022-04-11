package model;

/**
 * A client.
 * Maps to a table in a database.
 */
public class Client {
    private int id;
    private String name;
    private String address;
    private String email;

    public Client(){
        id = 0;
        name = "";
        address = "";
        email = "";
    }

    public Client(int id, String name, String address, String email){
        this.id = id;
        this.name = name;
        this.address = address;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
