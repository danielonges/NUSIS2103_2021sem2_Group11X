package easyappointmentsystemcustomerterminalclient;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import ws.client.AppointmentEntity;
import ws.client.AppointmentNotFoundException_Exception;
import ws.client.BusinessCategoryEntity;
import ws.client.BusinessCategoryNotFoundException_Exception;
import ws.client.CreateNewAppointmentEntityException_Exception;
import ws.client.CustomerEntity;
import ws.client.CustomerNotFoundException_Exception;
import ws.client.InvalidLoginException_Exception;
import ws.client.ServiceProviderEntity;
import ws.client.ServiceProviderNotFoundException_Exception;
import ws.client.ServiceProviderStatus;
import ws.client.UnauthorisedOperationException_Exception;

/**
 *
 * @author danielonges
 */
public class CustomerAppointmentModule {

    Scanner sc;

    CustomerEntity currentCustomerEntity;

    public CustomerAppointmentModule() {
        sc = new Scanner(System.in);
    }

    public void setCurrentCustomerEntity(CustomerEntity customerEntity) {
        this.currentCustomerEntity = customerEntity;
    }

    public void doSearchOperation(boolean doAddAppointment) {

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

        // need to convert to 0 based index
        Long categoryId = businessCategories.get(Integer.parseInt(categoryInput) - 1).getCategoryId();

        // enter city
        System.out.print("Enter city> ");
        String city = sc.nextLine().trim();

        while (city.length() == 0) {
            System.out.println("No input entered!");
            System.out.print("Enter city> ");
            city = sc.nextLine().trim();
        }

        // enter date
        // will not allow date that is in the past as well
        System.out.print("Enter date (YYYY-MM-DD)> ");
        String dateInput = sc.nextLine().trim();
        Date dateToSearch = null;

        while (!isValidDateInput(dateInput)) {
            System.out.print("Enter date (YYYY-MM-DD)> ");
            dateInput = sc.nextLine().trim();
        }

        // implement logic to filter out based on date
        try {
            dateToSearch = new SimpleDateFormat("yyyy-MM-dd").parse(dateInput);
        } catch (ParseException ex) {
            // this shouldn't happen because input is already validated
            ex.printStackTrace();
        }

        List<ServiceProviderEntity> approvedServiceProviders = null;

        try {
            // retrieve list of service providers
            approvedServiceProviders
                    = retrieveServiceProvidersByCategoryIdAndCity(categoryId, city)
                            .stream()
                            .filter(sp -> sp.getStatus() == ServiceProviderStatus.APPROVE)
                            .collect(Collectors.toList());
            boolean hasAvailableAppointment = false;
            System.out.printf("\n%20s | %20s | %20s | %20s | %20s \n", "Service Provider Id", "Name", "First available time", "Address", "Overall rating");
            for (ServiceProviderEntity sp : approvedServiceProviders) {
                List<Integer> availableTimings = findAvailableTimingsOnDate(sp, dateToSearch);
                if (availableTimings.size() > 0) {
                    hasAvailableAppointment = true;
                    System.out.printf("\n%20s | %20s | %20s | %20s | %20s ", sp.getProviderId(), sp.getName(), String.format("%02d:30", availableTimings.get(0)), sp.getAddress(), sp.getOverallRating());
                }
            }
            if (!hasAvailableAppointment) {
                System.out.print("\nNo available service providers found on " + new SimpleDateFormat("yyyy-MM-dd").format(dateToSearch) + ".");
                System.out.println("\n");
                if (doAddAppointment) {
                    System.out.println("Returning to main menu now...\n");
                }
                return;
            }
            System.out.println("\n");
        } catch (ServiceProviderNotFoundException_Exception ex) {
            System.out.println("Unable to find service providers: " + ex.getMessage() + "\n");
        } catch (BusinessCategoryNotFoundException_Exception ex) {
            System.out.println("Unable to find business category: " + ex.getMessage() + "\n");
        }

        if (doAddAppointment) {
            doAddAppointment(approvedServiceProviders, dateToSearch);
        }
    }

