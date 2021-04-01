package ejb.session.stateless;

import entity.AppointmentEntity;
import entity.CustomerEntity;
import java.util.List;
import util.exception.AppointmentNotFoundException;
import util.exception.CustomerNotFoundException;

public interface CustomerEntitySessionBeanLocal {
    
    public void createCustomerEntity(CustomerEntity newCustomerEntity);
    
    public CustomerEntity retrieveCustomerEntityByCustomerId(Long customerId) throws CustomerNotFoundException;
    
    public void updateCustomerEntity(CustomerEntity customerEntity);
    
    public void deleteCustomerEntity(Long customerId) throws CustomerNotFoundException;

    public List<AppointmentEntity> retrieveCustomerAppointments(Long customerId) throws CustomerNotFoundException;

}
