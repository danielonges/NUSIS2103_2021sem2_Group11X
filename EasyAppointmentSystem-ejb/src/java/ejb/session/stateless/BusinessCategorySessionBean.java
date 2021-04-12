/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.BusinessCategoryEntity;
import java.util.List;
import java.util.Set;
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
import javax.xml.bind.annotation.XmlTransient;
import util.exception.BusinessCategoryAlreadyExistsException;
import util.exception.BusinessCategoryNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author leele
 */
@Stateless
public class BusinessCategorySessionBean implements BusinessCategorySessionBeanRemote, BusinessCategorySessionBeanLocal {

    @PersistenceContext(unitName = "EasyAppointmentSystem-ejbPU")
    private EntityManager em;
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public BusinessCategorySessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    

    @Override
    public Long createBusinessCategoryEntity(BusinessCategoryEntity newBusinessCategoryEntity) throws BusinessCategoryAlreadyExistsException,UnknownPersistenceException,InputDataValidationException {
         try
        {
            Set<ConstraintViolation<BusinessCategoryEntity>> constraintViolations = validator.validate(newBusinessCategoryEntity);
        
            if(constraintViolations.isEmpty())
            {
                em.persist(newBusinessCategoryEntity);
                em.flush();

                return newBusinessCategoryEntity.getCategoryId();
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
                    throw new BusinessCategoryAlreadyExistsException();
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
    public BusinessCategoryEntity retrieveBusinessCategoryEntityByBusinessCategoryId(Long businessCategoryId) throws BusinessCategoryNotFoundException {
        try {
            BusinessCategoryEntity businessCategoryEntity = em.find(BusinessCategoryEntity.class, businessCategoryId);
            if (businessCategoryEntity != null) {
                return businessCategoryEntity;
            } else {
                throw new BusinessCategoryNotFoundException("Customer not found!");
            }
            
        } catch (NoResultException ex) {
            throw new BusinessCategoryNotFoundException("Customer not found!");
        }
        // TODO: implement checking for null

    }

    @Override
    public BusinessCategoryEntity retrieveBusinessCategoryEntityByName(String businessCategory) throws BusinessCategoryNotFoundException {

        Query query = em.createQuery("SELECT s FROM BusinessCategoryEntity s WHERE s.category = :inCategory");
        query.setParameter("inCategory", businessCategory);

        try {
            return (BusinessCategoryEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new BusinessCategoryNotFoundException("Business Category does not exist!");
        }
    }

    @Override
    public List<BusinessCategoryEntity> retrieveAllBusinessCategories() {
        Query query = em.createQuery("SELECT s FROM BusinessCategoryEntity s ORDER BY s.categoryId");
        return query.getResultList();
    }

    @Override
    public void deleteBusinessCategoryEntity(String businessCategory) throws BusinessCategoryNotFoundException {
        try {
            BusinessCategoryEntity businessCategoryEntity = retrieveBusinessCategoryEntityByName(businessCategory);
            em.remove(businessCategoryEntity);
        } catch (BusinessCategoryNotFoundException ex) {
            throw new BusinessCategoryNotFoundException("Business Category not found!");
        }
    }
    
     public void updateBusinessCategoryEntity(BusinessCategoryEntity businessCategoryEntity) {
        // TODO: implement checking for null
        em.merge(businessCategoryEntity);
    }
     
     public BusinessCategoryEntity retrieveServiceProviders (Long businessCategoryEntityId) {
         BusinessCategoryEntity currentBusinessCategoryEntity = em.find(BusinessCategoryEntity.class,businessCategoryEntityId);
        currentBusinessCategoryEntity.getServiceProviders().size();
        return currentBusinessCategoryEntity;
     }
     private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<BusinessCategoryEntity>>constraintViolations)
    {
        String msg = "Input data validation error!:";
            
        for(ConstraintViolation constraintViolation:constraintViolations)
        {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }
        
        return msg;
    }
}
