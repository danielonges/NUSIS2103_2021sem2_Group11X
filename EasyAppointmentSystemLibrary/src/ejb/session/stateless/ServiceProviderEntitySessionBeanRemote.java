package ejb.session.stateless;

import entity.AppointmentEntity;
import entity.ServiceProviderEntity;
import util.exception.InvalidLoginException;
import util.exception.ServiceProviderNotFoundException;
import java.util.List;
import util.exception.BusinessCategoryNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.ServiceProviderAlreadyExistsException;
import util.exception.UnknownPersistenceException;

public interface ServiceProviderEntitySessionBeanRemote {

    public void createServiceProviderEntity(ServiceProviderEntity newServiceProviderEntity)  throws ServiceProviderAlreadyExistsException, UnknownPersistenceException, InputDataValidationException ;

    public ServiceProviderEntity retrieveServiceProviderEntityByProviderId(Long providerId) throws ServiceProviderNotFoundException;

    public void updateServiceProviderEntity(ServiceProviderEntity serviceProviderEntity);

    public void deleteServiceProviderEntity(Long providerId) throws ServiceProviderNotFoundException;

    public ServiceProviderEntity retrieveServiceProviderByEmail(String email) throws ServiceProviderNotFoundException;

    public ServiceProviderEntity ServiceProviderLogin(String email, String password) throws InvalidLoginException;

    public List<ServiceProviderEntity> retrieveListOfServiceProviders() throws ServiceProviderNotFoundException;

    public List<ServiceProviderEntity> retrieveListOfServiceProvidersWithPendingApproval() throws ServiceProviderNotFoundException;

    public ServiceProviderEntity retrieveListOfAppointments(Long serviceProviderId) throws ServiceProviderNotFoundException;

    public List<ServiceProviderEntity> retrieveServiceProviderByCategoryIdAndCity(Long categoryId, String city) throws ServiceProviderNotFoundException, BusinessCategoryNotFoundException;

    public void updateServiceProviderRating(Long providerId, Integer rating) throws ServiceProviderNotFoundException;
    
    public List<ServiceProviderEntity> retrieveListOfServiceProvidersNotBlocked() throws ServiceProviderNotFoundException;
    
    public List<AppointmentEntity> retrieveListOfPendingAppointments(Long serviceProviderId) throws ServiceProviderNotFoundException;
    
    
}
