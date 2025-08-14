package com.example.flightapi.Booking.service.impl;

import com.example.flightapi.Booking.service.BookingService;
import java.security.SecureRandom;
import com.example.flightapi.Booking.entity.Booking;
import com.example.flightapi.Booking.repository.BookingRepository;
import com.example.flightapi.cabin.service.CabinClassService;
import com.example.flightapi.common.exception.EntityNotFoundException;
import com.example.flightapi.common.exception.SystemException;
import com.example.flightapi.common.utils.SecurityUtils;
import com.example.flightapi.flight.entity.Flight;
import com.example.flightapi.flight.service.FlightService;
import com.example.flightapi.Booking.service.PassengerService;
import org.bson.types.ObjectId;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.flightapi.common.utils.PageResult;
import com.example.flightapi.common.utils.PageUtil;

@Service
public class BookingServiceImpl implements BookingService {

    private static final String REFERENCE_PREFIX = "BOOK-";
    private static final int REFERENCE_LENGTH = 8;
    private static final String REFERENCE_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final SecureRandom random = new SecureRandom();

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private FlightService flightService;
    @Autowired
    private CabinClassService cabinClassService;
    @Autowired
    private PassengerService passengerService;

    /**
     * 生成符合格式要求的唯一预订引用号
     * 格式: BOOK-XXXXXXXX-TIMESTAMP，其中X是大写字母或数字，TIMESTAMP是时间戳的数字串
     * @return 唯一的预订引用号
     */
    private String generateUniqueReference() {
        StringBuilder sb = new StringBuilder(REFERENCE_PREFIX);

        // 生成8位随机字符
        for (int i = 0; i < REFERENCE_LENGTH; i++) {
            int randomIndex = random.nextInt(REFERENCE_CHARS.length());
            sb.append(REFERENCE_CHARS.charAt(randomIndex));
        }

        // 添加时间戳
        sb.append("-").append(System.currentTimeMillis());

        String reference = sb.toString();

        // 检查引用号是否已存在，如果存在则重新生成
        if (bookingRepository.findByReference(reference).isPresent()) {
            return generateUniqueReference();
        }

        return reference;
    }

    @Override
    public Booking createBooking(Booking booking) {
        // Validate flight availability
        String flightId = booking.getFlightId();
        Flight flight = flightService.getFlightById(flightId.toString());

        // 获取舱位信息并检查可用座位
        int availableSeats = cabinClassService.getCabinClassByFlightIdAndclassType(flightId, booking.getCabinClassType())
            .orElseThrow(() -> new EntityNotFoundException(Booking.class, "flightId", booking.getFlightId().toString()))
            .getAvailableSeats();

        if (availableSeats <= 0) {
            throw new SystemException(String.valueOf(HttpStatus.CONFLICT.value()), "No available seats for the selected cabin class");
        }

        Integer currentUserId = SecurityUtils.getCurrentUserId();
        booking.setUserId(currentUserId);

        if (booking.getStatus() == null) {
            booking.setStatus("PENDING");
        } else {
            booking.setStatus(booking.getStatus());
        }

        // Calculate total price based on flight base price and number of passengers
        // 使用String构造器创建BigDecimal以保持精确度
        BigDecimal basePrice = new BigDecimal(String.valueOf(flight.getPrice())).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal passengerCount = new BigDecimal(booking.getPassengers().size());
        BigDecimal totalPrice = basePrice.multiply(passengerCount).setScale(2, BigDecimal.ROUND_HALF_UP);
        booking.setTotalPrice(totalPrice);
        booking.setBookingTime(LocalDateTime.now());

        // 设置唯一的预订引用号
        booking.setReference(generateUniqueReference());

        // Update available seats in cabin class
        cabinClassService.decreaseAvailableSeats(
            booking.getFlightId(),
            booking.getCabinClassType(),
            booking.getPassengers().size()
        );

        return bookingRepository.save(booking);
    }

    @Override
    public Optional<Booking> getBookingById(String id) {
        return bookingRepository.findById(id);
    }

    @Override
    public Optional<Booking> getBookingByIdAndEmail(String id, String email) {
        return bookingRepository.findByIdAndContactEmail(id, email);
    }

    @Override
    public Optional<Booking> getBookingByReference(String reference) {
        return bookingRepository.findByReference(reference);
    }

    @Override
    public List<Booking> getBookingsByFlightId(String flightId) {
        return bookingRepository.findByFlightId(flightId);
    }

    @Override
    public List<Booking> getBookingsByUserId(int userId) {
        return bookingRepository.findByUserId(userId);
    }

    @Override
    public List<Booking> getBookingsByStatus(String status) {
        return bookingRepository.findByStatus(status);
    }

    @Override
    public List<Booking> getBookingsByUserIdAndFlightId(int userId, String flightId) {
        return bookingRepository.findByUserIdAndFlightId(userId, flightId);
    }

