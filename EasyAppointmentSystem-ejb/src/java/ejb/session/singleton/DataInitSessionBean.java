/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.AdminEntitySessionBeanLocal;
import entity.AdminEntity;
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

    @PersistenceContext(unitName = "EasyAppointmentSystem-ejbPU")
    private EntityManager em;

    @EJB(name = "AdminEntitySessionBeanLocal")
    private AdminEntitySessionBeanLocal adminEntitySessionBeanLocal;
    
//    @PostConstruct
//    public void postConstruct() {
//        if(em.find(AdminEntity.class,1L) == null) {
//        adminEntitySessionBeanLocal.createAdminEntity(new AdminEntity("Leonard","leonard@gmail.com","password"));
//        adminEntitySessionBeanLocal.createAdminEntity(new AdminEntity("Zikun","zikun@gmail.com","password"));
//        adminEntitySessionBeanLocal.createAdminEntity(new AdminEntity("Daniel","daniel@gmail.com","password"));
//        }
//    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    
}
