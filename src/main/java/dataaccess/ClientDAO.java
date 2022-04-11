package dataaccess;

import model.Client;

/**
 * Singleton class. Data access class for the Client type.
 */
public class ClientDAO extends AbstractDAO<Client>{
    private static ClientDAO dao = new ClientDAO();

    public static ClientDAO getDao() {
        return dao;
    }
}
