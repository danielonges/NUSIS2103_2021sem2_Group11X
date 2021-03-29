/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package easyappointmentsystemclient;

import ejb.session.stateless.AdminEntitySessionBeanRemote;
import ejb.session.stateless.AppointmentEntitySessionBeanRemote;
import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import ejb.session.stateless.ServiceProviderEntitySessionBeanRemote;
import entity.ServiceProviderEntity;
import exception.ServiceProviderNotFoundException;
import java.util.List;

/**
 *
 * @author leele
 */
public class ServiceProviderModule {
    private AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote;
    private AdminEntitySessionBeanRemote adminEntitySessionBeanRemote;
    private CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote;
    private ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote;

    ServiceProviderModule(AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote, AdminEntitySessionBeanRemote adminEntitySessionBeanRemote, CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote, ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote) {
        this.appointmentEntitySessionBeanRemote = appointmentEntitySessionBeanRemote;
        this.adminEntitySessionBeanRemote = adminEntitySessionBeanRemote;
        this.customerEntitySessionBeanRemote = customerEntitySessionBeanRemote;
        this.serviceProviderEntitySessionBeanRemote = serviceProviderEntitySessionBeanRemote;
    }

    public void viewAppointments() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void viewListOfProviders(){
        try{
       List<ServiceProviderEntity> serviceProviders = serviceProviderEntitySessionBeanRemote.retrieveListOfServiceProviders();
       for(ServiceProviderEntity value: serviceProviders ) {
           System.out.println(value.toString());
       }
        }catch (ServiceProviderNotFoundException ex){
               System.out.println("Service Providers does not exist!");
               }
    }

    public void approveProvider() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void blockProvider() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
