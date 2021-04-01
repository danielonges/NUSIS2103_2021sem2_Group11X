package ejb.session.stateless;

import entity.AppointmentEntity;
import entity.CustomerEntity;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.AppointmentNotFoundException;
import util.exception.CustomerNotFoundException;

/**
 *
 * @author danielonges
 */
@Local(CustomerEntitySessionBeanLocal.class)
@Remote(CustomerEntitySessionBeanRemote.class)
@Stateless
public class CustomerEntitySessionBean implements CustomerEntitySessionBeanRemote, CustomerEntitySessionBeanLocal {

    @PersistenceContext(unitName = "EasyAppointmentSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public void createCustomerEntity(CustomerEntity newCustomerEntity) {
        em.persist(newCustomerEntity);
        em.flush();
        
    }
    
    @Override
    public CustomerEntity retrieveCustomerEntityByCustomerId(Long customerId) throws CustomerNotFoundException{
        try {
        CustomerEntity customerEntity = em.find(CustomerEntity.class, customerId);
            return customerEntity;
        } catch (NoResultException ex) {
            throw new CustomerNotFoundException("Customer not found!");
        }
        // TODO: implement checking for null
        
    }
    
    @Override
    public void updateCustomerEntity(CustomerEntity customerEntity) {
        // TODO: implement checking for null
        em.merge(customerEntity);
    }
    
    @Override
    public void deleteCustomerEntity(Long customerId) throws CustomerNotFoundException{
        try {
        CustomerEntity customerEntity = retrieveCustomerEntityByCustomerId(customerId);
        em.remove(customerEntity);
        } catch (CustomerNotFoundException ex) {
            throw new CustomerNotFoundException("Customer not found!");
        }
    }
    
    public List<AppointmentEntity> retrieveCustomerAppointments (Long customerId) throws CustomerNotFoundException {
        try {
            CustomerEntity currentCustomerEntity = retrieveCustomerEntityByCustomerId(customerId);
            return currentCustomerEntity.getAppointments();
        } catch (CustomerNotFoundException ex) {
            throw new CustomerNotFoundException("Customer not found!");
        }
    }    
  
    
}
