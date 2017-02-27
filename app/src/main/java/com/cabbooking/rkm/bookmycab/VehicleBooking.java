package com.cabbooking.rkm.bookmycab;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Ramakrishna Mission on 28-11-2016.
 */
public class VehicleBooking implements Serializable
{
    private Integer _Id;
    private String _VehicleId;
    private String _BookingId;
    private Date _BookedFrom;
    private Date _BookedTo;
    private boolean _IsAvailable;
    private boolean _IsTravelComplete;
    private Vehicle _vehicle;

    public VehicleBooking()
    {

    }

    public VehicleBooking(String VehicleId,
                          String BookingId,
                          Date BookedFrom,
                          Date BookedTo,
                          boolean IsAvailable,
                          boolean IsTravelComplete,
                          Vehicle vehicle)
    {
        //this._Id  = Id;
        this._VehicleId =  VehicleId;
        this._BookingId =  BookingId;
        this._BookedFrom = BookedFrom;
        this._BookedTo =  BookedTo;
        this._IsAvailable  =  IsAvailable;
        this._IsTravelComplete =  IsTravelComplete;
        this._vehicle = vehicle;
    }
/*
    public int getId()
    {
        return this._Id;
    }

    public void setId(int Id)
    {
        this._Id = Id;
    }
*/

    public Vehicle getVehicle()
    {
        return this._vehicle;
    }

    public void setvehicle(Vehicle vehicle)
    {
        this._vehicle = vehicle;
    }


    public String getVehicleId()
    {
        return this._VehicleId;
    }

    public void setVehicleId(String VehicleId)
    {
        this._VehicleId = VehicleId;
    }

    public String getBookingId()
    {
        return this._BookingId;
    }

    public void setBookingId(String BookingId)
    {
        this._BookingId = BookingId;
    }

    public Date getBookedFrom()
    {
        return this._BookedFrom;
    }

    public void setBookedFrom(Date BookedFrom)
    {
        this._BookedFrom = BookedFrom;
    }

    public Date getBookedTo()
    {
        return this._BookedTo;
    }

    public void setBookedTo(Date BookedTo)
    {
        this._BookedTo = BookedTo;
    }

    public boolean getIsTravelComplete()
    {
        return this._IsTravelComplete;
    }

    public void setIsTravelComplete(boolean IsTravelComplete)
    {
        this._IsTravelComplete = IsTravelComplete;
    }

    public boolean getIsAvailable()
    {
        return this._IsAvailable;
    }

    public void setIsAvailable(boolean IsAvailable)
    {
        this._IsAvailable = IsAvailable;
    }

}
