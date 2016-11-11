package com.cabbooking.rkm.bookmycab;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by RamaKrishna Math Chennai on 09-09-2016.
 */
public class DBHelper extends SQLiteOpenHelper
{

    /* Static Strings */

    public static final String DATABASE_NAME =  "CabBooking.db";
    static final String AB = "ABCDEFGHIJKLMNOPQRSTUVWYZ";
    static SecureRandom rnd = new SecureRandom();
    /**
     Users Table Definition
     */
    public static final String USERS_TABLE = "Users";
    public static final String USERS_COLUMN_ID = "Id";
    public static final String USERS_COLUMN_GlobalUserID = "GlobalUserId";
    public static final String USERS_COLUMN_NAME = "Name";
    public static final String USERS_COLUMN_EMAIL = "Email";
    public static final String USERS_COLUMN_PASSWORD = "Password";
    public static final String USERS_COLUMN_USERROLEID = "UserRoleId";
    public static final String USERS_COLUMN_IS_AVAILABLE = "IsAvailable";

    /**
     Booking Table Definition
     */
    public static final String BOOKING_TABLE = "Booking";

    public static final String BOOKING_COLUMN_ID = "GlobalBookingTransactionId";
    public static final String BOOKING_COLUMN_CABID = "CabId";
    public static final String BOOKING_COLUMN_DRIVERID = "DriverId";
    public static final String BOOKING_COLUMN_BOOKING_REQUESTER_ID = "BookingRequesterId";
    public static final String BOOKING_COLUMN_PLACE_OF_PICKUP = "PlaceOfPickup";
    public static final String BOOKING_COLUMN_PLACE_OF_VISIT = "PlaceOfVisit";
    public static final String BOOKING_COLUMN_PICKUP_DATETIME = "PickupDateTime";
    public static final String BOOKING_COLUMN_NUMBER_OF_PERSONS = "NumberOfPersons";
    public static final String BOOKING_COLUMN_REQUIRED_HOURS = "RequiredHours";
    public static final String BOOKING_COLUMN_REASON_FOR_TRAVEL = "ReasonForTravel";
    public static final String BOOKING_COLUMN_IS_APPROVED = "IsApproved";
    public static final String BOOKING_COLUMN_APPROVEDDATE = "ApprovedDate";
    public static final String BOOKING_COLUMN_APPROVEDBY = "ApprovedBy";
    public static final String BOOKING_COLUMN_IS_TRAVEL_COMPLETE = "IsTravelComplete";
    public static final String BOOKING_COLUMN_TRAVEL_COMPLETE_DATETIME = "TravelCompleteDateTime";
    public static final String BOOKING_COLUMN_CREATED_DATETIME = "CreatedDateTime";
    public static final String BOOKING_COLUMN_CREATED_BY = "CreatedBy";
    public static final String BOOKING_COLUMN_MODIFIED_DATETIME = "ModifiedDateTime";
    public static final String BOOKING_COLUMN_MODIFIEDBY = "ModifiedBy";

    /**
     UsersRole Table Definition
     */
    public static final String USERS_ROLE_TABLE = "UsersRole";

    public static final String USERS_ROLE_COLUMN_ID = "Id";
    public static final String USERS_ROLE_COLUMN_ROLENAME = "RoleName";

    /**
     UsersRolePermissions Table Definition
     */
    public static final String USERS_Role_Permission_TABLE = "UsersRolePermissions";

    public static final String USERS_Role_Permission_COLUMN_ID = "Id";
    public static final String USERS_Role_Permission_COLUMN_USERID = "GlobalUserId";
    public static final String USERS_Role_Permission_COLUMN_USERROLEID = "UsersRoleId";
    public static final String USERS_Role_Permission_COLUMN_PERMISSIONSID = "PermissionsId";

    /**
     BookingStatus Table Definition
     */
    public static final String BOOKING_STATUS_TABLE = "BookingStatus";

    public static final String BOOKING_STATUS_COLUMN_ID  = "Id";
    public static final String BOOKING_STATUS_COLUMN_STATUSNAME = "StatusName";

