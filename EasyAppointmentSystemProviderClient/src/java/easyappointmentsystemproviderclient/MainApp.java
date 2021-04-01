/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package easyappointmentsystemproviderclient;

import ejb.session.stateless.AdminEntitySessionBeanRemote;
import ejb.session.stateless.AppointmentEntitySessionBeanRemote;
import ejb.session.stateless.BusinessCategorySessionBeanRemote;
import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import ejb.session.stateless.ServiceProviderEntitySessionBeanRemote;
import entity.BusinessCategoryEntity;
import entity.ServiceProviderEntity;
import util.exception.AppointmentNotFoundException;
import util.exception.InputInvalidException;
import util.exception.InvalidLoginException;
import util.exception.InvalidRegistrationException;
import util.exception.ServiceProviderEmailExistException;
import util.exception.UnknownPersistenceException;
import java.util.List;
import java.util.Scanner;
import util.enumeration.ServiceProviderStatus;
import static util.enumeration.ServiceProviderStatus.APPROVE;
//import util.exception.InvalidRegistrationException;
/**
 *
 * @author meleenoob
 */
public class MainApp {

    private AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote;
    private AdminEntitySessionBeanRemote adminEntitySessionBeanRemote;
    private CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote;
    private ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote;
    private BusinessCategorySessionBeanRemote businessCategorySessionBeanRemote;
    private ServiceProviderEntity currentServiceProviderEntity;
    private ProfileModule profileModule;
    private AppointmentModule appointmentModule;

    public MainApp() {
    }

    public MainApp(AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote, CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote, AdminEntitySessionBeanRemote adminEntitySessionBeanRemote, ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote, BusinessCategorySessionBeanRemote businessCategorySessionBeanRemote) {
        this.appointmentEntitySessionBeanRemote = appointmentEntitySessionBeanRemote;
        this.customerEntitySessionBeanRemote = customerEntitySessionBeanRemote;
        this.adminEntitySessionBeanRemote = adminEntitySessionBeanRemote;
        this.serviceProviderEntitySessionBeanRemote = serviceProviderEntitySessionBeanRemote;
        this.businessCategorySessionBeanRemote = businessCategorySessionBeanRemote;
    }