    private List<Integer> findAvailableTimingsOnDate(ServiceProviderEntity serviceProvider, Date date) throws ServiceProviderNotFoundException_Exception {
        List<Integer> availableTimings = getAvailableHoursInADay();
        List<AppointmentEntity> appointments = retrieveServiceProviderAppointments(serviceProvider.getProviderId());

        for (AppointmentEntity a : appointments) {
            if (date.getDate() == a.getDate().getDay()
                    && date.getMonth() + 1 == a.getDate().getMonth()
                    && date.getYear() + 1900 == a.getDate().getYear()
                    && availableTimings.contains(a.getDate().getHour())
                    && !a.isIsCancelled()) {
                int index = availableTimings.indexOf(a.getDate().getHour());
                availableTimings.remove(index);
            }
        }
        Collections.sort(availableTimings);
        return availableTimings;
    }

    private List<Integer> getAvailableHoursInADay() {
        List<Integer> availableHoursInADay = new ArrayList<>();
        for (int i = 8; i <= 17; i++) {
            availableHoursInADay.add(i);
        }
        return availableHoursInADay;
    }

    public void doAddAppointment(List<ServiceProviderEntity> approvedServiceProviders, Date dateToSearch) {
        System.out.println("Enter 0 to go back to the previous menu.");
        System.out.print("Service provider Id> ");
        String input = sc.nextLine().trim();

        while (!isValidDigitInput(input)) {
            System.out.print("Service provider Id> ");
            input = sc.nextLine().trim();
        }

        Long providerId = Long.parseLong(input);

        if (providerId == 0) {
            return;
        }

        try {
            ServiceProviderEntity serviceProvider = retrieveServiceProviderByProviderId(providerId);

            List<Integer> availableTimings = findAvailableTimingsOnDate(serviceProvider, dateToSearch);

            if (availableTimings.size() == 0) {
                System.out.println("Service provider with Id provided is not free on date " + new SimpleDateFormat("yyyy-MM-dd").format(dateToSearch) + ".\n");
                return;
            }
            System.out.println("Available appointment slots:");
            System.out.println(availableTimings.stream()
                    .map(hour -> String.format("%02d:30", hour))
                    .collect(Collectors.joining(" | ")));
            System.out.println("Enter 0 to go back to the previous menu.");
            System.out.print("Enter time> ");
            String time = sc.nextLine().trim();

            int hour;
            int minute;

            while (true) {

                if (!isValidTime(time)) {
                    System.out.print("Enter time> ");
                    time = sc.nextLine().trim();
                } else {
                    hour = Integer.parseInt(time.split(":")[0]);
                    minute = Integer.parseInt(time.split(":")[1]);
                    if (!availableTimings.contains(hour)) {
                        System.out.println("Service provider is not available during that time!");
                        System.out.print("Enter time> ");
                        time = sc.nextLine().trim();
                    } else {
                        break;
                    }
                }
            }

            Date now = new Date();
            now.setSeconds(0);
            
            Date appointmentDate = new Date(dateToSearch.getTime());
            appointmentDate.setHours(hour);
            appointmentDate.setMinutes(minute);

            Date twoHoursBeforeAppointmentDate = new Date(dateToSearch.getTime());
            twoHoursBeforeAppointmentDate.setHours(hour - 2);
            twoHoursBeforeAppointmentDate.setMinutes(minute);

            if (now.before(appointmentDate) && now.after(twoHoursBeforeAppointmentDate)) {
                System.out.println("Appointments must be made at least two hours in advance!\n");
            } else if (now.after(appointmentDate)) {
                System.out.println("Appointment time has already past!\n");
            } else {
                AppointmentEntity appointmentEntity = new AppointmentEntity();
                appointmentEntity.setAppointmentNo(Long.parseLong(String.format("%02d%02d%02d%02d", dateToSearch.getMonth() + 1, dateToSearch.getDate(), hour, minute)));
                appointmentEntity.setBusinessCategory(serviceProvider.getBusinessCategory().getCategory());
                GregorianCalendar c = new GregorianCalendar();

                try {
                    c.setTime(new Date(dateToSearch.getYear(), dateToSearch.getMonth(), dateToSearch.getDate(), hour, minute));
                    XMLGregorianCalendar xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
                    appointmentEntity.setDate(xmlDate);
                } catch (DatatypeConfigurationException ex) {
                    ex.printStackTrace();
                }

                addAppointment(currentCustomerEntity.getEmail(), currentCustomerEntity.getPassword(), serviceProvider.getProviderId(), appointmentEntity);
                System.out.println(String.format("The appointment with %s at %s on %s is confirmed.\n", serviceProvider.getName(), time, new SimpleDateFormat("yyyy-MM-dd").format(dateToSearch)));
            }

        } catch (ServiceProviderNotFoundException_Exception ex) {
            System.out.println("Service provider with Id provided does not exist!\n");
        } catch (CreateNewAppointmentEntityException_Exception | CustomerNotFoundException_Exception | InvalidLoginException_Exception ex) {
            System.out.println("An error occured while creating the appointment: " + ex.getMessage());
        }

    }

