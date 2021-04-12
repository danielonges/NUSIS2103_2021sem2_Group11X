/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package easyappointmentsystemclient;

import ejb.session.stateless.AdminEntitySessionBeanRemote;
import ejb.session.stateless.AppointmentEntitySessionBeanRemote;
import ejb.session.stateless.CustomerEntitySessionBeanRemote;
import ejb.session.stateless.ServiceProviderEntitySessionBeanRemote;
import entity.AppointmentEntity;
import entity.ServiceProviderEntity;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.InputMismatchException;
import util.exception.ServiceProviderNotFoundException;
import java.util.List;
import java.util.Scanner;
import util.enumeration.ServiceProviderStatus;

/**
 *
 * @author leele
 */
public class ServiceProviderModule {

    private AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote;
    private AdminEntitySessionBeanRemote adminEntitySessionBeanRemote;
    private CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote;
    private ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote;

    ServiceProviderModule(AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote, AdminEntitySessionBeanRemote adminEntitySessionBeanRemote, CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote, ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote) {
        this.appointmentEntitySessionBeanRemote = appointmentEntitySessionBeanRemote;
        this.adminEntitySessionBeanRemote = adminEntitySessionBeanRemote;
        this.customerEntitySessionBeanRemote = customerEntitySessionBeanRemote;
        this.serviceProviderEntitySessionBeanRemote = serviceProviderEntitySessionBeanRemote;
    }

    public void viewAppointments() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Admin terminal :: View Appointments for service providers ***");
        System.out.print("Enter service provider Id> ");

