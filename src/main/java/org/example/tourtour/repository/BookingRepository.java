package org.example.tourtour.repository;

import org.example.tourtour.entity.Booking;
import org.example.tourtour.entity.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByTour(Tour tour);

    @Query("SELECT COALESCE(SUM(b.numberOfPeople), 0) FROM Booking b WHERE b.tour = :tour")
    Integer sumPeopleByTour(@Param("tour") Tour tour);
}


