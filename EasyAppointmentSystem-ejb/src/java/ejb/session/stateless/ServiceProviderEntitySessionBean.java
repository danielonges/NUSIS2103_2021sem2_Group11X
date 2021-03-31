package ejb.session.stateless;

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
import static util.enumeration.Status.APPROVE;

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
    public ServiceProviderEntity retrieveServiceProviderEntityByProviderId(Long providerId) {
        ServiceProviderEntity serviceProviderEntity = em.find(ServiceProviderEntity.class, providerId);

        // TODO: implement checking for null
        return serviceProviderEntity;
    }

    @Override
    public void updateServiceProviderEntity(ServiceProviderEntity serviceProviderEntity) {
        // TODO: implement checking for null
        em.merge(serviceProviderEntity);
    }

    @Override
    public void deleteServiceProviderEntity(Long providerId) {
        ServiceProviderEntity serviceProviderEntity = retrieveServiceProviderEntityByProviderId(providerId);
        em.remove(serviceProviderEntity);
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

    public List<ServiceProviderEntity> retrieveListOfServiceProviders() throws ServiceProviderNotFoundException {

        Query query = em.createQuery("SELECT s FROM ServiceProviderEntity s", ServiceProviderEntity.class);

        try {
            return (List<ServiceProviderEntity>) query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new ServiceProviderNotFoundException("Service Providers does not exist!");
        }

    }
}
