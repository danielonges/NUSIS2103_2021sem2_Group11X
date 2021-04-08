package easyappointmentsystemclient;

import ejb.session.stateless.AdminEntitySessionBeanRemote;
import ejb.session.stateless.AppointmentEntitySessionBeanRemote;
import ejb.session.stateless.BusinessCategorySessionBeanRemote;
import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import ejb.session.stateless.EmailSessionBeanRemote;
import ejb.session.stateless.ServiceProviderEntitySessionBeanRemote;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;

/**
 *
 * @author danielonges
 */
public class Main {


    @EJB(name = "EmailSessionBeanRemote")
    private static EmailSessionBeanRemote emailSessionBeanRemote;

    @Resource(mappedName = "jms/queueApplication")
    private static Queue queueApplication;

    @Resource(mappedName = "jms/queueApplicationFactory")
    private static ConnectionFactory queueApplicationFactory;

    @EJB(name = "BusinessCategorySessionBeanRemote")
    private static BusinessCategorySessionBeanRemote businessCategorySessionBeanRemote;

    @EJB(name = "ServiceProviderEntitySessionBeanRemote")
    private static ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote;

    @EJB(name = "CustomerEntitySessionBeanRemote")
    private static CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote;

    @EJB(name = "AppointmentEntitySessionBeanRemote")
    private static AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote;

    @EJB(name = "AdminEntitySessionBeanRemote")
    private static AdminEntitySessionBeanRemote adminEntitySessionBeanRemote;
    
    
    
   

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MainApp mainApp = new MainApp(appointmentEntitySessionBeanRemote, customerEntitySessionBeanRemote,
                adminEntitySessionBeanRemote, serviceProviderEntitySessionBeanRemote,businessCategorySessionBeanRemote,emailSessionBeanRemote,queueApplication,queueApplicationFactory);
        mainApp.runApp();
       
    }

    
   
    }

    

