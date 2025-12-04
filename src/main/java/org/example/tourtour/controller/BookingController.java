package org.example.tourtour.controller;

import org.example.tourtour.entity.Booking;
import org.example.tourtour.entity.Customer;
import org.example.tourtour.entity.Tour;
import org.example.tourtour.repository.BookingRepository;
import org.example.tourtour.repository.CustomerRepository;
import org.example.tourtour.repository.TourRepository;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingRepository bookingRepository;
    private final TourRepository tourRepository;
    private final CustomerRepository customerRepository;
    private final JavaMailSender mailSender;

    public BookingController(BookingRepository bookingRepository,
                             TourRepository tourRepository,
                             CustomerRepository customerRepository,
                             JavaMailSender mailSender) {
        this.bookingRepository = bookingRepository;
        this.tourRepository = tourRepository;
        this.customerRepository = customerRepository;
        this.mailSender = mailSender;
    }

    // DTO đơn giản cho request đặt tour
    public record CreateBookingRequest(
            Long tourId,
            String fullName,
            String email,
            String phoneNumber,
            Integer numberOfPeople,
            Boolean paid
    ) {
    }

    // Khách hàng: Đặt tour online
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Booking createBooking(@RequestBody CreateBookingRequest request) {
        if (request.tourId() == null || request.numberOfPeople() == null || request.numberOfPeople() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid tour or number of people");
        }

        Tour tour = tourRepository.findById(request.tourId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tour not found"));

        // Kiểm soát số lượng khách tối đa cho mỗi tour
        Integer currentPeople = bookingRepository.sumPeopleByTour(tour);
        int totalAfterBooking = currentPeople + request.numberOfPeople();
        if (totalAfterBooking > tour.getMaxPeople()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tour is full or exceeds max people");
        }

        // Tìm hoặc tạo khách hàng theo email
        if (request.fullName() == null || request.email() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer name and email are required");
        }

        Customer customer = customerRepository.findByEmail(request.email())
                .orElseGet(() -> {
                    Customer c = new Customer();
                    c.setFullName(request.fullName());
                    c.setEmail(request.email());
                    c.setPhoneNumber(request.phoneNumber());
                    return c;
                });

        // Cập nhật lại thông tin khách hàng nếu có thay đổi
        customer.setFullName(request.fullName());
        customer.setPhoneNumber(request.phoneNumber());
        customer = customerRepository.save(customer);

        // Tính tổng giá
        BigDecimal totalPrice = tour.getPrice()
                .multiply(BigDecimal.valueOf(request.numberOfPeople()));

        Booking booking = new Booking();
        booking.setTour(tour);
        booking.setCustomer(customer);
        booking.setNumberOfPeople(request.numberOfPeople());
        booking.setTotalPrice(totalPrice);
        booking.setBookingTime(LocalDateTime.now());
        booking.setStatus(Boolean.TRUE.equals(request.paid())
                ? Booking.PaymentStatus.PAID
                : Booking.PaymentStatus.UNPAID);

        Booking saved = bookingRepository.save(booking);

        // Gửi email xác nhận đặt tour
        sendConfirmationEmail(saved);

        return saved;
    }

    // Admin: Xem danh sách khách đặt theo từng tour
    @GetMapping("/tour/{tourId}")
    public List<Booking> getBookingsByTour(@PathVariable Long tourId) {
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tour not found"));
        return bookingRepository.findByTour(tour);
    }

    private void sendConfirmationEmail(Booking booking) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(booking.getCustomer().getEmail());
            message.setSubject("Xác nhận đặt tour: " + booking.getTour().getName());
            String text = "Xin chào " + booking.getCustomer().getFullName() + ",\n\n"
                    + "Bạn đã đặt thành công tour: " + booking.getTour().getName() + "\n"
                    + "Địa điểm: " + booking.getTour().getLocation() + "\n"
                    + "Ngày khởi hành: " + booking.getTour().getStartDate() + "\n"
                    + "Số người: " + booking.getNumberOfPeople() + "\n"
                    + "Tổng tiền: " + booking.getTotalPrice() + "\n"
                    + "Trạng thái thanh toán: " + booking.getStatus() + "\n\n"
                    + "Cảm ơn bạn đã sử dụng dịch vụ!";
            message.setText(text);

            mailSender.send(message);
        } catch (Exception e) {
            // Không ném lỗi để không làm hỏng quá trình đặt tour nếu email bị lỗi cấu hình
        }
    }
}


