package easyappointmentsystemcustomerterminalclient;

import ws.client.AppointmentNotFoundException_Exception;
import ws.client.CreateNewAppointmentEntityException_Exception;
import ws.client.CustomerEntity;
import ws.client.CustomerNotFoundException_Exception;
import ws.client.InvalidLoginException_Exception;
import ws.client.ServiceProviderNotFoundException_Exception;
import ws.client.UnauthorisedOperationException_Exception;

/**
 *
 * @author danielonges
 */
public class EasyAppointmentSystemCustomerTerminalClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MainApp mainApp = new MainApp();
        mainApp.runApp();
    }    
}
