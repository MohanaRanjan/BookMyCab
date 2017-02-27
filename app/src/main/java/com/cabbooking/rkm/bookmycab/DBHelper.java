package com.cabbooking.rkm.bookmycab;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
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

    public static final String DATABASE_NAME =  "BookMyCab.db";
    static final String AB = "ABCDEFGHIJKLMNOPQRSTUVWYZ";
    static SecureRandom rnd = new SecureRandom();

    private static String DB_PATH = "/data/data/com.cabbooking.rkm.bookmycab/databases/";
    private final Context myContext;

    private static String DB_NAME = "BookMyCab.db";
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
    public static final String VEHICLE_TABLE = "Vehicle";

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

    public static final String VEHICLE_BOOKING_TABLE = "VehicleBooking";
    SQLiteDatabase dbi;
    /* Create Table Commands

     */

    public static final String USERS_TABLE_SQL =  "create table if not exists Users " +
            " ( " +
            "Id integer primary key, GlobalUserId text,Name text, Email text,MobileNumber text,Password text, UserRoleId text,IsAvailable boolean" +
            " ) " ;

    public static final String BOOKING_TABLE_SQL =  "create table if not exists Booking " +
            " ( " +
            "Id integer primary key, GlobalBookingTransactionId text,BookingTrackingId text,BookingRequesterId text,PlaceOfPickup text,PlaceOfVisit text,VehicleId text, DriverId text,PickUpDateTime DateTime,BookTo DateTime,NumberOfPersons integer,RequiredHours integer,ReasonForTravel text,IsApproved integer, IsTravelComplete integer, TravelCompleteDate DateTime, IsTravelAborted integer,ApprovalForwardedBy text, CreatedDate DateTime, CreatedBy text, UpdatedDate DateTime, UpdatedBy text" +
            " ) " ;

    public static final String BOOKINGSTATUS_TABLE_SQL =  "create table if not exists BookingStatus " +
            " ( " +
            "Id integer primary key, BookingId integer,BookingStatus text, ApprovalForwardedBy text,ApprovedBy String,StatusChangeDate DateTime, CreatedDate DateTime, CreatedBy text, UpdatedDate DateTime, UpdatedBy text" +
            " ) " ;

    public static final String Cab_TABLE_SQL =  "create table if not exists Vehicle " +
            " ( " +
            "Id integer primary key,VehicleId text,VehicleNumber text,VehicleDescription text,Capacity integer, Model text" +
            " ) " ;

    public static final String VehicleBooking_TABLE_SQL =  "create table if not exists VehicleBooking " +
            " ( " +
               "Id integer primary key,VehicleId text,BookingId text,BookedFrom DateTime, BookedTo DateTime, IsTravelComplete integer,IsAborted integer" +
            " ) " ;
    /// Booking Tracking ID = Firat 4 Chars of Place of Visit + Datetime in only numbers eg
    //// Kanchipuram, 13/08/2016 4.00 AM {Booking Tracking ID = KANC130820160400}

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
        this.myContext =  context;
      try
        {
            dbi =  getWritableDatabase();

            if(dbi != null)
            {
              //dbi.execSQL("DROP TABLE IF EXISTS " + BOOKING_TABLE);
               //dbi.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE);
               dbi.execSQL(USERS_TABLE_SQL);
               dbi.execSQL(BOOKING_TABLE_SQL);
               dbi.execSQL(Cab_TABLE_SQL);
               dbi.execSQL(VehicleBooking_TABLE_SQL);
               dbi.execSQL(BOOKINGSTATUS_TABLE_SQL);
            }
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
       /* db.execSQL(USERS_TABLE_SQL);
        db.execSQL(BOOKING_TABLE_SQL);
        db.execSQL(Cab_TABLE_SQL);
        db.execSQL(VehicleBooking_TABLE_SQL);
        */
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
      /*  // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE);

        db.execSQL("DROP TABLE IF EXISTS " + BOOKING_TABLE);
        // Create tables again
        onCreate(db);
        */
    }


    public boolean IsUniquePhone(String MobileNumber)
    {

        SQLiteDatabase dbreadable;
        Users user = new Users();
        dbreadable = getReadableDatabase();
        Boolean IsSuccess =  true;

        try
        {
            String [] columns = new String[]{"Id","GlobalUserId","Name","Email","MobileNumber","IsAvailable","UserRoleId","password"};
            String Where = "MobileNumber =?";
            String [] args = new String[]{MobileNumber};

            Cursor cursor = dbreadable.query(USERS_TABLE,columns,Where,args,null,null,null);

            if (cursor != null)
                cursor.moveToFirst();

            if(cursor.getCount() > 0)
            {
                IsSuccess = false;
            }
        }
        catch(Exception ex)
        {
            String S = ex.getMessage();
            System.out.println(String.format("Cab Booking Exception details :{0}",S));
        }
        finally
        {
            dbreadable.close();
        }
        return IsSuccess;

    }
    public Users GetUserLogin(String UserPhone, String Password)
    {
       // "Id integer primary key, GlobalUserId text,Name text, Email text,MobileNumber text,Password text, UserRoleId text,IsAvailable boolean" +
        SQLiteDatabase dbreadable;
        Users user = new Users();
        dbreadable = getReadableDatabase();
        Boolean IsSuccess =  false;
        try
        {
            String [] columns = new String[]{"Id","GlobalUserId","Name","Email","MobileNumber","IsAvailable","UserRoleId","password"};
            String Where = "MobileNumber =? AND password=?";
            String [] args = new String[]{UserPhone,Password};

            Cursor cursor = dbreadable.query(USERS_TABLE,columns,Where,args,null,null,null);

            if (cursor != null)
                cursor.moveToFirst();

            if(cursor.getCount() > 0)
            {
                user = new Users(cursor.getInt(cursor.getColumnIndex("Id")),
                        cursor.getString(cursor.getColumnIndex("GlobalUserId")),
                        cursor.getString(cursor.getColumnIndex("Name")),
                        cursor.getString(cursor.getColumnIndex("Email")),
                        cursor.getString(cursor.getColumnIndex("Password")),
                        cursor.getString(cursor.getColumnIndex("MobileNumber")),
                        cursor.getString(cursor.getColumnIndex("UserRoleId")),
                        (cursor.getInt(cursor.getColumnIndex("IsAvailable")) == 1) ? true : false);

                IsSuccess = true;
            }
        }
        catch(Exception ex)
        {
            String S = ex.getMessage();
            System.out.println(String.format("Cab Booking Exception details :{0}",S));
        }
        finally
        {
            dbreadable.close();
        }

        return user;
    }

    public void AddUser(Users User)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put("GlobalUserId",GenerateIdForTable(2,USERS_TABLE,USERS_COLUMN_GlobalUserID));
            contentValues.put("Name",User.getName());
            contentValues.put("Email",User.getEmail());
            contentValues.put("Password",User.getPassword());
            contentValues.put("MobileNumber",User.getMobileNumber());
            contentValues.put("IsAvailable",User.getIsAvailable());
            contentValues.put("UserRoleId",User.getUserRoleId());
            db.insert(USERS_TABLE,null,contentValues);
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

    public void EditUser(Users User)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put("GlobalUserId",User.getId());
            contentValues.put("Name",User.getName());
            contentValues.put("Email",User.getEmail());
            contentValues.put("Password",User.getPassword());
            contentValues.put("MobileNumber",User.getMobileNumber());
            contentValues.put("IsAvailable",User.getIsAvailable());
            contentValues.put("UserRoleId",User.getUserRoleId());
            db.update(USERS_TABLE,contentValues,"GlobalUserId=?",new String [] {User.getId()});
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

    public Users GetUser(String GlobalUserId)
    {
        SQLiteDatabase dbreadable;
        Users user = new Users();
        dbreadable = getReadableDatabase();
        try
        {
            Cursor cursor = dbreadable.query(USERS_TABLE,
                    new String[]{"Id","GlobalUserId","Name","Email","MobileNumber","IsAvailable","UserRoleId","password"},"GlobalUserId=?",
                    new String []{String.valueOf(GlobalUserId)},null,null,null,null);

            if (cursor != null)
                cursor.moveToFirst();

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
        }
        catch(Exception ex)
        {
            String S = ex.getMessage();
            System.out.println(String.format("Cab Booking Exception details :{0}",S));
        }
        finally
        {
            dbreadable.close();
        }

        return user;
    }

    public ArrayList<Users> GetAllUsers()
    {
        SQLiteDatabase db  =  getReadableDatabase();
        ArrayList<Users> allUser = new ArrayList<Users>();
        try
        {
            Cursor cursor = db.query(USERS_TABLE,
                    new String[]{"Id","GlobalUserId","Name","Email","MobileNumber","IsAvailable","UserRoleId","Password"},null,null,null,null,null);
            Users user = new Users();

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

        return allUser;
    }

    public void AddEditBooking(String GlobalBookingTransactionId,
                               String BookingTrackingId,
                               String BookingRequesterId,
                               Integer NumberOfPersons,
                               Date PickUpDateTime,
                               String PlaceOfPickup,
                               String PlaceOfVisit,
                               String ReasonForTravel,
                               Integer RequiredHours,
                               Date TravelCompleteDate,
                               boolean IsApproved,
                               boolean IsTravelComplete,
                               boolean IsTravelAborted,
                               String BookingRequesterName,
                               String OldBookingTrackingId,
                               Boolean IsEdit,
                               String DriverId,
                               String CabId,
                               String ApprovedById,
                               ApplicationConstants.BOOKINGSTATUS BookingStatusId,
                               String AdminId
    )
                                  //String BookingStatusId)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        //String BookingTrackingId ="";
       // String GlobalBookingTransactionId="";
        String DateString ="1900-01-01 00:00:00";
        String DateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat);
        Date BookingTo;
        Calendar cal  =  Calendar.getInstance();


        String CurrentDateString = String.format("%s-%s-%s %s:%s:%s",
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH) + 1 ,
                cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE),"00");

        cal.setTime(PickUpDateTime);
        cal.add(Calendar.HOUR,RequiredHours);
        BookingTo =  cal.getTime();

        String BookToString  = sdf.format(BookingTo);

        try
        {
            Date DummyDate = sdf.parse(DateString);

            BookingTrackingId = GenerateBookingTrackingId(
                    PlaceOfVisit,
                    PickUpDateTime,
                    BookingRequesterId
            );

            String bookingStatusCode = "";

            switch (BookingStatusId)
            {
                case BOOKING_REQUEST:
                    bookingStatusCode = "A";
                    break;
                case APPROVED:
                    bookingStatusCode = "B";
                    break;
                case HOI_APPROVAL_REQUEST:
                    bookingStatusCode = "C";
                    break;
                case COMPLETED:
                    bookingStatusCode = "D";
                    break;
                case REJECTED:
                    bookingStatusCode = "E";
                    break;
                case CANCELED:
                    bookingStatusCode = "F";
                    break;
                case HOI_APPROVAL_REQUEST_ACCEPTED:
                    bookingStatusCode = "G";
                    break;
                case HOI_APPROVAL_REQUEST_REJECTED:
                    bookingStatusCode = "H";
                    break;
            }

            GlobalBookingTransactionId =  GenerateGlobalBookingId(
                                                                    BookingTrackingId,
                                                                    BookingRequesterId,
                                                                    DriverId,CabId,
                                                                    ApprovedById,
                                                                    bookingStatusCode
                                                                 );
            ContentValues contentValues = new ContentValues();

            contentValues.put("BookingRequesterId",BookingRequesterId);
            contentValues.put("GlobalBookingTransactionId",GlobalBookingTransactionId);
            contentValues.put("PlaceOfPickup",PlaceOfPickup);
            contentValues.put("BookingTrackingId",BookingTrackingId);
            contentValues.put("PlaceOfVisit",PlaceOfVisit);
            contentValues.put("VehicleId",CabId);
            contentValues.put("DriverId",DriverId);
            contentValues.put("PickUpDateTime",sdf.format(PickUpDateTime));
            contentValues.put("BookTo",sdf.format(BookingTo));
            contentValues.put("NumberOfPersons",NumberOfPersons);
            contentValues.put("RequiredHours",RequiredHours);
            contentValues.put("ReasonForTravel",ReasonForTravel);
            contentValues.put("TravelCompleteDate",TravelCompleteDate != null ? sdf.format(TravelCompleteDate): DateString);
            contentValues.put("IsApproved",(IsApproved)? 1:0);
            contentValues.put("IsTravelComplete",(IsTravelComplete) ? 1:0);
            contentValues.put("IsTravelAborted",(IsTravelAborted) ? 1:0);
            contentValues.put("ApprovalForwardedBy",AdminId);
            contentValues.put("ApprovedBy",ApprovedById);
            contentValues.put("UpdatedDate",CurrentDateString);
            contentValues.put("UpdatedBy",BookingRequesterId);

            ContentValues contentValuesBookingStaus = new ContentValues();

            db.beginTransaction();
            if(IsEdit)
            {
                if(!OldBookingTrackingId.isEmpty())
                {
                    String WhereClause =  "BookingTrackingId = ? ";
                    String[] WhereArgs = new String []{String.valueOf(OldBookingTrackingId)};
                    //delete old Booking because Booking Transaction id is different
                    db.delete(BOOKING_TABLE,WhereClause,WhereArgs);
                    //create new booking
                    long bookingId = db.insertOrThrow(BOOKING_TABLE,null,contentValues);

                }
                else
                {
                    String WhereClause =  "BookingTrackingId = ? ";
                    String[] WhereArgs = new String []{String.valueOf(BookingTrackingId)};

                    db.update(BOOKING_TABLE,contentValues,WhereClause,WhereArgs);
                }
            }
            else
            {
                contentValues.put("CreatedDate",CurrentDateString);
                contentValues.put("CreatedBy",BookingRequesterId);
                db.insertOrThrow(BOOKING_TABLE,null,contentValues);
            }

            db.setTransactionSuccessful();
        }
        catch (RuntimeException RE)
        {
            String S = RE.getMessage();
            System.out.println(String.format("Cab Booking Exception details :{0}",S));
        }
        catch(Throwable ex)
        {
            String S = ex.getMessage();
            System.out.println(String.format("Cab Booking Exception details :{0}",S));
        }
        finally
        {
            db.endTransaction();
            db.close();
        }
    }