    public void runApp() throws InvalidLoginException, AppointmentNotFoundException {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Welcome to Service provider terminal ***\n");
            System.out.println("1: Registration");
            System.out.println("2: Login");
            System.out.println("3: Exit\n");
            response = 0;
            while (response < 1 || response > 4) {
                System.out.print("> ");

                response = scanner.nextInt();

                if (response == 1) {
                    try {
                        doRegister();
                    } catch (InvalidRegistrationException ex) {
                        System.out.println("registration is invalid!");
                    }
                } else if (response == 2) {
                    try {
                        doLogin();
                        System.out.println("Login successful!\n");
                        profileModule = new ProfileModule(appointmentEntitySessionBeanRemote, adminEntitySessionBeanRemote, customerEntitySessionBeanRemote, serviceProviderEntitySessionBeanRemote,currentServiceProviderEntity);
                        appointmentModule = new AppointmentModule(appointmentEntitySessionBeanRemote, adminEntitySessionBeanRemote, customerEntitySessionBeanRemote, serviceProviderEntitySessionBeanRemote,currentServiceProviderEntity);
                        //insert module here
                        menuMain();
                    } catch (InvalidLoginException ex) {
                        System.out.println("invalid login");
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

    private void doRegister() throws InvalidRegistrationException {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("*** Service Provider Terminal :: Registration Operation ***\n");
        System.out.print("Enter Name> ");
        String name = scanner.nextLine().trim();
        List<BusinessCategoryEntity> businessCategoryEntities = businessCategorySessionBeanRemote.retrieveAllBusinessCategories();
        int sizeOfBusinessCategoryList = businessCategoryEntities.size();
        for (int i = 0; i < sizeOfBusinessCategoryList - 1; i++) {
            System.out.printf( businessCategoryEntities.get(i).getId().toString() + "  " + businessCategoryEntities.get(i).getCategory() + "  |  ");
        }
        System.out.println(businessCategoryEntities.get(sizeOfBusinessCategoryList - 1).getId().toString() + "  " + businessCategoryEntities.get(sizeOfBusinessCategoryList - 1).getCategory());
        System.out.print("Enter Business Category> ");
        int businessCategoryId = scanner.nextInt();
        scanner.nextLine();
        businessCategoryId--; //the actual index in the list
        String businessCategory = businessCategoryEntities.get(businessCategoryId).getCategory();
        System.out.print("Enter Business Registration Number> ");
        String businessRegistrationNum = scanner.nextLine().trim();
        System.out.print("Enter City> ");
        String city = scanner.nextLine().trim();
        System.out.print("Enter Phone> ");
        String phone = scanner.nextLine().trim();
        System.out.print("Enter Business Address> ");
        String businessAddress = scanner.nextLine().trim();
        System.out.print("Enter Email> ");
        String email = scanner.nextLine().trim();
        System.out.print("Enter Password> ");
        String password = scanner.nextLine().trim();
        if (email.length() > 0 && password.length() > 0) {
            ServiceProviderEntity newServiceProviderEntity = new ServiceProviderEntity();
            newServiceProviderEntity.setName(name);
            newServiceProviderEntity.setBusinessCategory(businessCategory);
            newServiceProviderEntity.setBusinessRegNum(businessRegistrationNum);
            newServiceProviderEntity.setCity(city);
            newServiceProviderEntity.setPhone(phone);
            newServiceProviderEntity.setStatus(ServiceProviderStatus.APPROVE);//change to approve for testing, default:PENDING
            newServiceProviderEntity.setAddress(businessAddress);
            newServiceProviderEntity.setEmail(email);
            newServiceProviderEntity.setPassword(password);
            serviceProviderEntitySessionBeanRemote.createServiceProviderEntity(newServiceProviderEntity);
            System.out.println("You have been registered successfully! \n");
            System.out.println("Enter 0 to go back to the previous menu. \n");
            System.out.print("> ");
            Integer goBack = scanner.nextInt();
            while (goBack < 0 || goBack > 0) {
                if (goBack != 0) {
                    System.out.println("Enter 0 to go back to the previous menu. \n");
                    System.out.print("> ");
                    goBack = scanner.nextInt();
                } else {
                    break;
                }
            }
        } else {
            throw new InvalidRegistrationException("invalid registration!");
        }
    }

    private void doLogin() throws InvalidLoginException {
        Scanner scanner = new Scanner(System.in);
        String email = "";
        String password = "";
        System.out.println("*** Service provider terminal :: Login ***\n");
        System.out.print("Enter Email Address> ");
        email = scanner.nextLine().trim();
        System.out.print("Enter password> ");
        password = scanner.nextLine().trim();
        if (email.length() > 0 && password.length() > 0) {
            currentServiceProviderEntity = serviceProviderEntitySessionBeanRemote.ServiceProviderLogin(email, password);
        } else {
            throw new InvalidLoginException("Invalid Login!");
        }
    }

    private void menuMain() throws AppointmentNotFoundException {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            System.out.println("*** Service provider terminal :: Main ***\n");
            System.out.println("You are login as " + currentServiceProviderEntity.getName() + " \n");
            System.out.println("1: View profile");
            System.out.println("2: Edit Profile");
            System.out.println("3: View Appointments");
            System.out.println("4: Cancel Appointments");
            System.out.println("5: Logout\n");
            response = 0;
            while (response < 1 || response > 5) {
                System.out.print("> ");
                response = scanner.nextInt();
                if (response == 1) {
                    profileModule.viewProfile();
                } else if (response == 2) {
                    profileModule.editProfile();
                } else if (response == 3) {
                    appointmentModule.viewAppointments();
                } else if (response == 4) {
                    appointmentModule.cancelAppointment();
                } else if (response == 5) {
                    break;
                } else {
                    System.out.println("Invalid option, please try again!\n");
                }
            }
            if (response == 5) {
                break;
            }
        }
    }
}
