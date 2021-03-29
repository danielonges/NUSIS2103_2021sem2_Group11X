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
import entity.AppointmentEntity;
import entity.ServiceProviderEntity;
import exception.AppointmentNotFoundException;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author meleenoob
 */
public class AppointmentModule {

    private AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote;
    private AdminEntitySessionBeanRemote adminEntitySessionBeanRemote;
    private CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote;
    private ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote;
    private ServiceProviderEntity currentServiceProviderEntity;
    private AppointmentEntity currentAppointmentEntity;

    public AppointmentModule() {
    }

    public AppointmentModule(AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote, AdminEntitySessionBeanRemote adminEntitySessionBeanRemote, CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote, ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote) {
        this.appointmentEntitySessionBeanRemote = appointmentEntitySessionBeanRemote;
        this.adminEntitySessionBeanRemote = adminEntitySessionBeanRemote;
        this.customerEntitySessionBeanRemote = customerEntitySessionBeanRemote;
        this.serviceProviderEntitySessionBeanRemote = serviceProviderEntitySessionBeanRemote;
    }

    void viewAppointments() throws AppointmentNotFoundException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** Service provider terminal :: View Appointments ***\n");

        Long serviceProviderId = currentServiceProviderEntity.getId();
        List<AppointmentEntity> appointmentEntities = appointmentEntitySessionBeanRemote.retrieveAppointmentsByServiceProviderId(serviceProviderId);
        System.out.println("");
        for (AppointmentEntity appointment : appointmentEntities) {
            System.out.println("Name     | Date     | Time  | Appointment No. ");
            System.out.println(appointment);
        }
        while (true) {
            System.out.println("Enter 0 to go back to the previous menu");
            Integer response = scanner.nextInt();
            if (response == 0) {
                break;
            } else {
                System.out.println("invalid input!");
            }
        }

    }

    void cancelAppointment() throws AppointmentNotFoundException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** Service provider terminal :: Cancel Appointments ***\n");
        // the following code is the same as viewAppointment()
        Long serviceProviderId = currentServiceProviderEntity.getId();
        List<AppointmentEntity> appointmentEntities = appointmentEntitySessionBeanRemote.retrieveAppointmentsByServiceProviderId(serviceProviderId);
        System.out.println("");
        for (AppointmentEntity appointment : appointmentEntities) {
            System.out.println("Name     | Date     | Time  | Appointment No. ");
            System.out.println(appointment);
        }
        while (true) {
            System.out.println("Enter 0 to go back to the previous menu");
            System.out.println("Enter Appointment Id> ");
            Long response = scanner.nextLong();
            if (response == 0L) {
                break;
            } else {
                // here add try catch later to handle cases where appointmentId does not exist
                //currentAppointmentEntity = appointmentEntitySessionBeanRemote.retrieveAppointmentEntityByAppointmentId(response);
                appointmentEntitySessionBeanRemote.deleteAppointmentEntity(response);
                System.out.println("Appointment " + response + " has been canceled successfully");

            }
        }

    }

}
