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
import ejb.session.stateless.BusinessCategorySessionBeanRemote;
import ejb.session.stateless.EmailSessionBeanRemote;
import entity.AppointmentEntity;
import entity.BusinessCategoryEntity;
import entity.CustomerEntity;
import entity.ServiceProviderEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import util.exception.BusinessCategoryAlreadyExistsException;
import util.exception.BusinessCategoryNotFoundException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author leele
 */
public class AdminModule {

    private AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote;
    private AdminEntitySessionBeanRemote adminEntitySessionBeanRemote;
    private CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote;
    private ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote;
    private BusinessCategorySessionBeanRemote businessCategorySessionBeanRemote;
    private EmailSessionBeanRemote emailSessionBeanRemote;
    private Queue queueApplication;
    private ConnectionFactory queueApplicationFactory;

    AdminModule(AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote, AdminEntitySessionBeanRemote adminEntitySessionBeanRemote,
            CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote, ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote, BusinessCategorySessionBeanRemote businessCategorySessionBeanRemote,
            EmailSessionBeanRemote emailSessionBeanRemote, Queue queueApplication, ConnectionFactory queueApplicationFactory) {
        this.appointmentEntitySessionBeanRemote = appointmentEntitySessionBeanRemote;
        this.adminEntitySessionBeanRemote = adminEntitySessionBeanRemote;
        this.customerEntitySessionBeanRemote = customerEntitySessionBeanRemote;
        this.serviceProviderEntitySessionBeanRemote = serviceProviderEntitySessionBeanRemote;
        this.businessCategorySessionBeanRemote = businessCategorySessionBeanRemote;
        this.emailSessionBeanRemote = emailSessionBeanRemote;
        this.queueApplication = queueApplication;
        this.queueApplicationFactory = queueApplicationFactory;
    }

    public AdminModule() {
    }

    public void addBusinessCategory() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Admin terminal :: Add a Business category ***");

        while (true) {
            try {
                System.out.println("Enter 0 to go back to the previous menu.");
                System.out.print("Enter new business category> ");
                String newBusinessCategoryEntity = sc.nextLine().trim();
                if (newBusinessCategoryEntity.equals("0")) {
                    break;
                } else {
                    businessCategorySessionBeanRemote.createBusinessCategoryEntity(new BusinessCategoryEntity(newBusinessCategoryEntity));
                    System.out.println("The business category \"" + newBusinessCategoryEntity + "\" is added. ");

                }
            } catch (InputMismatchException ex) {
                System.out.println("Wrong Input! \n");
            } catch (BusinessCategoryAlreadyExistsException ex) {
                System.out.println("Business Category already exists!");
            } catch (UnknownPersistenceException ex) {
                Logger.getLogger(AdminModule.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InputDataValidationException ex) {
                Logger.getLogger(AdminModule.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public void removeBusinessCategory() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Admin terminal :: Remove a Business category ***");

        List<BusinessCategoryEntity> businessCategoryEntitys = businessCategorySessionBeanRemote.retrieveAllBusinessCategories();

        while (true) {
            try {
                System.out.println("Enter 0 to go back to the previous menu.");
                System.out.print("Enter business category to remove> ");
                String businessCategoryEntity = sc.nextLine().trim();
                if (businessCategoryEntity.equals("0")) {
                    break;
                } else {
                    BusinessCategoryEntity businessCategory = businessCategorySessionBeanRemote.retrieveBusinessCategoryEntityByName(businessCategoryEntity);
                    List<ServiceProviderEntity> serviceProviders = businessCategorySessionBeanRemote.retrieveServiceProviders(businessCategory.getCategoryId()).getServiceProviders();

                    if (serviceProviders.size() > 0) {
                        System.out.println("Cannot delete business category; some service providers still fall under that category.\n");
                    } else {
                        businessCategorySessionBeanRemote.deleteBusinessCategoryEntity(businessCategoryEntity);
                        System.out.println("The business category \"" + businessCategoryEntity + "\" is removed. ");
                    }

                }
            } catch (InputMismatchException ex) {
                System.out.println("Wrong Input! \n");
            } catch (BusinessCategoryNotFoundException ex) {
                System.out.println("Business category not found!");
            }

        }
    }

    public void sendReminderEmail() {
        Scanner sc = new Scanner(System.in);
        System.out.println("* Admin terminal :: Send reminder email *");

        while (true) {
            try {
                System.out.println("Enter 0 to go back to the previous menu.");
                System.out.print("Enter customer Id> ");
                Long customerId = sc.nextLong();

                if (customerId == 0L) {
                    break;
                } else {
                    CustomerEntity currentCustomerEntity = customerEntitySessionBeanRemote.retrieveCustomerAppointments(customerId);
                    List<AppointmentEntity> appointments = currentCustomerEntity.getAppointments();
                    String toEmailAddress = currentCustomerEntity.getEmail();
                    List<AppointmentEntity> upcomingAppointments = new ArrayList<>();

                    if (appointments.isEmpty()) {
                        System.out.println("There are no new appointments to " + currentCustomerEntity.getFullName() + ".");
                    } else {
                        for (AppointmentEntity appointment : appointments) {
                            Date currentDate = new Date();
                            long diff_in_time = appointment.getDate().getTime() - currentDate.getTime();
                            long diff_in_hours = (diff_in_time / (1000 * 60 * 60)) % 365;
                            if (diff_in_hours >= 0 && appointment.getIsCancelled() == false) {
                                upcomingAppointments.add(appointment);
                            }
                        }
                        AppointmentEntity earliestAppointment = upcomingAppointments.get(0);
                        for (AppointmentEntity appointment : upcomingAppointments) {
                            if (earliestAppointment.getDate().getTime() > appointment.getDate().getTime()) {
                                earliestAppointment = appointment;
                            }
                        }
                        if (toEmailAddress.length() > 0) {
                            try {
                                // 03 - JMS Messaging with Message Driven Bean
                                sendJMSMessageToQueueApplication(earliestAppointment.getAppointmentId(), "exleolee@gmail.com", toEmailAddress);

                                System.out.println("An email is sent to " + currentCustomerEntity.getFullName() + " for the appointment " + earliestAppointment.getAppointmentNo());
                            } catch (Exception ex) {
                                System.out.println("An error has occurred while sending the checkout notification email: " + ex.getMessage() + "\n");
                            }
                        }

                    }
                }

            } catch (InputMismatchException ex) {
                System.out.println("Wrong Input! \n");
            } catch (CustomerNotFoundException ex) {
                System.out.println("Customer Entity not found!");

            }
        }
    }
    
    private void sendJMSMessageToQueueApplication(Long appointmentId, String fromEmailAddress, String toEmailAddress) throws JMSException {
        Connection connection = null;
        Session session = null;
        try {
            connection = queueApplicationFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            MapMessage mapMessage = session.createMapMessage();
            mapMessage.setString("fromEmailAddress", fromEmailAddress);
            mapMessage.setString("toEmailAddress", toEmailAddress);
            mapMessage.setLong("appointmentId", appointmentId);
            MessageProducer messageProducer = session.createProducer(queueApplication);
            messageProducer.send(mapMessage);

        } finally {
            if (session != null) {
                try {
                    session.close();
                } catch (JMSException e) {
                    Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Cannot close session", e);
                }
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

}
