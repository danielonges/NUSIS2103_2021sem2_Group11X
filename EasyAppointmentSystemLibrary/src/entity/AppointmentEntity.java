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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
    private CustomerEntity cust;
    private Boolean isCompleted;
    private Long serviceProviderId;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private CustomerEntity customer;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private AdminEntity admin;

    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private ServiceProviderEntity service;
    
    

    public AppointmentEntity() {
    }

    public AppointmentEntity(Long appointmentId, Long appointmentNo, String businessCategory, Integer Rating, Date date, CustomerEntity cust, Boolean isCompleted, Long serviceProviderId, CustomerEntity customer, AdminEntity admin, ServiceProviderEntity service) {
        this.appointmentId = appointmentId;
        this.appointmentNo = appointmentNo;
        this.businessCategory = businessCategory;
        this.Rating = Rating;
        this.date = date;
        this.cust = cust;
        this.isCompleted = isCompleted;
        this.serviceProviderId = serviceProviderId;
        this.customer = customer;
        this.admin = admin;
        this.service = service;
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
    public CustomerEntity getCust() {
        return cust;
    }

    /**
     * @param cust the customer to set
     */
    public void setCust(CustomerEntity cust) {
        this.cust = cust;
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
     * @return the admin
     */
    public AdminEntity getAdmin() {
        return admin;
    }

    /**
     * @param admin the admin to set
     */
    public void setAdmin(AdminEntity admin) {
        this.admin = admin;
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

    /**
     * @return the serviceProviderId
     */
    public Long getServiceProviderId() {
        return serviceProviderId;
    }

    /**
     * @param serviceProviderId the serviceProviderId to set
     */
    public void setServiceProviderId(Long serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

}
