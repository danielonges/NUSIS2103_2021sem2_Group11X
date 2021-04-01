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
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
    public List<BusinessCategoryEntity> retrieveAllBusinessCategories() {
        Query query = em.createQuery("SELECT s FROM BusinessCategoryEntity s");
        return query.getResultList();
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