    private boolean isValidTime(String input) {
        String validationRegex = "\\d{1,2}:\\d{2}";
        if (!input.matches(validationRegex)) {
            System.out.println("Invalid date provided!");
            return false;
        } else {
            return true;
        }
    }

    public void doViewAppointments() {

        try {
            List<AppointmentEntity> appointmentEntities = retrieveCustomerAppointments(currentCustomerEntity.getEmail(), currentCustomerEntity.getPassword());

            System.out.println(String.format("Appointments for %s %s:\n", currentCustomerEntity.getFirstName(), currentCustomerEntity.getLastName()));
            boolean hasAppointments = false;
            if (appointmentEntities.size() > 0) {

                System.out.println(String.format("%20s | %20s | %20s | %10s | %20s", "Name", "Business Category", "Date", "Time", "Appointment no."));

                for (AppointmentEntity a : appointmentEntities) {
                    if (!a.isIsCancelled()) {
                        hasAppointments = true;
                        System.out.println(String.format("%20s | %20s | %20s | %10s | %20s", a.getServiceProvider().getName(), a.getBusinessCategory(), String.format("%04d-%02d-%02d", a.getDate().getYear(), a.getDate().getMonth(), a.getDate().getDay()), String.format("%02d:%02d", a.getDate().getHour(), a.getDate().getMinute()), a.getAppointmentNo()));
                    }
                }
            }

            if (!hasAppointments) {
                System.out.println("You don't have any appointments!");
            }

            System.out.println();

        } catch (InvalidLoginException_Exception | CustomerNotFoundException_Exception ex) {
            System.out.println("An error occured while performing the operation: " + ex.getMessage());
        }

    }