        try {
            Long serviceProviderId = sc.nextLong();
            ServiceProviderEntity currentServiceProviderEntity = serviceProviderEntitySessionBeanRemote.retrieveListOfAppointments(serviceProviderId);
            List<AppointmentEntity> appointments = currentServiceProviderEntity.getAppointments();
            if (appointments.isEmpty()) {
                System.out.println("No current appointments.");
            } else {
                System.out.println(String.format("%20s | %20s | %10s | %20s | %20s", "Name", "Date", "Time", "Appointment no.", "Status"));
                for (AppointmentEntity a : appointments) {
                    DateFormat dateFormat = new SimpleDateFormat("hh:mm");
                    String strDate = dateFormat.format(a.getDate());
                    String status = a.getIsCancelled() ? "Cancelled" : "Approved";

//                    String[] array = strDate.toString().split(" ");
//                    String month = array[0];
//                    String time = array[1];
                    System.out.println(String.format("%20s | %20s | %10s | %20s | %20s", a.getServiceProvider().getName(), String.format("%04d-%02d-%02d", a.getDate().getYear() + 1900, a.getDate().getMonth() + 1, a.getDate().getDate()), strDate, a.getAppointmentNo(), status));
                }
            }
            while (true) {

                System.out.println("Enter 0 to go back to the previous menu");
                System.out.print(">");
                Integer response = sc.nextInt();
                if (response == 0) {
                    break;
                } else {
                    System.out.println("invalid input!");
                }
            }
        } catch (ServiceProviderNotFoundException | InputMismatchException | NullPointerException ex) {
            System.out.println("Service Providers does not exist or your input is invalid!");
        }
    }

    public void viewListOfProviders() {
        System.out.println("*** Admin terminal :: View service provider ***");
        System.out.println("List of service providers: ");
        Scanner sc = new Scanner(System.in);
        try {
            List<ServiceProviderEntity> serviceProviders = serviceProviderEntitySessionBeanRemote.retrieveListOfServiceProviders();
            if (serviceProviders.isEmpty()) {
                System.out.println("**No current pending service providers**");
            } else {
                System.out.println("ID | Name | BusinessCategory | BusinessRegistrationNumber | City"
                        + " | Address | Email | Phone");
                for (ServiceProviderEntity serviceProvider : serviceProviders) {
                    System.out.println(serviceProvider);
                }
            }
            while (true) {
                System.out.print("Enter 0 to go back to the previous menu> ");
                Integer response = sc.nextInt();
                if (response == 0) {
                    break;
                } else {
                    System.out.println("invalid input!");
                }
            }
        } catch (ServiceProviderNotFoundException | InputMismatchException ex) {
            System.out.println("Service Providers does not exist or invalid data type!");
        }
    }

    public void approveProvider() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Admin terminal :: Approve service provider ***");
        System.out.println("List of service providers with pending approval:");

        try {
            List<ServiceProviderEntity> serviceProviders = serviceProviderEntitySessionBeanRemote.retrieveListOfServiceProvidersWithPendingApproval();
            if (serviceProviders.isEmpty()) {
                System.out.println("**No current pending service providers**");
            } else {
                System.out.println("ID | Name | BusinessCategory | BusinessRegistrationNumber | City"
                        + " | Address | Email | Phone");
                for (ServiceProviderEntity serviceProvider : serviceProviders) {
                    System.out.println(serviceProvider);
                }
            }

            while (true) {
                System.out.println("Enter 0 to go back to the previous menu.");
                System.out.print("Enter service provider Id to approve> ");
                Long serviceProviderId = sc.nextLong();
                if (serviceProviderId == 0L) {
                    break;
                } else {

                    ServiceProviderEntity currentServiceProvider = serviceProviderEntitySessionBeanRemote.retrieveServiceProviderEntityByProviderId(serviceProviderId);
                    if (currentServiceProvider != null && currentServiceProvider.getStatus() == ServiceProviderStatus.PENDING) {
                        currentServiceProvider.setStatus(ServiceProviderStatus.APPROVE);
                        serviceProviderEntitySessionBeanRemote.updateServiceProviderEntity(currentServiceProvider);
                        System.out.println("Service provider with Id: " + serviceProviderId + " has been successfully approved!");
                    } else {
                        System.out.println("Service provider Id is invalid or service provider has already been approved");
                    }
                }
            }
        } catch (ServiceProviderNotFoundException | InputMismatchException ex) {
            System.out.println("Service Providers does not exist or wrong data type!");
        }
    }

    public void blockProvider() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Admin terminal :: Block service provider ***");
        System.out.println("List of service providers :");

        try {
            List<ServiceProviderEntity> serviceProviders = serviceProviderEntitySessionBeanRemote.retrieveListOfServiceProvidersNotBlocked();
            if (serviceProviders.isEmpty()) {
                System.out.println("**No current pending service providers**");
            } else {
                System.out.println("ID | Name | BusinessCategory | BusinessRegistrationNumber | City"
                        + " | Address | Email | Phone");
                for (ServiceProviderEntity serviceProvider : serviceProviders) {
                    System.out.println(serviceProvider);
                }
            }

            while (true) {
                System.out.println("Enter 0 to go back to the previous menu.");
                System.out.print("Enter service provider  Id> ");
                Long serviceProviderId = sc.nextLong();
                if (serviceProviderId == 0L) {
                    break;
                } else {

                    ServiceProviderEntity currentServiceProvider = serviceProviderEntitySessionBeanRemote.retrieveServiceProviderEntityByProviderId(serviceProviderId);
                    if (currentServiceProvider != null && currentServiceProvider.getStatus() != ServiceProviderStatus.BLOCK) {
                        currentServiceProvider.setStatus(ServiceProviderStatus.BLOCK);
                        serviceProviderEntitySessionBeanRemote.updateServiceProviderEntity(currentServiceProvider);
                        System.out.println("Service provider with Id: " + serviceProviderId + " has been successfully blocked!");
                    } else {
                        System.out.println("Service provider Id is invalid!");
                    }
                }
            }
        } catch (ServiceProviderNotFoundException | InputMismatchException ex) {
            System.out.println("Service Providers does not exist!");
        }
    }

}
