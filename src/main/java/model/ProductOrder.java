package model;

/**
 * An order.
 * Maps to a table in a database.
 */
public class ProductOrder {
    private int id;
    private int clientId;
    private int productId;
    private int quantity;
    private String date;

    public ProductOrder(){
        id = 0;
        clientId = 0;
        productId = 0;
        quantity = 0;
        date = "";
    }

    public ProductOrder(int id, int clientId, int productId, int quantity, String date){
        this.id = id;
        this.clientId = clientId;
        this.productId = productId;
        this.quantity = quantity;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public int getClientId() {
        return clientId;
    }

    public int getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDate() {
        return date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