    /**
     Permissions Table Definition
     */
    public static final String PERMISSION_TABLE = "Permissions";

    public static final String PERMISSION_COLUMN_ID = "Id";
    public static final String PERMISSION_COLUMN_PERMISSIONSNAME = "PermissionsName";

    /**
     BookingStatusChangeTracker Table Definition
     */
    public static final String BOOKING_STATUS_CHANGE_TRACKER_TABLE = "BookingStatusChangeTracker";

    public static final String BOOKING_STATUS_CHANGE_TRACKER_COLUMN_ID = "Id";
    public static final String BOOKING_STATUS_CHANGE_TRACKER_COLUMN_GBTID = "GlobalBookingTransactionId";
    public static final String BOOKING_STATUS_CHANGE_TRACKER_COLUMN_STATUSCODE = "BookingStatusCode";
    public static final String BOOKING_STATUS_CHANGE_TRACKER_COLUMN_MODIFIEDDATE = "ModifiedDate";
    public static final String BOOKING_STATUS_CHANGE_TRACKER_COLUMN_MODIFIEDBY = "ModifiedBy";

    /**
     Cab Table Definition
     */
    public static final String CAB_TABLE = "Cab";

    public static final String CAB_COLUMN_ID = "Id";
    public static final String CAB_COLUMN_DESCRIPTION = "CabDescription";
    public static final String CAB_COLUMN_CAB_NUMBER = "CabNumber";
    public static final String CAB_COLUMN_IS_AVAILABLE = "IsAvailable";

    /**
     CabRepair Table Definition
     */
    public static final String CAB_REPAIR_TABLE = "CabRepair";

    public static final String CAB_REPAIR_COLUMN_ID = "Id";
    public static final String CAB_REPAIR_COLUMN_CABID = "CabId";
    public static final String CAB_REPAIR_COLUMN_DRIVERID = "DriverId";
    public static final String CAB_REPAIR_COLUMN_VEHICLE_CONDITION = "VehicleCondition";
    public static final String CAB_REPAIR_COLUMN_REPAIRDATE = "RepairDateTime";
    public static final String CAB_REPAIR_COLUMN_IS_READY_FOR_TRAVEL = "IsReadyForTravel";
    SQLiteDatabase dbi;
    /* Create Table Commands

     */

    public static final String USERS_TABLE_SQL =  "create table if not exists Users " +
            " ( " +
            "Id integer primary key, GlobalUserId text,Name text, Email text,MobileNumber text,Password text, UserRoleId text,IsAvailable boolean" +
            " ) " ;

    public static final String BOOKING_TABLE_SQL =  "create table if not exists Booking " +
            " ( " +
            "Id integer primary key, GlobalBookingTransactionId text,BookingTrackingId text,PlaceOfPickup text,PlaceOfVisit text,PickUpDateTime text,NumberOfPersons integer,RequiredHours integer,ReasonForTravel text,IsApproved boolean, IsTravelComplete boolean, TravelCompleteDate NUMERIC, IsTravelAborted boolean, CreatedDate NUMERIC, CreatedBy text, UpdatedDate NUMERIC, UpdatedBy text" +
            " ) " ;

    /// Booking Tracking ID = Firat 4 Chars of Place of Visit + Datetime in only numbers eg
    //// Kanchipuram, 13/08/2016 4.00 AM {Booking Tracking ID = KANC130820160400}

    public DBHelper(Context context)

    {
        super(context, DATABASE_NAME , null, 1);

        try
        {
            dbi =  getWritableDatabase();
            dbi.execSQL(USERS_TABLE_SQL);
            dbi.execSQL(BOOKING_TABLE_SQL);

        }
        catch(Exception ex)
        {
            String S = ex.getMessage();
            System.out.println(String.format("Cab Booking Exception details :{0}",S));
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        dbi.execSQL(USERS_TABLE_SQL);
        dbi.execSQL(BOOKING_TABLE_SQL);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE);

        db.execSQL("DROP TABLE IF EXISTS " + BOOKING_TABLE);
        // Create tables again
        onCreate(db);
    }


