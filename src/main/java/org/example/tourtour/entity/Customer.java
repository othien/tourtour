package org.example.tourtour.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.service.spi.InjectService;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;

}
