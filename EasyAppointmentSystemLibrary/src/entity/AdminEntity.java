/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author danielonges
 */
@Entity
public class AdminEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminId;
    private String username;
    private String password;
    private String name;
    
    @OneToMany(mappedBy = "admin")
    private List<AppointmentEntity> appointmentEntitys;    
   
    @OneToMany
    private List<CustomerEntity> customerEntitys;
    
    @OneToMany
    private List<ServiceProviderEntity> serviceProviderEntitys;

    public AdminEntity() {
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (adminId != null ? adminId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the adminId fields are not set
        if (!(object instanceof AdminEntity)) {
            return false;
        }
        AdminEntity other = (AdminEntity) object;
        if ((this.adminId == null && other.adminId != null) || (this.adminId != null && !this.adminId.equals(other.adminId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entity.AdminEntity[ id=" + adminId + " ]";
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the appointmentEntitys
     */
    public List<AppointmentEntity> getAppointmentEntitys() {
        return appointmentEntitys;
    }

    /**
     * @param appointmentEntitys the appointmentEntitys to set
     */
    public void setAppointmentEntitys(List<AppointmentEntity> appointmentEntitys) {
        this.appointmentEntitys = appointmentEntitys;
    }

    /**
     * @return the customerEntitys
     */
    public List<CustomerEntity> getCustomerEntitys() {
        return customerEntitys;
    }

    /**
     * @param customerEntitys the customerEntitys to set
     */
    public void setCustomerEntitys(List<CustomerEntity> customerEntitys) {
        this.customerEntitys = customerEntitys;
    }

    /**
     * @return the serviceProviderEntitys
     */
    public List<ServiceProviderEntity> getServiceProviderEntitys() {
        return serviceProviderEntitys;
    }

    /**
     * @param serviceProviderEntitys the serviceProviderEntitys to set
     */
    public void setServiceProviderEntitys(List<ServiceProviderEntity> serviceProviderEntitys) {
        this.serviceProviderEntitys = serviceProviderEntitys;
    }
    
}
