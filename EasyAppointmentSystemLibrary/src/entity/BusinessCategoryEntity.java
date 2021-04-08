/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author leele
 */
@Entity
public class BusinessCategoryEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;
    @Column(nullable = false, unique = true)
    private String category;
    
    @OneToMany(mappedBy = "businessCategoryEntity")
    private List<ServiceProviderEntity> serviceProviders;
    

    public BusinessCategoryEntity() {
        this.serviceProviders = new ArrayList<>();
    }

    public BusinessCategoryEntity(String category) {
        this();
        this.category = category;
    }
    
    
    

    
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getCategoryId() != null ? getCategoryId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BusinessCategoryEntity)) {
            return false;
        }
        BusinessCategoryEntity other = (BusinessCategoryEntity) object;
        if ((this.getCategoryId() == null && other.getCategoryId() != null) || (this.getCategoryId() != null && !this.categoryId.equals(other.categoryId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getCategory();
    }

    /**
     * @return the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @return the serviceProviders
     */
    @XmlTransient
    public List<ServiceProviderEntity> getServiceProviders() {
        return serviceProviders;
    }

    /**
     * @param serviceProviders the serviceProviders to set
     */
    public void setServiceProviders(List<ServiceProviderEntity> serviceProviders) {
        this.serviceProviders = serviceProviders;
    }
    
}
