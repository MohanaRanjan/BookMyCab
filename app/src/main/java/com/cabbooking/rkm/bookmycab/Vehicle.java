package com.cabbooking.rkm.bookmycab;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Ramakrishna Mission Chennai on 27-11-2016.
 */
public class Vehicle implements Serializable
{
    private String _VehicleNumber;
    private String _VehicleDescription;
    private boolean _IsAvailable;
    private Integer _Id;
    private Integer _Capacity;
    private String _VehicleModel;
    private String _VehicleId;
    private ArrayList<VehicleBooking> _vehicleBookingList;

    public Vehicle()
    {

    }

    public Vehicle(Integer Id,
                   String VehicleId,
                   String VehicleNumber,
                   String VehicleDescription,
                   Integer Capacity,
                   String VehicleModel,ArrayList<VehicleBooking> vehicleBookingList )
    {
        this._VehicleNumber =  VehicleNumber;
        this._VehicleDescription = VehicleDescription;
        this._Capacity = Capacity;
        this._VehicleModel =  VehicleModel;
        this._vehicleBookingList = vehicleBookingList;
        this._VehicleId =  VehicleId;
    }

    public int getId()
    {
        return this._Id;
    }

    public void setId(int Id)
    {
        this._Id = Id;
    }


    public String getVehicleId()
    {
        return this._VehicleId;
    }

    public void setVehicleId(String VehicleId)
    {
        this._VehicleId = VehicleId;
    }


    public String getVehicleNumber()
    {
        return this._VehicleNumber;
    }

    public void setVehicleNumber(String VehicleNumber)
    {
        this._VehicleNumber = VehicleNumber;
    }

    public String getVehicleDescription()
    {
        return this._VehicleDescription;
    }

    public void setVehicleDescription(String VehicleDescription)
    {
        this._VehicleDescription = VehicleDescription;
    }

    public String getVehicleModel()
    {
        return this._VehicleModel;
    }

    public void setVehicleModel(String VehicleModel)
    {
        this._VehicleModel = VehicleModel;
    }

    public Integer getCapacity()
    {
        return this._Capacity;
    }

    public void setCapacity(String Capacity)
    {
        this._Capacity = _Capacity;
    }

    public ArrayList <VehicleBooking> getVehicleBookingList()
    {
        return this._vehicleBookingList;
    }

    public void setVehicleBookingList( ArrayList <VehicleBooking> vehicleBookingList)
    {
        this._vehicleBookingList = vehicleBookingList;
    }
}
