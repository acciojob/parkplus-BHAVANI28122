package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.ReservationRepository;
import com.driver.repository.SpotRepository;
import com.driver.repository.UserRepository;
import com.driver.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

        ParkingLot parkingLot = parkingLotRepository3.findById(parkingLotId).get();

        Reservation reservation = new Reservation();
        reservation.setNumberOfHours(timeInHours);

        User user = userRepository3.findById(userId).get();


        Spot spot = null;

        List<Spot> spotList = parkingLot.getSpotList();




        if(parkingLot == null ||user == null || spotList == null ){


            return null;
        }


        int min = Integer.MAX_VALUE;

        for(Spot spot1:spotList){


          if( spot1.getPricePerHour()*reservation.getNumberOfHours() < min){

              spot = spot1;
          }

        }
        reservation.setUser(user);
        reservation.setSpot(spot);
        if(numberOfWheels < 2){

            reservation.getSpot().setSpotType(SpotType.TWO_WHEELER);

        }
        else if(numberOfWheels < 4){
            reservation.getSpot().setSpotType(SpotType.FOUR_WHEELER);
        }
        else {
            reservation.getSpot().setSpotType(SpotType.OTHERS);
        }

        spot.setOccupied(true);

        List<Reservation> reservationList = user.getReservationList();
        reservationList.add(reservation);
        user.setReservationList(reservationList);

        userRepository3.save(user);

        List<Reservation> reservationList1 = spot.getReservationList();
        reservationList1.add(reservation);
        spot.setReservationList(reservationList1);

        spotRepository3.save(spot);

        return  reservation;

    }
}
