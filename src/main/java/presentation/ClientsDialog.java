package presentation;

import businesslogic.ClientBL;
import businesslogic.ProductBL;
import model.Client;
import presentation.tables.ClientTable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * A Dialog which can display any number of clients
 * Can add, delete or edit the displayed clients
 */
public class ClientsDialog extends JDialog {

    ClientTable clientTable = new ClientTable();

    JButton addClientBtn = new JButton("Add client");
    JButton doneAddingClientBtn = new JButton("Done adding client");
    JButton doneEditingClientBtn = new JButton("Done editing client");
    JButton editClientBtn = new JButton("Edit client");
    JButton deleteClientBtn = new JButton("Delete client");

    JTextField name = new JTextField("");
    JTextField address = new JTextField("");
    JTextField email = new JTextField("");

    JLabel nameLbl = new JLabel("Name");
    JLabel addressLbl = new JLabel("Address");
    JLabel emailLbl = new JLabel("Email");

    public ClientsDialog(ActionListener addNewClient, ActionListener editClient,
                         ActionListener deleteClient){
        super();

        deleteClientBtn.addActionListener(deleteClient);
        doneEditingClientBtn.addActionListener(editClient);
        doneEditingClientBtn.setEnabled(false);
        doneAddingClientBtn.addActionListener(addNewClient);
        doneAddingClientBtn.setEnabled(false);

        addClientBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addClientBtn.setEnabled(false);
                doneAddingClientBtn.setEnabled(true);
                name.setEditable(true);
                address.setEditable(true);
                email.setEditable(true);
                name.setText("");
                address.setText("");
                email.setText("");
            }
        });

        editClientBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editClientBtn.setEnabled(false);
                doneEditingClientBtn.setEnabled(true);
                name.setEditable(true);
                address.setEditable(true);
                email.setEditable(true);
            }
        });

        clientTable.addSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                try {
                    name.setText(clientTable.getSelectedValueAtColumn(1));
                    address.setText(clientTable.getSelectedValueAtColumn(2));
                    email.setText(clientTable.getSelectedValueAtColumn(3));
                }
                catch(ArrayIndexOutOfBoundsException exception){
                    name.setText("");
                    address.setText("");
                    email.setText("");
                }
            }
        });

        name.setEditable(false);
        address.setEditable(false);
        email.setEditable(false);

        setSize(500,630);
        setLocationRelativeTo(null);
        setLayout(new FlowLayout());

        JPanel namePanel = new JPanel(new FlowLayout());
        JPanel addressPanel = new JPanel(new FlowLayout());
        JPanel emailPanel = new JPanel(new FlowLayout());

        JPanel edit = new JPanel(new GridLayout(3,1));


        name.setColumns(20);
        address.setColumns(20);
        email.setColumns(20);

        namePanel.add(nameLbl);
        namePanel.add(name);
        addressPanel.add(addressLbl);
        addressPanel.add(address);
        emailPanel.add(emailLbl);
        emailPanel.add(email);

        edit.add(namePanel);
        edit.add(addressPanel);
        edit.add((emailPanel));

        add(edit);
        add(clientTable);
        add(addClientBtn);
        add(doneAddingClientBtn);
        add(doneEditingClientBtn);
        add(editClientBtn);
        add(deleteClientBtn);

        setVisible(true);
    }

    public Client getInsertedClient(){
        doneAddingClientBtn.setEnabled(false);
        addClientBtn.setEnabled(true);
        name.setEditable(false);
        address.setEditable(false);
        email.setEditable(false);
        Client c = new Client(0, name.getText(), address.getText(), email.getText());
        return c;
    }

    /**
     * @return the Client just edited
     */
    public Client getEditedClient(){
        doneEditingClientBtn.setEnabled(false);
        editClientBtn.setEnabled(true);
        name.setEditable(false);
        address.setEditable(false);
        email.setEditable(false);
        Client c = new Client(clientTable.getSelectedId(), name.getText(), address.getText(), email.getText());
        return c;
    }

    /**
     * inserts the Client into the table
     * @param client
     */
    public void insertClient(Client client){
        clientTable.insert(client);
    }

    /**
     * edits the client in the tables. The id's must match.
     * @param client
     */
    public void editClient(Client client){
        clientTable.edit(client);
    }

    /**
     * @return the id from the selected row in the table
     */
    public int getSelectedId(){
        return clientTable.getSelectedId();
    }

    /**
     * deletes the selected row in the table
     */
    public void deleteSelectedClient(){
        clientTable.deleteSelected();
    }

    /**
     * shows the list of clients in a table.
     * @param clients
     */
    public void viewAll(List<Client> clients) {
        clientTable.viewAll(clients);
    }



}
