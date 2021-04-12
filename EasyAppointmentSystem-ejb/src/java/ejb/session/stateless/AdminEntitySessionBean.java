package ejb.session.stateless;

import util.exception.AdminNotFoundException;
import entity.AdminEntity;
import java.util.Set;
import util.exception.InvalidLoginException;
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
import util.exception.AdminAlreadyExistsException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.security.CryptographicHelper;

/**
 *
 * @author danielonges
 */
@Local(AdminEntitySessionBeanLocal.class)
@Remote(AdminEntitySessionBeanRemote.class)
@Stateless
public class AdminEntitySessionBean implements AdminEntitySessionBeanRemote, AdminEntitySessionBeanLocal {

    @PersistenceContext(unitName = "EasyAppointmentSystem-ejbPU")
    private EntityManager em;
    private final ValidatorFactory validatorFactory;
    private final Validator validator;
    
    public AdminEntitySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    

    @Override
    public Long createAdminEntity(AdminEntity newAdminEntity) throws AdminAlreadyExistsException, UnknownPersistenceException,InputDataValidationException {
         
            Set<ConstraintViolation<AdminEntity>> constraintViolations = validator.validate(newAdminEntity);

            if (constraintViolations.isEmpty()) {
                try {
                    em.persist(newAdminEntity);
                    em.flush();

                    return newAdminEntity.getAdminId();

                } catch (PersistenceException ex) {
                    if (ex.getCause() != null && ex.getCause().getClass().getName().equals("org.eclipse.persistence.exceptions.DatabaseException")) {
                        if (ex.getCause().getCause() != null && ex.getCause().getCause().getClass().getName().equals("java.sql.SQLIntegrityConstraintViolationException")) {
                            throw new AdminAlreadyExistsException();
                        } else {
                            throw new UnknownPersistenceException(ex.getMessage());
                        }
                    } else {
                        throw new UnknownPersistenceException(ex.getMessage());
                    }
                }
            } else {
                throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
            }
        

    }

    @Override
    public AdminEntity retrieveAdminEntityByAdminId(Long adminId) {
        AdminEntity adminEntity = em.find(AdminEntity.class, adminId);

        // TODO: implement checking for null
        return adminEntity;
    }

    @Override
    public void updateAdminEntity(AdminEntity adminEntity) {
        // TODO: implement checking for null
        em.merge(adminEntity);
    }

    @Override
    public void deleteAdminEntity(Long adminId) {
        AdminEntity adminEntity = retrieveAdminEntityByAdminId(adminId);
        em.remove(adminEntity);
    }

    @Override
    public AdminEntity AdminLogin(String email, String password) throws InvalidLoginException {
        try {
            AdminEntity adminEntity = retrieveAdminByEmail(email);
            String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + adminEntity.getSalt()));
            if (adminEntity.getPassword().equals(passwordHash)) {
                return adminEntity;
            } else {
                throw new InvalidLoginException("Email does not exist or invalid password!");
            }
        } catch (AdminNotFoundException | NullPointerException ex) {
            throw new InvalidLoginException("Email does not exist or invalid password!");
        }
    }

    @Override
    public AdminEntity retrieveAdminByEmail(String email) throws AdminNotFoundException {
        Query query = em.createQuery("SELECT s FROM AdminEntity s WHERE s.username = :inUsername");
        query.setParameter("inUsername", email);

        try {
            return (AdminEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new AdminNotFoundException("Admin Email " + email + " does not exist!");
        }
    }
    
      private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<AdminEntity>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
    
}
