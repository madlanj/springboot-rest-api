package springboot.restfull.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "contacts")
public class Contact {
    @Id
    private String id;

    private String firstName;

    private String lastName;

    private String phone;

    private String email;

    @ManyToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    // name = "username" -> merupakan kolom username pada table contacts
    // referencedColumnName = "username" -> merupakan kolom username pada table users
    private User user;

    @OneToMany(mappedBy = "contact")
    private List<Address> address;

}
