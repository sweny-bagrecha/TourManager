package com.example.user.groupexpensetracker.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.user.groupexpensetracker.R;

public class AboutUs extends AppCompatActivity {
    TextView txtaboutus;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        txtaboutus=(TextView)findViewById(R.id.txtaboutus);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("About Us");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_navigation_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txtaboutus.setText("Tour Management is a android application designed to ease the pain of managing and reimbursing expenses for groups as well as tracking the location of group member (if whosoever got in trouble).Using Tour Management application, you can create any number of expense groups. Add your friends or colleagues or relatives as participants. It's easy to add participants just by entering the username and email id of the participant. It will send out invitations to collaborate who is already a member. And whosoever is not a member then the application will send the invitation message to join the group and they can sign up and begin managing expenses in your reimbursement list! Such group expenses can continue on through numerous situations, so add information about different people or different payments as time goes on. Adding expenses is flexible, and takes into account unique situations where expenses are not divided equally among participants. Tour Management also travels with you, helping you manage an unlimited number of currencies and exchange rates, and converting everything to one easy final count in the currency of your choice. This application provides a running current balance between everybody in the group, as well as your personal at-a-glance damage. With its expense compacting algorithm, you always see the minimum number of transactions required to balance out the group—allowing you to write the fewest checks, and keeping everything simple. Tour Management helps you to travel with you and keep the track of the other Group Member with the help of the GPS System.Its easy to track the group members just by connecting the group member in a group which will be an inbuilt functionality in Tour Management. To get in touch with the group members, chatting facility is also provided by the Tour Management. Tour Management is an ultimate combo of what the traveler needs for most. User Interface will make the users attract towards this application. The idea behind this application was to reduce some sort of headache from the users to calculate and most importantly many user are not able to request the other person to take money.\n" +
                "By, this application this will make it more easier and comfortable for the users to receive and pay legally with all rights and without hesitation. \n" +
                "\n" +
                "Innovative Functionality:\n" +
                "The appealing functionalities of this project are as follows:\n" +
                "•\tLogin  with Phone number\n" +
                "•\tSecurity- OTP facility \n" +
                "•\tSingle User can create group for multiple\n" +
                "•\tCalculator: Calculates what amount to pay and what to receive\n" +
                "•\tCurrency Convertor\n" +
                "•\tGCM (Google Cloud Messaging) notification \n" +
                "•\tSMS and CALL verification through Digits Library\n" +
                "•\tLocation Tracker\n" +
                "•\tGroup Chatting\n" +
                "N number of group members in single group\n");


}
}
