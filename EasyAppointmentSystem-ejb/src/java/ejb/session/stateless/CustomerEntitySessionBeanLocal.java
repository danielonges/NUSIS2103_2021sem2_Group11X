package ejb.session.stateless;

import entity.CustomerEntity;

public interface CustomerEntitySessionBeanLocal {
    
    public void createCustomerEntity(CustomerEntity newCustomerEntity);
    
    public CustomerEntity retrieveCustomerEntityByCustomerId(Long customerId);
    
    public void updateCustomerEntity(CustomerEntity customerEntity);
    
    public void deleteCustomerEntity(Long customerId);
}
