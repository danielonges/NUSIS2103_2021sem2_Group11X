package ejb.session.stateless;

import entity.AppointmentEntity;
import exception.AppointmentNotFoundException;
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

    @Override
    public Long createAppointmentEntity(AppointmentEntity newAppointmentEntity) {
        em.persist(newAppointmentEntity);
        em.flush();

        return newAppointmentEntity.getAppointmentId();
    }

    @Override
    public AppointmentEntity retrieveAppointmentEntityByAppointmentId(Long appointmentId) throws AppointmentNotFoundException {
        try {
            AppointmentEntity appointmentEntity = em.find(AppointmentEntity.class, appointmentId);
            return appointmentEntity;
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new AppointmentNotFoundException("Appointments not found!");
        }
    }

    @Override
    public void updateAppointmentEntity(AppointmentEntity appointmentEntity) {
        // TODO: implement checking for null
        em.merge(appointmentEntity);
    }

    @Override
    public void deleteAppointmentEntity(Long appointmentId) {
        AppointmentEntity appointmentEntity = null;
        try {
            appointmentEntity = retrieveAppointmentEntityByAppointmentId(appointmentId);
        } catch (AppointmentNotFoundException ex) {
            Logger.getLogger(AppointmentEntitySessionBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        em.remove(appointmentEntity);
    }

    @Override
    public List<AppointmentEntity> retrieveAppointmentsByServiceProviderId(Long serviceProviderId) throws AppointmentNotFoundException {
        Query query = em.createQuery("SELECT s FROM AppointmentEntity s WHERE s.serviceProviderId = :inServiceProviderId");
        query.setParameter("inServiceProviderId", serviceProviderId);

        try {
            return query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new AppointmentNotFoundException("Appointments not found!");
        }
    }
    
    public List<AppointmentEntity> retrieveListOfAppointments() throws AppointmentNotFoundException {
         Query query = em.createQuery("SELECT s FROM AppointmentEntity s", AppointmentEntity.class);
        
        try {
        return (List<AppointmentEntity>) query.getResultList();
    } catch (NoResultException | NonUniqueResultException ex) {
            throw new AppointmentNotFoundException("Appointments does not exist!");
        }
    
    }

}
