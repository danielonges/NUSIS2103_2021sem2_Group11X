package ejb.session.stateless;

import entity.AdminEntity;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author danielonges
 */

@Local(AdminEntitySessionBeanLocal.class)
@Remote(AdminEntitySessionBeanRemote.class)
@Stateless
public class AdminEntitySessionBean implements AdminEntitySessionBeanRemote, AdminEntitySessionBeanLocal {

    @PersistenceContext(unitName = "EasyAppointmentSystem-ejbPU")
    private EntityManager em;
    
    @Override
    public Long createAdminEntity(AdminEntity newAdminEntity) {
        em.persist(newAdminEntity);
        em.flush();
        
        return newAdminEntity.getAdminId();
    }
    
    @Override
    public AdminEntity retrieveAdminEntityByAdminId(Long adminId) {
        AdminEntity adminEntity = em.find(AdminEntity.class, adminId);
        
        // TODO: implement checking for null
        return adminEntity;
    }
    
    @Override
    public void updateAdminEntity(AdminEntity adminEntity) {
        // TODO: implement checking for null
        em.merge(adminEntity);
    }
    
    @Override
    public void deleteAdminEntity(Long adminId) {
        AdminEntity adminEntity = retrieveAdminEntityByAdminId(adminId);
        em.remove(adminEntity);
    }
}
