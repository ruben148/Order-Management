package businesslogic;

import businesslogic.validators.ProductValidator;
import dataaccess.ClientDAO;
import dataaccess.ProductDAO;
import model.Product;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

/**
 * The business logic for the product.
 * Can add, edit, remove products.
 * Can also find all products, or find a product by id.
 */
public class ProductBL {
    public static int insert(Product product) throws IllegalArgumentException {
        ProductValidator.validate(product);
        ProductDAO.getDao().insert(product);
        return ProductDAO.getDao().findLastId();
    }
    public static void edit(Product p) throws IllegalArgumentException {
        ProductDAO.getDao().update(p);
    }
    public static List<Product> findAll(){
        return ProductDAO.getDao().findAll();
    }
    public static Product findById(int id) { return ProductDAO.getDao().findById(id); }
    public static void deleteById(int id) throws SQLException {
        ProductDAO.getDao().deleteById(id);
    }
}
