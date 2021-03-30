/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.BusinessCategoryEntity;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author leele
 */
@Local
public interface BusinessCategorySessionBeanLocal {

    public Long createBusinessCategoryEntity(BusinessCategoryEntity newBusinessCategoryEntity);

    public List<BusinessCategoryEntity> retrieveAllBusinessCategories();
    
}
