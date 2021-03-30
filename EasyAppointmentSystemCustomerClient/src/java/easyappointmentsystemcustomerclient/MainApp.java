package easyappointmentsystemcustomerclient;

import ejb.session.stateless.AdminEntitySessionBeanRemote;
import ejb.session.stateless.AppointmentEntitySessionBeanRemote;
import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import ejb.session.stateless.ServiceProviderEntitySessionBeanRemote;
import entity.CustomerEntity;
import exception.InvalidLoginException;
import java.util.Scanner;

/**
 *
 * @author danielonges
 */
public class MainApp {
    private AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote;
    private CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote;
    private ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote;
    
    private CustomerAppointmentModule customerAppointmentModule;
    
    private CustomerEntity currentCustomerEntity;

    public MainApp() {
    }

    public MainApp(AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote, CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote, ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote) {
        this.appointmentEntitySessionBeanRemote = appointmentEntitySessionBeanRemote;
        this.customerEntitySessionBeanRemote = customerEntitySessionBeanRemote;
        this.serviceProviderEntitySessionBeanRemote = serviceProviderEntitySessionBeanRemote;
    }
    
    public void runApp() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;
        
        while (true) {
            System.out.println("*** Welcome to Customer terminal ***\n");
            System.out.println("1: Registration");
            System.out.println("2: Login");
            System.out.println("3: Exit\n");
            
            response = 0;
            
            while (response < 1 || response > 3) {
                System.out.print("> ");
                        
                response = sc.nextInt();
                
                if (response == 1) {
                    // register
                } else if (response == 2) {
                    // login
                    try {
                        doLogin();
                        System.out.println("Login successful!\n");
                        // instantiate the required modules
                        customerAppointmentModule = new CustomerAppointmentModule(appointmentEntitySessionBeanRemote, customerEntitySessionBeanRemote, serviceProviderEntitySessionBeanRemote, currentCustomerEntity);
                        menuMain();
                    } catch (InvalidLoginException ile) {
                        System.out.println("Invalid Login!\n");
                    }
                } else if (response == 3) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            
            if (response == 3) {
                break;
            }
        }
    }
    
    private void doLogin() throws InvalidLoginException {
        Scanner sc = new Scanner(System.in);
        String email = "";
        String password = "";
        System.out.println("*** Customer terminal :: Login ***\n");
        System.out.print("Enter Email Address> ");
        email = sc.nextLine().trim();
        System.out.print("Enter password> ");
        password = sc.nextLine().trim();
        
        if (email.length() > 0 && password.length() > 0) {
            // do customer login
            // currentCustomerEntity = customerEntitySessionBeanRemote.CustomerLogin(email, password);
        } else {
            throw new InvalidLoginException("Invalid Login!");
        }
    }
    
    private void doRegisterCustomer() {
        // hello
    }
    
    private void menuMain() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Customer terminal :: Main ***\n");
            System.out.println("You are login as " + currentCustomerEntity.getFullName() + " \n");
            System.out.println("1: Search Operation");
            System.out.println("2: Add Appointment");
            System.out.println("3: View Appointments");
            System.out.println("4: Cancel Appointment");
            System.out.println("5: Rate Service Provider");
            System.out.println("6: Logout\n");
            response = 0;
            OUTER:
            while (response < 1 || response > 6) {
                System.out.print("> ");
                response = sc.nextInt();
                switch (response) {
                    case 1:
                        customerAppointmentModule.doSearchOperation();
                        break;
                    case 2:
                        customerAppointmentModule.doAddAppointment();
                        break;
                    case 3:
                        customerAppointmentModule.doViewAppointments();
                        break;
                    case 4:
                        customerAppointmentModule.doCancelAppointment();
                        break;
                    case 5:
                        customerAppointmentModule.doRateProvider();
                        break;
                    case 6:
                        break OUTER;
                    default:
                        System.out.println("Invalid option, please try again!\n");
                        break;
                }
            }
            if (response == 6) {
                break;
            }
        }
    }
}
