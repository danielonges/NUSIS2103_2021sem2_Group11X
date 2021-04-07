/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.BusinessCategoryEntity;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.BusinessCategoryNotFoundException;

/**
 *
 * @author leele
 */
@Stateless
public class BusinessCategorySessionBean implements BusinessCategorySessionBeanRemote, BusinessCategorySessionBeanLocal {

    @PersistenceContext(unitName = "EasyAppointmentSystem-ejbPU")
    private EntityManager em;

    @Override
    public Long createBusinessCategoryEntity(BusinessCategoryEntity newBusinessCategoryEntity) {
        em.persist(newBusinessCategoryEntity);
        em.flush();
        return newBusinessCategoryEntity.getId();
    }

    @Override
    public BusinessCategoryEntity retrieveBusinessCategoryEntityByBusinessCategoryId(Long businessCategoryId) throws BusinessCategoryNotFoundException {
        try {
            BusinessCategoryEntity businessCategoryEntity = em.find(BusinessCategoryEntity.class, businessCategoryId);
            return businessCategoryEntity;
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
        Query query = em.createQuery("SELECT s FROM BusinessCategoryEntity s ORDER BY s.id");
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
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
