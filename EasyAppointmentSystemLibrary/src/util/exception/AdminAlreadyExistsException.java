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
public class AdminAlreadyExistsException extends Exception{

    public AdminAlreadyExistsException() {
    }

    public AdminAlreadyExistsException(String string) {
        super(string);
    }
    
    
}
