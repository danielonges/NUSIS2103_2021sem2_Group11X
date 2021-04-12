package ejb.session.stateless;

import entity.AppointmentEntity;
import entity.BusinessCategoryEntity;
import entity.RatingEntity;
import entity.ServiceProviderEntity;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import util.exception.InvalidLoginException;
import util.exception.ServiceProviderNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.enumeration.ServiceProviderStatus;
import static util.enumeration.ServiceProviderStatus.APPROVE;
import util.exception.BusinessCategoryNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.ServiceProviderAlreadyExistsException;
import util.exception.UnknownPersistenceException;
import util.security.CryptographicHelper;

/**
 *
 * @author danielonges
 */
@Local(ServiceProviderEntitySessionBeanLocal.class)
@Remote(ServiceProviderEntitySessionBeanRemote.class)
@Stateless
public class ServiceProviderEntitySessionBean implements ServiceProviderEntitySessionBeanRemote, ServiceProviderEntitySessionBeanLocal {

    @PersistenceContext(unitName = "EasyAppointmentSystem-ejbPU")
    private EntityManager em;

    @EJB
    private BusinessCategorySessionBeanLocal businessCategorySessionBeanLocal;
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public ServiceProviderEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    
    @Override
    public void createServiceProviderEntity(ServiceProviderEntity newServiceProviderEntity) throws ServiceProviderAlreadyExistsException, UnknownPersistenceException, InputDataValidationException {
        try
        {
            Set<ConstraintViolation<ServiceProviderEntity>> constraintViolations = validator.validate(newServiceProviderEntity);
        
            if(constraintViolations.isEmpty())
            {
                em.persist(newServiceProviderEntity);
                em.flush();

                
            }
            else
            {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }            
        }
        catch(PersistenceException ex)
        {
            if(ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException"))
            {
                if(ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException"))
                {
                    throw new ServiceProviderAlreadyExistsException();
                }
                else
                {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            }
            else
            {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }
    }

    @Override
    public ServiceProviderEntity retrieveServiceProviderEntityByProviderId(Long providerId) throws ServiceProviderNotFoundException {
        try {
            ServiceProviderEntity serviceProviderEntity = em.find(ServiceProviderEntity.class, providerId);
            if (serviceProviderEntity != null) {
                return serviceProviderEntity;
            } else {
                throw new ServiceProviderNotFoundException("Service provider not found!");
            }
        } catch (NoResultException ex) {
            throw new ServiceProviderNotFoundException("Service provider not found!");
        }
        // TODO: implement checking for null

    }

    @Override
    public void updateServiceProviderEntity(ServiceProviderEntity serviceProviderEntity) {
        // TODO: implement checking for null
        em.merge(serviceProviderEntity);
    }

    @Override
    public void deleteServiceProviderEntity(Long providerId) throws ServiceProviderNotFoundException {
        try {
            ServiceProviderEntity serviceProviderEntity = retrieveServiceProviderEntityByProviderId(providerId);
            em.remove(serviceProviderEntity);
        } catch (ServiceProviderNotFoundException ex) {
            throw new ServiceProviderNotFoundException("Service provider not found!");
        }
    }

    @Override
    public ServiceProviderEntity ServiceProviderLogin(String email, String password) throws InvalidLoginException {
        try {
            ServiceProviderEntity serviceProviderEntity = retrieveServiceProviderByEmail(email);
            String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + serviceProviderEntity.getSalt()));

            if (serviceProviderEntity.getPassword().equals(passwordHash) && serviceProviderEntity.getStatus() == APPROVE) {
                return serviceProviderEntity;
            } else {
                throw new InvalidLoginException("Email does not exist or invalid password!");
            }
        } catch (ServiceProviderNotFoundException ex) {
            throw new InvalidLoginException("Email does not exist or invalid password!");
        }
    }

    @Override
    public ServiceProviderEntity retrieveServiceProviderByEmail(String email) throws ServiceProviderNotFoundException {
        Query query = em.createQuery("SELECT s FROM ServiceProviderEntity s WHERE s.email = :inEmail");
        query.setParameter("inEmail", email);

        try {
            return (ServiceProviderEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new ServiceProviderNotFoundException("Service Provider Email " + email + " does not exist!");
        }
    }

    @Override
    public List<ServiceProviderEntity> retrieveListOfServiceProviders() throws ServiceProviderNotFoundException {

        Query query = em.createQuery("SELECT s FROM ServiceProviderEntity s ORDER BY s.providerId", ServiceProviderEntity.class);

        try {
            return (List<ServiceProviderEntity>) query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new ServiceProviderNotFoundException("Service Providers does not exist!");
        }

    }

    @Override
    public List<ServiceProviderEntity> retrieveServiceProviderByCategoryIdAndCity(Long categoryId, String city) throws ServiceProviderNotFoundException, BusinessCategoryNotFoundException {
        Query query = em.createQuery("SELECT s FROM ServiceProviderEntity s WHERE s.businessCategoryEntity = :businessCategoryEntity AND s.city = :city AND s.status = :status ORDER BY s.providerId");
        BusinessCategoryEntity businessCategoryEntity = businessCategorySessionBeanLocal.retrieveBusinessCategoryEntityByBusinessCategoryId(categoryId);
        query.setParameter("businessCategoryEntity", businessCategoryEntity);
        query.setParameter("city", city);
        query.setParameter("status", ServiceProviderStatus.APPROVE);

        try {
            return (List<ServiceProviderEntity>) query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new ServiceProviderNotFoundException("");
        }
    }

    @Override
    public List<ServiceProviderEntity> retrieveListOfServiceProvidersWithPendingApproval() throws ServiceProviderNotFoundException {
        Query query = em.createQuery("SELECT s FROM ServiceProviderEntity s WHERE s.status = :status");
        query.setParameter("status", ServiceProviderStatus.PENDING);

        try {
            return (List<ServiceProviderEntity>) query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new ServiceProviderNotFoundException("Service Providers does not exist!");
        }
    }

    public List<ServiceProviderEntity> retrieveListOfServiceProvidersNotBlocked() throws ServiceProviderNotFoundException {
        Query query = em.createQuery("SELECT s FROM ServiceProviderEntity s WHERE s.status != :status");
        query.setParameter("status", ServiceProviderStatus.BLOCK);

        try {
            return (List<ServiceProviderEntity>) query.getResultList();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new ServiceProviderNotFoundException("Service Providers does not exist!");
        }
    }

    @Override
    public ServiceProviderEntity retrieveListOfAppointments(Long serviceProviderId) throws ServiceProviderNotFoundException {
        ServiceProviderEntity currentServiceProviderEntity = em.find(ServiceProviderEntity.class, serviceProviderId);
        if (currentServiceProviderEntity != null) {
            currentServiceProviderEntity.getAppointments().size();
            return currentServiceProviderEntity;
        } else {
            throw new ServiceProviderNotFoundException("not found!");
        }
    }

//    public ServiceProviderEntity retrieveListOfPendingAppointments (Long serviceProviderId) throws ServiceProviderNotFoundException{
//        ServiceProviderEntity currentServiceProviderEntity = em.find(ServiceProviderEntity.class,serviceProviderId);
//        
//        Query query = em.createQuery("SELECT a FROM AppointmentEntity a WHERE a.serviceProvider =: serviceProvider AND a.isCancelled = 'FALSE'")
//    }
    @Override
    public List<AppointmentEntity> retrieveListOfPendingAppointments(Long serviceProviderId) throws ServiceProviderNotFoundException {
        ServiceProviderEntity currentServiceProviderEntity = em.find(ServiceProviderEntity.class, serviceProviderId);
        List<AppointmentEntity> tempList = currentServiceProviderEntity.getAppointments();
        List<AppointmentEntity> newList = new ArrayList<>();
        for (AppointmentEntity appointment : tempList) {
            Date currentDate = new Date();
            long diff_in_time = appointment.getDate().getTime() - currentDate.getTime();
            long diff_in_hours = (diff_in_time / (1000 * 60 * 60)) % 365;
            if (diff_in_hours >= -1 && appointment.getIsCancelled() == false) {
                newList.add(appointment);
            }
        }
        return newList;
    }

    public void updateServiceProviderRating(Long providerId, Integer rating) throws ServiceProviderNotFoundException {
        ServiceProviderEntity serviceProviderEntity = em.find(ServiceProviderEntity.class, providerId);
        List<RatingEntity> ratings = serviceProviderEntity.getRatings();
        RatingEntity ratingEntity = new RatingEntity(rating, serviceProviderEntity);
        ratings.add(ratingEntity);

        double avgRating = 0;
        // update overall rating
        for (RatingEntity r : ratings) {
            avgRating += r.getRating();
        }
        double size = ratings.size();
        BigDecimal overallRating = new BigDecimal(avgRating / size);

        serviceProviderEntity.setRatings(ratings);
        serviceProviderEntity.setOverallRating(overallRating);

//        try {
            em.merge(serviceProviderEntity);
//        em.flush();
//        
            em.persist(ratingEntity);
            em.flush();
//        } catch (ConstraintViolationException e) {
//            e.getConstraintViolations().forEach(err -> System.out.println(err));
//        }

    }
    
    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<ServiceProviderEntity>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }

}
