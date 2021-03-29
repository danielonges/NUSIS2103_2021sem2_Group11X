package ejb.session.stateless;

import entity.ServiceProviderEntity;
import exception.InvalidLoginException;
import exception.ServiceProviderNotFoundException;
import java.util.List;

public interface ServiceProviderEntitySessionBeanLocal {
    
    
    public void createServiceProviderEntity(ServiceProviderEntity newServiceProviderEntity);

    public ServiceProviderEntity retrieveServiceProviderEntityByProviderId(Long providerId);
    
    public void updateServiceProviderEntity(ServiceProviderEntity serviceProviderEntity);
    
    public void deleteServiceProviderEntity(Long providerId);

    public ServiceProviderEntity retrieveServiceProviderByEmail(String email) throws ServiceProviderNotFoundException;

    public ServiceProviderEntity ServiceProviderLogin(String email, String password) throws InvalidLoginException;

    public List<ServiceProviderEntity> retrieveListOfServiceProviders() throws ServiceProviderNotFoundException;
}
