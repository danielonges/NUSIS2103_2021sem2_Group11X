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
import entity.BusinessCategoryEntity;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import util.exception.BusinessCategoryNotFoundException;

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

    AdminModule(AppointmentEntitySessionBeanRemote appointmentEntitySessionBeanRemote, AdminEntitySessionBeanRemote adminEntitySessionBeanRemote,
            CustomerEntitySessionBeanRemote customerEntitySessionBeanRemote, ServiceProviderEntitySessionBeanRemote serviceProviderEntitySessionBeanRemote, BusinessCategorySessionBeanRemote businessCategorySessionBeanRemote) {
        this.appointmentEntitySessionBeanRemote = appointmentEntitySessionBeanRemote;
        this.adminEntitySessionBeanRemote = adminEntitySessionBeanRemote;
        this.customerEntitySessionBeanRemote = customerEntitySessionBeanRemote;
        this.serviceProviderEntitySessionBeanRemote = serviceProviderEntitySessionBeanRemote;
        this.businessCategorySessionBeanRemote = businessCategorySessionBeanRemote;
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
                    businessCategorySessionBeanRemote.deleteBusinessCategoryEntity(businessCategoryEntity);
                    System.out.println("The business category \"" + businessCategoryEntity + "\" is removed. ");

                }
            } catch (InputMismatchException ex) {
                System.out.println("Wrong Input! \n");
            } catch (BusinessCategoryNotFoundException ex) {
                System.out.println("Business category not found!");
            }

        }
    }

    public void sendReminderEmail() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
