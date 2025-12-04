package org.example.tourtour.repository;

import org.example.tourtour.entity.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface TourRepository extends JpaRepository<Tour, Long> {

    @Query("""
            SELECT t FROM Tour t
            WHERE (:minPrice IS NULL OR t.price >= :minPrice)
              AND (:maxPrice IS NULL OR t.price <= :maxPrice)
              AND (:location IS NULL OR LOWER(t.location) LIKE LOWER(CONCAT('%', :location, '%')))
              AND (:days IS NULL OR t.days = :days)
            """)
    List<Tour> search(
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("location") String location,
            @Param("days") Integer days
    );
}


