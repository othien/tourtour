package org.example.tourtour.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tours")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false, length = 100)
    private String location;

    @Column(name = "duration_days", nullable = false)
    private Integer durationDays;

    @Column(name = "departure_date", nullable = false)
    private LocalDate departureDate;

    @Column(name = "max_capacity", nullable = false)
    private Integer maxCapacity;

    @Column(columnDefinition = "TEXT")
    private String description;

}
