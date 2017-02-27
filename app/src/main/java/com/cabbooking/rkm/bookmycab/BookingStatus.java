package com.cabbooking.rkm.bookmycab;

import java.util.Date;

/**
 * Created by Ramakrishna Mission  on 24-02-2017.
 */
public class BookingStatus
{
    private int _BookingId;
    private Date _BookingStatusChangeDate;
    private String _BookingStatus;

    public BookingStatus()
    {

    }

    public BookingStatus(int _BookingId, String _BookingStatus, Date _BookingStatusChangeDate)
    {
        this._BookingId = _BookingId;
        this._BookingStatus = _BookingStatus;
        this._BookingStatusChangeDate = _BookingStatusChangeDate;
    }

    public int get_BookingId()
    {
        return _BookingId;
    }

    public void set_BookingId(int _BookingId)
    {
        this._BookingId = _BookingId;
    }

    public String get_BookingStatus()
    {
        return _BookingStatus;
    }

    public void set_BookingStatus(String _BookingStatus)
    {
        this._BookingStatus = _BookingStatus;
    }

    public Date get_BookingStatusChangeDate()
    {
        return _BookingStatusChangeDate;
    }

    public void set_BookingStatusChangeDate(Date _BookingStatusChangeDate)
    {
        this._BookingStatusChangeDate = _BookingStatusChangeDate;
    }
}