    public void AddUser(Users User)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("GlobalUserId",GenerateIdForTable(2,USERS_TABLE,USERS_COLUMN_GlobalUserID));
        contentValues.put("Name",User.getName());
        contentValues.put("Email",User.getEmail());
        contentValues.put("Password",User.getPassword());
        contentValues.put("MobileNumber",User.getMobileNumber());
        contentValues.put("IsAvailable",User.getIsAvailable());
        contentValues.put("UserRoleId",User.getUserRoleId());
        db.insert(USERS_TABLE,null,contentValues);
        db.close();
    }


    public void EditUser(Users User)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("GlobalUserId",User.getId());
        contentValues.put("Name",User.getName());
        contentValues.put("Email",User.getEmail());
        contentValues.put("Password",User.getPassword());
        contentValues.put("MobileNumber",User.getMobileNumber());
        contentValues.put("IsAvailable",User.getIsAvailable());
        contentValues.put("UserRoleId",User.getUserRoleId());
        db.update(USERS_TABLE,contentValues,"GlobalUserId=?",new String [] {User.getId()});
        db.close();
    }

    public Users GetUser(String GlobalUserId)
    {
        SQLiteDatabase dbreadable;

        dbreadable = getReadableDatabase();

        Cursor cursor = dbreadable.query(USERS_TABLE,
                new String[]{"Id","GlobalUserId","Name","Email","MobileNumber","IsAvailable","UserRoleId","password"},"GlobalUserId=?",
                new String []{String.valueOf(GlobalUserId)},null,null,null,null);

        if (cursor != null)
            cursor.moveToFirst();

        Users user = new Users();

        if(cursor.getCount() >0)
        {
            user = new Users(cursor.getInt(cursor.getColumnIndex("Id")),
                    cursor.getString(cursor.getColumnIndex("GlobalUserId")),
                    cursor.getString(cursor.getColumnIndex("Name")),
                    cursor.getString(cursor.getColumnIndex("Email")),
                    cursor.getString(cursor.getColumnIndex("Password")),
                    cursor.getString(cursor.getColumnIndex("MobileNumber")),
                    cursor.getString(cursor.getColumnIndex("UserRoleId")),
                    (cursor.getInt(cursor.getColumnIndex("IsAvailable")) == 1) ? true : false);
        }

        dbreadable.close();
        return user;

    }

    public ArrayList<Users> GetAllUsers()
    {
        SQLiteDatabase db  =  getReadableDatabase();

        Cursor cursor = db.query(USERS_TABLE,
                new String[]{"Id","GlobalUserId","Name","Email","MobileNumber","IsAvailable","UserRoleId","Password"},null,null,null,null,null);
        Users user = new Users();
        ArrayList<Users> allUser = new ArrayList<Users>();

        if (cursor != null && cursor.moveToFirst() )
        {
            if( cursor.getCount() > 0 )
            {


                do
                {
                    user = new Users(cursor.getInt(cursor.getColumnIndex("Id")),
                            cursor.getString(cursor.getColumnIndex("GlobalUserId")),
                            cursor.getString(cursor.getColumnIndex("Name")),
                            cursor.getString(cursor.getColumnIndex("Email")),
                            cursor.getString(cursor.getColumnIndex("Password")),
                            cursor.getString(cursor.getColumnIndex("MobileNumber")),
                            cursor.getString(cursor.getColumnIndex("UserRoleId")),
                            (cursor.getInt(cursor.getColumnIndex("IsAvailable")) == 1) ? true : false);

                    allUser.add(user);

                }while(cursor.moveToNext());

            }

        }

        return allUser;
    }


    public void AddBooking(Booking booking)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String BookingTrackingId ="";

        try
        {
            BookingTrackingId = GenerateBookingTrackingId(booking.getPlaceOfVisit(),booking.getPickUpDateTime());
            ContentValues contentValues = new ContentValues();
            contentValues.put("GlobalBookingTransactionId",GenerateGlobalBookingId(BookingTrackingId,booking.getBookingRequesterId(),"","","","A"));
            contentValues.put("BookingTrackingId",BookingTrackingId);
            contentValues.put("PlaceOfPickup",booking.getPlaceOfPickup());
            contentValues.put("PlaceOfVisit",booking.getPlaceOfVisit());
            contentValues.put("PickUpDateTime",booking.getPickUpDateTime().toString());
            contentValues.put("NumberOfPersons",booking.getNumberOfPersons());
            contentValues.put("RequiredHours",booking.getRequiredHours());
            contentValues.put("ReasonForTravel",booking.getReasonForTravel());
            contentValues.put("TravelCompleteDate",booking.getTravelCompleteDate() != null ?booking.getTravelCompleteDate().toString(): "1900-01-01" );
            contentValues.put("IsApproved",booking.getIsApproved());
            contentValues.put("IsTravelComplete",booking.getIsTravelComplete());
            contentValues.put("IsTravelAborted()",booking.getIsTravelAborted());

            db.insert(BOOKING_TABLE,null,contentValues);
        }
        catch(Exception ex)
        {
            String S = ex.getMessage();
            System.out.println(String.format("Cab Booking Exception details :{0}",S));
        }
        finally
        {
            db.close();
        }
    }

    public void EditBooking(Booking booking)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        try
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put("GlobalBookingTransactionId",booking.getGlobalBookingTransactionId());
            contentValues.put("PlaceOfPickup",booking.getPlaceOfPickup());
            contentValues.put("PlaceOfVisit",booking.getPlaceOfVisit());
            contentValues.put("PickUpDateTime",booking.getPickUpDateTime().toString());
            contentValues.put("NumberOfPersons",booking.getNumberOfPersons());
            contentValues.put("RequiredHours",booking.getRequiredHours());
            contentValues.put("ReasonForTravel",booking.getReasonForTravel());
            contentValues.put("TravelCompleteDate",booking.getTravelCompleteDate().toString());
            contentValues.put("IsApproved",booking.getIsApproved());
            contentValues.put("IsTravelComplete",booking.getIsTravelComplete());
            contentValues.put("IsTravelAborted()",booking.getIsTravelAborted());
            db.update(BOOKING_TABLE,contentValues,"GlobalBookingTransactionId=?",new String [] {booking.getGlobalBookingTransactionId()});
        }
        catch(Exception ex)
        {
            String S = ex.getMessage();
            System.out.println(String.format("Cab Booking Exception detaiols :{0}",S));
        }
        finally
        {
            db.close();
        }
    }

    public Booking GetBookingById(String GlobalBookingTransactionId)
    {
        SQLiteDatabase dbreadable = getReadableDatabase();
        Booking booking = new Booking();

        try
        {
            Cursor cursor = dbreadable.query(BOOKING_TABLE,
                    new String[]{"Id","GlobalBookingTransactionId","BookingTrackingId","PlaceOfPickup",
                            "PlaceOfVisit","PickUpDateTime","NumberOfPersons","RequiredHours","ReasonForTravel",
                            "IsApproved","IsTravelComplete","TravelCompleteDate","IsTravelAborted",
                            "CreatedDate","CreatedBy","UpdatedDate","UpdatedBy"},"GlobalBookingTransactionId=?",
                    new String []{String.valueOf(GlobalBookingTransactionId)},null,null,null,null);

            if (cursor != null)
                cursor.moveToFirst();

            String DateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS";
            SimpleDateFormat sdf = new SimpleDateFormat(DateFormat);


            if(cursor.getCount() > 0)
            {
                Date PickUpDateTime = sdf.parse( cursor.getString(cursor.getColumnIndex("PickUpDateTime")));
                Date TravelCompleteDate = sdf.parse( cursor.getString(cursor.getColumnIndex("TravelCompleteDate")));

                booking = new Booking(
                        cursor.getString(cursor.getColumnIndex("GlobalBookingTransactionId")),
                        cursor.getString(cursor.getColumnIndex("BookingTrackingId")),
                        cursor.getString(cursor.getColumnIndex("BookingRequesterId")),
                        cursor.getInt(cursor.getColumnIndex("NumberOfPersons")),
                        PickUpDateTime,
                        cursor.getString(cursor.getColumnIndex("PlaceOfPickup")),
                        cursor.getString(cursor.getColumnIndex("PlaceOfVisit")),
                        cursor.getString(cursor.getColumnIndex("ReasonForTravel")),
                        cursor.getInt(cursor.getColumnIndex("RequiredHours")),
                        TravelCompleteDate,
                        (cursor.getInt(cursor.getColumnIndex("IsApproved")) == 1) ? true : false,
                        (cursor.getInt(cursor.getColumnIndex("IsTravelComplete")) == 1) ? true : false,
                        (cursor.getInt(cursor.getColumnIndex("IsTravelAborted")) == 1) ? true : false);
            }

        }
        catch(Exception ex)
        {
            String S = ex.getMessage();
            System.out.println(String.format("Cab Booking Exception detaiols :{0}",S));
        }
        finally
        {
            dbreadable.close();
        }

        return booking;

    }

    public ArrayList<Booking> GetAllBooking()
    {
        SQLiteDatabase db  =  getReadableDatabase();
        Booking booking = new Booking();
        ArrayList<Booking> allBooking = new ArrayList<Booking>();

        try
        {
            Cursor cursor = db.query(BOOKING_TABLE,
                    new String[]{"Id","GlobalBookingTransactionId","BookingTrackingId","PlaceOfPickup",
                            "PlaceOfVisit","PickUpDateTime","NumberOfPersons","RequiredHours","ReasonForTravel",
                            "IsApproved","IsTravelComplete","TravelCompleteDate","IsTravelAborted",
                            "CreatedDate","CreatedBy","UpdatedDate","UpdatedBy"},null,null,null,null,null);

            String DateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS";
            SimpleDateFormat sdf = new SimpleDateFormat(DateFormat);

            if (cursor != null && cursor.moveToFirst() )
            {
                if( cursor.getCount() > 0 )
                {
                    do
                    {
                        Date PickUpDateTime = sdf.parse(cursor.getString(cursor.getColumnIndex("PickUpDateTime")));
                        Date TravelCompleteDate = sdf.parse( cursor.getString(cursor.getColumnIndex("TravelCompleteDate")));

                        booking = new Booking(
                                cursor.getString(cursor.getColumnIndex("GlobalBookingTransactionId")),
                                cursor.getString(cursor.getColumnIndex("BookingTrackingId")),
                                cursor.getString(cursor.getColumnIndex("BookingRequesterId")),
                                cursor.getInt(cursor.getColumnIndex("NumberOfPersons")),
                                PickUpDateTime,
                                cursor.getString(cursor.getColumnIndex("PlaceOfPickup")),
                                cursor.getString(cursor.getColumnIndex("PlaceOfVisit")),
                                cursor.getString(cursor.getColumnIndex("ReasonForTravel")),
                                cursor.getInt(cursor.getColumnIndex("RequiredHours")),
                                TravelCompleteDate,
                                (cursor.getInt(cursor.getColumnIndex("IsApproved")) == 1) ? true : false,
                                (cursor.getInt(cursor.getColumnIndex("IsTravelComplete")) == 1) ? true : false,
                                (cursor.getInt(cursor.getColumnIndex("IsTravelAborted")) == 1) ? true : false);

                        allBooking.add(booking);

                    }while(cursor.moveToNext());
                }
            }
        }
        catch(Exception ex)
        {
            String S = ex.getMessage();
            System.out.println(String.format("Cab Booking Exception details :{0}",S));
        }
        finally
        {
            db.close();
        }

        return allBooking;
    }

    public String GenerateGlobalBookingId(String BookingTrackingId,
                                          String BookingRequesterId,
                                          String DriverId,
                                          String CabId,
                                          String ApprovedById,
                                          String BookingStatusId )
    {
        StringBuilder sb = new StringBuilder(19);
        Boolean IsToIterate = true;

        while(IsToIterate)
        {
            sb.append(BookingTrackingId);

            sb.append("-");

            BookingRequesterId =  BookingRequesterId != "" ? BookingRequesterId : "XX";
            sb.append(BookingRequesterId);

            //Driver Id
            sb.append("-");
            DriverId =  DriverId != "" ? DriverId : "XX";
            sb.append(DriverId);

            //Cab Id
            sb.append("-");
            CabId =  CabId != "" ? CabId : "XX";
            sb.append(CabId);

            //Booking Status Id
            sb.append("-");
            BookingStatusId =  BookingStatusId != "" ? BookingStatusId : "X";
            sb.append(BookingStatusId);

            if (!IsBookingExists(sb.toString()))
            {
                IsToIterate = false;
                return sb.toString();
            }
        }
        return sb.toString();
    }

    public String GenerateIdForTable( int len,String TableName,String ColumnName )
    {
        StringBuilder sb = new StringBuilder( len );
        Boolean IsToIterate = true;

        while(IsToIterate) {
            for (int i = 0; i < len; i++)
            {
                sb.append(AB.charAt(rnd.nextInt(AB.length())));
            }

            if (!IsIdExists(sb.toString(), TableName, ColumnName))
            {
                IsToIterate = false;
                return sb.toString();
            }
        }
        return sb.toString();
    }

    Boolean IsBookingExists( String GlobalBookingTxnId)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query =  "select * from Booking where " + BOOKING_COLUMN_ID + " =?";
        Cursor cursor = db.rawQuery(Query, new String[]{String.valueOf(GlobalBookingTxnId)});

        if(cursor.getCount() <= 0)
        {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    Boolean IsIdExists(String GlobalUserId, String TableName, String ColumnName)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query =  "select * from "+ TableName +" where " + ColumnName + " =?";
        Cursor cursor = db.rawQuery(Query, new String[]{String.valueOf(GlobalUserId)});

        if(cursor.getCount() <= 0)
        {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    private String GenerateBookingTrackingId(String PlaceofVisit, Date PickupDateTime)
    {
        String BookingTrackingId = "" ;

        String DateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS";
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat);

        try
        {
            //Date PickUpDateTimeFormatted = sdf.parse(PickupDateTime);

            Calendar calendar = Calendar.getInstance();
            String Month = ((Integer)calendar.get(Calendar.MONTH)).toString();
            String DayOfMonth =  ((Integer)calendar.get(Calendar.DAY_OF_MONTH)).toString();
            String HourOfDay =  ((Integer)calendar.get(Calendar.HOUR_OF_DAY)).toString();
            String Minute =  ((Integer)calendar.get(Calendar.MINUTE)).toString();

            if (Month.length() < 2)
            {
                Month = "0"+ Month;
            }
            if (DayOfMonth.length() < 2)
            {
                DayOfMonth = "0"+ DayOfMonth;
            }

            if (HourOfDay.length() < 2)
            {
                HourOfDay = "0"+ HourOfDay;
            }

            if (Minute.length() < 2)
            {
                Minute = "0"+ Minute;
            }

            if(PlaceofVisit != null || PickupDateTime!= null)
            {
                calendar.setTime(PickupDateTime);

                if(PlaceofVisit.length() > 3)
                {
                    BookingTrackingId = String.format("%s%d%s%s%s%s",
                            PlaceofVisit.substring(0,3).toUpperCase(),
                            calendar.get(Calendar.YEAR),
                            Month,
                            DayOfMonth,
                            HourOfDay,
                            Minute);
                }
                else if(PlaceofVisit.length() <= 3)
                {
                    String modifiedPlaceName =  PlaceofVisit.substring(0,PlaceofVisit.length()-1).toUpperCase();
                    for(int i=0;i < 4-PlaceofVisit.length();i++ )
                    {
                        modifiedPlaceName =  modifiedPlaceName + "#";
                    }
                    BookingTrackingId = String.format("%s%d%s%s%s%s",
                            modifiedPlaceName,
                            calendar.get(Calendar.YEAR),
                            Month,
                            DayOfMonth,
                            HourOfDay,
                            Minute);
                }
            }
        }
        catch(Exception ex)
        {
            String S = ex.getMessage();
            System.out.println(String.format("Cab Booking Exception details :{0}",S));
        }

        return BookingTrackingId;
    }
}
