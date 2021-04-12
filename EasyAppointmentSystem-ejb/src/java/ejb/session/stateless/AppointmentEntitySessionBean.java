package ejb.session.stateless;

import entity.AppointmentEntity;
import entity.CustomerEntity;
import entity.ServiceProviderEntity;
import java.lang.reflect.InvocationTargetException;
import util.exception.AppointmentNotFoundException;
import util.exception.DeleteAppointmentException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CreateNewAppointmentEntityException;
import util.exception.CustomerNotFoundException;
import util.exception.ServiceProviderNotFoundException;
import util.exception.UnauthorisedOperationException;

/**
 *
 * @author danielonges
 */
@Local(AppointmentEntitySessionBeanLocal.class)
@Remote(AppointmentEntitySessionBeanRemote.class)
@Stateless
public class AppointmentEntitySessionBean implements AppointmentEntitySessionBeanRemote, AppointmentEntitySessionBeanLocal {

    @PersistenceContext(unitName = "EasyAppointmentSystem-ejbPU")
    private EntityManager em;

    @EJB
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;
    @EJB
    private ServiceProviderEntitySessionBeanLocal serviceProviderEntitySessionBeanLocal;

    @Override
    public Long createAppointmentEntity(Long customerId, Long providerId, AppointmentEntity newAppointmentEntity) throws CreateNewAppointmentEntityException {
        if (newAppointmentEntity != null) {
            try {
                CustomerEntity customerEntity = customerEntitySessionBeanLocal.retrieveCustomerEntityByCustomerId(customerId);
                ServiceProviderEntity serviceProviderEntity = serviceProviderEntitySessionBeanLocal.retrieveServiceProviderEntityByProviderId(providerId);
                newAppointmentEntity.setCustomer(customerEntity);
                newAppointmentEntity.setServiceProvider(serviceProviderEntity);
                customerEntity.getAppointments().add(newAppointmentEntity);
                serviceProviderEntity.getAppointments().add(newAppointmentEntity);

                em.persist(newAppointmentEntity);
                em.flush();

                return newAppointmentEntity.getAppointmentId();

            } catch (CustomerNotFoundException | ServiceProviderNotFoundException ex) {
                throw new CreateNewAppointmentEntityException(ex.getMessage());
            }
        } else {
            throw new CreateNewAppointmentEntityException("Appointment information not provided!");
        }
    }

    @Override
    public void cancelAppointmentByCustomerId(Long customerId, Long appointmentNo) throws CustomerNotFoundException, AppointmentNotFoundException, UnauthorisedOperationException {
        CustomerEntity customerEntity = customerEntitySessionBeanLocal.retrieveCustomerEntityByCustomerId(customerId);

        AppointmentEntity appointmentEntity = retrieveAppointmentEntityByAppointmentNo(appointmentNo);

        if (!appointmentEntity.getCustomer().equals(customerEntity)) {
            throw new UnauthorisedOperationException("Appointment does not belong to customer with customer ID " + customerId);
        }

        appointmentEntity.setIsCancelled(true);
        updateAppointmentEntity(appointmentEntity);
    }

    @Override
    public AppointmentEntity retrieveAppointmentEntityByAppointmentId(Long appointmentId) throws AppointmentNotFoundException {
        try {
            AppointmentEntity appointmentEntity = em.find(AppointmentEntity.class, appointmentId);
            if (appointmentEntity != null) {
                return appointmentEntity;
            } else {
                throw new AppointmentNotFoundException("Appointments not found!");
            }
            
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new AppointmentNotFoundException("Appointments not found!");
        }
    }

    @Override
    public AppointmentEntity retrieveAppointmentEntityByAppointmentNo(Long appointmentNo) throws AppointmentNotFoundException {
   
        Query query = em.createQuery("SELECT a FROM AppointmentEntity a WHERE a.appointmentNo = :inAppointmentNo");
        query.setParameter("inAppointmentNo", appointmentNo);
        
        try {
            return (AppointmentEntity) query.getSingleResult();
        } catch (NoResultException ex) {
            throw new AppointmentNotFoundException("Appointment with AppointmentNo " + appointmentNo + " does not exist.");
        }
        
    }

    @Override
    public void updateAppointmentEntity(AppointmentEntity appointmentEntity) {
        // TODO: implement checking for null
        em.merge(appointmentEntity);
    }

    @Override
    public void deleteAppointmentEntity(Long appointmentId) throws AppointmentNotFoundException {
        AppointmentEntity appointmentEntity = null;
        try {
            appointmentEntity = retrieveAppointmentEntityByAppointmentId(appointmentId);
            em.remove(appointmentEntity);
        } catch (AppointmentNotFoundException ex) {
            //throw new DeleteAppointmentException("unable to delete!");
        }
    }

}