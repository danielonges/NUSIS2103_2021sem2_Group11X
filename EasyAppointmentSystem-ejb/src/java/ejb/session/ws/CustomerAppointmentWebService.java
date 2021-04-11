/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.ws;

import ejb.session.stateless.AppointmentEntitySessionBeanLocal;
import ejb.session.stateless.BusinessCategorySessionBean;
import ejb.session.stateless.BusinessCategorySessionBeanLocal;
import ejb.session.stateless.CustomerEntitySessionBeanLocal;
import ejb.session.stateless.ServiceProviderEntitySessionBeanLocal;
import entity.AppointmentEntity;
import entity.BusinessCategoryEntity;
import entity.CustomerEntity;
import entity.ServiceProviderEntity;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import util.exception.AppointmentNotFoundException;
import util.exception.BusinessCategoryNotFoundException;
import util.exception.CreateNewAppointmentEntityException;
import util.exception.CustomerAlreadyExistsException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginException;
import util.exception.ServiceProviderNotFoundException;
import util.exception.UnauthorisedOperationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author danielonges
 */
@WebService(serviceName = "customerAppointmentWebService")
@Stateless
public class CustomerAppointmentWebService {

    @EJB
    private CustomerEntitySessionBeanLocal customerEntitySessionBeanLocal;
    @EJB
    private AppointmentEntitySessionBeanLocal appointmentEntitySessionBeanLocal;
    @EJB
    private ServiceProviderEntitySessionBeanLocal serviceProviderEntitySessionBeanLocal;
    @EJB
    private BusinessCategorySessionBeanLocal businessCategorySessionBeanLocal;

    @WebMethod(operationName = "customerLogin")
    public CustomerEntity customerLogin(@WebParam(name = "email") String email,
            @WebParam(name = "password") String password)
            throws InvalidLoginException {
        CustomerEntity customerEntity = customerEntitySessionBeanLocal.customerLogin(email, password);

        return customerEntity;
    }

    @WebMethod(operationName = "registerCustomer")
    public void registerCustomer(@WebParam(name = "newCustomerEntity") CustomerEntity newCustomerEntity)
            throws CustomerAlreadyExistsException, InputDataValidationException, UnknownPersistenceException {
        customerEntitySessionBeanLocal.createCustomerEntity(newCustomerEntity);
    }

    @WebMethod(operationName = "retrieveCustomerAppointments")
    public List<AppointmentEntity> retrieveCustomerAppointments(@WebParam(name = "email") String email,
            @WebParam(name = "password") String password)
            throws CustomerNotFoundException, InvalidLoginException {
        // authentication needs to be performed
        CustomerEntity customerEntity = customerEntitySessionBeanLocal.customerLoginHash(email, password);
        customerEntity = customerEntitySessionBeanLocal.retrieveCustomerAppointments(customerEntity.getCustomerId());
        return customerEntity.getAppointments();
    }
    
    @WebMethod(operationName = "retrieveServiceProviderAppointments")
    public List<AppointmentEntity> retrieveServiceProviderAppointments(@WebParam(name = "providerId") Long providerId) throws ServiceProviderNotFoundException {
        ServiceProviderEntity serviceProviderEntity = serviceProviderEntitySessionBeanLocal.retrieveListOfAppointments(providerId);
        return serviceProviderEntity.getAppointments();
    }

    @WebMethod(operationName = "retrieveServiceProvidersByCategoryIdAndCity")
    public List<ServiceProviderEntity> retrieveServiceProvidersByCategoryIdAndCity(
            @WebParam(name = "categoryId") Long categoryId,
            @WebParam(name = "city") String city) throws ServiceProviderNotFoundException, BusinessCategoryNotFoundException {
        List<ServiceProviderEntity> serviceProviders = serviceProviderEntitySessionBeanLocal.retrieveServiceProviderByCategoryIdAndCity(categoryId, city);
        return serviceProviders;
    }

    @WebMethod(operationName = "addAppointment")
    public void addAppointment(
            @WebParam(name = "email") String email,
            @WebParam(name = "password") String password,
            @WebParam(name = "providerId") Long providerId,
            @WebParam(name = "newAppointmentEntity") AppointmentEntity newAppointmentEntity)
            throws CustomerNotFoundException, InvalidLoginException, CreateNewAppointmentEntityException {
        // authentication needs to be performed
        CustomerEntity customerEntity = customerEntitySessionBeanLocal.customerLoginHash(email, password);

        Long appointmentId = appointmentEntitySessionBeanLocal.createAppointmentEntity(customerEntity.getCustomerId(), providerId, newAppointmentEntity);
        // update appointment no manually
        AppointmentEntity appointmentEntity = null;
        try {
            appointmentEntity = appointmentEntitySessionBeanLocal.retrieveAppointmentEntityByAppointmentId(appointmentId);
        } catch (AppointmentNotFoundException ex) {
            // this should not happen
            ex.printStackTrace();
        }
        appointmentEntity.setAppointmentNo(Long.parseLong(appointmentEntity.getAppointmentId() + String.format("%08d", appointmentEntity.getAppointmentNo())));
        appointmentEntitySessionBeanLocal.updateAppointmentEntity(appointmentEntity);
    }

    @WebMethod(operationName = "cancelAppointment")
    public void cancelAppointment(
            @WebParam(name = "email") String email,
            @WebParam(name = "password") String password,
            @WebParam(name = "appointmentNo") Long appointmentNo)
            throws InvalidLoginException, CustomerNotFoundException, AppointmentNotFoundException, UnauthorisedOperationException {
        // authentication needs to be performed

        CustomerEntity customerEntity = customerEntitySessionBeanLocal.customerLoginHash(email, password);

        appointmentEntitySessionBeanLocal.cancelAppointmentByCustomerId(customerEntity.getCustomerId(), appointmentNo);
    }

    @WebMethod(operationName = "rateServiceProvider")
    public void rateServiceProvider(
            @WebParam(name = "email") String email,
            @WebParam(name = "password") String password,
            @WebParam(name = "providerId") Long providerId,
            @WebParam(name = "rating") Integer rating) throws InvalidLoginException, ServiceProviderNotFoundException {
        // authentication needs to be performed
        CustomerEntity customerEntity = customerEntitySessionBeanLocal.customerLoginHash(email, password);

        serviceProviderEntitySessionBeanLocal.updateServiceProviderRating(providerId, rating);
    }

    @WebMethod(operationName = "retrieveAllBusinessCategories")
    public List<BusinessCategoryEntity> retrieveAllBusinessCategories() {
        return businessCategorySessionBeanLocal.retrieveAllBusinessCategories();
    }

    @WebMethod(operationName = "retrieveServiceProviderByProviderId")
    public ServiceProviderEntity retrieveServiceProviderByProviderId(@WebParam(name = "providerId") Long providerId)
            throws ServiceProviderNotFoundException {
        return serviceProviderEntitySessionBeanLocal.retrieveServiceProviderEntityByProviderId(providerId);
    }
    
    @WebMethod(operationName = "retrieveAppointmentByAppointmentNo")
    public AppointmentEntity retrieveAppointmentByAppointmentNo(@WebParam(name = "appointmentNo") Long appointmentNo) throws AppointmentNotFoundException {
        return appointmentEntitySessionBeanLocal.retrieveAppointmentEntityByAppointmentNo(appointmentNo);
    }

}
