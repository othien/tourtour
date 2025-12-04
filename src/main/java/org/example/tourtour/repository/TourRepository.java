package org.example.tourtour.repository;

import org.example.tourtour.entity.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TourRepository extends JpaRepository<Tour, Long> {
    List<Tour> findByLocationContainingIgnoreCase(String location);

    List<Tour> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    List<Tour> findByDurationDays(Integer durationDays);

    List<Tour> findByDepartureDateAfter(LocalDate date);

}
