package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    UserRepository userRepository3;
    @Autowired
    SpotRepository spotRepository3;
    @Autowired
    ReservationRepository reservationRepository3;
    @Autowired
    ParkingLotRepository parkingLotRepository3;
    @Override
    public Reservation reserveSpot(Integer userId, Integer parkingLotId, Integer timeInHours, Integer numberOfWheels) throws Exception {


        //Reserve a spot in the given parkingLot such that the total price is minimum. Note that the price per hour for each spot is different
        //Note that the vehicle can only be parked in a spot having a type equal to or larger than given vehicle
        //If parkingLot is not found, user is not found, or no spot is available, throw "Cannot make reservation" exception.


        try {
            ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();
            User user = userRepository3.findById(userId).get();


            if (parkingLot == null || user == null) {
                throw new Exception("Cannot make reservation");
            }

            Spot spot = null;

            List<Spot> spotList = parkingLot.getSpotList();

            int min = Integer.MAX_VALUE;

            if (spotList != null) {

                for (Spot spot1 : spotList) {

                    int wheelCount = Integer.MAX_VALUE;

                    if (spot1.getSpotType().equals(SpotType.TWO_WHEELER)) {
                        wheelCount = 2;
                    } else if (spot1.getSpotType().equals(SpotType.FOUR_WHEELER)) {
                        wheelCount = 4;
                    }
                    if (wheelCount >= numberOfWheels && min > spot1.getPricePerHour() && spot1.getOccupied() == false) {
                        spot = spot1;
                        min = spot1.getPricePerHour();
                    }

                }

            }

            if (spot == null) {
                throw new Exception("Cannot make reservation");
            }

            Reservation reservation = new Reservation();
            reservation.setNumberOfHours(timeInHours);
            reservation.setSpot(spot);
            reservation.setUser(user);

            List<Reservation> reservationList = user.getReservationList();

            if (reservationList == null) {
                reservationList = new ArrayList<>();
            }
            reservationList.add(reservation);
            user.setReservationList(reservationList);

            userRepository3.save(user);


            List<Reservation> reservationList1 = spot.getReservationList();

            if (reservationList1 == null) {
                reservationList1 = new ArrayList<>();
            }

            reservationList1.add(reservation);
            spot.setReservationList(reservationList1);

            spotRepository3.save(spot);

            return reservation;

        }
        catch (Exception e) {
            return null;
        }
    }
}
