/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package easyappointmentsystemproviderclient;

import ejb.session.stateless.AdminEntitySessionBeanRemote;
import ejb.session.stateless.AppointmentEntitySessionBeanRemote;
import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import ejb.session.stateless.ServiceProviderEntitySessionBeanRemote;
import entity.ServiceProviderEntity;
import exception.AppointmentNotFoundException;
import exception.InputInvalidException;
import exception.InvalidLoginException;
import exception.InvalidRegistrationException;
import exception.ServiceProviderEmailExistException;
import exception.UnknownPersistenceException;
import java.util.Scanner;
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
    private ServiceProviderEntity currentServiceProviderEntity;
    private ProfileModule profileModule;
    private AppointmentModule appointmentModule;

    public MainApp() {
    }

    public MainApp(AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote, CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote, AdminEntitySessionBeanRemote adminEntitySessionBeanRemote, ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote) {
        this.appointmentEntitySessionBeanRemote = appointmentEntitySessionBeanRemote;
        this.customerEntitySessionBeanRemote = customerEntitySessionBeanRemote;
        this.adminEntitySessionBeanRemote = adminEntitySessionBeanRemote;
        this.serviceProviderEntitySessionBeanRemote = serviceProviderEntitySessionBeanRemote;
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
                        profileModule = new ProfileModule(appointmentEntitySessionBeanRemote, adminEntitySessionBeanRemote, customerEntitySessionBeanRemote, serviceProviderEntitySessionBeanRemote);
                        appointmentModule = new AppointmentModule(appointmentEntitySessionBeanRemote, adminEntitySessionBeanRemote, customerEntitySessionBeanRemote, serviceProviderEntitySessionBeanRemote);
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
        String name = "";
        Integer businessCat = 0;
        String businessRegistrationNum = "";
        String city = "";
        String phone = "";
        String businessAddress = "";
        String email = "";
        String password = "";
        System.out.println("*** Service Provider Terminal :: Registration Operation ***\n");
        System.out.print("Enter Name> ");
        name = scanner.nextLine().trim();
        System.out.println("1  Health | 2 Fashion | 3 Education ");
        System.out.print("Enter Business Category> ");
        businessCat = scanner.nextInt();
        String category = "";
        switch (businessCat) {
            case 1:
                category = "Health";
                break;
            case 2:
                category = "Fashion";
                break;
            case 3:
                category = "Education";
                break;
            default:
                break;
        }
        System.out.print("Enter Business Registration Number> ");
        businessRegistrationNum = scanner.nextLine().trim();
        System.out.print("Enter City> ");
        city = scanner.nextLine().trim();
        System.out.print("Enter Phone> ");
        phone = scanner.nextLine().trim();
        System.out.print("Enter Business Address> ");
        businessAddress = scanner.nextLine().trim();
        System.out.print("Enter Email> ");
        email = scanner.nextLine().trim();
        System.out.print("Enter Password> ");
        password = scanner.nextLine().trim();
        if (email.length() > 0 && password.length() > 0) {
            ServiceProviderEntity newServiceProviderEntity = new ServiceProviderEntity();
            newServiceProviderEntity.setName(name);
            newServiceProviderEntity.setBusinessCategory(category);
            newServiceProviderEntity.setBusinessRegNum(businessRegistrationNum);
            newServiceProviderEntity.setCity(city);
            newServiceProviderEntity.setPhone(phone);
            newServiceProviderEntity.setAddress(businessAddress);
            newServiceProviderEntity.setEmail(email);
            newServiceProviderEntity.setPassword(password);
            serviceProviderEntitySessionBeanRemote.createServiceProviderEntity(newServiceProviderEntity);
            System.out.println("You have been registered successfully!+ \n");
            System.out.println("Enter 0 to go back to the previous menu.+ \n");
            System.out.print("> ");
            Integer goBack = scanner.nextInt();
            while (goBack < 0 || goBack > 0) {
                if (goBack != 0) {
                    System.out.println("Enter 0 to go back to the previous menu.+ \n");
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