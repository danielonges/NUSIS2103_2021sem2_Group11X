/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package easyappointmentsystemclient;

import ejb.session.stateless.AdminEntitySessionBeanRemote;
import ejb.session.stateless.AppointmentEntitySessionBeanRemote;
import ejb.session.stateless.BusinessCategorySessionBeanRemote;
import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import ejb.session.stateless.EmailSessionBeanRemote;
import ejb.session.stateless.ServiceProviderEntitySessionBeanRemote;
import entity.AdminEntity;
import java.util.InputMismatchException;
import util.exception.InvalidLoginException;
import java.util.Scanner;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;

/**
 *
 * @author leele
 */
public class MainApp {

    private AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote;
    private AdminEntitySessionBeanRemote adminEntitySessionBeanRemote;
    private CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote;
    private ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote;
    private BusinessCategorySessionBeanRemote businessCategorySessionBeanRemote;
    private AdminEntity currentAdminEntity;
    private CustomerModule customerModule;
    private ServiceProviderModule serviceProviderModule;
    private AdminModule adminModule;  
    private EmailSessionBeanRemote emailSessionBeanRemote;
    private Queue queueApplication;
    private ConnectionFactory queueApplicationFactory;

    public MainApp() {
    }

    public MainApp(AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote, CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote, AdminEntitySessionBeanRemote adminEntitySessionBeanRemote, ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote, BusinessCategorySessionBeanRemote businessCategorySessionBeanRemote,EmailSessionBeanRemote emailSessionBeanRemote, Queue queueApplication,ConnectionFactory queueApplicationFactory ) {
        this.appointmentEntitySessionBeanRemote = appointmentEntitySessionBeanRemote;
        this.customerEntitySessionBeanRemote = customerEntitySessionBeanRemote;
        this.adminEntitySessionBeanRemote = adminEntitySessionBeanRemote;
        this.serviceProviderEntitySessionBeanRemote = serviceProviderEntitySessionBeanRemote;
        this.businessCategorySessionBeanRemote = businessCategorySessionBeanRemote;
        this.emailSessionBeanRemote = emailSessionBeanRemote;
        this.queueApplication = queueApplication;
        this.queueApplicationFactory = queueApplicationFactory;
    }


    public void runApp() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            try{
            System.out.println("*** Welcome to Admin terminal ***\n");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");
            response = 0;
            while (response < 1 || response > 2) {
                System.out.print("> ");
                
                    response = Integer.parseInt(scanner.nextLine());

                    if (response == 1) {
                        try {
                            doLogin();
                            System.out.println("Login successful!\n");
                            customerModule = new CustomerModule(appointmentEntitySessionBeanRemote, adminEntitySessionBeanRemote, customerEntitySessionBeanRemote, serviceProviderEntitySessionBeanRemote);
                            serviceProviderModule = new ServiceProviderModule(appointmentEntitySessionBeanRemote, adminEntitySessionBeanRemote, customerEntitySessionBeanRemote, serviceProviderEntitySessionBeanRemote);
                            adminModule = new AdminModule(appointmentEntitySessionBeanRemote, adminEntitySessionBeanRemote, customerEntitySessionBeanRemote, serviceProviderEntitySessionBeanRemote, businessCategorySessionBeanRemote, emailSessionBeanRemote,queueApplication,queueApplicationFactory);
                            menuMain();
                        } catch (InvalidLoginException ex) {
                            System.out.println("Invalid login");
                        }
                    } else if (response == 2) {
                        break;
                    } else {
                        System.out.println("Invalid option, please try again!\n");
                        System.out.println("*** Welcome to Admin terminal ***\n");
            System.out.println("1: Login");
            System.out.println("2: Exit\n");
                        
                    }
                } 
            
            if (response == 2) {
                break;
            }

        }catch (NumberFormatException ex) {
                    System.out.println("Wrong inputs!");
                    
                }
    }
    }

   private void doLogin() throws InvalidLoginException {
        try {
            Scanner scanner = new Scanner(System.in);
            String email = "";
            String password = "";
            System.out.println("*** Admin terminal :: Login ***\n");
            System.out.print("Enter Email Address> ");
            email = scanner.nextLine().trim();
            System.out.print("Enter password> ");
            password = scanner.nextLine().trim();

            if (email.length() > 0 && password.length() > 0) {
                currentAdminEntity = adminEntitySessionBeanRemote.AdminLogin(email, password);
            } else {
                throw new InvalidLoginException("Invalid Login!");
            }
        } catch (InputMismatchException ex) {
            System.out.println("Wrong input!");
        }
    }


    private void menuMain() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            try {
                System.out.println("*** Service provider terminal :: Main ***\n");
                System.out.println("You are login as " + currentAdminEntity.getName() + " \n");
                System.out.println("1: View Appointments for customers");
                System.out.println("2: View Appointments for service providers");
                System.out.println("3: View service providers");
                System.out.println("4: Approve service provider");
                System.out.println("5: Block service provider");
                System.out.println("6: Add Business category");
                System.out.println("7: Remove Business category");
                System.out.println("8: Send reminder email");
                System.out.println("9: Logout\n");
                response = 0;
                OUTER:
                while (response < 1 || response > 9) {
                    System.out.print("> ");
                    response = Integer.parseInt(scanner.nextLine());
                    switch (response) {
                        case 1:
                            customerModule.viewAppointments();
                            break;
                        case 2:
                            serviceProviderModule.viewAppointments();
                            break;
                        case 3:
                            serviceProviderModule.viewListOfProviders();
                            break;
                        case 4:
                            serviceProviderModule.approveProvider();
                            break;
                        case 5:
                            serviceProviderModule.blockProvider();
                            break;
                        case 6:
                            adminModule.addBusinessCategory();
                            break;
                        case 7:
                            adminModule.removeBusinessCategory();
                            break;
                        case 8:
                            adminModule.sendReminderEmail();
                            break;
                        case 9:
                            break OUTER;
                        default:
                            System.out.println("Invalid option, please try again!\n");
                            break;
                    }
                }
                if (response == 9) {
                    break;
                }
            } catch (NumberFormatException ex) {
                {
                    System.out.println("Invalid data type!");
                }
            }
        }
    }
}
