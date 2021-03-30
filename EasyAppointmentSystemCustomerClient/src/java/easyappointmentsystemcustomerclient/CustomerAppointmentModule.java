package easyappointmentsystemcustomerclient;

import ejb.session.stateless.AppointmentEntitySessionBeanRemote;
import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import ejb.session.stateless.ServiceProviderEntitySessionBeanRemote;
import entity.CustomerEntity;

/**
 *
 * @author danielonges
 */
public class CustomerAppointmentModule {
    private AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote;
    private CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote;
    private ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote;
    
    private CustomerEntity currentCustomerEntity;

    public CustomerAppointmentModule() {
    }

    public CustomerAppointmentModule(AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote, CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote, ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote, CustomerEntity currentCustomerEntity) {
        this.appointmentEntitySessionBeanRemote = appointmentEntitySessionBeanRemote;
        this.customerEntitySessionBeanRemote = customerEntitySessionBeanRemote;
        this.serviceProviderEntitySessionBeanRemote = serviceProviderEntitySessionBeanRemote;
        this.currentCustomerEntity = currentCustomerEntity;
    }
    
    public void doSearchOperation() {
        
    }

    public void doAddAppointment() {
        
    }
    
    public void doViewAppointments() {
        
    }
    
    public void doCancelAppointment() {
        
    }
    
    public void doRateProvider() {
        
    }
    
}