    public void doCancelAppointment() {
        doViewAppointments();
        

        while (true) {

            // this is the appointment number
            System.out.println("Enter 0 to go back to the previous menu.");
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
                AppointmentEntity appointmentEntity = retrieveAppointmentByAppointmentNo(input);

                Date oneDayAfterNow = new Date();
                oneDayAfterNow.setDate(oneDayAfterNow.getDate() + 1);

                Date dateToday = new Date(new Date().getYear(), new Date().getMonth(), new Date().getDate());

                if (appointmentEntity.getDate().toGregorianCalendar().getTime().before(dateToday)) {
                    System.out.println("Appointment is already over!\n");
                } else if (appointmentEntity.getDate().toGregorianCalendar().getTime().before(oneDayAfterNow)) {
                    System.out.println("Cannot cancel appointment less than 24 hours before!\n");
                } else if (appointmentEntity.isIsCancelled()) {
                    System.out.println("Appointment has already been cancelled!\n");
                } else {
                    cancelAppointment(currentCustomerEntity.getEmail(), currentCustomerEntity.getPassword(), input);
                    System.out.println("Appointment " + appointmentNo + " has been successfully cancelled.\n");
                    
                }

            } catch (AppointmentNotFoundException_Exception | CustomerNotFoundException_Exception | InvalidLoginException_Exception | UnauthorisedOperationException_Exception ex) {
                System.out.println("An error occured while performing the operation: " + ex.getMessage() + "\n");
            }
        }
    }

    public void doRateProvider() {
        System.out.print("Enter service provider Id> ");
        String providerIdStr = sc.nextLine();
        ServiceProviderEntity serviceProviderEntity = null;

        // check if service provider with id exists
        while (true) {
            while (!isValidDigitInput(providerIdStr)) {
                System.out.print("Enter service provider Id> ");
                providerIdStr = sc.nextLine();
            }

            Long providerId = Long.parseLong(providerIdStr);

            try {
                serviceProviderEntity = retrieveServiceProviderByProviderId(providerId);
                if (serviceProviderEntity != null) {
                    break;
                }

            } catch (ServiceProviderNotFoundException_Exception ex) {
                System.out.println("Service provider with Id " + providerId + " does not exist!");
                System.out.print("Enter service provider Id> ");
                providerIdStr = sc.nextLine();
            }
        }

        try {
            List<AppointmentEntity> appointmentEntities = retrieveCustomerAppointments(currentCustomerEntity.getEmail(), currentCustomerEntity.getPassword());

            final ServiceProviderEntity[] providerWrapper = new ServiceProviderEntity[]{serviceProviderEntity};
            Date oneHourBeforeNow = new Date();
            oneHourBeforeNow.setHours(oneHourBeforeNow.getHours() - 1);

            // check whether appointment is not cancelled, appointment has completed (1 hr before now) and the service provider for that appt is the same as the one being rated now
            boolean canRate = appointmentEntities.stream().anyMatch(a
                    -> !a.isIsCancelled()
                    && a.getDate().toGregorianCalendar().getTime().before(oneHourBeforeNow)
                    && a.getServiceProvider().getProviderId() == providerWrapper[0].getProviderId());

            if (canRate) {
                System.out.print("Enter rating (between 1 to 5) > ");
                String ratingStr = sc.nextLine().trim();

                while (!isValidIntegerInput(ratingStr, 5, "rating")) {
                    System.out.print("Enter rating (between 1 to 5) > ");
                    ratingStr = sc.nextLine().trim();
                }

                Integer rating = Integer.parseInt(ratingStr);

                rateServiceProvider(currentCustomerEntity.getEmail(), currentCustomerEntity.getPassword(), serviceProviderEntity.getProviderId(), rating);

                System.out.println("Service provider rated successfully!\n");
            } else {
                System.out.println("You have not had a past appointment with this service provider before; cannot rate!\n");
            }

        } catch (InvalidLoginException_Exception | ServiceProviderNotFoundException_Exception | CustomerNotFoundException_Exception ex) {
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
            Date dateToday = new Date(new Date().getYear(), new Date().getMonth(), new Date().getDate());

            if (date.before(dateToday)) {
                System.out.println("Invalid input! Date entered is in the past.");
                return false;
            } else if (date.getDay() == 0) {
                System.out.println("Service providers don't operate on Sunday.");
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

    private static void addAppointment(java.lang.String email, java.lang.String password, java.lang.Long providerId, ws.client.AppointmentEntity newAppointmentEntity) throws InvalidLoginException_Exception, CustomerNotFoundException_Exception, CreateNewAppointmentEntityException_Exception {
        ws.client.CustomerAppointmentWebService_Service service = new ws.client.CustomerAppointmentWebService_Service();
        ws.client.CustomerAppointmentWebService port = service.getCustomerAppointmentWebServicePort();
        port.addAppointment(email, password, providerId, newAppointmentEntity);
    }

    private static void cancelAppointment(java.lang.String email, java.lang.String password, java.lang.Long appointmentNo) throws AppointmentNotFoundException_Exception, UnauthorisedOperationException_Exception, CustomerNotFoundException_Exception, InvalidLoginException_Exception {
        ws.client.CustomerAppointmentWebService_Service service = new ws.client.CustomerAppointmentWebService_Service();
        ws.client.CustomerAppointmentWebService port = service.getCustomerAppointmentWebServicePort();
        port.cancelAppointment(email, password, appointmentNo);
    }

    private static void rateServiceProvider(java.lang.String email, java.lang.String password, java.lang.Long providerId, java.lang.Integer rating) throws InvalidLoginException_Exception, ServiceProviderNotFoundException_Exception {
        ws.client.CustomerAppointmentWebService_Service service = new ws.client.CustomerAppointmentWebService_Service();
        ws.client.CustomerAppointmentWebService port = service.getCustomerAppointmentWebServicePort();
        port.rateServiceProvider(email, password, providerId, rating);
    }

    private static java.util.List<ws.client.BusinessCategoryEntity> retrieveAllBusinessCategories() {
        ws.client.CustomerAppointmentWebService_Service service = new ws.client.CustomerAppointmentWebService_Service();
        ws.client.CustomerAppointmentWebService port = service.getCustomerAppointmentWebServicePort();
        return port.retrieveAllBusinessCategories();
    }

    private static java.util.List<ws.client.AppointmentEntity> retrieveCustomerAppointments(java.lang.String email, java.lang.String password) throws CustomerNotFoundException_Exception, InvalidLoginException_Exception {
        ws.client.CustomerAppointmentWebService_Service service = new ws.client.CustomerAppointmentWebService_Service();
        ws.client.CustomerAppointmentWebService port = service.getCustomerAppointmentWebServicePort();
        return port.retrieveCustomerAppointments(email, password);
    }

    private static ServiceProviderEntity retrieveServiceProviderByProviderId(java.lang.Long providerId) throws ServiceProviderNotFoundException_Exception {
        ws.client.CustomerAppointmentWebService_Service service = new ws.client.CustomerAppointmentWebService_Service();
        ws.client.CustomerAppointmentWebService port = service.getCustomerAppointmentWebServicePort();
        return port.retrieveServiceProviderByProviderId(providerId);
    }

    private static java.util.List<ws.client.ServiceProviderEntity> retrieveServiceProvidersByCategoryIdAndCity(java.lang.Long categoryId, java.lang.String city) throws ServiceProviderNotFoundException_Exception, BusinessCategoryNotFoundException_Exception {
        ws.client.CustomerAppointmentWebService_Service service = new ws.client.CustomerAppointmentWebService_Service();
        ws.client.CustomerAppointmentWebService port = service.getCustomerAppointmentWebServicePort();
        return port.retrieveServiceProvidersByCategoryIdAndCity(categoryId, city);
    }

    private static java.util.List<ws.client.AppointmentEntity> retrieveServiceProviderAppointments(java.lang.Long providerId) throws ServiceProviderNotFoundException_Exception {
        ws.client.CustomerAppointmentWebService_Service service = new ws.client.CustomerAppointmentWebService_Service();
        ws.client.CustomerAppointmentWebService port = service.getCustomerAppointmentWebServicePort();
        return port.retrieveServiceProviderAppointments(providerId);
    }

    private static AppointmentEntity retrieveAppointmentByAppointmentNo(java.lang.Long appointmentNo) throws AppointmentNotFoundException_Exception {
        ws.client.CustomerAppointmentWebService_Service service = new ws.client.CustomerAppointmentWebService_Service();
        ws.client.CustomerAppointmentWebService port = service.getCustomerAppointmentWebServicePort();
        return port.retrieveAppointmentByAppointmentNo(appointmentNo);
    }

}
