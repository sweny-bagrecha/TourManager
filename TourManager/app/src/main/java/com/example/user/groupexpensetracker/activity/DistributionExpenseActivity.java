package com.example.user.groupexpensetracker.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.groupexpensetracker.Util.Config;
import com.example.user.groupexpensetracker.adapter.ChooseGroupMemberAdapter;
import com.example.user.groupexpensetracker.adapter.DistributionExpenseAdapter;
import com.example.user.groupexpensetracker.R;
import com.example.user.groupexpensetracker.bean.User;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DistributionExpenseActivity extends AppCompatActivity {
    LinearLayout linearlayout1,linearlayout2;
    TextView txtexpenseamount, txtamount, txtMembers;
    View view;
    DistributionExpenseAdapter distributionExpenseAdapter;
    ArrayList<User> USER;
    ArrayList<String> member;
    ListView listReceive;
    ListView listPay;
    ListView listview;
    Toolbar toolbar;
    Button btnSubmit;
    String expenseName;
    String groupName;
    String distributionType;
    String totalExpense;
    String userName;
    String userAmount;
    String ManualExpenseKey;
    int countOfUserWhichAdded = 0;
    String receive;
    String pay;
    ArrayList<String> arraylistReceive=new ArrayList<String>();
    ArrayList<String> arraylistPay=new ArrayList<String>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distribution_expense);

        linearlayout1=(LinearLayout)findViewById(R.id.linearlayout1);
        linearlayout2=(LinearLayout)findViewById(R.id.linearlayout2);

        listview = (ListView) findViewById(R.id.listview);
        listReceive=(ListView)findViewById(R.id.listReceive);
        listPay=(ListView)findViewById(R.id.listPay);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Distributed Expense");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_navigation_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        txtamount = (TextView) findViewById(R.id.txtamount);
        txtMembers = (TextView) findViewById(R.id.txtMembers);

        view = (View) findViewById(R.id.view);

        member = getIntent().getStringArrayListExtra("array");
        USER = new ArrayList<>();
        txtexpenseamount = (TextView) findViewById(R.id.txtexpenseamount);

        for (int i = 0; i < member.size(); i++) {
            String select = ChooseGroupMemberAdapter.selectedUserList.get(i).getName().toString();
            USER.add(new User(select, "0"));
        }


        distributionExpenseAdapter = new DistributionExpenseAdapter(DistributionExpenseActivity.this, USER);
        listview.setAdapter(distributionExpenseAdapter);
        distributionExpenseAdapter.getCount();

        if (DistributionExpenseAdapter.Userlist.size() == 0) {
            txtamount.setVisibility(View.VISIBLE);
            txtMembers.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
            listview.setVisibility(View.GONE);
            btnSubmit.setVisibility(View.GONE);
        }


        Intent i = getIntent();
        String amount = i.getStringExtra("amount");
        txtexpenseamount.setText(amount);

        expenseName = i.getStringExtra("expensename");
        Log.e("KINJAL---", "expensename   --->>" + expenseName);

        groupName = i.getStringExtra("groupname");
        Log.e("KINJAL---", "groupname   --->>" + groupName);

        distributionType = i.getStringExtra("distributiontype");
        Log.e("KINJAL---", "distributiontype   --->>" + distributionType);

        totalExpense = i.getStringExtra("totalexpense");
        Log.e("KINJAL---", "totalexpense   --->>" + totalExpense);

        txtamount.setText("Equal Distributed Amount is:  " + i.getStringExtra("divideopr"));


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                insertexpense(expenseName, distributionType, groupName, totalExpense);

                linearlayout1.setVisibility(View.GONE);
                linearlayout2.setVisibility(View.VISIBLE);

                int i1;
                float te = 0;

                for (i1 = 0; i1 < DistributionExpenseAdapter.Userlist.size(); i1++) {
                    te = te + Float.parseFloat(distributionExpenseAdapter.Userlist.get(i1).getNumber());

                }
                float rr[] = new float[i1];
                te = te / DistributionExpenseAdapter.Userlist.size();

                for (i1 = 0; i1 < DistributionExpenseAdapter.Userlist.size(); i1++) {
                    float v = Float.parseFloat(distributionExpenseAdapter.Userlist.get(i1).getNumber());
                    v = v - te;
                    rr[i1] = v;

                    Log.i("kinjal", "addition" + v);
                    Log.i("kinjal", "addition" + te);

                    userName = String.valueOf(distributionExpenseAdapter.getName(i1));
                    userAmount = String.valueOf(v);

                    Log.i("kinjal", "xyz" + userAmount);


                    Log.e("KINJAL", "NAME : " + distributionExpenseAdapter.Userlist.get(i1).getName());
                    Log.e("project kinjal", "AMONUT : " + distributionExpenseAdapter.Userlist.get(i1).getNumber());

                    Log.i("KINJAL", "ARRAYLIST SIZE---- " + distributionExpenseAdapter.Userlist.size());


                    insertUserExpense(ManualExpenseKey, userName, userAmount);
                }




                float ans[][];
                ans = calc(rr);
                i1 = rr.length;
                Log.i("kinjal", "xyz1  " + String.valueOf(i1));

                for (int i = 0; i < i1; i++)

                {
                    for (int j = 0; j < i1; j++) {
                        Log.i("kinjal", "xyz2  " + String.valueOf(ans[i][j]));
                        if (ans[i][j] > 0) {
                            Log.i("kinjal", "xyz   " + String.valueOf(distributionExpenseAdapter.getName(i)) + " receives " + String.valueOf(ans[i][j]) + " from  " + String.valueOf(distributionExpenseAdapter.getName(j)));

                            receive=String.valueOf(distributionExpenseAdapter.getName(i)) + " receives " + String.valueOf(ans[i][j]) + " from  " + String.valueOf(distributionExpenseAdapter.getName(j));
                            Log.i("kinjal","  receive " +receive);

                            arraylistReceive.add(receive);
                            Log.i("kinjal"," receive array size " +arraylistReceive.size());

                            ArrayAdapter<String> adapterReceive=new ArrayAdapter<String>(DistributionExpenseActivity.this,android.R.layout.simple_list_item_1,arraylistReceive);
                            listReceive.setAdapter(adapterReceive);

                        } else if (ans[i][j] < 0) {
                            Log.i("kinjal", "xyz   " + String.valueOf(distributionExpenseAdapter.getName(i)) + " pays  " + (String.valueOf(-ans[i][j])) + " to  " + String.valueOf(distributionExpenseAdapter.getName(j)));

                            pay=String.valueOf(distributionExpenseAdapter.getName(i)) + " pays  " + (String.valueOf(-ans[i][j])) + " to  " + String.valueOf(distributionExpenseAdapter.getName(j));
                            Log.i("kinjal"," pay " +pay);

                            arraylistPay.add(pay);
                            Log.i("kinjal"," par array size " +arraylistPay.size());

                            ArrayAdapter<String> adapterPay=new ArrayAdapter<String>(DistributionExpenseActivity.this,android.R.layout.simple_list_item_1,arraylistPay);
                            listPay.setAdapter(adapterPay);

                        }


                    }

                }



            }

        });

    }

    public static float[][] calc(float[] val)
    {
        int len = val.length;
        float[][]arr=new float[len][len];
        for(int i=0;i<len;i++)
        {
            float min=Float.MAX_VALUE,max=Float.MIN_VALUE;
            int m=0,n=0;
            for(int j=0;j<len;j++)
            {
                if(val[j]>max)
                {
                    max=val[j];
                    m=j;
                }
                if(val[j]<min)
                {
                    min=val[j];
                    n=j;
                }
            }
            if(max==0)
                break;
            if(max>-min)
            {
                arr[m][n]=-min;
                arr[n][m]=min;
                val[m]+=min;
                val[n]=0;
            }
            else
            {
                arr[m][n]=max;
                arr[n][m]=-max;
                val[m]=0;
                val[n]+=max;
            }
        }

        return arr;
    }

    private void insertexpense(  String expensename, String distributiontype, String groupname, String totalexpense) {
        Firebase ref = new Firebase(Config.FIREBASE_URL);

        final Firebase postRef = ref.child(Config.MANUAL_EXPENSE_TABLE);
        ManualExpenseKey = postRef.child(Config.MANUAL_EXPENSE_TABLE).push().getKey();


        Map<String, String> post1 = new HashMap<String, String>();
        post1.put(Config.EXPENSE_ID, ManualExpenseKey);
        post1.put(Config.EXPENSE_NAME, expensename);
        post1.put(Config.EXPENSE_DISTRIBUTION_TYPE, distributiontype);
        post1.put(Config.EXPENSE_GROUP_NAME, groupname);
        post1.put(Config.EXPENSE_TOTAL_AMOUNT, totalexpense);

        postRef.push().setValue(post1);

    }

    private void insertUserExpense(String expenseId, String username, String useramount) {

        Firebase ref = new Firebase(Config.FIREBASE_URL);
        final Firebase postRefMapping = ref.child(Config.MANUAL_EXPENSE_MAPPING_TABLE);

        String keyMapping = postRefMapping.child(Config.MANUAL_EXPENSE_MAPPING_TABLE).push().getKey();

        Map<String, String> post1 = new HashMap<String, String>();
        post1.put(Config.EXPENSE_ID,expenseId);
        post1.put(Config.EXPENSE_USER_NAME, username);
        post1.put(Config.EXPENSE_USER_AMOUNT, useramount);
        post1.put(Config.EXPENSE_MAPPING_ID, keyMapping);

        Log.e("KINJAL---","EXPENSE ID---- " +ManualExpenseKey);
        Log.e("KINJAL---", "username   --->>" + userName);
        Log.e("KINJAL---", "useramount   --->>" + userAmount);



        postRefMapping.push().setValue(post1);

//        postRefMapping.addValueEventListener(new com.firebase.client.ValueEventListener() {
//            @Override
//            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
//                countOfUserWhichAdded++;
//
//                if (countOfUserWhichAdded == DistributionExpenseAdapter.Userlist.size()) {
//                    postRefMapping.removeEventListener(this);
//                    Toast.makeText(getApplicationContext(), getString(R.string.toastExpenseUser), Toast.LENGTH_SHORT).show();
//                    finish();
//
//                } else {
//                    postRefMapping.removeEventListener(this);
////                    Config.cancelDialog();
//                }
//
//            }

//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
    }
}
