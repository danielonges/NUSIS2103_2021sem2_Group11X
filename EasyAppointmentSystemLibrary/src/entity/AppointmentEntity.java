/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlTransient;

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
    @Column(nullable = false, unique = true)
    private Long appointmentNo;
    @Column(nullable = false)
    private String businessCategory;
    private Integer rating;// (between 1 to 5)
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    //timestamp   

    private Boolean isCancelled;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private CustomerEntity customer;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private ServiceProviderEntity serviceProvider;

    public AppointmentEntity() {
        isCancelled = false;
        this.rating = 0;
    }

    public AppointmentEntity(Long appointmentNo, String businessCategory, Date date, CustomerEntity customerEntity, ServiceProviderEntity serviceProviderEntity) {
        this.appointmentNo = appointmentNo;
        this.rating = 0;
        this.businessCategory = businessCategory;
        this.date = date;
        this.isCancelled = false;
        this.customer = customerEntity;
        this.serviceProvider = serviceProviderEntity;
    }

    public Boolean getIsCancelled() {
        return isCancelled;
    }

    public void setIsCancelled(Boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public Long getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Long appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Long getAppointmentNo() {
        return appointmentNo;
    }

    public void setAppointmentNo(Long appointmentNo) {
        this.appointmentNo = appointmentNo;
    }

    public String getBusinessCategory() {
        return businessCategory;
    }

    public void setBusinessCategory(String businessCategory) {
        this.businessCategory = businessCategory;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

//    @XmlTransient
    public CustomerEntity getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

//    @XmlTransient
    public ServiceProviderEntity getServiceProvider() {
        return serviceProvider;
    }

    public void setServiceProvider(ServiceProviderEntity serviceProvider) {
        this.serviceProvider = serviceProvider;
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

//    @Override
//    public String toString() {
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss"); 
//        String strDate = dateFormat.format(this.date); 
//        
//        String[] array = strDate.toString().split(" ");
//        String month = array[0];
//        String time = array[1];
//     // System.out.printf("\n%20s | %20s | %20s | %20s | %20s \n","Name", "| Date", "| Time", "| Appointment No.");
//        return (this.customer.getFullName() + "     | " + month + "     | " + time + "  | " + this.appointmentNo + " ");
//    }

}
