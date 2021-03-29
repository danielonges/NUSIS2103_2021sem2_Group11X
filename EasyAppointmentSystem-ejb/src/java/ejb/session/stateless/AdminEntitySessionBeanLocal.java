package ejb.session.stateless;

import exception.AdminNotFoundException;
import entity.AdminEntity;
import exception.InvalidLoginException;

public interface AdminEntitySessionBeanLocal {
    
    public Long createAdminEntity(AdminEntity newAdminEntity);
    
    public AdminEntity retrieveAdminEntityByAdminId(Long adminId);
    
    public void updateAdminEntity(AdminEntity adminEntity);
    
    public void deleteAdminEntity(Long adminId);

    public AdminEntity AdminLogin(String email, String password) throws InvalidLoginException;

    public AdminEntity retrieveAdminByEmail(String email) throws AdminNotFoundException;
}
