package org.example.tourtour.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "booking")
@Data
public class Booking {

    public enum PaymentStatus {
        PAID,
        UNPAID
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Mỗi booking gắn với một tour
    @ManyToOne(optional = false)
    @JoinColumn(name = "tour_id")
    private Tour tour;

    // Mỗi booking gắn với một khách hàng
    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    // Số lượng người đi
    @Column(nullable = false)
    private Integer numberOfPeople;

    // Tổng giá = số người * giá tour
    @Column(nullable = false)
    private BigDecimal totalPrice;

    // Thời gian đặt
    @Column(nullable = false)
    private LocalDateTime bookingTime;

    // Trạng thái thanh toán (đã thanh toán / chưa thanh toán)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;
}
