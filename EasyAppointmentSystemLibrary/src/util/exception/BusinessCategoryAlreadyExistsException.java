/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author leele
 */
public class BusinessCategoryAlreadyExistsException extends Exception{

    public BusinessCategoryAlreadyExistsException() {
    }

    public BusinessCategoryAlreadyExistsException(String string) {
        super(string);
    }
    
    
}
