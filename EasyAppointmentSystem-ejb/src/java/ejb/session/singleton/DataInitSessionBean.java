/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import ejb.session.stateless.AdminEntitySessionBeanLocal;
import ejb.session.stateless.AppointmentEntitySessionBeanLocal;
import ejb.session.stateless.BusinessCategorySessionBeanLocal;
import ejb.session.stateless.CustomerEntitySessionBeanLocal;
import ejb.session.stateless.ServiceProviderEntitySessionBeanLocal;
import entity.AdminEntity;
import entity.AppointmentEntity;
import entity.BusinessCategoryEntity;
import entity.CustomerEntity;
import entity.ServiceProviderEntity;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import util.enumeration.ServiceProviderStatus;
import util.exception.BusinessCategoryNotFoundException;
import util.exception.ServiceProviderNotFoundException;

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

    @EJB(name = "CustomerEntitySessionBeanLocal")
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;

    @EJB(name = "ServiceProviderEntitySessionBeanLocal")
    private ServiceProviderEntitySessionBeanLocal serviceProviderEntitySessionBeanLocal;

    public DataInitSessionBean() {
    }

    
    @PostConstruct
    public void postConstruct() {
        if (em.find(AdminEntity.class, 1L) == null || em.find(BusinessCategoryEntity.class, 1L) == null || em.find(ServiceProviderEntity.class, 1L) == null || em.find(CustomerEntity.class, 1L) == null) {
            initializeData();
        }
    }
    
    private void initializeData() {
        try {
            adminEntitySessionBeanLocal.createAdminEntity(new AdminEntity("Leonard", "leonard@gmail.com", "123456"));
            adminEntitySessionBeanLocal.createAdminEntity(new AdminEntity("Zikun", "em", "123456"));
            adminEntitySessionBeanLocal.createAdminEntity(new AdminEntity("Daniel", "daniel@gmail.com", "123456"));

            businessCategorySessionBeanLocal.createBusinessCategoryEntity(new BusinessCategoryEntity("Health"));
            businessCategorySessionBeanLocal.createBusinessCategoryEntity(new BusinessCategoryEntity("Fashion"));
            businessCategorySessionBeanLocal.createBusinessCategoryEntity(new BusinessCategoryEntity("Education"));
            
            BusinessCategoryEntity healthCategory = businessCategorySessionBeanLocal.retrieveBusinessCategoryEntityByName("Health");
            BusinessCategoryEntity educationCategory = businessCategorySessionBeanLocal.retrieveBusinessCategoryEntityByName("Education");
            
            ServiceProviderEntity mcDonalds = new ServiceProviderEntity("McDonalds", "1 Computing Drive", "Clementi", "mcd@gmail.com", "123456", "ABCD1234", ServiceProviderStatus.PENDING, 0, "999");
            mcDonalds.setBusinessCategory(healthCategory);
            serviceProviderEntitySessionBeanLocal.createServiceProviderEntity(mcDonalds);

            ServiceProviderEntity nus = new ServiceProviderEntity("NUS", "100 Computing Drive", "Clementi", "nus@gmail.com", "123456", "NUSORWTV", ServiceProviderStatus.PENDING, 0, "12345678");
            nus.setBusinessCategory(educationCategory);
            serviceProviderEntitySessionBeanLocal.createServiceProviderEntity(nus);
            
            customerEntitySessionBeanLocal.createCustomerEntity(new CustomerEntity("S1234567A", "John", "Doe", 'M', 20, "62353535", "10 Heng Mui Keng Terrace", "Singapore", "johndoe@gmail.com", "123456"));
            customerEntitySessionBeanLocal.createCustomerEntity(new CustomerEntity("T1234567A", "Ching", "Chong", 'F', 20, "999", "1 Stack Overflow Drive", "Singapore", "chingchong@gmail.com", "123456"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// Add business logic below. (Right-click in editor and choose
// "Insert Code > Add Business Method")

//if (em.find(AdminEntity.class, 1L) == null) {
//            adminEntitySessionBeanLocal.createAdminEntity(new AdminEntity("Leonard", "leonard@gmail.com", "123456"));
//            adminEntitySessionBeanLocal.createAdminEntity(new AdminEntity("Zikun", "em", "123456"));
//            adminEntitySessionBeanLocal.createAdminEntity(new AdminEntity("Daniel", "daniel@gmail.com", "123456"));
//
//        }
//        if (em.find(BusinessCategoryEntity.class, 1L) == null) {
//            businessCategorySessionBeanLocal.createBusinessCategoryEntity(new BusinessCategoryEntity("Health"));
//            businessCategorySessionBeanLocal.createBusinessCategoryEntity(new BusinessCategoryEntity("Fashion"));
//            businessCategorySessionBeanLocal.createBusinessCategoryEntity(new BusinessCategoryEntity("Education"));
//        }
//        /*  if(em.find(AppointmentEntity.class,1L) == null) {
//           appointmentEntitySessionBeanLocal.createAppointmentEntity(new AppointmentEntity())
//        }*/
//
//        BusinessCategoryEntity healthCategory = null;
//        BusinessCategoryEntity educationCategory = null;
//        try {
//            healthCategory = businessCategorySessionBeanLocal.retrieveBusinessCategoryEntityByName("Health");
//            educationCategory = businessCategorySessionBeanLocal.retrieveBusinessCategoryEntityByName("Education");
//
//        } catch (BusinessCategoryNotFoundException ex) {
//            Logger.getLogger(DataInitSessionBean.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        if (em.find(ServiceProviderEntity.class, 1L) == null) {
//            try {
//                ServiceProviderEntity mcDonalds = new ServiceProviderEntity("McDonalds", "1 Computing Drive", "Clementi", "mcd@gmail.com", "123456", "ABCD1234", ServiceProviderStatus.PENDING, 0, "999");
//                mcDonalds.setBusinessCategory(healthCategory);
//                serviceProviderEntitySessionBeanLocal.createServiceProviderEntity(mcDonalds);
//
//                ServiceProviderEntity nus = new ServiceProviderEntity("NUS", "100 Computing Drive", "Clementi", "nus@gmail.com", "123456", "NUSORWTV", ServiceProviderStatus.PENDING, 0, "12345678");
//                mcDonalds.setBusinessCategory(educationCategory);
//                serviceProviderEntitySessionBeanLocal.createServiceProviderEntity(nus);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        if (em.find(CustomerEntity.class, 1L) == null) {
//            try {
//                customerEntitySessionBeanLocal.createCustomerEntity(new CustomerEntity("S1234567A", "John", "Doe", 'M', 20, "62353535", "10 Heng Mui Keng Terrace", "Singapore", "johndoe@gmail.com", "123456"));
//                customerEntitySessionBeanLocal.createCustomerEntity(new CustomerEntity("T1234567A", "Ching", "Chong", 'F', 20, "999", "1 Stack Overflow Drive", "Singapore", "chingchong@gmail.com", "123456"));
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }