package util.exception;

/**
 *
 * @author danielonges
 */
public class CustomerAlreadyExistsException extends Exception {

    public CustomerAlreadyExistsException() {
    }

    public CustomerAlreadyExistsException(String string) {
        super(string);
    }
    
}
