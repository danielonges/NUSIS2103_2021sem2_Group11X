package ejb.session.stateless;

import entity.ServiceProviderEntity;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
    public Long createServiceProviderEntity(ServiceProviderEntity newServiceProviderEntity) {
        em.persist(newServiceProviderEntity);
        em.flush();
        
        return newServiceProviderEntity.getProviderId();
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
}
