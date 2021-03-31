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
import util.enumeration.Status;

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
        
        try{
            Long serviceProviderId = sc.nextLong();
            ServiceProviderEntity currentServiceProviderEntity = serviceProviderEntitySessionBeanRemote.retrieveServiceProviderEntityByProviderId(serviceProviderId);
            List<AppointmentEntity> appointments = currentServiceProviderEntity.getAppointments();
            if(appointments.size() >0) {
            for(AppointmentEntity appointment: appointments ) {
                System.out.println(appointment);
            }
            } else {
                System.out.println("No apppointments booked!");
            }
        }catch (ServiceProviderNotFoundException ex){
               System.out.println("Service Provider does not exist!");
        }
    }
    

    public void viewListOfProviders(){
        try{
       List<ServiceProviderEntity> serviceProviders = serviceProviderEntitySessionBeanRemote.retrieveListOfServiceProviders();
       for(ServiceProviderEntity value: serviceProviders ) {
           System.out.println(value.toString());
       }
        }catch (ServiceProviderNotFoundException ex){
               System.out.println("Service Providers does not exist!");
               }
    }

    public void approveProvider() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Admin terminal :: Approve service provider ***");
        System.out.println("List of service providers with pending approval");
        while (true) {           
           
        try {
            List<ServiceProviderEntity> serviceProviders = serviceProviderEntitySessionBeanRemote.retrieveListOfServiceProvidersWithPendingApproval();
            for(ServiceProviderEntity serviceProvider: serviceProviders ) {
            System.out.println(serviceProvider);
            }
            System.out.println("Enter 0 to go back to the previous menu.");
            System.out.println("Enter service provider  Id> ");
            Long serviceProviderId = sc.nextLong();
            ServiceProviderEntity currentServiceProvider = serviceProviderEntitySessionBeanRemote.retrieveServiceProviderEntityByProviderId(serviceProviderId);
            currentServiceProvider.setStatus(Status.APPROVE);
            serviceProviderEntitySessionBeanRemote.updateServiceProviderEntity(currentServiceProvider);
        } catch (ServiceProviderNotFoundException | InputMismatchException ex) {
            System.out.println("Service Providers does not exist!");
        }
    }
    }
        

    public void blockProvider() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
