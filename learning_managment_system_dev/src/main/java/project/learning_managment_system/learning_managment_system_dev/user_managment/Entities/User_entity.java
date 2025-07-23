package project.learning_managment_system.learning_managment_system_dev.user_managment.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class User_entity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    @Column(name = "firstname",nullable = false)
    public String firstname;
    @Column(name = "lastname",nullable = false)
    public String lastname;
    @Column(name = "email",nullable = false,unique = true)
    public String mail;
    @Column(name = "password",nullable = false,unique = true)
    public  String password;

}
