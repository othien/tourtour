package org.example.tourtour.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tour")
@Data
public class Tour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    private String description;
}
