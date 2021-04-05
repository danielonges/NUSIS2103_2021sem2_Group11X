package ejb.session.stateless;

import entity.AppointmentEntity;
import entity.ServiceProviderEntity;
import util.exception.InvalidLoginException;
import util.exception.ServiceProviderNotFoundException;
import java.util.List;

public interface ServiceProviderEntitySessionBeanRemote {

    public void createServiceProviderEntity(ServiceProviderEntity newServiceProviderEntity);

    public ServiceProviderEntity retrieveServiceProviderEntityByProviderId(Long providerId) throws ServiceProviderNotFoundException;

    public void updateServiceProviderEntity(ServiceProviderEntity serviceProviderEntity);

    public void deleteServiceProviderEntity(Long providerId) throws ServiceProviderNotFoundException;

    public ServiceProviderEntity retrieveServiceProviderByEmail(String email) throws ServiceProviderNotFoundException;

    public ServiceProviderEntity ServiceProviderLogin(String email, String password) throws InvalidLoginException;

    public List<ServiceProviderEntity> retrieveListOfServiceProviders() throws ServiceProviderNotFoundException;

    public List<ServiceProviderEntity> retrieveListOfServiceProvidersWithPendingApproval() throws ServiceProviderNotFoundException;

    public ServiceProviderEntity retrieveListOfAppointments(Long serviceProviderId) throws ServiceProviderNotFoundException;

    public List<ServiceProviderEntity> retrieveServiceProviderByCategoryAndCity(String category, String city) throws ServiceProviderNotFoundException;

    public void updateServiceProviderRating(Long providerId, Integer rating) throws ServiceProviderNotFoundException;
    
    public List<ServiceProviderEntity> retrieveListOfServiceProvidersNotBlocked() throws ServiceProviderNotFoundException;
    
    public ServiceProviderEntity retrieveListOfAppointmentsByProvider(ServiceProviderEntity currentServiceProviderEntity) throws ServiceProviderNotFoundException;
}
