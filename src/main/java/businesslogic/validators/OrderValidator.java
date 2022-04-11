package businesslogic.validators;

import model.ProductOrder;

/**
 * Validation logic for the Order information.
 */
public class OrderValidator {
    public static void validate(ProductOrder productOrder) throws IllegalArgumentException {
        if(productOrder.getQuantity()<=0){
            throw new IllegalArgumentException("Invalid quantity.");
        }
    }
}
