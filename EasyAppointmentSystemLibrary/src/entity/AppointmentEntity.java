/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author danielonges
 */
@Entity
public class AppointmentEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appointmentId;
    private Long appointmentNo;
    private String businessCategory;
    private Integer Rating;// (between 1 to 5)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    //timestamp   
    private Boolean isCompleted;
    

    @JoinColumn(nullable = false)
    @ManyToOne(optional = false)
    private CustomerEntity customer;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private ServiceProviderEntity service;
    
    

    public AppointmentEntity() {
    }

    public AppointmentEntity(Long appointmentId, Long appointmentNo, String businessCategory, Integer Rating, Date date, Boolean isCompleted) {
        this.appointmentId = appointmentId;
        this.appointmentNo = appointmentNo;
        this.businessCategory = businessCategory;
        this.Rating = Rating;
        this.date = date;
        this.isCompleted = isCompleted;
       
        
    }
    
    
    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getAppointmentId() != null ? getAppointmentId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the appointmentId fields are not set
        if (!(object instanceof AppointmentEntity)) {
            return false;
        }
        AppointmentEntity other = (AppointmentEntity) object;
        if ((this.getAppointmentId() == null && other.getAppointmentId() != null) || (this.getAppointmentId() != null && !this.appointmentId.equals(other.appointmentId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AppointmentEntity[ id=" + getAppointmentId() + " ]";
    }

    /**
     * @return the Rating
     */
    public Integer getRating() {
        return Rating;
    }

    /**
     * @param Rating the Rating to set
     */
    public void setRating(Integer Rating) {
        this.Rating = Rating;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }


    /**
     * @return the customer
     */
    public CustomerEntity getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }


    /**
     * @return the service
     */
    public ServiceProviderEntity getService() {
        return service;
    }

    /**
     * @param service the service to set
     */
    public void setService(ServiceProviderEntity service) {
        this.service = service;
    }



}
