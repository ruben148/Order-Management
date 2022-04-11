package dataaccess;

import dataaccess.connection.DBConnection;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A class used to send commands to a MySQL database
 * Can create sql statements, run them, then retrieve the result in the necessary form based on the type of the generic parameter T.
 * @param <T>
 */
public class AbstractDAO<T> {
    private final Class<T> type;

    @SuppressWarnings("unchecked")
    public AbstractDAO() {
        this.type = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    private String createSelectQuery(String field) {
        return "SELECT " +
                " * " +
                " FROM " +
                type.getSimpleName() +
                " WHERE " + field + " =?";
    }

    private String createSelectQuery() {
        return "SELECT " +
                " * " +
                " FROM " +
                type.getSimpleName();
    }

    private String createInsertStmt(T instance) {
        String insertStatementString = "INSERT INTO "
                + type.getSimpleName()
                + " (";
        for(Field field: type.getDeclaredFields())
            if(!field.getName().equals("id"))
                insertStatementString += field.getName() + ",";
        insertStatementString = insertStatementString.substring(0,insertStatementString.length()-1);
        insertStatementString += ")";
        insertStatementString += " VALUES (";
        for(Field field: type.getDeclaredFields()){
            String fieldName = field.getName();
            if(fieldName.equals("id"))
                continue;
            PropertyDescriptor propertyDescriptor = null;
            try {
                propertyDescriptor = new PropertyDescriptor(fieldName, type);
            } catch (IntrospectionException e) {
                e.printStackTrace();
            }
            Method method = propertyDescriptor.getReadMethod();
            try {
                insertStatementString += "\"" + method.invoke(instance) + "\",";
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        insertStatementString = insertStatementString.substring(0,insertStatementString.length()-1);
        insertStatementString += ")";
        return insertStatementString;
    }

    private String createUpdateStmt(T instance) {
        String updateStatementString = "UPDATE "
                + type.getSimpleName()
                + " set ";
        int id = 0;
        for (Field field : type.getDeclaredFields()) {
            String fieldName = field.getName();
            PropertyDescriptor propertyDescriptor = null;
            try {
                propertyDescriptor = new PropertyDescriptor(fieldName, type);
            } catch (IntrospectionException e) {
                e.printStackTrace();
            }
            Method method = propertyDescriptor.getReadMethod();
            try {
                if(!fieldName.equals("id")) {
                    updateStatementString += fieldName + " = ";
                    updateStatementString += "\"" + method.invoke(instance) + "\",";
                }
                else
                    id = (int) method.invoke(instance);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        updateStatementString = updateStatementString.substring(0,updateStatementString.length()-1);
        updateStatementString += " where id = " + id;
        return updateStatementString;
    }

    private String createDeleteStmt(int id) {
        String deleteStatementString = "Delete from "+
                type.getSimpleName()
                +" where id="+id;
        return deleteStatementString;
    }

    /**
     * Retreives a list of objects from the database.
     * @return the list of objects in the form of an ArrayList
     */
    public List<T> findAll() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery();
        try {
            connection = DBConnection.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            return createObjects(resultSet);
        } catch (SQLException e) {

        } finally {
            DBConnection.close(resultSet);
            DBConnection.close(statement);
            DBConnection.close(connection);
        }
        return null;
    }

    /**
     * retreives from the database the id of the last inserted object
     * @return
     */
    public int findLastId() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = "Select MAX(id) FROM "
                + type.getSimpleName();
        try {
            connection = DBConnection.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt("max(id)");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(resultSet);
            DBConnection.close(statement);
            DBConnection.close(connection);
        }
        return -1;
    }

    /**
     * retrieves from the database the object with the specified id.
     * @param id
     * @return
     */
    public T findById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("id");
        try {
            connection = DBConnection.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            return createObjects(resultSet).get(0);
        } catch (SQLException e) {

        } finally {
            DBConnection.close(resultSet);
            DBConnection.close(statement);
            DBConnection.close(connection);
        }
        return null;
    }

    private List<T> createObjects(ResultSet resultSet) {
        List<T> list = new ArrayList<T>();
        Constructor[] ctors = type.getDeclaredConstructors();
        Constructor ctor = null;
        for (int i = 0; i < ctors.length; i++) {
            ctor = ctors[i];
            if (ctor.getGenericParameterTypes().length == 0)
                break;
        }
        try {
            while (resultSet.next()) {
                assert ctor != null;
                ctor.setAccessible(true);
                T instance = (T)ctor.newInstance();
                for (Field field : type.getDeclaredFields()) {
                    String fieldName = field.getName();
                    Object value = resultSet.getObject(fieldName);
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(fieldName, type);
                    Method method = propertyDescriptor.getWriteMethod();
                    method.invoke(instance, value);
                }
                list.add(instance);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * @param t inserts the object into the databse
     * @return
     */
    public void insert(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        String ins = createInsertStmt(t);
        try {
            connection = DBConnection.getConnection();
            statement = connection.prepareStatement(ins);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(statement);
            DBConnection.close(connection);
        }
    }

    /**
     * @param id deletes the object with this id from the database
     * @throws SQLException
     */
    public void deleteById(int id) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        String ins = createDeleteStmt(id);
        connection = DBConnection.getConnection();
        statement = connection.prepareStatement(ins);
        statement.execute();
        DBConnection.close(statement);
        DBConnection.close(connection);
    }

    /**
     * modifies an entry in the database
     * @param t
     */
    public void update(T t) {
        Connection connection = null;
        PreparedStatement statement = null;
        String ins = createUpdateStmt(t);
        try {
            connection = DBConnection.getConnection();
            statement = connection.prepareStatement(ins);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.close(statement);
            DBConnection.close(connection);
        }
    }
}