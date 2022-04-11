package dataaccess;

import model.ProductOrder;

/**
 * Singleton class. Data access class for the Order type.
 */
public class OrderDAO extends AbstractDAO<ProductOrder>{
    private static OrderDAO dao = new OrderDAO();

    public static OrderDAO getDao() {
        return dao;
    }
}
