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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author danielonges
 */
@Entity
public class CustomerEntity extends UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private String identityNum;
    

   
    public CustomerEntity() {
    }

    public CustomerEntity(String name, String address, String city, String email, String password,String identityNum) {
        super(name, address, city, email, password);
        this.identityNum = identityNum;
    }

}
