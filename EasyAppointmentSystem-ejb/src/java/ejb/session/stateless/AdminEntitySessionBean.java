package ejb.session.stateless;

import exception.AdminNotFoundException;
import entity.AdminEntity;
import exception.InvalidLoginException;
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
    
    @Override
    public AdminEntity AdminLogin(String email, String password) throws InvalidLoginException {
        try{
            AdminEntity adminEntity = retrieveAdminByEmail(email);
            if (adminEntity.getPassword().equals(password)) {
                return adminEntity;
            } else {
                throw new InvalidLoginException("Email does not exist or invalid password!");
            }
        } catch (AdminNotFoundException ex) {
            throw new InvalidLoginException("Email does not exist or invalid password!");
        }
    }
    
    @Override
      public AdminEntity retrieveAdminByEmail(String email) throws AdminNotFoundException {
        Query query = em.createQuery("SELECT s FROM AdminEntity s WHERE s.username = :inUsername");
        query.setParameter("inUsername", email);

        try {
            return (AdminEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new AdminNotFoundException("Admin Email " + email + " does not exist!");
        }
    }
}
