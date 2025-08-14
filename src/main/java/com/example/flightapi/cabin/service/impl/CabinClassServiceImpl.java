package com.example.flightapi.cabin.service.impl;

import com.example.flightapi.cabin.entity.CabinClass;
import com.example.flightapi.cabin.repository.CabinClassRepository;
import com.example.flightapi.cabin.service.CabinClassService;
import com.example.flightapi.common.exception.EntityNotFoundException;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CabinClassServiceImpl implements CabinClassService {

    private static final Logger logger = LoggerFactory.getLogger(CabinClassServiceImpl.class);

    @Autowired
    private CabinClassRepository cabinClassRepository;

    @Override
    public CabinClass createCabinClass(CabinClass cabinClass) {
        CabinClass savedCabin = cabinClassRepository.save(cabinClass);
        return savedCabin;
    }

    @Override
    public CabinClass updateCabinClass(CabinClass cabinClass) {
        if (!cabinClassRepository.existsById(cabinClass.getId())) {
            throw new EntityNotFoundException(CabinClass.class, "id", cabinClass.getId());
        }
        return cabinClassRepository.save(cabinClass);
    }

    @Override
    public Optional<CabinClass> getCabinClassById(String id) {
        return cabinClassRepository.findById(id);
    }

    @Override
    public List<CabinClass> getCabinClassesByFlightId(String flightId) {
        return cabinClassRepository.findByFlightId(flightId);
    }

    @Override
    public List<CabinClass> getAvailableCabinClassesByFlightId(String flightId) {
        return cabinClassRepository.findAvailableCabinsByFlightId(flightId);
    }

    @Override
    public CabinClass bookSeats(String cabinClassId, int numSeats) {
        Optional<CabinClass> cabinOpt = cabinClassRepository.findById(cabinClassId);
        if (!cabinOpt.isPresent()) {
            throw new EntityNotFoundException(CabinClass.class, "id", cabinClassId);
        }

        CabinClass cabin = cabinOpt.get();
        if (cabin.getAvailableSeats() < numSeats) {
            throw new IllegalStateException("Not enough available seats");
        }

        cabin.setAvailableSeats(cabin.getAvailableSeats() - numSeats);
        return cabinClassRepository.save(cabin);
    }

    @Override
    public CabinClass cancelBooking(String cabinClassId, int numSeats) {
        Optional<CabinClass> cabinOpt = cabinClassRepository.findById(cabinClassId);
        if (!cabinOpt.isPresent()) {
            throw new EntityNotFoundException(CabinClass.class, "id", cabinClassId);
        }

        CabinClass cabin = cabinOpt.get();
        if (cabin.getAvailableSeats() + numSeats > cabin.getTotalSeats()) {
            throw new IllegalStateException("Cannot cancel more seats than total seats");
        }

        cabin.setAvailableSeats(cabin.getAvailableSeats() + numSeats);
        return cabinClassRepository.save(cabin);
    }

    @Override
    public void deleteCabinClass(String id) {
        if (!cabinClassRepository.existsById(id)) {
            throw new EntityNotFoundException(CabinClass.class, "id", id);
        }
        cabinClassRepository.deleteById(id);
    }

    @Override
    public Optional<CabinClass> getCabinClassByFlightIdAndclassType(String flightId, Integer classType) {
        logger.debug("Getting cabin class for flight ID: {} and class ID: {}", flightId, classType);
        return cabinClassRepository.findByFlightIdAndClassType(flightId, classType);
    }

    @Override
    public CabinClass decreaseAvailableSeats(String flightId, Integer classType, int numSeats) {
        Optional<CabinClass> cabinOpt = cabinClassRepository.findByFlightIdAndClassType(flightId, classType);
        if (!cabinOpt.isPresent()) {
            throw new EntityNotFoundException(CabinClass.class, "flightId/classType", flightId + "/" + classType);
        }

        CabinClass cabin = cabinOpt.get();
        if (cabin.getAvailableSeats() < numSeats) {
            throw new IllegalStateException("Not enough available seats");
        }

        cabin.setAvailableSeats(cabin.getAvailableSeats() - numSeats);
        return cabinClassRepository.save(cabin);
    }

    @Override
    public CabinClass increaseAvailableSeats(String flightId, Integer classType, int numSeats) {
        Optional<CabinClass> cabinOpt = cabinClassRepository.findByFlightIdAndClassType(flightId, classType);
        if (!cabinOpt.isPresent()) {
            throw new EntityNotFoundException(CabinClass.class, "flightId/classType", flightId + "/" + classType);
        }

        CabinClass cabin = cabinOpt.get();
        if (cabin.getAvailableSeats() + numSeats > cabin.getTotalSeats()) {
            throw new IllegalStateException("Cannot exceed total seats capacity");
        }

        cabin.setAvailableSeats(cabin.getAvailableSeats() + numSeats);
        return cabinClassRepository.save(cabin);
    }
}
