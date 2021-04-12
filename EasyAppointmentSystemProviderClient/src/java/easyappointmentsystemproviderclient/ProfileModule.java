
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
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author meleenoob
 */
public class ProfileModule {

    private AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote;
    private AdminEntitySessionBeanRemote adminEntitySessionBeanRemote;
    private CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote;
    private ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote;
    private ServiceProviderEntity currentServiceProviderEntity;

    public ProfileModule() {
    }

    public ProfileModule(AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote, AdminEntitySessionBeanRemote adminEntitySessionBeanRemote, CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote, ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote, ServiceProviderEntity currentServiceProviderEntity) {
        this.appointmentEntitySessionBeanRemote = appointmentEntitySessionBeanRemote;
        this.adminEntitySessionBeanRemote = adminEntitySessionBeanRemote;
        this.customerEntitySessionBeanRemote = customerEntitySessionBeanRemote;
        this.serviceProviderEntitySessionBeanRemote = serviceProviderEntitySessionBeanRemote;
        this.currentServiceProviderEntity = currentServiceProviderEntity;
    }

    public void viewProfile() {
        System.out.println("*** Service provider terminal :: View Profile ***\n");
        System.out.println("Name: " + currentServiceProviderEntity.getName());
        System.out.println("Business Category: " + currentServiceProviderEntity.getBusinessCategory());
        System.out.println("Business Registration Number: " + currentServiceProviderEntity.getBusinessRegNum());
        System.out.println("City: " + currentServiceProviderEntity.getCity());
        System.out.println("Business Address: " + currentServiceProviderEntity.getAddress());
        System.out.println("Email Address: " + currentServiceProviderEntity.getEmail());
        System.out.println("Phone Number: " + currentServiceProviderEntity.getPhone());
        System.out.println("Overall Rating: " + currentServiceProviderEntity.getOverallRating());
        System.out.println("Status: " + currentServiceProviderEntity.getStatus() + "\n");

    }

    void editProfile() {
        Scanner scanner = new Scanner(System.in);
        Integer response = 9;

        while (true) {
            try {
                System.out.println("*** Service provider terminal :: Edit Profile ***\n");
                System.out.println("1: Edit city");
                System.out.println("2: Edit Business Address");
                System.out.println("3: Edit Email Address");
                System.out.println("4: Edit Phone Number");
                System.out.println("5: Edit Password");
                System.out.println("Enter 0 to go back to the previous menu\n");
                response = 9;

                OUTER:
                while (response < 0 || response > 5) {
                    System.out.print("> ");
                    response = Integer.parseInt(scanner.nextLine());
                    switch (response) {
                        case 1:
                            System.out.print("Enter new city> ");
                            String newCity = scanner.nextLine().trim();
                            while (newCity.length() == 0) {
                                System.out.println("Invalid input! City field must be present.");
                                System.out.print("Enter city> ");
                                newCity = scanner.nextLine().trim();
                            }
                            currentServiceProviderEntity.setCity(newCity);

                            serviceProviderEntitySessionBeanRemote.updateServiceProviderEntity(currentServiceProviderEntity);
                            System.out.println("changed successfully!");
                            break;
                        case 2:
                            System.out.print("Enter new business address> ");
                            String newBusinessAddress = scanner.nextLine().trim();
                            while (newBusinessAddress.length() == 0) {
                                System.out.println("Invalid input! Business Address field must be present.");
                                System.out.print("Enter new Business Address> ");
                                newBusinessAddress = scanner.nextLine().trim();
                            }
                            currentServiceProviderEntity.setAddress(newBusinessAddress);
                            serviceProviderEntitySessionBeanRemote.updateServiceProviderEntity(currentServiceProviderEntity);
                            System.out.println("changed successfully!");
                            break;
                        case 3:
                            System.out.print("Enter new email address> ");
                            String newEmailAddress = scanner.nextLine().trim();
                            while (!isValidEmail(newEmailAddress)) {
                                System.out.print("Enter new email address> ");
                                newEmailAddress = scanner.nextLine().trim();
                            }
                            currentServiceProviderEntity.setEmail(newEmailAddress);
                            serviceProviderEntitySessionBeanRemote.updateServiceProviderEntity(currentServiceProviderEntity);
                            System.out.println("changed successfully!");
                            break;
                        case 4:
                            System.out.print("Enter new phone number> ");
                            String newPhoneNumber = scanner.nextLine().trim();
                            while (!isValidDigitInput(newPhoneNumber)) {
                                System.out.print("Enter phone (digits only, without spaces or hyphens) > ");
                                newPhoneNumber = scanner.nextLine().trim();
                            }
                            currentServiceProviderEntity.setPhone(newPhoneNumber);
                            serviceProviderEntitySessionBeanRemote.updateServiceProviderEntity(currentServiceProviderEntity);
                            System.out.println("changed successfully!");
                            break;
                        case 5:
                            System.out.print("Enter new password> ");
                            String newPassword = scanner.nextLine().trim();
                            while (newPassword.length() != 6 || !newPassword.matches("[0-9]+")) {
                                System.out.println("password must be a 6 digit number!");
                                System.out.print("Enter new password> ");
                                newPassword = scanner.nextLine().trim();
                            }

                            currentServiceProviderEntity.setPassword(newPassword);
                            serviceProviderEntitySessionBeanRemote.updateServiceProviderEntity(currentServiceProviderEntity);
                            System.out.println("changed successfully!");
                            break;
                        case 0:
                            break OUTER;
                        default:
                            System.out.println("Invalid option, please try again!\n");
                            break;
                    }

                }
                if (response == 0) {
                    break;
                }

            } catch (NumberFormatException ex) {
                System.out.println("wrong data type!");
            }

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
}
