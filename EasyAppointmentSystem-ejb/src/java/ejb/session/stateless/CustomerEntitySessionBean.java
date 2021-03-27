package ejb.session.stateless;

import entity.CustomerEntity;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
    public CustomerEntity retrieveCustomerEntityByCustomerId(Long customerId) {
        CustomerEntity customerEntity = em.find(CustomerEntity.class, customerId);
        
        // TODO: implement checking for null
        return customerEntity;
    }
    
    @Override
    public void updateCustomerEntity(CustomerEntity customerEntity) {
        // TODO: implement checking for null
        em.merge(customerEntity);
    }
    
    @Override
    public void deleteCustomerEntity(Long customerId) {
        CustomerEntity customerEntity = retrieveCustomerEntityByCustomerId(customerId);
        em.remove(customerEntity);
    }
}
