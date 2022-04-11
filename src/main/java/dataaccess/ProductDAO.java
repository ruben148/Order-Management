package dataaccess;

import model.Product;

/**
 * Singleton class. Data access class for the Product type.
 */
public class ProductDAO extends AbstractDAO<Product> {
    private static ProductDAO dao = new ProductDAO();

    public static ProductDAO getDao() {
        return dao;
    }
}
