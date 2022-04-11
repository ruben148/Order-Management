package presentation.tables;

import businesslogic.ClientBL;
import model.Client;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A generic table.
 * Generates the specific table header for the type of generic parameter T.
 * Has methods to add, edit or delete rows in the table.
 * @param <T>
 */
public class GenericTable<T> extends JScrollPane {
    int lastId;
    int selectedRow = -1;
    JTable table = new JTable();
    DefaultTableModel model;
    Class<T> type;

    //TODO id-ul este auto-increment
    //TODO se va tine minte ultimul id

    public GenericTable() {
        ArrayList<String> list = new ArrayList<>();
        type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        for (Field field : type.getDeclaredFields())
            list.add(field.getName());
        model = new DefaultTableModel(
                null,
                list.toArray(new String[0])
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(model);
        setViewportView(table);
    }

    /**
     * inserts the object t into the table
     * @param t
     */
    public void insert(T t){
        ArrayList<String> strings = new ArrayList<>();
        for(Field field : type.getDeclaredFields()) {
            String fieldName = field.getName();
            PropertyDescriptor propertyDescriptor = null;
            try {
                propertyDescriptor = new PropertyDescriptor(fieldName, type);
            } catch (IntrospectionException e) {
                e.printStackTrace();
            }
            Method method = propertyDescriptor.getReadMethod();
            try {
                strings.add(method.invoke(t).toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        model.addRow(strings.toArray());
        lastId++;
    }

    /**
     * puts in the table the objects in the list
     * @param ts
     */
    public void viewAll(List<T> ts){
        model.setRowCount(0);
        lastId=-1;
        for(T t : ts){
            insert(t);
        }
    }

    /**
     * @return the id (the first column) of the selected object in the table.
     */
    public int getSelectedId(){
        return Integer.parseInt(table.getModel().getValueAt(table.getSelectedRow(),0).toString());
    }

    /**
     * returns the value of the selected object at column k
     * @param k
     * @return
     */
    public String getSelectedValueAtColumn(int k){
        return table.getModel().getValueAt(table.getSelectedRow(),k).toString();
    }

    /**
     * edits the object in the table
     * @param t the id of t must be the same with the one in the table.
     */
    public void edit(T t) {
        int id = 0;
        try {
            PropertyDescriptor propertyDescriptor = new PropertyDescriptor("id", type);
            Method method = propertyDescriptor.getReadMethod();
            id = (int)method.invoke(t);
        }
        catch(Exception e){}
        for(int i=0; i<table.getRowCount();i++){
            if(Integer.parseInt(table.getModel().getValueAt(i,0).toString()) == id){
                int j = 0;
                for(Field field : type.getDeclaredFields()){
                    String fieldName = field.getName();
                    if(fieldName.equals("id"))
                        continue;
                    j++;
                    try {
                        PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                        Method method = propertyDescriptor.getReadMethod();
                        table.getModel().setValueAt(method.invoke(t),i,j);
                    }
                    catch(Exception e){}
                }
                break;
            }
        }

    }

    /**
     * deletes the selected row in the table.
     */
    public void deleteSelected() {
        model.removeRow(table.getSelectedRow());
    }

    /**
     * adds a selection listener to the table.
     * @param e
     */
    public void addSelectionListener(ListSelectionListener e){
        table.getSelectionModel().addListSelectionListener(e);
    }

}
