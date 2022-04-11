package model;

/**
 * A product.
 * Maps to a table in a database.
 */
public class Product {
    private int id;
    private String name;
    private int quantity;

    public Product(){
        id = 0;
        name = "";
        quantity = 0;
    }

    public Product(int id, String name, int quantity){
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
