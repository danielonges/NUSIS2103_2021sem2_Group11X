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
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import static java.util.Calendar.LONG;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.InputMismatchException;
import util.exception.AppointmentNotFoundException;
import java.util.List;
import java.util.Scanner;
import javax.persistence.NoResultException;
import util.exception.ServiceProviderNotFoundException;

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

    public AppointmentModule(AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote, AdminEntitySessionBeanRemote adminEntitySessionBeanRemote, CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote, ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote, ServiceProviderEntity currentServiceProviderEntity) {
        this.appointmentEntitySessionBeanRemote = appointmentEntitySessionBeanRemote;
        this.adminEntitySessionBeanRemote = adminEntitySessionBeanRemote;
        this.customerEntitySessionBeanRemote = customerEntitySessionBeanRemote;
        this.serviceProviderEntitySessionBeanRemote = serviceProviderEntitySessionBeanRemote;
        this.currentServiceProviderEntity = currentServiceProviderEntity;
    }

    void viewAppointments() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("*** Service provider terminal :: View Appointments ***\n");
        try {
            List<AppointmentEntity> appointmentEntities = serviceProviderEntitySessionBeanRemote.retrieveListOfPendingAppointments(currentServiceProviderEntity.getProviderId());
            if (appointmentEntities.isEmpty()) {
                System.out.println("No current appointments.");
            } else {
                //  System.out.printf("\n%20s | %20s | %20s | %20s | %20s \n","Name", "| Date", "| Time", "| Appointment No.");
                System.out.println(String.format("%20s | %20s | %10s | %20s", "Name", "Date", "Time", "Appointment no."));
                //     for (AppointmentEntity appointment : appointmentEntities) {
                //     System.out.println(appointment);
                //   System.out.println(String.format("%20s | %20s | %10s | %20s", "Name", "Date", "Time", "Appointment no."));
                for (AppointmentEntity a : appointmentEntities) {
                    if (a.getIsCancelled() == false) {
                        DateFormat dateFormat = new SimpleDateFormat("hh:mm");
                        String strDate = dateFormat.format(a.getDate());
                        System.out.println(String.format("%20s | %20s | %10s | %20s", a.getServiceProvider().getName(), String.format("%04d-%02d-%02d", a.getDate().getYear() + 1900, a.getDate().getMonth() + 1, a.getDate().getDate()), strDate, a.getAppointmentNo()));
                    }
                }

            }
            while (true) {
                System.out.print("Enter 0 to go back to the previous menu> ");
                Integer response = scanner.nextInt();
                if (response == 0) {
                    break;

                } else {
                    for (AppointmentEntity a : appointmentEntities) {
                        if (a.getIsCancelled() == false) {
                            DateFormat dateFormat = new SimpleDateFormat("hh:mm");
                            String strDate = dateFormat.format(a.getDate());
                            System.out.println(String.format("%20s | %20s | %10s | %20s", a.getServiceProvider().getName(), String.format("%04d-%02d-%02d", a.getDate().getYear() + 1900, a.getDate().getMonth() + 1, a.getDate().getDate()), strDate, a.getAppointmentNo()));
                        }
                    }
                }
            }
        } catch (ServiceProviderNotFoundException ex) {
            System.out.println("Service provider not found!");
        } catch (InputMismatchException ex) {
            System.out.println("Wrong inputs!");
        }
    }

    void cancelAppointment() throws AppointmentNotFoundException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("* Service provider terminal :: Cancel Appointments *\n");
        // the following code is the same as viewAppointment()
        try {
            List<AppointmentEntity> appointmentEntities = serviceProviderEntitySessionBeanRemote.retrieveListOfPendingAppointments(currentServiceProviderEntity.getProviderId());

            if (appointmentEntities.isEmpty()) {
                System.out.println("No current appointments.");
            } else {
                System.out.println(String.format("%20s | %20s | %10s | %20s", "Name", "Date", "Time", "Appointment no."));
                for (AppointmentEntity a : appointmentEntities) {
                    if (a.getIsCancelled() == false) {
                        DateFormat dateFormat = new SimpleDateFormat("hh:mm");
                        String strDate = dateFormat.format(a.getDate());
                        System.out.println(String.format("%20s | %20s | %10s | %20s", a.getServiceProvider().getName(), String.format("%04d-%02d-%02d", a.getDate().getYear() + 1900, a.getDate().getMonth() + 1, a.getDate().getDate()), strDate, a.getAppointmentNo()));
                    }
                }

            }
            while (true) {
                System.out.println("Enter 0 to go back to the previous menu> ");
                System.out.print("Enter Appointment Id> ");
                Integer response = Integer.parseInt(scanner.nextLine().trim());
                if (response == 0) {
                    break;
                } else {
                    try {
                        Long value = new Long(response);
                        AppointmentEntity currentAppointmentEntity = appointmentEntitySessionBeanRemote.retrieveAppointmentEntityByAppointmentNo(value);
                        if (currentAppointmentEntity != null) {
                            Date todayDate = new GregorianCalendar().getTime();
                            Date appointmentDate = currentAppointmentEntity.getDate();
                            long MILLIS_PER_DAY = 24 * 60 * 60 * 1000L;
                            boolean moreThanDay = appointmentDate.getTime() - todayDate.getTime() > MILLIS_PER_DAY;
                            if (moreThanDay && currentAppointmentEntity.getIsCancelled() == false) {
                                currentAppointmentEntity.setIsCancelled(Boolean.TRUE);
                                appointmentEntitySessionBeanRemote.updateAppointmentEntity(currentAppointmentEntity);
                                System.out.println("Appointment " + response + " has been cancelled successfully");
                            } else {
                                System.out.println("Your appointment is less than 24 hours away or its already been cancelled.");
                            }
                        }
                    } catch (AppointmentNotFoundException | NoResultException ex) {
                        System.out.println("Unable to cancel!");
                    }
                }
            }
        } catch (ServiceProviderNotFoundException | NumberFormatException ex) {
            System.out.println("Service provider not found");
        }
    }
}
