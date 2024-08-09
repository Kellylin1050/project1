package com.example.project1.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.util.*;

@Schema(description = "角色實體")
@Entity
@Table(name = "role")
public class Role {
    @Schema(description = "角色id", example = "1")
    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    @Schema(description = "角色名稱", example = "ADMIN")
    private String name;

   @Schema(description = "與此角色相關聯的User")
   @ManyToMany(mappedBy = "roles",cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
 //  @JsonIgnore
   private Set<User> users = new HashSet<>();
   //private Collection<User> users;

    /*@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(
            name = "roles_privileges",
            joinColumns = @JoinColumn(name = "role_id" , referencedColumnName = "id", insertable = true, updatable = true, nullable=false),
            inverseJoinColumns = @JoinColumn(name = "privilege_id"))
    private Collection<Privilege> privileges;*/

    public Role(){}
    public Role(String name){
        this.name = name;
    }

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*public void setPrivileges(Collection<Privilege> privileges) {
        this.privileges = privileges;
    }

    public Collection<? extends Privilege> getPrivileges() {
        return privileges;
    }*/


    //public boolean isEmpty() {
       // return false;
  //  }


}
