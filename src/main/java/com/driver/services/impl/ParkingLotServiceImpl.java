package com.driver.services.impl;

import com.driver.model.ParkingLot;
import com.driver.model.Spot;
import com.driver.model.SpotType;
import com.driver.repository.ParkingLotRepository;
import com.driver.repository.SpotRepository;
import com.driver.services.ParkingLotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParkingLotServiceImpl implements ParkingLotService {
    @Autowired
    ParkingLotRepository parkingLotRepository1;
    @Autowired
    SpotRepository spotRepository1;
    @Override
    public ParkingLot addParkingLot(String name, String address) {

        ParkingLot parkingLot = new ParkingLot();
        parkingLot.setName(name);
        parkingLot.setAddress(address);
        parkingLotRepository1.save(parkingLot);

        return  parkingLot;
    }

    @Override
    public Spot addSpot(int parkingLotId, Integer numberOfWheels, Integer pricePerHour) {


        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();

          Spot spot = new Spot();
          spot.setParkingLot(parkingLot);
          spot.setPricePerHour(pricePerHour);
          spot.setOccupied(false);

          spot.setSpotType(SpotType.TWO_WHEELER);

          List<Spot> spotList = parkingLot.getSpotList();
          spotList.add(spot);
          parkingLot.setSpotList(spotList);
          parkingLotRepository1.save(parkingLot);

          return  spot;



    }

    @Override
    public void deleteSpot(int spotId) {

        spotRepository1.deleteById(spotId);

//       if(spot != null) {
//           spotRepository1.delete(spot);
//       }


    }

    @Override
    public Spot updateSpot(int parkingLotId, int spotId, int pricePerHour) {

        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();

            List<Spot> spotList = parkingLot.getSpotList();
            for(Spot spot:spotList){
                if(spot.getId() == spotId){
                    spot.setPricePerHour(pricePerHour);
                    spot.setParkingLot(parkingLot);
                    spot.setSpotType(SpotType.TWO_WHEELER);
                    spotRepository1.save(spot);
                    return spot;
                }
            }
            return  null;

    }

    @Override
    public void deleteParkingLot(int parkingLotId) {

//        ParkingLot parkingLot = parkingLotRepository1.findById(parkingLotId).get();
//
//        if(parkingLot != null) {
//            parkingLotRepository1.delete(parkingLot);
//        }
        parkingLotRepository1.deleteById(parkingLotId);

    }
}
