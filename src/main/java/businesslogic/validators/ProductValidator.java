package businesslogic.validators;

import model.Product;

/**
 * Validation logic for the Product information.
 */
public class ProductValidator{

    public static void validate(Product product) throws IllegalArgumentException {
        if(product.getQuantity()<0){
            throw new IllegalArgumentException("Invalid quantity.");
        }
    }
}
