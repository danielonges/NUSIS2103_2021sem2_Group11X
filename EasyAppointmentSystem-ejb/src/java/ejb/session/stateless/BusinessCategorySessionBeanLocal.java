/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.BusinessCategoryEntity;
import java.util.List;
import javax.ejb.Local;
import util.exception.BusinessCategoryAlreadyExistsException;
import util.exception.BusinessCategoryNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author leele
 */
@Local
public interface BusinessCategorySessionBeanLocal {

    public Long createBusinessCategoryEntity(BusinessCategoryEntity newBusinessCategoryEntity) throws BusinessCategoryAlreadyExistsException,UnknownPersistenceException,InputDataValidationException;

    public List<BusinessCategoryEntity> retrieveAllBusinessCategories();

    public void deleteBusinessCategoryEntity(String businessCategory) throws BusinessCategoryNotFoundException;

    public BusinessCategoryEntity retrieveBusinessCategoryEntityByBusinessCategoryId(Long businessCategoryId) throws BusinessCategoryNotFoundException;

    public BusinessCategoryEntity retrieveBusinessCategoryEntityByName(String businessCategory) throws BusinessCategoryNotFoundException;

    public void updateBusinessCategoryEntity(BusinessCategoryEntity businessCategoryEntity);

    public BusinessCategoryEntity retrieveServiceProviders(Long businessCategoryEntityId);
    
}
