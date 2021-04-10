/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import util.enumeration.ServiceProviderStatus;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlTransient;
import static util.enumeration.ServiceProviderStatus.APPROVE;
import static util.enumeration.ServiceProviderStatus.PENDING;

/**
 *
 * @author danielonges
 */
@Entity
public class ServiceProviderEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long providerId;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, unique = true)
    private String businessRegNum;
    
    @Enumerated (EnumType.STRING)
    private ServiceProviderStatus status;
    private int overallRating;
    @Column(nullable = false, unique = true)
    private String phone;
    private boolean isCancelled;
    
   
    @OneToMany(mappedBy = "serviceProvider")
    private List<AppointmentEntity> appointments;
    
    @OneToMany(mappedBy = "serviceProvider")
    private List<RatingEntity> ratings;

    @ManyToOne (optional = false)
    @JoinColumn(nullable = false)
    private BusinessCategoryEntity businessCategoryEntity;

    public ServiceProviderEntity() {
        isCancelled = false;
        appointments = new ArrayList<>();
        this.overallRating = 0;
        ratings = new ArrayList<>();
        
    }

    public ServiceProviderEntity(String name, String address, String city, String email, String password, String businessRegNum, ServiceProviderStatus status, int overallRating, String phone) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.email = email;
        this.password = password;
        this.businessRegNum = businessRegNum;
        this.status = status;
        this.overallRating = overallRating;
        this.phone = phone;
        isCancelled = false;
        status = ServiceProviderStatus.PENDING;
        overallRating = 0;
        appointments = new ArrayList<>();
        ratings = new ArrayList<>();
    }
    
    @XmlTransient
    public List<RatingEntity> getRatings() {
        return ratings;
    }

    public void setRatings(List<RatingEntity> ratings) {
        this.ratings = ratings;
    }
    
    public boolean isIsCancelled() {
        return isCancelled;
    }

    public void setIsCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(Long providerId) {
        this.providerId = providerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBusinessRegNum() {
        return businessRegNum;
    }

    public void setBusinessRegNum(String businessRegNum) {
        this.businessRegNum = businessRegNum;
    }

    public ServiceProviderStatus getStatus() {
        return status;
    }

    public void setStatus(ServiceProviderStatus status) {
        this.status = status;
    }

    public int getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(int overallRating) {
        this.overallRating = overallRating;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    @XmlTransient
    public List<AppointmentEntity> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<AppointmentEntity> appointments) {
        this.appointments = appointments;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (providerId != null ? providerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ServiceProviderEntity other = (ServiceProviderEntity) obj;
        if (!Objects.equals(this.providerId, other.providerId)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return this.providerId + " | " + this.name + " | " + this.getBusinessCategory() + " | " + this.businessRegNum + " | "
                + this.city + " | " + this.address + " | " + this.email + " | " + this.phone;
    }

    /**
     * @return the businessCategory
     */
    public BusinessCategoryEntity getBusinessCategory() {
        return businessCategoryEntity;
    }

    /**
     * @param businessCategory the businessCategory to set
     */
    public void setBusinessCategory(BusinessCategoryEntity businessCategory) {
        this.businessCategoryEntity = businessCategory;
    }
    
    
}
