package ejb.session.stateless;

import entity.AdminEntity;

public interface AdminEntitySessionBeanLocal {
    
    public Long createAdminEntity(AdminEntity newAdminEntity);
    
    public AdminEntity retrieveAdminEntityByAdminId(Long adminId);
    
    public void updateAdminEntity(AdminEntity adminEntity);
    
    public void deleteAdminEntity(Long adminId);
}
