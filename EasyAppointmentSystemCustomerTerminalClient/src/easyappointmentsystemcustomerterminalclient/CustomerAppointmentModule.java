package easyappointmentsystemcustomerterminalclient;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import ws.client.AppointmentEntity;
import ws.client.AppointmentNotFoundException_Exception;
import ws.client.BusinessCategoryEntity;
import ws.client.CreateNewAppointmentEntityException_Exception;
import ws.client.CustomerEntity;
import ws.client.CustomerNotFoundException_Exception;
import ws.client.InvalidLoginException_Exception;
import ws.client.ServiceProviderEntity;
import ws.client.ServiceProviderNotFoundException_Exception;
import ws.client.UnauthorisedOperationException_Exception;

/**
 *
 * @author danielonges
 */
public class CustomerAppointmentModule {

    Scanner sc;

    CustomerEntity currentCustomerEntity;

    public CustomerAppointmentModule(CustomerEntity currentCustomerEntity) {
        this.currentCustomerEntity = currentCustomerEntity;
        sc = new Scanner(System.in);
    }

    public void doSearchOperation() {

        List<BusinessCategoryEntity> businessCategories = retrieveAllBusinessCategories();
        System.out.println("All available business categories:");

        int index = 1;
        for (BusinessCategoryEntity businessCategory : businessCategories) {
            System.out.println(String.format("%d. %s", index, businessCategory.getCategory()));
            index++;
        }
        System.out.println();

        // enter business category
        System.out.print("Enter business category> ");
        String categoryInput = sc.nextLine().trim();

        // validation for category input
        while (!isValidIntegerInput(categoryInput, businessCategories.size(), "index")) {
            System.out.print("Enter business category> ");
            categoryInput = sc.nextLine().trim();
        }

        // need to convert to 1 based index
        String category = businessCategories.get(Integer.parseInt(categoryInput) - 1).getCategory();

        // enter city
        System.out.print("Enter city> ");
        String city = sc.nextLine().trim();

        while (city.length() == 0) {
            System.out.println("No input entered!");
            System.out.print("Enter city> ");
            city = sc.nextLine().trim();
        }

        // enter date
        System.out.print("Enter date (YYYY-MM-DD)> ");
        String date = sc.nextLine().trim();

        while (!isValidDateInput(date)) {
            System.out.print("Enter date (YYYY-MM-DD)> ");
            date = sc.nextLine().trim();
        }

        // implement logic to filter out based on date
    }

    public void doAddAppointment() {
        doSearchOperation();
    }

    public void doViewAppointments() {

        try {
            List<AppointmentEntity> appointmentEntities = retrieveCustomerAppointments(currentCustomerEntity.getEmail(), currentCustomerEntity.getPassword());

            System.out.println(String.format("Appointments for %s %s:\n", currentCustomerEntity.getFirstName(), currentCustomerEntity.getLastName()));
            for (AppointmentEntity appointmentEntity : appointmentEntities) {
                System.out.println(appointmentEntity);
            }
        } catch (InvalidLoginException_Exception | CustomerNotFoundException_Exception ex) {
            System.out.println("An error occured while performing the operation: " + ex.getMessage());
        }

    }

    public void doCancelAppointment() {
        doViewAppointments();
        System.out.println("Enter 0 to go back to the previous menu.");

        while (true) {
            System.out.print("Enter Appointment Id> ");
            String appointmentNo = sc.nextLine().trim();

            if (!isValidDigitInput(appointmentNo)) {
                continue;
            }

            Long input = Long.parseLong(appointmentNo);

            if (input == 0) {
                break;
            }

            try {
                cancelAppointment(currentCustomerEntity.getEmail(), currentCustomerEntity.getPassword(), input);
                System.out.println("Appointment " + appointmentNo + " has been successfully cancelled.\n");
                System.out.println("Enter 0 to go back to the previous menu.");
            } catch (AppointmentNotFoundException_Exception | CustomerNotFoundException_Exception | InvalidLoginException_Exception | UnauthorisedOperationException_Exception ex) {
                System.out.println("An error occured while performing the operation: " + ex.getMessage());
            }
        }
    }

    public void doRateProvider() {
        System.out.print("Enter service provider Id> ");
        String providerIdStr = sc.nextLine();
        ServiceProviderEntity serviceProviderEntity;

        // check if service provider with id exists
        while (true) {
            while (!isValidDigitInput(providerIdStr)) {
                System.out.println("Enter service provider Id> ");
                providerIdStr = sc.nextLine();
            }

            Long providerId = Long.parseLong(providerIdStr);
            try {
                serviceProviderEntity = retrieveServiceProviderByProviderId(providerId);
                break;
            } catch (ServiceProviderNotFoundException_Exception ex) {
                System.out.println("Service provider with Id" + providerId + " does not exist!");
            }
        }

        System.out.print("Enter rating (between 1 to 5) > ");
        String ratingStr = sc.nextLine().trim();

        while (!isValidIntegerInput(ratingStr, 5, "rating")) {
            System.out.print("Enter rating (between 1 to 5) > ");
            ratingStr = sc.nextLine().trim();
        }
        
        Integer rating = Integer.parseInt(ratingStr);
        try {
            rateServiceProvider(currentCustomerEntity.getEmail(), currentCustomerEntity.getPassword(), serviceProviderEntity.getProviderId(), rating);
        } catch (InvalidLoginException_Exception | ServiceProviderNotFoundException_Exception ex) {
            System.out.println("An error occured while performing the operation: " + ex.getMessage());
        }
    }

