/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author danielonges
 */
@Entity
public class ServiceProviderEntity extends UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private String businessRegNum;
    private String businessCategory;
    private Enum status;
    private int overallRating;
    

    public ServiceProviderEntity() {
    }

    public ServiceProviderEntity(String name, String address, String city, String email, String password,String businessRegNum, String businessCategory, Enum status, int overallRating) {
        super(name, address, city, email, password);
        this.businessRegNum = businessRegNum;
        this.businessCategory = businessCategory;
        this.status = status;
        this.overallRating = overallRating;
    }
    
    

  
}