/*
    private boolean HasBookingStatusChanged(String NewStatus,String BookingTrackingId)
    {
        SQLiteDatabase db  =  getReadableDatabase();
        Booking booking = new Booking();
        String OldbookingStatus ="";

        try {

            String GetBookingStatus = "SELECT * FROM Booking b " +
                    "LEFT OUTER JOIN BookingStatusCode bs " +
                    "ON b.Id = bs.BookingId " +
                    "Where  b.BookingTrackingId =?";

            Cursor cursor = db.rawQuery(GetBookingStatus, new String[]{BookingTrackingId});

            if (cursor != null && cursor.moveToFirst() )
            {
                if (cursor.getCount() > 0)
                {
                    OldbookingStatus = cursor.getString(cursor.getColumnIndex("BookingStstus"));
                }
            }
        }
        catch(Throwable ex)
        {
            String exw = ex.getMessage();
        }

        return OldbookingStatus.equals(NewStatus) ? false:true;
    }
*/
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
            contentValues.put("VehicleId",booking.getVehicleId());
            contentValues.put("DriverId",booking.getDriverId());
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

    public Booking GetBookingById(String BookingTrackingnId)
    {
        SQLiteDatabase db  =  getReadableDatabase();
        Booking booking = new Booking();

        try
        {

            String GetBookingByIdSQL = "SELECT * FROM Booking b " +
                    "INNER JOIN Users u "+
                    "ON b.BookingRequesterId = u.GlobalUserId " +
                    "LEFT OUTER JOIN Vehicle v " +
                    "ON v.VehicleId = b.VehicleId " +
                    "Where  BookingTrackingId =?";

            String DateFormat =  "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat sdf = new SimpleDateFormat(DateFormat);

            Cursor cursor = db.rawQuery(GetBookingByIdSQL,new String[]{BookingTrackingnId});

            if (cursor != null )
            {
                if( cursor.getCount() > 0  && cursor.moveToFirst())
                {

                        Date TravelCompleteDateString = sdf.parse("1900-01-01 00:00:00");
                        Date PickUpDateTime = sdf.parse(cursor.getString(cursor.getColumnIndex("PickUpDateTime")));
                        Date TravelCompleteDate = sdf.parse(
                                String.valueOf((cursor.getString(cursor.getColumnIndex("TravelCompleteDate")) !=  null )
                                        ? cursor.getString(cursor.getColumnIndex("TravelCompleteDate")):TravelCompleteDateString));
                    Calendar  cal = Calendar.getInstance();
                    cal.setTime(PickUpDateTime);
                    cal.add(Calendar.HOUR, cursor.getInt(cursor.getColumnIndex("RequiredHours")));

                    Date DateTo = sdf.parse(sdf.format(cal.getTime()));
                    ArrayList<VehicleBooking> vbList = new ArrayList<VehicleBooking>();


                    Date StatusChangeDate = sdf.parse(cursor.getString(cursor.getColumnIndex("StatusChangeDate")));

                    cal.setTime(PickUpDateTime);


                    Vehicle vehicle = new Vehicle(cursor.getInt(29),
                            cursor.getString(cursor.getColumnIndex("VehicleId")),
                            cursor.getString(cursor.getColumnIndex("VehicleNumber")),
                            cursor.getString(cursor.getColumnIndex("VehicleDescription")),0,
                            cursor.getString(cursor.getColumnIndex("Model")),vbList

                    );

                    ArrayList<VehicleBooking> vehBookingList = new ArrayList<VehicleBooking>();
                    VehicleBooking vehicleBooking =  new VehicleBooking(
                            cursor.getString(cursor.getColumnIndex("VehicleId")),
                            cursor.getString(cursor.getColumnIndex("GlobalBookingTransactionId")),
                            PickUpDateTime,DateTo,true,false,vehicle
                    );

                    vehBookingList.add(vehicleBooking);

                    vehicle.setVehicleBookingList(vehBookingList);

                        booking = new Booking(cursor.getInt(cursor.getColumnIndex("Id")),
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
                                (cursor.getInt(cursor.getColumnIndex("IsTravelAborted")) == 1) ? true : false,
                                cursor.getString(cursor.getColumnIndex("Name")),
                                cursor.getString(cursor.getColumnIndex("VehicleId")),
                                cursor.getString(cursor.getColumnIndex("DriverId")),
                                new Vehicle(),
                                new BookingStatus(
                                        cursor.getInt(cursor.getColumnIndex("Id")),
                                        cursor.getString(cursor.getColumnIndex("BookingStatus")),
                                        StatusChangeDate
                                        )
                        );
                }
            }
        }
        catch(Throwable ex)
        {
            String S = ex.getMessage();
            System.out.println(String.format("Cab Booking Exception details :{0}",S));
        }
        finally
        {
            db.close();
        }
        return booking;
    }

    public ArrayList<Booking> GetAllBooking(Boolean IsSentforHOIApproval, Boolean IsHOIApproved,String UserId, String UserRoleId )
    {
        SQLiteDatabase db  =  getReadableDatabase();
        Booking booking = new Booking();
        ArrayList<Booking> allBooking = new ArrayList<Booking>();

        try
        {
            String GetAllBookingSQL ="";

            //Retrive booking for HOI
            if(IsSentforHOIApproval)
            {
                GetAllBookingSQL = "SELECT * FROM Booking b " +
                        "INNER JOIN Users u "+
                        "ON b.BookingRequesterId = u.GlobalUserId " +
                        "INNER JOIN BookingStatus bs "+
                        "ON b.Id = bs.BookingId " +
                        "WHERE 'C' = substr(b.GlobalBookingTransactionId,LENGTH(b.GlobalBookingTransactionId)) " +
                        "ORDER BY b.PickUpDateTime DESC ";
            }
            else if(IsHOIApproved != null)
            {
                if(IsHOIApproved)
                {
                    GetAllBookingSQL = "SELECT * FROM Booking b " +
                            "INNER JOIN Users u " +
                            "ON b.BookingRequesterId = u.GlobalUserId " +
                            "INNER JOIN BookingStatus bs "+
                            "ON b.Id = bs.BookingId " +
                            "WHERE 'G' = substr(b.GlobalBookingTransactionId,LENGTH(b.GlobalBookingTransactionId)) " +
                            "ORDER BY b.PickUpDateTime DESC ";
                }
                else
                {
                    GetAllBookingSQL = "SELECT * FROM Booking b " +
                            "INNER JOIN Users u "+
                            "ON b.BookingRequesterId = u.GlobalUserId " +
                            "INNER JOIN BookingStatus bs "+
                            "ON b.Id = bs.BookingId " +
                            "WHERE 'H' = substr(b.GlobalBookingTransactionId,LENGTH(b.GlobalBookingTransactionId)) " +
                            "ORDER BY b.PickUpDateTime DESC ";
                }
            }

            //Retrive booking for Booking Requester
            else if(UserRoleId.equals("B"))
            {
                GetAllBookingSQL = "SELECT * FROM Booking b " +
                        "INNER JOIN Users u "+
                        "ON b.BookingRequesterId = u.GlobalUserId = '" + UserId + "' " +
                        "INNER JOIN BookingStatus bs "+
                        "ON b.Id = bs.BookingId " +
                        "WHERE u.GlobalUserId = " +
                        "ORDER BY b.PickUpDateTime DESC ";
            }

            else if(UserRoleId.equals("D"))
            {
                GetAllBookingSQL = "SELECT * FROM Booking b " +
                        "INNER JOIN Users u "+
                        "ON b.DriverId = u.GlobalUserId " +
                        "INNER JOIN BookingStatus bs "+
                        "ON b.Id = bs.BookingId " +
                        "WHERE u.GlobalUserId = '" + UserId + "' " +
                        "ORDER BY b.PickUpDateTime DESC ";
            }
            //Retrive booking for Admin
            else
            {
                GetAllBookingSQL = "SELECT * FROM Booking b " +
                        "INNER JOIN Users u "+
                        "ON b.BookingRequesterId = u.GlobalUserId " +
                        "INNER JOIN BookingStatus bs "+
                        "ON b.Id = bs.BookingId " +
                        "ORDER BY b.PickUpDateTime DESC ";
            }

            String DateFormat =  "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat sdf = new SimpleDateFormat(DateFormat);

            Cursor cursor = db.rawQuery(GetAllBookingSQL,null);

            if (cursor != null )
            {
                if( cursor.getCount() > 0  && cursor.moveToFirst())
                {
                    do
                    {
                        Date TravelCompleteDateString = sdf.parse("1900-01-01 00:00:00");
                        Date PickUpDateTime = sdf.parse(cursor.getString(cursor.getColumnIndex("PickUpDateTime")));
                        Date TravelCompleteDate = sdf.parse(
                                String.valueOf((cursor.getString(cursor.getColumnIndex("TravelCompleteDate")) !=  null )
                                        ? cursor.getString(cursor.getColumnIndex("TravelCompleteDate")):TravelCompleteDateString));


                        Date StatusChangeDate = sdf.parse(cursor.getString(cursor.getColumnIndex("StatusChangeDate")));

                        BookingStatus bs  = new BookingStatus(
                            cursor.getInt(cursor.getColumnIndex("Id")),
                            cursor.getString(cursor.getColumnIndex("BookingStatus")),
                            StatusChangeDate
                    );
                        booking = new Booking(cursor.getInt(cursor.getColumnIndex("Id")),
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
                                (cursor.getInt(cursor.getColumnIndex("IsTravelAborted")) == 1) ? true : false,
                                cursor.getString(cursor.getColumnIndex("Name")),
                                cursor.getString(cursor.getColumnIndex("VehicleId")),
                                cursor.getString(cursor.getColumnIndex("DriverId")),
                                new Vehicle(),
                                bs
                        );

                        allBooking.add(booking);

                    }while(cursor.moveToNext());
                }
            }
        }
        catch(Throwable ex)
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


    public ArrayList<Booking> GetCurrentBooking(Boolean IsCurrentBooking)
    {
        SQLiteDatabase db  =  getReadableDatabase();
        Booking booking = new Booking();
        ArrayList<Booking> allBooking = new ArrayList<Booking>();

        try
        {

            String GetBookingSQL = "SELECT * FROM Booking b " +
                    "INNER JOIN Users u "+
                    "ON b.BookingRequesterId = u.GlobalUserId " +
                    "INNER JOIN BookingStatus bs "+
                    "ON b.Id = bs.BookingId " +
                    "Where  IsTravelComplete =?";
            String DateFormat =  "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat sdf = new SimpleDateFormat(DateFormat);

            Cursor cursor = db.rawQuery(GetBookingSQL,new String[]{String.valueOf(IsCurrentBooking ? 1:0)});

            if (cursor != null )
            {
                if( cursor.getCount() > 0  && cursor.moveToFirst())
                {
                    do
                    {
                        Date TravelCompleteDateString = sdf.parse("1900-01-01 00:00:00");
                        Date PickUpDateTime = sdf.parse(cursor.getString(cursor.getColumnIndex("PickUpDateTime")));
                        Date TravelCompleteDate = sdf.parse(
                                String.valueOf((cursor.getString(cursor.getColumnIndex("TravelCompleteDate")) !=  null )
                                        ? cursor.getString(cursor.getColumnIndex("TravelCompleteDate")):TravelCompleteDateString));
                        Date StatusChangeDate = sdf.parse(cursor.getString(cursor.getColumnIndex("StatusChangeDate")));
                        booking = new Booking(
                                cursor.getInt(cursor.getColumnIndex("Id")),
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
                                (cursor.getInt(cursor.getColumnIndex("IsTravelAborted")) == 1) ? true : false,
                                cursor.getString(cursor.getColumnIndex("Name")),
                                cursor.getString(cursor.getColumnIndex("VehicleId")),
                                cursor.getString(cursor.getColumnIndex("DriverId")),
                                new Vehicle(),
                                new BookingStatus(
                                cursor.getInt(cursor.getColumnIndex("Id")),
                                cursor.getString(cursor.getColumnIndex("BookingStatus")),
                                StatusChangeDate
                        )
                        );

                        allBooking.add(booking);

                    }while(cursor.moveToNext());
                }
            }
        }
        catch(Throwable ex)
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


    public ArrayList<Booking> GetHOIApprovalBooking(Boolean IsCurrentBooking, boolean IsHOIApproval)
    {
        SQLiteDatabase db  =  getReadableDatabase();
        Booking booking = new Booking();
        ArrayList<Booking> allBooking = new ArrayList<Booking>();

        try
        {

            String GetBookingSQL = "SELECT * FROM Booking b " +
                    "INNER JOIN Users u "+
                    "ON b.BookingRequesterId = u.GlobalUserId " +
                    "INNER JOIN BookingStatus bs "+
                    "ON b.Id = bs.BookingId " +
                    "Where  IsTravelComplete =?" +
                    " AND ";
            String DateFormat =  "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat sdf = new SimpleDateFormat(DateFormat);

            Cursor cursor = db.rawQuery(GetBookingSQL,new String[]{String.valueOf(IsCurrentBooking ? 1:0)});

            if (cursor != null )
            {
                if( cursor.getCount() > 0  && cursor.moveToFirst())
                {
                    do
                    {
                        Date TravelCompleteDateString = sdf.parse("1900-01-01 00:00:00");
                        Date PickUpDateTime = sdf.parse(cursor.getString(cursor.getColumnIndex("PickUpDateTime")));
                        Date TravelCompleteDate = sdf.parse(
                                String.valueOf((cursor.getString(cursor.getColumnIndex("TravelCompleteDate")) !=  null )
                                        ? cursor.getString(cursor.getColumnIndex("TravelCompleteDate")):TravelCompleteDateString));
                        Date StatusChangeDate = sdf.parse(cursor.getString(cursor.getColumnIndex("StatusChangeDate")));
                        booking = new Booking(
                                cursor.getInt(cursor.getColumnIndex("Id")),
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
                                (cursor.getInt(cursor.getColumnIndex("IsTravelAborted")) == 1) ? true : false,
                                cursor.getString(cursor.getColumnIndex("Name")),
                                cursor.getString(cursor.getColumnIndex("VehicleId")),
                                cursor.getString(cursor.getColumnIndex("DriverId")),
                                new Vehicle(),
                                new BookingStatus(
                                        cursor.getInt(cursor.getColumnIndex("Id")),
                                        cursor.getString(cursor.getColumnIndex("BookingStatus")),
                                        StatusChangeDate
                                )
                        );

                        allBooking.add(booking);

                    }while(cursor.moveToNext());
                }
            }
        }
        catch(Throwable ex)
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

        while(IsToIterate)
        {
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

    public void AddEditVehicle(String VehicleId,String VehicleNumber, String VehicleDescription, String VehicleModel, Integer Capacity, Boolean IsEdit)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put("VehicleNumber",VehicleNumber);
            contentValues.put("Model",VehicleModel);
            contentValues.put("Capacity",Capacity);

            contentValues.put("VehicleDescription",VehicleDescription);
            //contentValues.put("IsAvailable",(IsAvailable)? 1:0);

            if(IsEdit)
            {
                String WhereClause =  "VehicleId = ? ";
                String[] WhereArgs = new String []{String.valueOf(VehicleId)};
                contentValues.put("VehicleId",VehicleId);
                db.update(VEHICLE_TABLE,contentValues,WhereClause,WhereArgs);
            }
            else
            {
                contentValues.put("VehicleId",GenerateIdForTable(2,VEHICLE_TABLE,"VehicleId"));
                db.insertOrThrow(VEHICLE_TABLE,null,contentValues);
                String s= "";
            }
        }
        catch (RuntimeException rx)
        {
            String S = rx.getMessage();
            System.out.println(String.format("Cab Booking Exception details :{%s}",S));
            throw rx;
        }
        catch(Throwable ex)
        {
            String S = ex.getMessage();
            System.out.println(String.format("Cab Booking Exception details :{%s}",S));
            throw ex;
        }
        finally
        {
            db.close();
        }
    }

    public Vehicle GetVehicleById(String VehicleId)
    {
        SQLiteDatabase db  =  getReadableDatabase();
        Vehicle vehicle =  new Vehicle();

        String GetVehicleSQL ="";

        try
        {
            GetVehicleSQL = "SELECT * FROM Vehicle v WHERE v.VehicleId =" + VehicleId;

            Cursor cursor = db.rawQuery(GetVehicleSQL,null);

            if (cursor != null )
            {
                if(cursor.getCount() > 0  && cursor.moveToFirst())
                {
                    do
                    {
                            vehicle = new Vehicle(cursor.getInt(cursor.getColumnIndex("Id")),
                                    cursor.getString(cursor.getColumnIndex("VehicleId")),
                                    cursor.getString(cursor.getColumnIndex("VehicleNumber")),
                                    cursor.getString(cursor.getColumnIndex("VehicleDescription")),
                                    cursor.getInt(cursor.getColumnIndex("Capcity")),
                                    cursor.getString(cursor.getColumnIndex("Model")),
                                    new ArrayList<VehicleBooking>());
                    }
                    while(cursor.moveToNext());
                }
            }
        }
        catch(Throwable ex)
        {
            String S = ex.getMessage();
            System.out.println(String.format("Cab Booking Exception details :{%s}",S));
        }

        return vehicle;
    }

    public ArrayList<Vehicle> GetAllVehicles(Date BookFrom, Date BookTo)
    {
        SQLiteDatabase db  =  getReadableDatabase();
        Vehicle vehicle = new Vehicle();
        VehicleBooking vehicleBooking =  new VehicleBooking();
        ArrayList<Vehicle> allVehicle = new ArrayList<Vehicle>();
        ArrayList<VehicleBooking> allVehicleBooking = new ArrayList<VehicleBooking>();


        try
        {
            String DateFormat =  "yyyy-MM-dd HH:mm:ss";
            SimpleDateFormat sdf = new SimpleDateFormat(DateFormat);
            String GetAllVehicleSQL ="";
            if(BookFrom != null || BookTo != null )
            {
                String BookFromString = sdf.format(BookFrom);
                String BookToString = sdf.format(BookTo);

                GetAllVehicleSQL = "SELECT distinct v.Id,v.VehicleDescription, v.VehicleId,v.VehicleNumber,v.Model,v.Capacity FROM Vehicle v " +
                "WHERE v.VehicleId not in " +
                    "( " +
                            "SELECT  b.VehicleId FROM Vehicle v LEFT OUTER JOIN Booking b " +
                            "ON v.VehicleId = b.VehicleId " +
                            "WHERE (b.BookTo BETWEEN '" + BookFromString + "' and '" + BookToString + "') " +
                "or (b.PickUpDateTime  BETWEEN '" + BookFromString + "' and '" + BookToString + "') " +
                ")";
            }
            else
            {
                GetAllVehicleSQL = "SELECT * FROM Vehicle v " +
                        "LEFT OUTER JOIN Booking b ON v.VehicleId = b.VehicleId";
            }


            Date DefaultDateString = sdf.parse("1900-01-01 00:00:00");
            Cursor cursor = db.rawQuery(GetAllVehicleSQL,null);
            Boolean IsFirst = true;

            Calendar cal =  Calendar.getInstance();
            String VehiclenumberUsed ="";
            if (cursor != null )
            {
                if(cursor.getCount() > 0  && cursor.moveToFirst())
                {
                    do
                    {
                      String VehicleNumber = cursor.getString(cursor.getColumnIndex("VehicleNumber"));

                        if (IsFirst)
                        {
                            vehicle = new Vehicle(cursor.getInt(cursor.getColumnIndex("Id")),
                                    cursor.getString(cursor.getColumnIndex("VehicleId")),
                                    cursor.getString(cursor.getColumnIndex("VehicleNumber")),
                                    cursor.getString(cursor.getColumnIndex("VehicleDescription")),
                                    cursor.getInt(cursor.getColumnIndex("Capacity")),
                                    cursor.getString(cursor.getColumnIndex("Model")),
                                    new ArrayList<VehicleBooking>()
                            );
                            IsFirst = false;
                        }

                        if( !vehicle.getVehicleNumber().equals(VehicleNumber))
                        {
                            vehicle = new Vehicle(cursor.getInt(cursor.getColumnIndex("Id")),
                                    cursor.getString(cursor.getColumnIndex("VehicleId")),
                                    cursor.getString(cursor.getColumnIndex("VehicleNumber")),
                                    cursor.getString(cursor.getColumnIndex("VehicleDescription")),
                                    cursor.getInt(cursor.getColumnIndex("Capacity")),
                                    cursor.getString(cursor.getColumnIndex("Model")),
                                    new ArrayList<VehicleBooking>()
                                    );
                        }

                      if((BookFrom == null ||
                              BookTo ==  null) &&
                              vehicle.getVehicleNumber().equals( cursor.getString(cursor.getColumnIndex("VehicleNumber"))))
                      {
                          Date BookedFrom = cursor.getString(cursor.getColumnIndex("PickUpDateTime")) != null ? sdf.parse(
                                                        String.valueOf(
                                                                    cursor.getString(cursor.getColumnIndex("PickUpDateTime")))):
                                  DefaultDateString;


                          int RequiredHours = cursor.getInt(cursor.getColumnIndex("RequiredHours"));
                          cal.setTime(BookedFrom);
                          cal.add(Calendar.HOUR,RequiredHours);
                          Date BookedTo =  cal.getTime();

                          vehicleBooking  = new VehicleBooking((cursor.getString(cursor.getColumnIndex("VehicleId"))),
                                                               (cursor.getString(cursor.getColumnIndex("GlobalBookingTransactionId"))),
                                                               BookedFrom,
                                                               BookedTo,
                                                               (cursor.getInt(cursor.getColumnIndex("IsTravelAborted")) == 1) ? true : false,
                                                               (cursor.getInt(cursor.getColumnIndex("IsTravelComplete")) == 1) ? true : false,
                                                               vehicle
                                                                );

                          allVehicleBooking.add(vehicleBooking);
                          //vehicle.setVehicleBookingList(allVehicleBooking);
                          vehicle.getVehicleBookingList().add(vehicleBooking);

                      }

                        if(!allVehicle.contains(vehicle))
                        {
                            allVehicle.add(vehicle);
                        }
                    }
                    while(cursor.moveToNext());
                }
            }
        }
        catch(Throwable ex)
        {

            String S = ex.getMessage();
            System.out.println(String.format("Cab Booking Exception details :{%s}",S));
        }
        finally
        {
            db.close();
        }

        return allVehicle;
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

    private String GenerateBookingTrackingId(String PlaceofVisit, Date PickupDateTime, String BookingRequesterId)
    {
        String BookingTrackingId = "" ;

        String DateFormat = "yyyy-MM-dd HH:mm";
        SimpleDateFormat sdf = new SimpleDateFormat(DateFormat);

        try
        {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(PickupDateTime);

            String Month =String.valueOf((Integer)calendar.get(Calendar.MONTH) + 1 );
            String DayOfMonth = ((Integer)calendar.get(Calendar.DAY_OF_MONTH)).toString() ;
            String HourOfDay = ((Integer)calendar.get(Calendar.HOUR_OF_DAY)).toString();
            String Minute = ((Integer)calendar.get(Calendar.MINUTE)).toString();

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
                    BookingTrackingId = String.format("%s-%s-%d%s%s%s%s",
                            PlaceofVisit.substring(0,3).toUpperCase(),
                            BookingRequesterId,
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
                    BookingTrackingId = String.format("%s-%s-%d%s%s%s%s",
                            modifiedPlaceName,
                            BookingRequesterId,
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


    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();

        if(dbExist){
            //do nothing - database already exist
        }else{

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){

        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH + DATABASE_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        }catch(SQLiteException e){

            //database does't exist yet.

        }

        if(checkDB != null){

            checkDB.close();

        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{

        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException{

        //Open the database
        String myPath = DB_PATH + DB_NAME;
        dbi = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

    }

    @Override
    public synchronized void close() {

        if(dbi != null)
            dbi.close();

        super.close();

    }
/*
    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    */
}


// link 24-01-2017 - https://blog.reigndesign.com/blog/using-your-own-sqlite-database-in-android-applications/