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
                for (AppointmentEntity appointment : appointments) {
                    System.out.println(appointment);
                }
            }
            while (true) {
                System.out.println("Enter 0 to go back to the previous menu");
                Integer response = sc.nextInt();
                if (response == 0) {
                    break;
                } else {
                    System.out.println("invalid input!");
                }
            }
        } catch (ServiceProviderNotFoundException | InputMismatchException | NullPointerException ex) {
            System.out.println("Service Providers does not exist!");
        }
    }

    public void viewListOfProviders() {
        System.out.println("*** Admin terminal :: View service provider ***");
        System.out.println("List of service providers: ");
        Scanner sc = new Scanner(System.in);
        while (true) {
            try {
                List<ServiceProviderEntity> serviceProviders = serviceProviderEntitySessionBeanRemote.retrieveListOfServiceProviders();
                for (ServiceProviderEntity serviceProvider : serviceProviders) {
                    System.out.println(serviceProvider);
                }
                System.out.println("Enter 0 to go back to the previous menu.");
                int response = sc.nextInt();
                if (response == 0L) {
                    break;
                } else {
                    System.out.println("Wrong input!");
                }
            } catch (ServiceProviderNotFoundException | InputMismatchException ex) {
                System.out.println("Service Providers does not exist!");
            }
        }
    }

    public void approveProvider() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Admin terminal :: Approve service provider ***");
        System.out.println("List of service providers with pending approval:");

        try {
            List<ServiceProviderEntity> serviceProviders = serviceProviderEntitySessionBeanRemote.retrieveListOfServiceProvidersWithPendingApproval();
            for (ServiceProviderEntity serviceProvider : serviceProviders) {
                System.out.println(serviceProvider);
            }

            while (true) {
                System.out.println("Enter 0 to go back to the previous menu.");
                System.out.print("Enter service provider  Id> ");
                Long serviceProviderId = sc.nextLong();
                if (serviceProviderId == 0L) {
                    break;
                } else {

                    ServiceProviderEntity currentServiceProvider = serviceProviderEntitySessionBeanRemote.retrieveServiceProviderEntityByProviderId(serviceProviderId);
                    currentServiceProvider.setStatus(ServiceProviderStatus.APPROVE);
                    serviceProviderEntitySessionBeanRemote.updateServiceProviderEntity(currentServiceProvider);
                }
            }
        } catch (ServiceProviderNotFoundException | InputMismatchException ex) {
            System.out.println("Service Providers does not exist!");
        }
    }

    public void blockProvider() {
         Scanner sc = new Scanner(System.in);
        System.out.println("*** Admin terminal :: Block service provider ***");
        System.out.println("List of service providers :");

        try {
            List<ServiceProviderEntity> serviceProviders = serviceProviderEntitySessionBeanRemote.retrieveListOfServiceProvidersNotBlocked();
            for (ServiceProviderEntity serviceProvider : serviceProviders) {
                System.out.println(serviceProvider);
            }

            while (true) {
                System.out.println("Enter 0 to go back to the previous menu.");
                System.out.print("Enter service provider  Id> ");
                Long serviceProviderId = sc.nextLong();
                if (serviceProviderId == 0L) {
                    break;
                } else {

                    ServiceProviderEntity currentServiceProvider = serviceProviderEntitySessionBeanRemote.retrieveServiceProviderEntityByProviderId(serviceProviderId);
                    currentServiceProvider.setStatus(ServiceProviderStatus.BLOCK);
                    serviceProviderEntitySessionBeanRemote.updateServiceProviderEntity(currentServiceProvider);
                }
            }
        } catch (ServiceProviderNotFoundException | InputMismatchException ex) {
            System.out.println("Service Providers does not exist!");
        }
    }

}
