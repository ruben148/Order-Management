package presentation;

import model.Client;
import model.ProductOrder;
import model.Product;
import presentation.tables.ClientTable;
import presentation.tables.ProductTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * A Dialog used to display clients and products.
 * Used to create an order with the selected client and product.
 */
public class OrdersDialog extends JDialog {
    ClientTable clientsTable = new ClientTable();
    ProductTable productsTable = new ProductTable();

    JTextField quantity = new JTextField("");
    JButton addOrderBtn = new JButton("Make an order");

    public OrdersDialog(ActionListener addOrder){
        super();

        productsTable.setSize(70,70);
        clientsTable.setSize(70,70);

        setSize(1000,500);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        quantity.setColumns(20);

        addOrderBtn.addActionListener(addOrder);

        add(clientsTable);
        add(productsTable);
        add(quantity);
        add(addOrderBtn);

        setVisible(true);
    }

    public void viewAll(List<Client> clients, List<Product> products) {
        clientsTable.viewAll(clients);
        productsTable.viewAll(products);
    }

    public ProductOrder orderSelected() throws IllegalArgumentException{
        if(Integer.parseInt(productsTable.getSelectedValueAtColumn(2)) < Integer.parseInt(quantity.getText())){
            JOptionPane.showMessageDialog(new JFrame(), "Quantity too big");
            throw new IllegalArgumentException("Quantity too big");
        }
        return new ProductOrder(0, clientsTable.getSelectedId(), productsTable.getSelectedId(),
                Integer.parseInt(quantity.getText()), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }

}
