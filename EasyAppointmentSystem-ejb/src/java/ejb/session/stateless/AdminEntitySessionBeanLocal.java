package ejb.session.stateless;

import util.exception.AdminNotFoundException;
import entity.AdminEntity;
import util.exception.InvalidLoginException;

public interface AdminEntitySessionBeanLocal {
    
    public Long createAdminEntity(AdminEntity newAdminEntity);
    
    public AdminEntity retrieveAdminEntityByAdminId(Long adminId);
    
    public void updateAdminEntity(AdminEntity adminEntity);
    
    public void deleteAdminEntity(Long adminId);

    public AdminEntity AdminLogin(String email, String password) throws InvalidLoginException;

    public AdminEntity retrieveAdminByEmail(String email) throws AdminNotFoundException;
}
