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
import entity.CustomerEntity;
import java.util.InputMismatchException;
import util.exception.AppointmentNotFoundException;
import java.util.List;
import java.util.Scanner;
import util.exception.CustomerNotFoundException;

/**
 *
 * @author leele
 */
public class CustomerModule {

    private AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote;
    private AdminEntitySessionBeanRemote adminEntitySessionBeanRemote;
    private CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote;
    private ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote;

    CustomerModule(AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote, AdminEntitySessionBeanRemote adminEntitySessionBeanRemote, CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote, ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote) {
        this.appointmentEntitySessionBeanRemote = appointmentEntitySessionBeanRemote;
        this.adminEntitySessionBeanRemote = adminEntitySessionBeanRemote;
        this.customerEntitySessionBeanRemote = customerEntitySessionBeanRemote;
        this.serviceProviderEntitySessionBeanRemote = serviceProviderEntitySessionBeanRemote;
    }

    public void viewAppointments() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Admin terminal :: View Appointments for customers ***");
        System.out.print("Enter customer Id> ");

        try {
            Long customerId = sc.nextLong();
            CustomerEntity currentCustomerEntity = customerEntitySessionBeanRemote.retrieveCustomerAppointments(customerId);
            List<AppointmentEntity> appointments = currentCustomerEntity.getAppointments();
            if (appointments.isEmpty()) {
                System.out.println("No current appointments.");
            } else {
                for (AppointmentEntity appointment : appointments) {
                    System.out.println(appointment);
                }
            }

            while (true) {
<<<<<<< HEAD
                System.out.print("Enter 0 to go back to the previous menu> ");
=======
                System.out.println("Enter 0 to go back to the previous menu");
                System.out.print(">");
>>>>>>> c0af504506ddd8e376bb2e9b5887f1a488086c8e
                Integer response = sc.nextInt();
                if (response == 0) {
                    break;
                } else {
                    System.out.println("invalid input!");
                }
            }
        } catch (CustomerNotFoundException | InputMismatchException ex) {
            System.out.println("Customer does not exist!");
            System.out.println("");

        }
    }
}
