package businesslogic;

import businesslogic.validators.OrderValidator;
import dataaccess.OrderDAO;
import model.ProductOrder;

import java.util.List;

/**
 * The business logic for orders.
 * Can add an order into the database.
 */
public class OrderBL {
    /**
     *
     * @param productOrder
     */
    public static void insert(ProductOrder productOrder) throws IllegalArgumentException {
        OrderValidator.validate(productOrder);
        OrderDAO.getDao().insert(productOrder);
    }
    public static ProductOrder findById(int id){
        return OrderDAO.getDao().findById(id);
    }
    public static List<ProductOrder> findAll(){
        return OrderDAO.getDao().findAll();
    }
}
