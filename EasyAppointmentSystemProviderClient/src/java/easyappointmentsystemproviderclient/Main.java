/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package easyappointmentsystemproviderclient;

import ejb.session.stateless.AdminEntitySessionBeanRemote;
import ejb.session.stateless.AppointmentEntitySessionBeanRemote;
import ejb.session.stateless.BusinessCategorySessionBeanRemote;
import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import ejb.session.stateless.ServiceProviderEntitySessionBeanRemote;
import util.exception.AppointmentNotFoundException;
import util.exception.InvalidLoginException;
import javax.ejb.EJB;

/**
 *
 * @author danielonges
 */
public class Main {

    @EJB(name = "BusinessCategorySessionBeanRemote")
    private static BusinessCategorySessionBeanRemote businessCategorySessionBeanRemote;
    

    @EJB(name = "CustomerEntitySessionBeanRemote")
    private static CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote;

    @EJB(name = "AppointmentEntitySessionBeanRemote")
    private static AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote;

    @EJB(name = "AdminEntitySessionBeanRemote")
    private static AdminEntitySessionBeanRemote adminEntitySessionBeanRemote;

    @javax.ejb.EJB(name = "ServiceProviderEntitySessionBeanRemote")
    private static ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InvalidLoginException, AppointmentNotFoundException {
        MainApp mainApp = new MainApp(appointmentEntitySessionBeanRemote, customerEntitySessionBeanRemote,
                adminEntitySessionBeanRemote, serviceProviderEntitySessionBeanRemote,businessCategorySessionBeanRemote);
        mainApp.runApp();

    }

}