    private boolean isValidIntegerInput(String input, Integer upperLimit, String qualifier) {
        try {
            Integer result = Integer.parseInt(input);
            if (result > 0 && result <= upperLimit) {
                return true;
            } else {
                System.out.println("Invalid input! Input provided is an invalid " + qualifier + ".");
                return false;
            }
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid input! Input provided is not a valid integer.");
            return false;
        }
    }

    private boolean isValidDateInput(String input) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = df.parse(input);
            if (date.before(new Date())) {
                System.out.println("Invalid input! Date entered is in the past.");
                return false;
            } else {
                return true;
            }
        } catch (ParseException ex) {
            System.out.println("Invalid input! Input provided is not a valid YYYY-MM-DD format.");
            return false;
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

    private static void addAppointment(java.lang.String email, java.lang.String password, ws.client.ServiceProviderEntity serviceProviderEntity, ws.client.AppointmentEntity newAppointmentEntity) throws CreateNewAppointmentEntityException_Exception, InvalidLoginException_Exception, CustomerNotFoundException_Exception {
        ws.client.CustomerAppointmentWebService_Service service = new ws.client.CustomerAppointmentWebService_Service();
        ws.client.CustomerAppointmentWebService port = service.getCustomerAppointmentWebServicePort();
        port.addAppointment(email, password, serviceProviderEntity, newAppointmentEntity);
    }

    private static java.util.List<ws.client.AppointmentEntity> retrieveCustomerAppointments(java.lang.String email, java.lang.String password) throws InvalidLoginException_Exception, CustomerNotFoundException_Exception {
        ws.client.CustomerAppointmentWebService_Service service = new ws.client.CustomerAppointmentWebService_Service();
        ws.client.CustomerAppointmentWebService port = service.getCustomerAppointmentWebServicePort();
        return port.retrieveCustomerAppointments(email, password);
    }

    private static java.util.List<ws.client.ServiceProviderEntity> retrieveServiceProvidersByCategoryAndCity(java.lang.String category, java.lang.String city) throws ServiceProviderNotFoundException_Exception {
        ws.client.CustomerAppointmentWebService_Service service = new ws.client.CustomerAppointmentWebService_Service();
        ws.client.CustomerAppointmentWebService port = service.getCustomerAppointmentWebServicePort();
        return port.retrieveServiceProvidersByCategoryAndCity(category, city);
    }

    private static java.util.List<ws.client.BusinessCategoryEntity> retrieveAllBusinessCategories() {
        ws.client.CustomerAppointmentWebService_Service service = new ws.client.CustomerAppointmentWebService_Service();
        ws.client.CustomerAppointmentWebService port = service.getCustomerAppointmentWebServicePort();
        return port.retrieveAllBusinessCategories();
    }

    private static void cancelAppointment(java.lang.String email, java.lang.String password, java.lang.Long appointmentNo) throws AppointmentNotFoundException_Exception, UnauthorisedOperationException_Exception, InvalidLoginException_Exception, CustomerNotFoundException_Exception {
        ws.client.CustomerAppointmentWebService_Service service = new ws.client.CustomerAppointmentWebService_Service();
        ws.client.CustomerAppointmentWebService port = service.getCustomerAppointmentWebServicePort();
        port.cancelAppointment(email, password, appointmentNo);
    }

    private static void rateServiceProvider(java.lang.String email, java.lang.String password, java.lang.Long providerId, java.lang.Integer rating) throws ServiceProviderNotFoundException_Exception, InvalidLoginException_Exception {
        ws.client.CustomerAppointmentWebService_Service service = new ws.client.CustomerAppointmentWebService_Service();
        ws.client.CustomerAppointmentWebService port = service.getCustomerAppointmentWebServicePort();
        port.rateServiceProvider(email, password, providerId, rating);
    }

    private static ServiceProviderEntity retrieveServiceProviderByProviderId(java.lang.Long providerId) throws ServiceProviderNotFoundException_Exception {
        ws.client.CustomerAppointmentWebService_Service service = new ws.client.CustomerAppointmentWebService_Service();
        ws.client.CustomerAppointmentWebService port = service.getCustomerAppointmentWebServicePort();
        return port.retrieveServiceProviderByProviderId(providerId);
    }

}
