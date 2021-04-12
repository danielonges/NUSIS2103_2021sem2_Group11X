
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
import java.util.InputMismatchException;
import util.exception.AppointmentNotFoundException;
import util.exception.InputInvalidException;
import util.exception.InvalidLoginException;
import util.exception.InvalidRegistrationException;
import util.exception.ServiceProviderEmailExistException;
import util.exception.UnknownPersistenceException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.enumeration.ServiceProviderStatus;
import static util.enumeration.ServiceProviderStatus.APPROVE;
import util.exception.InputDataValidationException;
import util.exception.ServiceProviderAlreadyExistsException;
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
            try {
                System.out.println("* Welcome to Service provider terminal *\n");
                System.out.println("1: Registration");
                System.out.println("2: Login");
                System.out.println("3: Exit\n");
                response = 0;
                while (response < 1 || response > 4) {
                    System.out.print("> ");

                    response = Integer.parseInt(scanner.nextLine());

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
                            profileModule = new ProfileModule(appointmentEntitySessionBeanRemote, adminEntitySessionBeanRemote, customerEntitySessionBeanRemote, serviceProviderEntitySessionBeanRemote, currentServiceProviderEntity);
                            appointmentModule = new AppointmentModule(appointmentEntitySessionBeanRemote, adminEntitySessionBeanRemote, customerEntitySessionBeanRemote, serviceProviderEntitySessionBeanRemote, currentServiceProviderEntity);
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

            } catch (NumberFormatException ex) {
                System.out.println("Wrong inputs!");

            }
        }
    }

    private void doRegister() throws InvalidRegistrationException {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("* Service Provider Terminal :: Registration Operation *\n");
            System.out.print("Enter Name> ");
            String name = scanner.nextLine().trim();
            while (name.length() == 0) {
                System.out.println("Invalid input! Name field must be present.");
                System.out.print("Enter Name> ");
                name = scanner.nextLine().trim();
            }
            List<BusinessCategoryEntity> businessCategoryEntities = businessCategorySessionBeanRemote.retrieveAllBusinessCategories();
            int sizeOfBusinessCategoryList = businessCategoryEntities.size();
            for (int i = 0; i < sizeOfBusinessCategoryList - 1; i++) {
                System.out.printf(i + 1 + "  " + businessCategoryEntities.get(i).getCategory() + "  |  ");
            }
            System.out.println(sizeOfBusinessCategoryList + "  " + businessCategoryEntities.get(sizeOfBusinessCategoryList - 1).getCategory());
            System.out.print("Enter Business Category> ");
            int businessCategoryId = scanner.nextInt();
            scanner.nextLine();
            businessCategoryId--; //the actual index in the list
            BusinessCategoryEntity businessCategory = businessCategoryEntities.get(businessCategoryId);
            System.out.print("Enter Business Registration Number> ");
            String businessRegistrationNum = scanner.nextLine().trim();
            while (businessRegistrationNum.length() == 0) {
                System.out.println("Invalid input! Business Regsitration Number field must be present.");
                System.out.print("Enter Business Regsitration Number> ");
                businessRegistrationNum = scanner.nextLine().trim();
            }
            System.out.print("Enter City> ");
            String city = scanner.nextLine().trim();
            while (city.length() == 0) {
                System.out.println("Invalid input! City field must be present.");
                System.out.print("Enter city> ");
                city = scanner.nextLine().trim();
            }
            System.out.print("Enter Phone> ");
            String phone = scanner.nextLine().trim();
            while (!isValidDigitInput(phone)) {

                System.out.print("Enter phone (digits only, without spaces or hyphens) > ");
                phone = scanner.nextLine().trim();
            }
            System.out.print("Enter Business Address> ");
            String businessAddress = scanner.nextLine().trim();
            while (businessAddress.length() == 0) {
                System.out.println("Invalid input! Business Address field must be present.");
                System.out.print("Enter Business Address> ");
                businessAddress = scanner.nextLine().trim();
            }
            System.out.print("Enter Email> ");
            String email = scanner.nextLine().trim();
            while (!isValidEmail(email)) {
                System.out.print("Enter email> ");
                email = scanner.nextLine().trim();
            }
            System.out.print("Enter Password> ");
            String password = scanner.nextLine().trim();
            while (password.length() != 6 || !password.matches("[0-9]+")) {
                System.out.println("password must be a 6 digit number!");
                System.out.print("Enter Password> ");
                password = scanner.nextLine().trim();
            }
            if (email.length() > 0 && password.length() > 0) {
                ServiceProviderEntity newServiceProviderEntity = new ServiceProviderEntity();
                newServiceProviderEntity.setName(name);
                newServiceProviderEntity.setBusinessCategory(businessCategory);
                BusinessCategoryEntity currentBusinessCategory = businessCategorySessionBeanRemote.retrieveServiceProviders(businessCategory.getCategoryId());
                currentBusinessCategory.getServiceProviders().add(newServiceProviderEntity);
                newServiceProviderEntity.setBusinessRegNum(businessRegistrationNum);
                newServiceProviderEntity.setCity(city);
                newServiceProviderEntity.setPhone(phone);
                newServiceProviderEntity.setStatus(ServiceProviderStatus.PENDING);//change to approve for testing, default:PENDING
                newServiceProviderEntity.setAddress(businessAddress);
                newServiceProviderEntity.setEmail(email);
                newServiceProviderEntity.setPassword(password);
                serviceProviderEntitySessionBeanRemote.createServiceProviderEntity(newServiceProviderEntity);
                //  businessCategorySessionBeanRemote.updateBusinessCategoryEntity(currentBusinessCategory);
                System.out.println("You have been registered successfully! \n");
                System.out.println("Enter 0 to go back to the previous menu. \n");
                System.out.print("> ");
                Integer goBack = scanner.nextInt();
                while (goBack < 0 || goBack > 0) {
                    if (goBack != 0) {
                        System.out.print("Enter 0 to go back to the previous menu> ");
                        goBack = scanner.nextInt();
                    } else {
                        break;
                    }
                }
            } else {
                throw new InvalidRegistrationException("invalid registration!");
            }
        } catch (InputMismatchException | IndexOutOfBoundsException ex) {
            System.out.println("An error has occurred, please try again!");
        } catch (InputDataValidationException ex) {
            System.out.println(ex.getMessage());
        } catch (ServiceProviderAlreadyExistsException ex) {
            System.out.println("Service Provider already exists!");
        } catch (UnknownPersistenceException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean isValidDigitInput(String input) {
        if (!input.matches("\\d+")) {
            System.out.println("Invalid input! Input must be all digits.");
            return false;
        } else {
            return true;
        }
    }

    private boolean isValidEmail(String input) {
        String validationRegex = "^[\\w!#$%&'*+/=?`{|}~^.-]+@[^\\W_][a-zA-Z0-9.-]*[^\\W_]$";
        if (input.matches(validationRegex)) {
            return true;
        } else {
            System.out.println("Invalid input! Invalid email entered.");
            return false;
        }
    }

    private void doLogin() throws InvalidLoginException {
        try {
            Scanner scanner = new Scanner(System.in);
            String email = "";
            String password = "";
            System.out.println("* Service provider terminal :: Login *\n");
            System.out.print("Enter Email Address> ");
            email = scanner.nextLine().trim();
            System.out.print("Enter password> ");
            password = scanner.nextLine().trim();
            if (email.length() > 0 && password.length() > 0) {
                currentServiceProviderEntity = serviceProviderEntitySessionBeanRemote.ServiceProviderLogin(email, password);
            } else {
                throw new InvalidLoginException("Invalid Login!");
            }
        } catch (InputMismatchException ex) {
            System.out.println("Wrong input!");
        }
    }

    private void menuMain() throws AppointmentNotFoundException {
        Scanner scanner = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            try {
                System.out.println("* Service provider terminal :: Main *\n");
                System.out.println("You are login as " + currentServiceProviderEntity.getName() + " \n");
                System.out.println("1: View profile");
                System.out.println("2: Edit Profile");
                System.out.println("3: View Appointments");
                System.out.println("4: Cancel Appointments");
                System.out.println("5: Logout\n");
                response = 0;
                while (response < 1 || response > 5) {
                    System.out.print("> ");
                    response = Integer.parseInt(scanner.nextLine());
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
            } catch (NumberFormatException ex) {
                {
                    System.out.println("Invalid data type!");
                }
            }
        }
    }
}
