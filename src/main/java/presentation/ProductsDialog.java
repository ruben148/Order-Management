package presentation;

import businesslogic.ProductBL;
import model.Client;
import model.Product;
import presentation.tables.ClientTable;
import presentation.tables.ProductTable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * A Dialog which can display any number of products
 * Can add, delete or edit the displayed products
 */
public class ProductsDialog extends JDialog {

    ProductTable productTable = new ProductTable();

    JButton addBtn = new JButton("Add product");
    JButton doneAddingBtn = new JButton("Done adding product");
    JButton doneEditingBtn = new JButton("Done editing product");
    JButton editBtn = new JButton("Edit product");
    JButton deleteBtn = new JButton("Delete product");

    JTextField name = new JTextField("");
    JTextField quantity = new JTextField("");

    JLabel nameLbl = new JLabel("Name");
    JLabel quantityLbl = new JLabel("Quantity");

    public ProductsDialog(ActionListener addNewClient, ActionListener editProduct,
                          ActionListener deleteProduct){
        super();

        deleteBtn.addActionListener(deleteProduct);
        doneEditingBtn.addActionListener(editProduct);
        doneEditingBtn.setEnabled(false);
        doneAddingBtn.addActionListener(addNewClient);
        doneAddingBtn.setEnabled(false);
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBtn.setEnabled(false);
                doneAddingBtn.setEnabled(true);
                name.setEditable(true);
                quantity.setEditable(true);
                name.setText("");
                quantity.setText("");
            }
        });
        editBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editBtn.setEnabled(false);
                doneEditingBtn.setEnabled(true);
                name.setEditable(true);
                quantity.setEditable(true);
            }
        });
        productTable.addSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                try {
                    name.setText(productTable.getSelectedValueAtColumn(1));
                    quantity.setText(productTable.getSelectedValueAtColumn(2));
                }
                catch(ArrayIndexOutOfBoundsException exception) {
                    name.setText("");
                    quantity.setText("");
                }
            }
        });

        name.setEditable(false);
        quantity.setEditable(false);

        setSize(550,630);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        JPanel namePanel = new JPanel(new FlowLayout());
        JPanel quantityPanel = new JPanel(new FlowLayout());


        JPanel edit = new JPanel(new GridLayout(3,1));

        name.setColumns(20);
        quantity.setColumns(20);

        namePanel.add(nameLbl);
        namePanel.add(name);
        quantityPanel.add(quantityLbl);
        quantityPanel.add(quantity);

        edit.add(namePanel);
        edit.add(quantityPanel);

        add(edit);
        add(productTable);
        add(addBtn);
        add(doneAddingBtn);
        add(doneEditingBtn);
        add(editBtn);
        add(deleteBtn);

        setVisible(true);
    }

    public Product getInsertedProduct(){
        doneAddingBtn.setEnabled(false);
        addBtn.setEnabled(true);
        name.setEditable(false);
        quantity.setEditable(false);
        Product c = new Product(0, name.getText(), Integer.parseInt(quantity.getText()));
        return c;
    }

    public Product getEditedProduct(){
        doneEditingBtn.setEnabled(false);
        editBtn.setEnabled(true);
        name.setEditable(false);
        quantity.setEditable(false);
        Product c = new Product(productTable.getSelectedId(), name.getText(), Integer.parseInt(quantity.getText()));
        return c;
    }

    public void insertProduct(Product c){
        productTable.insert(c);
    }

    public void editProduct(Product c){
        productTable.edit(c);
    }

    public int getSelectedId(){
        return productTable.getSelectedId();
    }

    public void deleteSelectedProduct(){
        productTable.deleteSelected();
    }

    public void viewAll(List<Product> products) {
        productTable.viewAll(products);
    }



}
