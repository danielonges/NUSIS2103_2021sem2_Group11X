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

    public List<AppointmentEntity> retrieveListOfAppointments(ServiceProviderEntity serviceProviderEntity);
}
