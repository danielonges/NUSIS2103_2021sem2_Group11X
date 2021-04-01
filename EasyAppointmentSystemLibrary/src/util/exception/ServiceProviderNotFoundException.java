
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author meleenoob
 */
public class ServiceProviderNotFoundException extends Exception {

    public ServiceProviderNotFoundException() {
    }

    public ServiceProviderNotFoundException(String msg) {
        super(msg);
    }

}
