package springboot.restfull.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "addresses")
public class Address {
    @Id
    private String id;

    private String street;

    private String city;

    private String province;

    private String country;

    @Column(name = "postal_code")
    private String postalCode;

    @ManyToOne
    @JoinColumn(name = "contact_id", referencedColumnName = "id")
    // name = "contact_id" -> merupakan kolom contact_id pada table addresses
    // referencedColumnName = "id" -> merupakan kolom id pada table contacts
    private Contact contact;


}
