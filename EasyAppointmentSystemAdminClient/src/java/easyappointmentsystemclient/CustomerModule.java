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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
        } catch (CustomerNotFoundException | InputMismatchException ex) {
            System.out.println("Customer does not exist!");
            System.out.println("");

        }
    }
}
