package org.example.tourtour.service;

import org.example.tourtour.entity.Tour;
import org.example.tourtour.repository.TourRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TourService {

    private final TourRepository tourRepository;

    @Transactional
    public Tour createTour(Tour tour) {
        // Validation
        if (tour.getCurrentParticipants() == null) {
            tour.setCurrentParticipants(0);
        }

        if (tour.getMaxParticipants() <= 0) {
            throw new IllegalArgumentException("Số lượng khách tối đa phải lớn hơn 0");
        }

        return tourRepository.save(tour);
    }

    @Transactional
    public Tour updateTour(Long id, Tour tourDetails) {
        Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tour với ID: " + id));

        // Update fields
        tour.setName(tourDetails.getName());
        tour.setImageUrl(tourDetails.getImageUrl());
        tour.setPrice(tourDetails.getPrice());
        tour.setLocation(tourDetails.getLocation());
        tour.setDurationDays(tourDetails.getDurationDays());
        tour.setDepartureDate(tourDetails.getDepartureDate());
        tour.setDescription(tourDetails.getDescription());

        // Chỉ update maxParticipants nếu >= currentParticipants
        if (tourDetails.getMaxParticipants() >= tour.getCurrentParticipants()) {
            tour.setMaxParticipants(tourDetails.getMaxParticipants());
        } else {
            throw new IllegalArgumentException(
                    "Số lượng tối đa không thể nhỏ hơn số người đã đặt (" + tour.getCurrentParticipants() + ")"
            );
        }

        return tourRepository.save(tour);
    }

    @Transactional
    public void deleteTour(Long id) {
        if (!tourRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy tour với ID: " + id);
        }
        tourRepository.deleteById(id);
    }

    public Optional<Tour> getTourById(Long id) {
        return tourRepository.findById(id);
    }

    public List<Tour> getAllTours() {
        return tourRepository.findAll();
    }

    public List<Tour> searchTours(String location, BigDecimal minPrice, BigDecimal maxPrice, Integer durationDays) {
        return tourRepository.searchTours(location, minPrice, maxPrice, durationDays);
    }

    public List<Tour> getAvailableTours() {
        return tourRepository.findAvailableTours();
    }

    public boolean checkAvailability(Long tourId, int requestedSlots) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tour với ID: " + tourId));

        return tour.hasAvailableSlots(requestedSlots);
    }

    @Transactional
    public void updateParticipantCount(Long tourId, int additionalParticipants) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tour với ID: " + tourId));

        int newCount = tour.getCurrentParticipants() + additionalParticipants;

        if (newCount > tour.getMaxParticipants()) {
            throw new IllegalStateException("Vượt quá số lượng khách tối đa");
        }

        if (newCount < 0) {
            throw new IllegalStateException("Số lượng khách không thể âm");
        }

        tour.setCurrentParticipants(newCount);
        tourRepository.save(tour);
    }
}