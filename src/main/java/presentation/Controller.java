package presentation;

import businesslogic.ClientBL;
import businesslogic.OrderBL;
import businesslogic.ProductBL;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import model.Client;
import model.ProductOrder;
import model.Product;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.NoSuchElementException;

/**
 * Makes the connection between the sub-dialogs and the business logic
 */
public class Controller extends JFrame {
    JButton clients = new JButton("Clients");
    JButton products = new JButton("Products");
    JButton orders = new JButton("Orders");
    ClientsDialog clientsDialog;
    ProductsDialog productsDialog;
    OrdersDialog ordersDialog;

    public Controller() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(150, 150);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());
        add(clients);
        add(products);
        add(orders);

        ActionListener addNewClient = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Client client = clientsDialog.getInsertedClient();
                    int id = ClientBL.insert(client);
                    client.setId(id);
                    clientsDialog.insertClient(client);
                } catch (NoSuchElementException noSuchElementException) {
                    noSuchElementException.printStackTrace();
                } catch (IllegalArgumentException illegalArgumentException) {
                    JOptionPane.showMessageDialog(new JFrame(), illegalArgumentException.getMessage());
                }
            }
        };

        ActionListener editClient = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Client c = clientsDialog.getEditedClient();
                    ClientBL.edit(c);
                    clientsDialog.editClient(c);
                }
                catch(IllegalArgumentException illegalArgumentException){
                    JOptionPane.showMessageDialog(new JFrame(), illegalArgumentException.getMessage());
                }
            }
        };

        ActionListener deleteClient = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ClientBL.deleteById(clientsDialog.getSelectedId());
                    clientsDialog.deleteSelectedClient();
                } catch (SQLIntegrityConstraintViolationException e1) {
                    JOptionPane.showMessageDialog(new JFrame(), "Cannot delete client with active orders.");
                } catch (SQLException e2) {
                    JOptionPane.showMessageDialog(new JFrame(), "Database problem");
                }
            }
        };

        ActionListener addNewProduct = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Product product = productsDialog.getInsertedProduct();
                    int id = ProductBL.insert(product);
                    product.setId(id);
                    productsDialog.insertProduct(product);
                } catch (NoSuchElementException noSuchElementException) {
                    noSuchElementException.printStackTrace();
                } catch (IllegalArgumentException illegalArgumentException) {
                    JOptionPane.showMessageDialog(new JFrame(), illegalArgumentException.getMessage());
                }
            }
        };

        ActionListener editProduct = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Product c = productsDialog.getEditedProduct();
                    ProductBL.edit(c);
                    productsDialog.editProduct(c);
                } catch (IllegalArgumentException illegalArgumentException) {
                    JOptionPane.showMessageDialog(new JFrame(), illegalArgumentException.getMessage());
                }
            }
        };

        ActionListener deleteProduct = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ProductBL.deleteById(productsDialog.getSelectedId());
                    productsDialog.deleteSelectedProduct();
                } catch (SQLIntegrityConstraintViolationException e1) {
                    JOptionPane.showMessageDialog(new JFrame(), "Cannot delete product with active orders");
                } catch (SQLException e2) {
                    JOptionPane.showMessageDialog(new JFrame(), "Database problem");
                }
            }
        };

        ActionListener addNewOrder = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ProductOrder o = ordersDialog.orderSelected();
                    OrderBL.insert(o);
                    generateBill(o);
                    Product p = ProductBL.findById(o.getProductId());
                    p.setQuantity(p.getQuantity() - o.getQuantity());
                    ProductBL.edit(p);
                    ordersDialog.viewAll(ClientBL.findAll(), ProductBL.findAll());
                } catch (IllegalArgumentException illegalArgumentException) {
                    JOptionPane.showMessageDialog(new JFrame(), illegalArgumentException.getMessage());
                }
            }
        };

        clients.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clientsDialog = new ClientsDialog(addNewClient, editClient, deleteClient);
                clientsDialog.viewAll(ClientBL.findAll());
            }
        });
        products.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                productsDialog = new ProductsDialog(addNewProduct, editProduct, deleteProduct);
                productsDialog.viewAll(ProductBL.findAll());
            }
        });
        orders.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ordersDialog = new OrdersDialog(addNewOrder);
                ordersDialog.viewAll(ClientBL.findAll(), ProductBL.findAll());
            }
        });
        setVisible(true);
    }

    /**
     * Generates a .txt file for an Order
     * @param o the Order for which to generate the order
     */
    public static void generateBill(ProductOrder o){
        File file;
        FileWriter writer = null;
        int fileName = 0;
        file = new File("bill.txt");
        try {
            while (!file.createNewFile()){
                fileName++;
                file = new File("bill"+fileName+".txt");
            }
        }
        catch(Exception r){}

        try {
            writer = new FileWriter(file.getName());
            writer.write(o.getDate()+"\n"+ClientBL.findById(o.getClientId()).getName()+" orders "+o.getQuantity()+" "+ProductBL.findById(o.getProductId()).getName()+".");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            Desktop desktop = Desktop.getDesktop();
            desktop.open(file);
            writer.close();
        } catch (IOException e) {}
    }

}
