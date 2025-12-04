package org.example.tourtour.repository;

import org.example.tourtour.entity.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TourRepository extends JpaRepository<Tour, Long> {

    // Tìm tour theo địa điểm (chứa keyword)
    List<Tour> findByLocationContainingIgnoreCase(String location);

    // Lọc tour theo khoảng giá
    List<Tour> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    // Lọc tour theo số ngày
    List<Tour> findByDurationDays(Integer durationDays);

    // Tìm tour khởi hành sau ngày X (tour sắp tới)
    List<Tour> findByDepartureDateAfter(LocalDate date);

    // Tìm tour còn chỗ trống
    @Query("SELECT t FROM Tour t WHERE t.currentParticipants < t.maxParticipants")
    List<Tour> findAvailableTours();

    // Tìm tour theo nhiều điều kiện (custom query)
    @Query("SELECT t FROM Tour t WHERE " +
            "(:location IS NULL OR LOWER(t.location) LIKE LOWER(CONCAT('%', :location, '%'))) AND " +
            "(:minPrice IS NULL OR t.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR t.price <= :maxPrice) AND " +
            "(:durationDays IS NULL OR t.durationDays = :durationDays)")
    List<Tour> searchTours(
            @Param("location") String location,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("durationDays") Integer durationDays
    );
}