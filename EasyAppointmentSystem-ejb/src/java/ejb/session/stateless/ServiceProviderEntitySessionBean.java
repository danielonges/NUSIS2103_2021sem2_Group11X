package ejb.session.stateless;

import entity.AppointmentEntity;
import entity.ServiceProviderEntity;
import util.exception.InvalidLoginException;
import util.exception.ServiceProviderNotFoundException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.enumeration.ServiceProviderStatus;
import static util.enumeration.ServiceProviderStatus.APPROVE;

/**
 *
 * @author danielonges
 */
@Local(ServiceProviderEntitySessionBeanLocal.class)
@Remote(ServiceProviderEntitySessionBeanRemote.class)
@Stateless
public class ServiceProviderEntitySessionBean implements ServiceProviderEntitySessionBeanRemote, ServiceProviderEntitySessionBeanLocal {

    @PersistenceContext(unitName = "EasyAppointmentSystem-ejbPU")
    private EntityManager em;

    @Override
    public void createServiceProviderEntity(ServiceProviderEntity newServiceProviderEntity) {
        em.persist(newServiceProviderEntity);
        em.flush();
    }

    @Override
    public ServiceProviderEntity retrieveServiceProviderEntityByProviderId(Long providerId) throws ServiceProviderNotFoundException {
        try {
            ServiceProviderEntity serviceProviderEntity = em.find(ServiceProviderEntity.class, providerId);
            return serviceProviderEntity;
        } catch (NoResultException ex) {
            throw new ServiceProviderNotFoundException("Service provider not found!");
        }
        // TODO: implement checking for null

    }

    @Override
    public void updateServiceProviderEntity(ServiceProviderEntity serviceProviderEntity) {
        // TODO: implement checking for null
        em.merge(serviceProviderEntity);
    }

    @Override
    public void deleteServiceProviderEntity(Long providerId) throws ServiceProviderNotFoundException {
        try {
            ServiceProviderEntity serviceProviderEntity = retrieveServiceProviderEntityByProviderId(providerId);
            em.remove(serviceProviderEntity);
        } catch (ServiceProviderNotFoundException ex) {
            throw new ServiceProviderNotFoundException("Service provider not found!");
        }
    }

    @Override
    public ServiceProviderEntity ServiceProviderLogin(String email, String password) throws InvalidLoginException {
        try {
            ServiceProviderEntity serviceProviderEntity = retrieveServiceProviderByEmail(email);
            if (serviceProviderEntity.getPassword().equals(password) && serviceProviderEntity.getStatus() == APPROVE) {
                return serviceProviderEntity;
            } else {
                throw new InvalidLoginException("Email does not exist or invalid password!");
            }
        } catch (ServiceProviderNotFoundException ex) {
            throw new InvalidLoginException("Email does not exist or invalid password!");
        }
    }

    @Override
    public ServiceProviderEntity retrieveServiceProviderByEmail(String email) throws ServiceProviderNotFoundException {
        Query query = em.createQuery("SELECT s FROM ServiceProviderEntity s WHERE s.email = :inEmail");
        query.setParameter("inEmail", email);

        try {
            return (ServiceProviderEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new ServiceProviderNotFoundException("Service Provider Email " + email + " does not exist!");
        }
    }

    @Override
    public List<ServiceProviderEntity> retrieveListOfServiceProviders() throws ServiceProviderNotFoundException {

        Query query = em.createQuery("SELECT s FROM ServiceProviderEntity s ORDER BY s.providerId", ServiceProviderEntity.class);

        try {
            return (List<ServiceProviderEntity>) query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new ServiceProviderNotFoundException("Service Providers does not exist!");
        }

    }
    
    @Override
    public List<ServiceProviderEntity> retrieveServiceProviderByCategoryAndCity(String category, String city) throws ServiceProviderNotFoundException {
        Query query = em.createQuery("SELECT s FROM ServiceProviderEntity s WHERE s.businessCategory = :businessCategory AND s.city = :city AND s.status = :status");
        query.setParameter("businessCategory", category);
        query.setParameter("city", city);
        query.setParameter("status", ServiceProviderStatus.APPROVE);

        try {
            return (List<ServiceProviderEntity>) query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new ServiceProviderNotFoundException("");
        }
    }
    
    @Override
    public List<ServiceProviderEntity> retrieveListOfServiceProvidersWithPendingApproval() throws ServiceProviderNotFoundException {
        Query query = em.createQuery("SELECT s FROM ServiceProviderEntity s WHERE s.status = :status");
        query.setParameter("status", ServiceProviderStatus.PENDING);

        try {
            return (List<ServiceProviderEntity>) query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new ServiceProviderNotFoundException("Service Providers does not exist!");
        }
    }
    
    public List<ServiceProviderEntity> retrieveListOfServiceProvidersNotBlocked() throws ServiceProviderNotFoundException {
        Query query = em.createQuery("SELECT s FROM ServiceProviderEntity s WHERE s.status != :status");
        query.setParameter("status", ServiceProviderStatus.PENDING);

        try {
            return (List<ServiceProviderEntity>) query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new ServiceProviderNotFoundException("Service Providers does not exist!");
        }
    }
    
    @Override
    public ServiceProviderEntity retrieveListOfAppointments(Long serviceProviderId) throws ServiceProviderNotFoundException{
        ServiceProviderEntity currentServiceProviderEntity = em.find(ServiceProviderEntity.class,serviceProviderId);
        currentServiceProviderEntity.getAppointments().size();
        return currentServiceProviderEntity;
    }
    
//    public ServiceProviderEntity retrieveListOfPendingAppointments (Long serviceProviderId) throws ServiceProviderNotFoundException{
//        ServiceProviderEntity currentServiceProviderEntity = em.find(ServiceProviderEntity.class,serviceProviderId);
//        
//        Query query = em.createQuery("SELECT a FROM AppointmentEntity a WHERE a.serviceProvider =: serviceProvider AND a.isCancelled = 'FALSE'")
//    }
    
    
    public void updateServiceProviderRating(Long providerId, Integer rating) throws ServiceProviderNotFoundException {
        // TODO: implement logic
        // need to update ServiceProvider such that the average rating is captured!
    }
    
}
