/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import enumeration.Status;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author danielonges
 */
@Entity
public class ServiceProviderEntity extends UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private String businessRegNum;
    private String businessCategory;
    @Enumerated (EnumType.STRING)
    private Status status;
    private int overallRating;
    private String phone;
    private AppointmentEntity appointment;
    
    
   
    @OneToMany(mappedBy = "service")
    private List<AppointmentEntity> appointmentEntitys;
    
    

    public ServiceProviderEntity() {
    }

    public ServiceProviderEntity(String name, String address, String city, String email, String password,String businessRegNum, String businessCategory, Status status, int overallRating,String phone) {
        super(name, address, city, email, password);
        this.businessRegNum = businessRegNum;
        this.businessCategory = businessCategory;
        this.status = status;
        this.overallRating = overallRating;
        this.phone = phone;
    }

    /**
     * @return the businessRegNum
     */
    public String getBusinessRegNum() {
        return businessRegNum;
    }

    /**
     * @param businessRegNum the businessRegNum to set
     */
    public void setBusinessRegNum(String businessRegNum) {
        this.businessRegNum = businessRegNum;
    }

    /**
     * @return the businessCategory
     */
    public String getBusinessCategory() {
        return businessCategory;
    }

    /**
     * @param businessCategory the businessCategory to set
     */
    public void setBusinessCategory(String businessCategory) {
        this.businessCategory = businessCategory;
    }

    /**
     * @return the overallRating
     */
    public int getOverallRating() {
        return overallRating;
    }

    /**
     * @param overallRating the overallRating to set
     */
    public void setOverallRating(int overallRating) {
        this.overallRating = overallRating;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(Status status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return getId() + " | " + getName() + " | " + this.businessCategory + " | " + this.businessRegNum + " | " + getCity() 
                + " | " + getAddress() + " | " + getEmail() + " | " + getPhone();
    }

  
}
