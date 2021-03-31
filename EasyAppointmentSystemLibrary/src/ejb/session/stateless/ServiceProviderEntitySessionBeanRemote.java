package ejb.session.stateless;

import entity.ServiceProviderEntity;
import util.exception.InvalidLoginException;
import util.exception.ServiceProviderNotFoundException;
import java.util.List;

public interface ServiceProviderEntitySessionBeanRemote {
    
     public void createServiceProviderEntity(ServiceProviderEntity newServiceProviderEntity);

    public ServiceProviderEntity retrieveServiceProviderEntityByProviderId(Long providerId);
    
    public void updateServiceProviderEntity(ServiceProviderEntity serviceProviderEntity);
    
    public void deleteServiceProviderEntity(Long providerId);

    public ServiceProviderEntity retrieveServiceProviderByEmail(String email) throws ServiceProviderNotFoundException;

    public ServiceProviderEntity ServiceProviderLogin(String email, String password) throws InvalidLoginException;
    
    public List<ServiceProviderEntity> retrieveListOfServiceProviders() throws ServiceProviderNotFoundException;
}

