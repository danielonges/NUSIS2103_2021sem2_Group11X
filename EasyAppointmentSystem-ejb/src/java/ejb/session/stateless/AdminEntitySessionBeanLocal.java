package ejb.session.stateless;

import util.exception.AdminNotFoundException;
import entity.AdminEntity;
import util.exception.AdminAlreadyExistsException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginException;
import util.exception.UnknownPersistenceException;

public interface AdminEntitySessionBeanLocal {
    
    public Long createAdminEntity(AdminEntity newAdminEntity) throws AdminAlreadyExistsException, UnknownPersistenceException,InputDataValidationException ;
    
    public AdminEntity retrieveAdminEntityByAdminId(Long adminId);
    
    public void updateAdminEntity(AdminEntity adminEntity);
    
    public void deleteAdminEntity(Long adminId);

    public AdminEntity AdminLogin(String email, String password) throws InvalidLoginException;

    public AdminEntity retrieveAdminByEmail(String email) throws AdminNotFoundException;
    
}
