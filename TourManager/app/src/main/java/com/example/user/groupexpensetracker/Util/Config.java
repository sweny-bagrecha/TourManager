package com.example.user.groupexpensetracker.Util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.user.groupexpensetracker.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

/**
 * Created by user on 9/8/2016.
 */
public class Config {
    //    public static final String FIREBASE_URL = "https://groupexpensetracker-9142a.firebaseio.com/";
    public static final String FIREBASE_URL = "https://groupexpense-20529.firebaseio.com/";
//    public static final String FILEUPLOAD_URL = "gs://groupexpensetracker-9142a.appspot.com";

    public static final String FILEUPLOAD_URL = "gs://groupexpense-20529.appspot.com";


    public static final String USER_IMAGE_FOLDER = "userimages";
    public static final String TRIP_IMAGE_FOLDER="tripimages";


    //USER TABLE
    public static final String USER_TABLE = "User";
    public static final String USER_IMAGE = "imageurl";
    public static final String USER_PHONE_NO = "mobile_number";
    public static final String USER_NAME = "name";
//    public static final String USER_LAT="user_latitude";
//    public static final String USER_LONG="user_longitude";

    //FRIENDS TABLE
    public static final String FRIENDS_TABLE = "Friends";
    public static final String USER_WHO_ADDED_FRIEND_PHONE_NO = "added_mob_number";
    public static final String FRIEND_PHONE_NO = "friends_mob_number";
    public static final String FRIEND_NAME = "friends_name";

    //GROUP TABLE
    public static final String GROUP_TABLE = "Group_and_Trips";
    public static final String USER_WHO_ADDED_GROUP_PHONE_NO = "added_mobile_number";
    public static final String GROUP_ID = "groupid";
    public static final String GROUP_NAME = "groupname";
    public static final String GROUP_PARTICIPANTS = "number_of_participants";
    public static final String GROUP_DESCRIPTION="trip_description";

    //GROUP_MAPPING TABLE
    public static final String GROUP_MAPPING_TABLE = "Group_Mapping";
    public static final String USER_WHO_ADDED_FRIEND_IN_GROUP_PHONE_NO = "added_mobile_number";
    public static final String GROUP_MEMBER_PHONE_NO = "memberNumber";
    public static final String GROUP_MAPPING_ID = "group_mapping_id";

    //TRIP_MEMORIES TABLE
    public static final String TRIP_MEMORIES_TABLE="Trip_Memories";
    public static final String TRIP_ID="trip_id";
    public static final String TRIP_IMAGE="trip_image";

    //CHAT_ROOM_TABLE
    public static final String CHAT_TABLE="Chat_Message";
    public static final String CHAT_GROUP_ID="group_id";
    public static final String CHAT_MESSAGE_KEY="message_key";
    public static final String CHAT_MESSAGE="message";
    public static final String CHAT_MESSAGE_DATE_TIME="message_date_time";
    public static final String CHAT_USER_PHONE="user_phone_no";
    public static final String CHAT_USER_IMAGE="user_image";

    //USER_LAT_LON_TABLE
    public static final String USER_LAT_LON_TABLE="User_Lat_Lon_Table";
    public static final String USER_LATLON_ID="user_latid";
    public static final String USER_MOBILE_NO="user_mobile_no";
    public static final String USER_LATITUDE="user_latitude";
    public static final String USER_LONGITUDE="user_longitude";
    public static final String USER_NAMES="user_name";

    //    EXPENSE_EQUAL_TABLE
//    public static final String EXPENSE_EQUAL_TABLE="Equal_Expense";
//    public static final String EXPENSE_ID="expense_id";
//    public static final String EXPENSE_NAME="expense_name";
//    public static final String EXPENSE_DISTRIBUTION_TYPE="distribution_type";
//    public static final String EXPENSE_GROUP_NAME="group_name";
//    public static final String EXPENSE_TOTAL_AMOUNT="total_amount";
//    public static final String EXPENSE_EQUALLY_DISTRIBUTED_AMOUNT="equally_distributed_amount";


    public static final String EQUAL_EXPENSE_TABLE="Equally_Distributed_Expense";
    public static final String EXPENSE_ID="expense_id";
    public static final String EXPENSE_NAME="expense_name";
    public static final String EXPENSE_DISTRIBUTION_TYPE="distribution_type";
    public static final String EXPENSE_GROUP_NAME="group_name";
    public static final String EXPENSE_TOTAL_AMOUNT="total_amount";
    public static final String EXPENSE_EQUALLY_DISTRIBUTED_AMOUNT="equally_distributed_amount";



    //    EXPENSE_MANUAL_TABLE
    public static final String MANUAL_EXPENSE_TABLE="Manually_Distributed_Expense";
    //public static final String EXPENSE_ID="expense_id";
    //public static final String EXPENSE_NAME="expense_name";
    //public static final String EXPENSE_DISTRIBUTION_TYPE="distribution_type";
    //public static final String EXPENSE_GROUP_NAME="group_name";
    //public static final String EXPENSE_TOTAL_AMOUNT="total_amount";


//    MANUAL_EXPENSE_MAPPING_TABLE
    public static final String MANUAL_EXPENSE_MAPPING_TABLE="Manually_Distributed_Expense_Mapping_Table";
    public static final String EXPENSE_USER_NAME="user_name";
    public static final String EXPENSE_USER_AMOUNT="user_amount";
    public static final String EXPENSE_MAPPING_ID="expense_mapping_id";

    public static MaterialDialog.Builder builder = null;

    public static MaterialDialog dialog = null;

    public static void showDialog(Activity activity, String dailogTitle, String contentMessage) {
        builder = new MaterialDialog.Builder(activity)
                .title(dailogTitle)
                .content(contentMessage)
                .progress(true, 0)
                .progressIndeterminateStyle(true);
        dialog = builder.build();

        dialog.show();
    }

    public static void cancelDialog() {
        dialog.dismiss();
    }


}
