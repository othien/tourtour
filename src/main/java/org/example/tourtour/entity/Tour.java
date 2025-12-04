package org.example.tourtour.entity;

huy

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
=======
import jakarta.persistence.*;
import lombok.Data;
main

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
huy
@Table(name = "tours")
@Data
@NoArgsConstructor
@AllArgsConstructor
=======
@Table(name = "tour")
@Data
main
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

huy
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

    // Tên tour
    @Column(nullable = false)
    private String name;

    // Ảnh tour (URL hoặc đường dẫn)
    private String imageUrl;

    // Giá tour
    @Column(nullable = false)
    private BigDecimal price;

    // Địa điểm
    @Column(nullable = false)
    private String location;

    // Số ngày
    @Column(nullable = false)
    private Integer days;

    // Ngày khởi hành
    @Column(nullable = false)
    private LocalDate startDate;

    // Giới hạn số người (max khách)
    @Column(nullable = false)
    private Integer maxPeople;

    // Mô tả chi tiết
    @Lob
main
    private String description;
}
