package org.example.tourtour.controller;

import org.example.tourtour.entity.Tour;
import org.example.tourtour.repository.TourRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/tours")
public class TourController {

    private final TourRepository tourRepository;

    public TourController(TourRepository tourRepository) {
        this.tourRepository = tourRepository;
    }

    // Admin: Đăng tour mới
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Tour createTour(@RequestBody Tour tour) {
        tour.setId(null);
        return tourRepository.save(tour);
    }

    // Admin: Chỉnh sửa thông tin tour
    @PutMapping("/{id}")
    public Tour updateTour(@PathVariable Long id, @RequestBody Tour updatedTour) {
        Tour existing = tourRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tour not found"));

        existing.setName(updatedTour.getName());
        existing.setImageUrl(updatedTour.getImageUrl());
        existing.setPrice(updatedTour.getPrice());
        existing.setLocation(updatedTour.getLocation());
        existing.setDays(updatedTour.getDays());
        existing.setStartDate(updatedTour.getStartDate());
        existing.setMaxPeople(updatedTour.getMaxPeople());
        existing.setDescription(updatedTour.getDescription());

        return tourRepository.save(existing);
    }

    // Khách hàng: Xem danh mục tour (có thể kèm filter)
    @GetMapping
    public List<Tour> listTours(
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Integer days
    ) {
        if (minPrice == null && maxPrice == null && location == null && days == null) {
            return tourRepository.findAll();
        }
        return tourRepository.search(minPrice, maxPrice, location, days);
    }

    // Xem chi tiết 1 tour (phục vụ đặt tour)
    @GetMapping("/{id}")
    public Tour getTour(@PathVariable Long id) {
        return tourRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tour not found"));
    }
}


