package easyappointmentsystemcustomerterminalclient;

import java.util.InputMismatchException;
import java.util.Scanner;
import ws.client.CustomerAlreadyExistsException_Exception;
import ws.client.CustomerEntity;
import ws.client.InputDataValidationException_Exception;
import ws.client.InvalidLoginException_Exception;
import ws.client.UnknownPersistenceException_Exception;

/**
 *
 * @author danielonges
 */
public class MainApp {

    private CustomerAppointmentModule customerAppointmentModule;

    private CustomerEntity currentCustomerEntity;

    public MainApp() {
        customerAppointmentModule = new CustomerAppointmentModule();
    }

    public void runApp() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            try {
                System.out.println("*** Welcome to Customer terminal ***\n");
                System.out.println("1: Registration");
                System.out.println("2: Login");
                System.out.println("3: Exit\n");

                response = 0;

                while (response < 1 || response > 3) {
                    System.out.print("> ");

                    response = sc.nextInt();

                    if (response == 1) {
                        // register
                        doRegisterCustomer();
                    } else if (response == 2) {
                        // login
                        try {
                            doLogin();
                            System.out.println("Login successful!\n");

                            menuMain();
                        } catch (InvalidLoginException_Exception ex) {
                            System.out.println("Failed to login! " + ex.getMessage() + "\n");
                        }

                    } else if (response == 3) {
                        break;
                    } else {
                        System.out.println("Invalid option, please try again!\n");
                    }
                }

                if (response == 3) {
                    break;
                }
            } catch (InputMismatchException | NumberFormatException ex) {
                System.out.println("Invalid input! Please try again.\n");
                sc.nextLine();
                continue;
            }

        }
    }

    private void doLogin() throws InvalidLoginException_Exception {
        Scanner sc = new Scanner(System.in);
        String email = "";
        String password = "";
        System.out.println("*** Customer terminal :: Login ***\n");
        System.out.print("Enter Email Address> ");
        email = sc.nextLine().trim();
        System.out.print("Enter password> ");
        password = sc.nextLine().trim();

        currentCustomerEntity = customerLogin(email, password);
        customerAppointmentModule.setCurrentCustomerEntity(currentCustomerEntity);
    }

    private void doRegisterCustomer() {
        Scanner sc = new Scanner(System.in);
        System.out.println("*** Customer terminal :: Register customer ***\n");
        CustomerEntity newCustomerEntity = new CustomerEntity();

        // set id number
        System.out.print("Enter identity number (must be valid Singapore ID)> ");
        String identityNo = sc.nextLine().trim();

        /*   while (!isValidIdentityNo(identityNo)) {
            System.out.print("Enter identity number (must be valid Singapore ID)> ");
            identityNo = sc.nextLine().trim();
        }*/
        newCustomerEntity.setIdentityNum(identityNo);

        // set first name
        System.out.print("Enter first name> ");
        String firstName = sc.nextLine().trim();

        while (firstName.length() == 0) {
            System.out.println("Invalid input! First name field must be present.");
            System.out.print("Enter first name> ");
            firstName = sc.nextLine().trim();
        }
        newCustomerEntity.setFirstName(firstName);

        // set last name
        System.out.print("Enter last name> ");
        String lastName = sc.nextLine().trim();

        while (lastName.length() == 0) {
            System.out.println("Invalid input! Last name field must be present.");
            System.out.print("Enter last name> ");
            lastName = sc.nextLine().trim();
        }
        newCustomerEntity.setLastName(lastName);

        // set gender
        System.out.print("Enter gender> ");
        String gender = sc.nextLine().trim();

        while (!isValidGender(gender)) {
            System.out.print("Enter gender> ");
            gender = sc.nextLine().trim();
        }
        newCustomerEntity.setGender((int) gender.charAt(0)); // use ascii value

        // set age
        System.out.print("Enter age> ");
        String age = sc.nextLine().trim();

        while (!isValidAge(age)) {
            System.out.print("Enter age> ");
            age = sc.nextLine().trim();
        }
        newCustomerEntity.setAge(Integer.parseInt(age));

        // set address
        System.out.print("Enter address> ");
        String address = sc.nextLine().trim();

        while (address.length() == 0) {
            System.out.println("Invalid input! Address field must be present.");
            System.out.print("Enter address> ");
            address = sc.nextLine().trim();
        }
        newCustomerEntity.setAddress(address);

        // set phone
        System.out.print("Enter phone (digits only, without spaces or hyphens) > ");
        String phone = sc.nextLine().trim();

        while (!isValidDigitInput(phone)) {
            System.out.print("Enter phone (digits only, without spaces or hyphens) > ");
            phone = sc.nextLine().trim();
        }
        newCustomerEntity.setPhone(phone);

        // set city
        System.out.print("Enter city> ");
        String city = sc.nextLine().trim();

        while (city.length() == 0) {
            System.out.println("Invalid input! City field must be present.");
            System.out.print("Enter city> ");
            city = sc.nextLine().trim();
        }
        newCustomerEntity.setCity(city);

        // set email
        System.out.print("Enter email> ");
        String email = sc.nextLine().trim();

        while (!isValidEmail(email)) {
            System.out.print("Enter email> ");
            email = sc.nextLine().trim();
        }
        newCustomerEntity.setEmail(email);

        // set password
        System.out.print("Enter password> ");
        String password = sc.nextLine().trim();

        while (!password.matches("\\d{6}")) {
            System.out.println("Invalid input! Password field must be a 6 digit number.");
            System.out.print("Enter password> ");
            password = sc.nextLine().trim();
        }
        newCustomerEntity.setPassword(password);

        try {
            registerCustomer(newCustomerEntity);
            System.out.println("New customer successfully registered!\n");
        } catch (CustomerAlreadyExistsException_Exception ex) {
            System.out.println("An error has occurred while registering the new customer!: The customer with the same ID number, phone number or email address already exist.\n");
        } catch (UnknownPersistenceException_Exception ex) {
            System.out.println("An unknown error has occurred while registering the new customer!: " + ex.getMessage() + "\n");
        } catch (InputDataValidationException_Exception ex) {
            System.out.println(ex.getMessage() + "\n");
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

    private boolean isValidIdentityNo(String input) {
        // regex taken from here: https://stackoverflow.com/questions/29743154/regular-expression-for-nric-fin-in-singapore
        String validationRegex = "[STFG]\\d{7}[A-Z]";
        if (input.matches(validationRegex)) {
            return true;
        } else {
            System.out.println("Invalid input! Identity number provided is not a valid Singapore ID");
            return false;
        }
    }

    private boolean isValidGender(String input) {
        // only supports M and F, is case sensitive
        if (input.length() == 0) {
            System.out.println("Invalid input! Gender field is empty.");
            return false;
        } else if (!(input.length() == 1 && (input.charAt(0) == 'M' || input.charAt(0) == 'F'))) {
            System.out.println("Invalid input! Invalid gender entered.");
            return false;
        } else {
            return true;
        }
    }

    private boolean isValidAge(String input) {
        if (!input.matches("[-+]?\\d+")) {
            System.out.println("Invalid input! Input is not a number.");
            return false;
        } else {
            Integer age = Integer.parseInt(input);
            if (age <= 0) {
                System.out.println("Invalid input! Age provided is not a positive integer.");
                return false;
            } else {
                return true;
            }
        }
    }

    private boolean isValidEmail(String input) {
        String validationRegex = "^[\\w!#$%&'*+/=?`{|}~^.-]+@[^\\W_][a-zA-Z0-9.-]*[^\\W_]$";
        if (input.matches(validationRegex)) {
            return true;
        } else {
            System.out.println("Invalid input! Invalid email entered.");
            return false;
        }
    }

    private void menuMain() {
        Scanner sc = new Scanner(System.in);
        Integer response = 0;

        while (true) {
            try {
                System.out.println("*** Customer terminal :: Main ***\n");
                System.out.println(String.format("You are login as %s %s.\n", currentCustomerEntity.getFirstName(), currentCustomerEntity.getLastName()));
                System.out.println("1: Search Operation");
                System.out.println("2: Add Appointment");
                System.out.println("3: View Appointments");
                System.out.println("4: Cancel Appointment");
                System.out.println("5: Rate Service Provider");
                System.out.println("6: Logout\n");
                response = 0;
                OUTER:
                while (response < 1 || response > 6) {
                    System.out.print("> ");
                    response = sc.nextInt();
                    switch (response) {
                        case 1:
                            System.out.println("*** Customer terminal :: Search operation ***\n");
                            customerAppointmentModule.doSearchOperation(false);
                            break;
                        case 2:
                            System.out.println("*** Customer terminal :: Add appointment ***\n");
                            customerAppointmentModule.doSearchOperation(true);
                            break;
                        case 3:
                            System.out.println("*** Customer terminal :: View appointments ***\n");
                            customerAppointmentModule.doViewAppointments();
                            break;
                        case 4:
                            System.out.println("*** Customer terminal :: Cancel appointment ***\n");
                            customerAppointmentModule.doCancelAppointment();
                            break;
                        case 5:
                            System.out.println("*** Customer terminal :: Rate service provider ***\n");
                            customerAppointmentModule.doRateProvider();
                            break;
                        case 6:
                            break OUTER;
                        default:
                            System.out.println("Invalid option, please try again!\n");
                            break;
                    }
                }
                if (response == 6) {
                    currentCustomerEntity = null;
                    break;
                }
            } catch (InputMismatchException | NumberFormatException ex) {
                System.out.println("Invalid option, please try again!\n");
                sc.nextLine();
                continue;
            }

        }
    }

    private static CustomerEntity customerLogin(java.lang.String email, java.lang.String password) throws InvalidLoginException_Exception {
        ws.client.CustomerAppointmentWebService_Service service = new ws.client.CustomerAppointmentWebService_Service();
        ws.client.CustomerAppointmentWebService port = service.getCustomerAppointmentWebServicePort();
        return port.customerLogin(email, password);
    }

    private static void registerCustomer(ws.client.CustomerEntity newCustomerEntity) throws CustomerAlreadyExistsException_Exception, InputDataValidationException_Exception, UnknownPersistenceException_Exception {
        ws.client.CustomerAppointmentWebService_Service service = new ws.client.CustomerAppointmentWebService_Service();
        ws.client.CustomerAppointmentWebService port = service.getCustomerAppointmentWebServicePort();
        port.registerCustomer(newCustomerEntity);
    }

}
