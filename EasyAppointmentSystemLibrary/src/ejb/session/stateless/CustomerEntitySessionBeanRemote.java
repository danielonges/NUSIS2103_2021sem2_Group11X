package ejb.session.stateless;

import entity.AppointmentEntity;
import entity.CustomerEntity;
import java.util.List;
import util.exception.AppointmentNotFoundException;
import util.exception.CustomerAlreadyExistsException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginException;
import util.exception.UnknownPersistenceException;

public interface CustomerEntitySessionBeanRemote {
    
    public Long createCustomerEntity(CustomerEntity newCustomerEntity) throws CustomerAlreadyExistsException, UnknownPersistenceException, InputDataValidationException;    
    
    public CustomerEntity retrieveCustomerEntityByCustomerId(Long customerId) throws CustomerNotFoundException;
    
    public void updateCustomerEntity(CustomerEntity customerEntity);
    
    public void deleteCustomerEntity(Long customerId) throws CustomerNotFoundException;
    
    public CustomerEntity retrieveCustomerAppointments (Long customerId) throws CustomerNotFoundException;
    
    public CustomerEntity customerLogin(String email, String password) throws InvalidLoginException;
    
    public CustomerEntity retrieveCustomerByEmail(String email) throws CustomerNotFoundException;

    CustomerEntity customerLoginHash(String email, String hashPassword) throws InvalidLoginException;
}
