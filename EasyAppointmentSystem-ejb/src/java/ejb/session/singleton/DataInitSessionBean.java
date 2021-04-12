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
import java.math.BigDecimal;
import java.util.Date;
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
            adminEntitySessionBeanLocal.createAdminEntity(new AdminEntity("Zikun", "zikun@gmail.com", "123456"));
            adminEntitySessionBeanLocal.createAdminEntity(new AdminEntity("Daniel", "daniel@gmail.com", "123456"));

            businessCategorySessionBeanLocal.createBusinessCategoryEntity(new BusinessCategoryEntity("Health"));
            businessCategorySessionBeanLocal.createBusinessCategoryEntity(new BusinessCategoryEntity("Fashion"));
            businessCategorySessionBeanLocal.createBusinessCategoryEntity(new BusinessCategoryEntity("Education"));
            
            BusinessCategoryEntity healthCategory = businessCategorySessionBeanLocal.retrieveBusinessCategoryEntityByName("Health");
            BusinessCategoryEntity educationCategory = businessCategorySessionBeanLocal.retrieveBusinessCategoryEntityByName("Education");
            
            ServiceProviderEntity mcDonalds = new ServiceProviderEntity("McDonalds", "1 Computing Drive", "Clementi", "mcd@gmail.com", "123456", "ABCD1234", ServiceProviderStatus.PENDING, new BigDecimal(0), "999");
            mcDonalds.setBusinessCategory(healthCategory);
            serviceProviderEntitySessionBeanLocal.createServiceProviderEntity(mcDonalds);
            mcDonalds = serviceProviderEntitySessionBeanLocal.retrieveServiceProviderByEmail("mcd@gmail.com");
            mcDonalds.setStatus(ServiceProviderStatus.APPROVE);
            serviceProviderEntitySessionBeanLocal.updateServiceProviderEntity(mcDonalds);

            ServiceProviderEntity nus = new ServiceProviderEntity("NUS", "100 Computing Drive", "Clementi", "nus@gmail.com", "123456", "NUSORWTV", ServiceProviderStatus.PENDING, new BigDecimal(0), "12345678");
            nus.setBusinessCategory(educationCategory);
            serviceProviderEntitySessionBeanLocal.createServiceProviderEntity(nus);
            nus = serviceProviderEntitySessionBeanLocal.retrieveServiceProviderByEmail("nus@gmail.com");
            nus.setStatus(ServiceProviderStatus.APPROVE);
            serviceProviderEntitySessionBeanLocal.updateServiceProviderEntity(nus);
            
            customerEntitySessionBeanLocal.createCustomerEntity(new CustomerEntity("S1234567A", "John", "Doe", 'M', 20, "62353535", "10 Heng Mui Keng Terrace", "Singapore", "leeleonard_98@yahoo.com.sg", "123456"));
            customerEntitySessionBeanLocal.createCustomerEntity(new CustomerEntity("T1234567A", "Ching", "Chong", 'F', 20, "999", "1 Stack Overflow Drive", "Singapore", "ongfishh@gmail.com", "123456"));
            
            CustomerEntity johnDoe = customerEntitySessionBeanLocal.retrieveCustomerByEmail("leeleonard_98@yahoo.com.sg");
            
            // John Doe appointment with Macs on 04 Apr 2021 at 8:30 - already past
            AppointmentEntity appointmentWithMacsPast = new AppointmentEntity();
            appointmentWithMacsPast.setAppointmentNo(Long.parseLong("04040830"));
            appointmentWithMacsPast.setBusinessCategory(healthCategory.getCategory());
            appointmentWithMacsPast.setDate(new Date(121, 3, 4, 8, 30));
            Long macsPastAppointmentId = appointmentEntitySessionBeanLocal.createAppointmentEntity(johnDoe.getCustomerId(), mcDonalds.getProviderId(), appointmentWithMacsPast);
            appointmentWithMacsPast.setAppointmentNo(Long.parseLong(macsPastAppointmentId + String.format("%08d", appointmentWithMacsPast.getAppointmentNo())));
            appointmentEntitySessionBeanLocal.updateAppointmentEntity(appointmentWithMacsPast);
            
            // John Doe appointment with NUS on 06 June 2021 at 12:30
            AppointmentEntity appointmentWithNusFuture = new AppointmentEntity();
            appointmentWithNusFuture.setAppointmentNo(Long.parseLong("06061230"));
            appointmentWithNusFuture.setBusinessCategory(educationCategory.getCategory());
            appointmentWithNusFuture.setDate(new Date(121, 5, 6, 12, 30));
            Long nusFutureAppointmentId = appointmentEntitySessionBeanLocal.createAppointmentEntity(johnDoe.getCustomerId(), nus.getProviderId(), appointmentWithNusFuture);
            appointmentWithNusFuture.setAppointmentNo(Long.parseLong(nusFutureAppointmentId + String.format("%08d", appointmentWithNusFuture.getAppointmentNo())));
            appointmentEntitySessionBeanLocal.updateAppointmentEntity(appointmentWithNusFuture);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
