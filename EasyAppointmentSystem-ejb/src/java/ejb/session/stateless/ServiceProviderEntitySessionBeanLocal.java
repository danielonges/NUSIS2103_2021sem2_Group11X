package ejb.session.stateless;

import entity.ServiceProviderEntity;

public interface ServiceProviderEntitySessionBeanLocal {
    
    public void createServiceProviderEntity(ServiceProviderEntity newServiceProviderEntity);

    public ServiceProviderEntity retrieveServiceProviderEntityByProviderId(Long providerId);
    
    public void updateServiceProviderEntity(ServiceProviderEntity serviceProviderEntity);
    
    public void deleteServiceProviderEntity(Long providerId);
}
