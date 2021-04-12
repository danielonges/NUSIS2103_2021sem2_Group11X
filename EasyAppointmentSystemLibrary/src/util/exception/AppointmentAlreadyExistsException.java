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
public class AppointmentAlreadyExistsException extends Exception{

    public AppointmentAlreadyExistsException() {
    }

    public AppointmentAlreadyExistsException(String string) {
        super(string);
    }
    
    
}
