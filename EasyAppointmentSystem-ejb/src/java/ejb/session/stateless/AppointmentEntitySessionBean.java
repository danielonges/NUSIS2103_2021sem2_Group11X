package ejb.session.stateless;

import entity.AppointmentEntity;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
    public AppointmentEntity retrieveAppointmentEntityByAppointmentId(Long appointmentId) {
        AppointmentEntity appointmentEntity = em.find(AppointmentEntity.class, appointmentId);
        
        // TODO: implement checking for null
        return appointmentEntity;
    }
    
    @Override
    public void updateAppointmentEntity(AppointmentEntity appointmentEntity) {
        // TODO: implement checking for null
        em.merge(appointmentEntity);
    }
    
    @Override
    public void deleteAppointmentEntity(Long appointmentId) {
        AppointmentEntity appointmentEntity = retrieveAppointmentEntityByAppointmentId(appointmentId);
        em.remove(appointmentEntity);
    }
}
