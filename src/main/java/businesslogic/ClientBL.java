package businesslogic;

import businesslogic.validators.ClientValidator;
import dataaccess.ClientDAO;
import model.Client;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The business logic for a Client.
 * Can insert, edit, remove a Client.
 * Can also get a list of clients, or find a client by id.
 */
public class ClientBL{
    public static int insert(Client client) throws IllegalArgumentException {
        ClientValidator.validate(client);
        ClientDAO.getDao().insert(client);
        return ClientDAO.getDao().findLastId();
    }
    public static void edit(Client c) throws IllegalArgumentException {
        ClientValidator.validate(c);
        ClientDAO.getDao().update(c);
    }
    public static List<Client> findAll(){
        return ClientDAO.getDao().findAll();
    }
    public static Client findById(int id) {return ClientDAO.getDao().findById(id);}
    public static void deleteById(int id) throws SQLException {
        ClientDAO.getDao().deleteById(id);
    }
}