    @Override
    public List<Booking> getBookingsByUserIdAndDateRange(int userId, LocalDateTime start, LocalDateTime end) {
        // 如果没有提供日期范围，设置默认值
        LocalDateTime effectiveEndDate = start != null ? start : LocalDateTime.now();
        LocalDateTime effectiveStartDate = end != null ? end : effectiveEndDate.minusMonths(6);

        return bookingRepository.findByUserIdAndBookingTimeBetween(userId, effectiveStartDate, effectiveEndDate);
    }

    @Override
    public List<Booking> getBookingsByEmail(String email) {
        return bookingRepository.findByContactEmail(email);
    }

    @Override
    public List<Booking> getBookingsByDateRange(LocalDateTime start, LocalDateTime end) {
        return bookingRepository.findByBookingTimeBetween(start, end);
    }

    @Override
    public PageResult<Booking> getBookingsByUserIdAndDateRangePaged(int userId, LocalDateTime start, LocalDateTime end, Pageable pageable) {
        // 如果没有提供日期范围，设置默认值
        LocalDateTime effectiveEndDate = end != null ? end : LocalDateTime.now();
        LocalDateTime effectiveStartDate = start != null ? start : effectiveEndDate.minusMonths(6);

        // 使用分页查询
        Page<Booking> page = bookingRepository.findByUserIdAndBookingTimeBetween(userId, effectiveStartDate, effectiveEndDate, pageable);

        // 将Spring Data的Page转换为自定义的PageResult
        return PageUtil.toPage(page);
    }

    @Override
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public void cancelBooking(String id) {
        getBookingById(id).ifPresent(booking -> {
            // Update booking status
            booking.setStatus("CANCELLED");
            bookingRepository.save(booking);

            // Restore available seats in cabin class
            cabinClassService.increaseAvailableSeats(
                booking.getFlightId(),
                booking.getCabinClassType(),
                booking.getPassengers().size()
            );
        });
    }

    @Override
    public Booking updateBooking(Booking Booking) {
        // 验证booking是否存在
        Booking existingBooking = bookingRepository.findById(Booking.getId())
            .orElseThrow(() -> new EntityNotFoundException(Booking.class, "id", Booking.getId()));

        // 如果航班ID发生变化，需要验证新航班
        if (!existingBooking.getFlightId().equals(Booking.getFlightId())) {
            Flight flight = flightService.getFlightById(Booking.getFlightId().toString());

            // 检查新航班的舱位可用性
            int availableSeats = cabinClassService.getCabinClassByFlightIdAndclassType(
                Booking.getFlightId(),
                Booking.getCabinClassType()
            ).orElseThrow(() -> new EntityNotFoundException(Booking.class, "id", Booking.getId()))
            .getAvailableSeats();

            // 计算需要的座位数
            int requiredSeats = Booking.getPassengers().size();
            if (availableSeats < requiredSeats) {
//                throw new RuntimeException("Not enough available seats in the new flight");
                throw new SystemException(String.valueOf(HttpStatus.CONFLICT.value()), "Not enough available seats in the new flight");

            }

            // 恢复原航班的座位
            cabinClassService.increaseAvailableSeats(
                existingBooking.getFlightId().toString(),
                existingBooking.getCabinClassType(),
                existingBooking.getPassengers().size()
            );

            // 减少新航班的座位
            cabinClassService.decreaseAvailableSeats(
                Booking.getFlightId(),
                Booking.getCabinClassType(),
                requiredSeats
            );
        }

        // 更新总价（如果需要）
        if (Booking.getTotalPrice() == null) {
            Flight flight = flightService.getFlightById(Booking.getFlightId().toString());
            // 使用String构造器创建BigDecimal以保持精确度
            BigDecimal basePrice = new BigDecimal(String.valueOf(flight.getPrice())).setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal passengerCount = new BigDecimal(Booking.getPassengers().size());
            Booking.setTotalPrice(basePrice.multiply(passengerCount).setScale(2, BigDecimal.ROUND_HALF_UP));
        }

        // 复制更新的属性到现有booking
        Booking updatedBooking = new Booking();
        BeanUtils.copyProperties(Booking, updatedBooking);

        // 保持某些字段不变
        updatedBooking.setBookingTime(existingBooking.getBookingTime());
        if (updatedBooking.getStatus() == null) {
            updatedBooking.setStatus(existingBooking.getStatus());
        }

        // 确保reference不为空，如果为空则生成新的reference
        if (updatedBooking.getReference() == null || updatedBooking.getReference().isEmpty()) {
            updatedBooking.setReference(existingBooking.getReference() != null ?
                existingBooking.getReference() : generateUniqueReference());
        }

        // 保存更新后的booking
        return bookingRepository.save(updatedBooking);
    }

    @Override
    public void deleteBooking(String id) {
        bookingRepository.deleteById(id);
    }

}
