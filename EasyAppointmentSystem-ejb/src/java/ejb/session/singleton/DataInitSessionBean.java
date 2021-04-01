/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.AdminEntitySessionBeanLocal;
import ejb.session.stateless.AppointmentEntitySessionBeanLocal;
import ejb.session.stateless.BusinessCategorySessionBeanLocal;
import entity.AdminEntity;
import entity.AppointmentEntity;
import entity.BusinessCategoryEntity;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author leele
 */
@Singleton
@LocalBean
@Startup

public class DataInitSessionBean {

    @EJB(name = "AppointmentEntitySessionBeanLocal")
    private AppointmentEntitySessionBeanLocal appointmentEntitySessionBeanLocal;

    @EJB(name = "BusinessCategorySessionBeanLocal")
    private BusinessCategorySessionBeanLocal businessCategorySessionBeanLocal;

    @PersistenceContext(unitName = "EasyAppointmentSystem-ejbPU")
    private EntityManager em;

    @EJB(name = "AdminEntitySessionBeanLocal")
    private AdminEntitySessionBeanLocal adminEntitySessionBeanLocal;

    @PostConstruct
    public void postConstruct() {
        if (em.find(AdminEntity.class, 1L) == null) {
            adminEntitySessionBeanLocal.createAdminEntity(new AdminEntity("Leonard", "leonard@gmail.com", "password"));
            adminEntitySessionBeanLocal.createAdminEntity(new AdminEntity("Zikun", "zikun@gmail.com", "password"));
            adminEntitySessionBeanLocal.createAdminEntity(new AdminEntity("Daniel", "daniel@gmail.com", "password"));

        }
        if (em.find(BusinessCategoryEntity.class, 1L) == null) {
            businessCategorySessionBeanLocal.createBusinessCategoryEntity(new BusinessCategoryEntity("Health"));
            businessCategorySessionBeanLocal.createBusinessCategoryEntity(new BusinessCategoryEntity("Fashion"));
            businessCategorySessionBeanLocal.createBusinessCategoryEntity(new BusinessCategoryEntity("Education"));
        }
        /*  if(em.find(AppointmentEntity.class,1L) == null) {
           appointmentEntitySessionBeanLocal.createAppointmentEntity(new AppointmentEntity())
        }*/
    }
}

// Add business logic below. (Right-click in editor and choose
// "Insert Code > Add Business Method")

