package ejb.session.stateless;

import entity.CustomerEntity;
import java.util.Set;
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
import util.exception.CustomerAlreadyExistsException;
import util.exception.CustomerNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginException;
import util.exception.UnknownPersistenceException;
import util.security.CryptographicHelper;

/**
 *
 * @author danielonges
 */
@Local(CustomerEntitySessionBeanLocal.class)
@Remote(CustomerEntitySessionBeanRemote.class)
@Stateless
public class CustomerEntitySessionBean implements CustomerEntitySessionBeanRemote, CustomerEntitySessionBeanLocal {

    @PersistenceContext(unitName = "EasyAppointmentSystem-ejbPU")
    private EntityManager em;

    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public CustomerEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public Long createCustomerEntity(CustomerEntity newCustomerEntity) throws CustomerAlreadyExistsException, UnknownPersistenceException, InputDataValidationException {
        try {

            Set<ConstraintViolation<CustomerEntity>> constraintViolations = validator.validate(newCustomerEntity);

            if (constraintViolations.isEmpty()) {
                em.persist(newCustomerEntity);
                em.flush();

                return newCustomerEntity.getCustomerId();
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        } catch (PersistenceException ex) {
            if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                    throw new CustomerAlreadyExistsException();
                } else {
                    throw new UnknownPersistenceException(ex.getMessage());
                }
            } else {
                throw new UnknownPersistenceException(ex.getMessage());
            }
        }

    }

    @Override
    public CustomerEntity retrieveCustomerEntityByCustomerId(Long customerId) throws CustomerNotFoundException {
        try {
            CustomerEntity customerEntity = em.find(CustomerEntity.class, customerId);
            if (customerEntity != null) {
                return customerEntity;
            } else {
                throw new CustomerNotFoundException("Customer not found!");
            }

        } catch (NoResultException ex) {
            throw new CustomerNotFoundException("Customer not found!");
        }
        // TODO: implement checking for null

    }

    @Override
    public CustomerEntity retrieveCustomerByEmail(String email) throws CustomerNotFoundException {
        Query query = em.createQuery("SELECT c FROM CustomerEntity c WHERE c.email = :inEmail");
        query.setParameter("inEmail", email);

        try {
            return (CustomerEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new CustomerNotFoundException("Customer with Email " + email + " does not exist!");
        }
    }

    @Override
    public void updateCustomerEntity(CustomerEntity customerEntity) {
        // TODO: implement checking for null
        em.merge(customerEntity);
    }

    @Override
    public void deleteCustomerEntity(Long customerId) throws CustomerNotFoundException {
        try {
            CustomerEntity customerEntity = retrieveCustomerEntityByCustomerId(customerId);
            em.remove(customerEntity);
        } catch (CustomerNotFoundException ex) {
            throw new CustomerNotFoundException("Customer not found!");
        }
    }

    @Override
    public CustomerEntity retrieveCustomerAppointments(Long customerId) throws CustomerNotFoundException {
        try {
            CustomerEntity currentCustomerEntity = em.find(CustomerEntity.class, customerId);
            currentCustomerEntity.getAppointments().size();
            return currentCustomerEntity;
        } catch (NoResultException | NullPointerException ex) {
            throw new CustomerNotFoundException("Customer not found!");
        }
    }

    @Override
    public CustomerEntity customerLogin(String email, String password) throws InvalidLoginException {
        try {
            CustomerEntity customerEntity = retrieveCustomerByEmail(email);
            String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + customerEntity.getSalt()));
            if (customerEntity.getPassword().equals(passwordHash)) {
                return customerEntity;
            } else {
                throw new InvalidLoginException("Email does not exist or invalid password!");
            }
        } catch (CustomerNotFoundException ex) {
            throw new InvalidLoginException("Email does not exist or invalid password!");
        }
    }

    @Override
    public CustomerEntity customerLoginHash(String email, String hashPassword) throws InvalidLoginException {
        try {
            CustomerEntity customerEntity = retrieveCustomerByEmail(email);
            if (customerEntity.getPassword().equals(hashPassword)) {
                return customerEntity;
            } else {
                throw new InvalidLoginException("Email does not exist or invalid password!");
            }
        } catch (CustomerNotFoundException ex) {
            throw new InvalidLoginException("Email does not exist or invalid password!");
        }
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<CustomerEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
