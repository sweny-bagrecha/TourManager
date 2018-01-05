package com.example.user.groupexpensetracker.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.groupexpensetracker.Util.GlobalData;
import com.example.user.groupexpensetracker.activity.AddGroupActivity;
import com.example.user.groupexpensetracker.activity.GroupDetailsActivity;
import com.example.user.groupexpensetracker.adapter.ChooseGroupMemberAdapter;
import com.example.user.groupexpensetracker.adapter.CustomAdapters;
import com.example.user.groupexpensetracker.Util.Config;
import com.example.user.groupexpensetracker.activity.DistributionExpenseActivity;
import com.example.user.groupexpensetracker.adapter.UserAdapter;
import com.example.user.groupexpensetracker.bean.Group;
import com.example.user.groupexpensetracker.bean.Members;
import com.example.user.groupexpensetracker.R;
import com.example.user.groupexpensetracker.bean.User;
import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class AddExpenseFragment extends Fragment {
    EditText edtexpensename, edttotalamount;
    TextView txtexpensename, txtexpensetype, txttotalamount, txtgroup,txtmembers;
    View view;
    Button btnNext;
    RecyclerView recyclerList;
    ArrayList<String> name;
    ArrayList<String> list;
    ArrayList<String> arrayList;
    Toolbar toolbar;
    int n;
    String value;
    ArrayList<String> groupmember;
    Firebase ref;
    private String my_sel_items;
    Query firebaseQuery;
    boolean israndomexists = true;
    DatabaseReference database;
    List<Group> groupdata = new ArrayList<>();
    Spinner spinner, spinnergroup;
    private DatabaseReference mDatabase;
    String phonenumber;
    ArrayList<String> groupIdslist;
    SharedPreferences sharedPreferences;
    int hasNoOfGroup = 0;
    int hasNumberOfGroup = 0;
    private String group_id = "";
    ArrayList<String> userMembersMobile;
    List<User> membersData;
    private ChooseGroupMemberAdapter userAdapter;
    List<User> groupData;
    String expenseName = "";
    String groupName = "";
    String distributionType = "";
    String totalExpense = "";
    List<Group> getParticipantsData;
    double edtvalue,totalvalue;
    String tot_value;
    String key="";
    String mappingValue;
    String distributedValue;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_expense, container, false);
        // firebase = new Firebase(Config.FIREBASE_URL);
        //     mDatabase = FirebaseDatabase.getInstance().getReference();


//        Firebase.setAndroidContext(getActivity());
        sharedPreferences = getActivity().getSharedPreferences(GlobalData.mysharedpreference, Context.MODE_PRIVATE);
        phonenumber = sharedPreferences.getString(GlobalData.Phonenumber, "N/A");

        Firebase.setAndroidContext(getActivity());

        my_sel_items = new String();
        groupmember = new ArrayList<String>();
        getParticipantsData = new ArrayList<>();

        list = new ArrayList<>();
        groupIdslist = new ArrayList<>();
        arrayList = new ArrayList<>();
        userMembersMobile = new ArrayList<>();
        membersData = new ArrayList<>();
        groupData = new ArrayList<>();


        recyclerList = (RecyclerView) v.findViewById(R.id.recyclerList);

        edtexpensename = (EditText) v.findViewById(R.id.edtexpensename);
        edttotalamount = (EditText) v.findViewById(R.id.edttotalamount);
        txtexpensename = (TextView) v.findViewById(R.id.txtexpensename);
        txttotalamount = (TextView) v.findViewById(R.id.txttotalamount);
        txtexpensetype = (TextView) v.findViewById(R.id.txtexpensetype);
        view=(View)v.findViewById(R.id.view);
        txtgroup = (TextView) v.findViewById(R.id.txtgroup);
        txtmembers=(TextView)v.findViewById(R.id.txtmembers);
        toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        spinnergroup = (Spinner) v.findViewById(R.id.spinnergroup);
        final String[] values =
                {"Equal Distribution", "Manual Distribution"};
        spinner = (Spinner) v.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if (position == 0) {
                    recyclerList.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            view.setVisibility(View.GONE);
                            txtmembers.setVisibility(View.GONE);
                            recyclerList.setVisibility(View.GONE);


                        }
                    }, n);
                } else if (position == 1) {

                    view.setVisibility(View.VISIBLE);
                    txtmembers.setVisibility(View.VISIBLE);
                    recyclerList.setVisibility(View.VISIBLE);
                    getUserMembers();


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        spinner.setAdapter(adapter);


        name = new ArrayList<>();
        getGroupsFromGroupMapping();


        spinnergroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                group_id = groupdata.get(i).getGroupid();
                Log.i("kinjal", "groupid " + group_id);

                GlobalData.groupInt = Integer.parseInt(getParticipantsData.get(i).getGroupparticipant());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        btnNext = (Button) v.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setIsrandomexists();

                if (isValidInput()) {
                    int position = spinner.getSelectedItemPosition();
                    if (position == 0) {

                        edtvalue = Double.parseDouble(edttotalamount.getText().toString());
                        totalvalue = edtvalue / GlobalData.groupInt;
                        tot_value = String.valueOf(totalvalue);
                        Log.i("KINJAL", "the edtvalue:" + edtvalue);
                        Log.i("KINJAL", "the totalvalue:" + totalvalue);

                        expenseName = edtexpensename.getText().toString();
                        groupName = spinnergroup.getSelectedItem().toString();
                        distributionType = spinner.getSelectedItem().toString();
                        totalExpense = edttotalamount.getText().toString();



                        Intent intent = new Intent(getActivity(), DistributionExpenseActivity.class);
                            intent.putStringArrayListExtra("array", list);
                            intent.putExtra("amount", edttotalamount.getText().toString());
                            intent.putExtra("divideopr", tot_value);


                           //checkIfGroupExists();
                        insertexpenseEqualDistribution(expenseName, distributionType, groupName, totalExpense,tot_value);
                            startActivity(intent);


                    } else

                    {
                        if (isValidMembers()) {
                            list.clear();

                            for (int i = 0; i < ChooseGroupMemberAdapter.selectedUserList.size(); i++) {
                                if (ChooseGroupMemberAdapter.selectedUserList.get(i).isSelected()) {

                                    list.add(groupData.get(i).getName());

                                }
                            }

                            Log.e("tanuja", "size   --->>" + list.size());
                            Log.e("tanuja", "group size   --->>" + groupmember.size());

                            edtvalue = Double.parseDouble(edttotalamount.getText().toString());
                            totalvalue = edtvalue / GlobalData.groupInt;
                            String tot_value = String.valueOf(totalvalue);
                            Log.i("KINJAL", "the edtvalue:" + edtvalue);
                            Log.i("KINJAL", "the totalvalue:" + totalvalue);


                            expenseName = edtexpensename.getText().toString();
                            groupName = spinnergroup.getSelectedItem().toString();
                            distributionType = spinner.getSelectedItem().toString();
                            totalExpense = edttotalamount.getText().toString();


                            Intent i = new Intent(getActivity(), DistributionExpenseActivity.class);
                            i.putStringArrayListExtra("array", list);

                            i.putExtra("amount", edttotalamount.getText().toString());
                            i.putExtra("divideopr", edtvalue);

                            i.putExtra("expensename", expenseName);
                            Log.e("KINJAL", "expensename   --->>" + expenseName);

                            i.putExtra("groupname", groupName);
                            Log.e("KINJAL", "groupname   --->>" + groupName);

                            i.putExtra("distributiontype", distributionType);
                            Log.e("KINJAL", "distributiontype   --->>" + distributionType);

                            i.putExtra("totalexpense", totalExpense);
                            Log.e("KINJAL", "totalexpense   --->>" + totalExpense);

                            i.putExtra("divideopr", tot_value);


                            startActivity(i);
                        }


                    }
                }
            }
        });



        return v;

    }

    private boolean isValidInput() {
        if (edtexpensename.getText().toString().trim().length() == 0) {
            edtexpensename.requestFocus();
            edtexpensename.setError("Please Enter Expense Name");
            return false;
        }
        else if (edttotalamount.getText().toString().trim().length() == 0) {
            edttotalamount.requestFocus();
            edttotalamount.setError("Please Enter Total Amount");
            return false;
        }
        return true;
    }

    private boolean isValidMembers()
    {
        if (ChooseGroupMemberAdapter.selectedUserList.size()==0)
    {
        Toast.makeText(getContext(),"Please select members",Toast.LENGTH_SHORT).show();
        return false;
    }
        return true;
    }

//    private boolean checkIfGroupExists() {
//
//        Log.i("KINJAL","I M IN THE METHOD=============== ");
//        mDatabase = FirebaseDatabase.getInstance().getReference().child(Config.EXPENSE_EQUAL_TABLE);
//
//        Log.i("KINJAL","VALUE OF KEY========>>>>>>>>> " +distributionType);
////        Log.i("KINJAL","VALUE OF EXPENSE ID==========>>>>>>> " +Config.EXPENSE_ID.toString());
//        firebaseQuery = mDatabase.orderByChild(Config.EXPENSE_DISTRIBUTION_TYPE).equalTo(distributionType);
//
////        Log.i("KINJAL","VALUE OF EXPENSE ID==========>>>>>>> " +Config.EXPENSE_ID.toString());
//        Log.i("KINJAL","VALUE OF KEY========>>>>>>>>> " +distributionType);
//
//        firebaseQuery.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//
//                if (snapshot.getChildrenCount() > 0) {
//                    for (com.google.firebase.database.DataSnapshot postSnapshot : snapshot.getChildren()) {
//
//                        Log.i("KINJAL","I M IN THE FOR LOOP::::::::::::::::::::::: ");
//                        Log.i("KINJAL","KEY........................." +groupName);
//                        if (groupName.equalsIgnoreCase(postSnapshot.child(Config.EXPENSE_GROUP_NAME).getValue().toString())) {
//
//                            Log.i("KINJAL", "I M IN THE MAIN LOOP--------------->>>>>>> ");
//                            String value=postSnapshot.child(Config.EXPENSE_TOTAL_AMOUNT).getValue().toString();
//                            mappingValue = totalExpense + value;
//                            //String tempStr = "+91" + edtNumber.getText().toString();
//                            Log.e("KINJAL", "MAPPING VALUE ===== " + mappingValue);
//
//                            String dvalue=postSnapshot.child(Config.EXPENSE_EQUALLY_DISTRIBUTED_AMOUNT).getValue().toString();
//                            distributedValue = String.valueOf(totalvalue) + dvalue;
//                            Log.e("KINJAL", "DISTRIBUTED VALUE ===== " + distributedValue);
//
//                            updateexpenseEqualDistribution(expenseName, distributionType, groupName, mappingValue, distributedValue);
//                            break;
//                        }else{
//
//                                insertexpenseEqualDistribution(expenseName, distributionType, groupName, totalExpense, tot_value);
//
//                            }
//
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        return false;
//    }



    private int setIsrandomexists() {


        israndomexists = false;
//        setIsrandomexists();

        database = FirebaseDatabase.getInstance().getReference().child("groupexpensemappingtable");
        firebaseQuery = database.orderByChild("expensename");
        firebaseQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int random = new Random().nextInt(100);

                if (snapshot.getChildrenCount() > 0) {
//                        if(finalI !=userMembersMobile.size()-1)
//                        membersData.clear();


                    boolean exists = true;

                    for (com.google.firebase.database.DataSnapshot postSnapshot : snapshot.getChildren()) {
                        if (postSnapshot.hasChild("expenseid")) {
                            String temp = postSnapshot.child("expenseid").getValue().toString();
                            if (random == Integer.parseInt(temp)) {
                                exists = true;
                                random = new Random().nextInt(100);
                                break;
                            } else {
                                exists = false;
                            }
                        }
                    }

                    if (!exists) {
//                        insertexpense(edtexpensename.getText().toString(), edttotalamount.getText().toString(), spinner.getSelectedItem().toString(), spinnergroup.getSelectedItem().toString(), random);
//                        expenseName=edtexpensename.getText().toString();
//                        groupName=spinnergroup.getSelectedItem().toString();
//                        distributionType=spinner.getSelectedItem().toString();
//                        totalExpense=edttotalamount.getText().toString();
                    } else {
                        Toast.makeText(getActivity(), "Please try again", Toast.LENGTH_LONG).show();
                    }

                    firebaseQuery.removeEventListener(this);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return 0;
    }

    private void getGroupsFromGroupMapping() {
        //Config.showDialog(getActivity(), getString(R.string.loadinggroups), getString(R.string.please_wait));
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Config.GROUP_MAPPING_TABLE);
        firebaseQuery = mDatabase.orderByChild(Config.GROUP_MEMBER_PHONE_NO).equalTo(phonenumber);
        firebaseQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                if (snapshot.getChildrenCount() > 0) {
                    groupIdslist.clear();

                    for (com.google.firebase.database.DataSnapshot postSnapshot : snapshot.getChildren()) {
                        if (postSnapshot.hasChild(Config.GROUP_ID)) {
                            String temp = postSnapshot.child(Config.GROUP_ID).getValue().toString();

                            if (!groupIdslist.contains(temp)) {
                                groupIdslist.add(temp);
                                hasNoOfGroup++;

                            }

                        } else {
                            Config.cancelDialog();
                        }

                    }

                    getgroups();

                } else {
//                    Config.cancelDialog();


                }
                firebaseQuery.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Config.cancelDialog();

            }
        });

    }

    private void getgroups() {

        mDatabase = FirebaseDatabase.getInstance().getReference().child(Config.GROUP_TABLE);
        arrayList.clear();
        groupdata.clear();

        for (int i = 0; i < groupIdslist.size(); i++) {

            firebaseQuery = mDatabase.orderByChild(Config.GROUP_ID).equalTo(groupIdslist.get(i));
            firebaseQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    if (snapshot.getChildrenCount() > 0) {

                        for (com.google.firebase.database.DataSnapshot postSnapshot : snapshot.getChildren()) {
                            if (postSnapshot.hasChild(Config.GROUP_NAME)) {
                                String temp = postSnapshot.child(Config.GROUP_PARTICIPANTS).getValue().toString();
                                groupdata.add(new Group(postSnapshot.child(Config.GROUP_ID).getValue().toString(), postSnapshot.child(Config.GROUP_NAME).getValue().toString(), temp + " participants"));
                                getParticipantsData.add(new Group(postSnapshot.child(Config.GROUP_ID).getValue().toString(), postSnapshot.child(Config.GROUP_NAME).getValue().toString(), temp));
                                arrayList.add(postSnapshot.child(Config.GROUP_NAME).getValue().toString());
                                Log.e("sweny", String.valueOf(arrayList));
                                if (hasNoOfGroup == arrayList.size()) {


                                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, arrayList);
                                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                    spinnergroup.setAdapter(arrayAdapter);
                                    spinnergroup.setSelection(0);

                                    group_id = groupdata.get(0).getGroupid();

                                    // Config.cancelDialog();

                                }
                            }

                        }
                    } else {
                        Config.cancelDialog();

                    }

                    firebaseQuery.removeEventListener(this);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                    Config.cancelDialog();
                }
            });
        }
    }

    private void getUserMembers() {

        //Config.showDialog(getActivity(), getString(R.string.loadingFriend), getString(R.string.please_wait));
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Config.GROUP_MAPPING_TABLE);
        final Query firebase_Query = mDatabase.orderByChild(Config.GROUP_ID).equalTo(group_id);

        firebase_Query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                Log.i("niral", "FRIENDS " + group_id);
                Log.i("niral", "FRIENDS  " + snapshot.toString());

                if (snapshot.getChildrenCount() > 0) {
                    userMembersMobile.clear();
                    groupData.clear();
                    for (com.google.firebase.database.DataSnapshot postSnapshot : snapshot.getChildren()) {
                        if (postSnapshot.hasChild(Config.GROUP_MEMBER_PHONE_NO)) {

                            hasNumberOfGroup++;
                            Log.i("niral", "no of groups " + hasNumberOfGroup);
                            userMembersMobile.add(postSnapshot.child(Config.GROUP_MEMBER_PHONE_NO).getValue().toString());
                            //groupData.add(new User(postSnapshot.child(Config.GROUP_ID).getValue().toString(), postSnapshot.child(Config.GROUP_NAME).getValue().toString(), postSnapshot.child(Config.GROUP_PARTICIPANTS).getValue().toString()));
                        }
                    }
                    if (userMembersMobile.size() > 0) {
                        Log.i("niral", "FRIENDS " + group_id);
                        getMemberDetails();
                    } else {

                        Config.cancelDialog();
//                        llNoFriendFound.setVisibility(View.VISIBLE);
//                        recyclerParticipants.setVisibility(View.GONE);
                    }
                } else {
                    Config.cancelDialog();
//                    llNoFriendFound.setVisibility(View.VISIBLE);
//                    recyclerParticipants.setVisibility(View.GONE);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getMemberDetails() {

        for (int i = 0; i < userMembersMobile.size(); i++) {

            mDatabase = FirebaseDatabase.getInstance().getReference().child(Config.USER_TABLE);
            firebaseQuery = mDatabase.orderByChild(Config.USER_PHONE_NO).equalTo(userMembersMobile.get(i).toString());
            firebaseQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.getChildrenCount() > 0) {

                        Log.i("niral", "FRIENDS===  " + snapshot.toString());

//                        groupData.clear();
                        for (com.google.firebase.database.DataSnapshot postSnapshot : snapshot.getChildren()) {
                            if (postSnapshot.hasChild(Config.USER_PHONE_NO)) {
                                groupData.add(new User(postSnapshot.child(Config.USER_NAME).getValue().toString(), postSnapshot.child(Config.USER_PHONE_NO).getValue().toString(), postSnapshot.child(Config.USER_IMAGE).getValue().toString()));
                                Log.i("niral", "FRIENDS::::: " + groupData.size());
                            }
                        }
                        if (hasNumberOfGroup == groupData.size()) {

                            Log.i("niral", "I am in if=== " + groupData.size());

                            userAdapter = new ChooseGroupMemberAdapter(getActivity(), groupData);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            recyclerList.setLayoutManager(linearLayoutManager);
                            recyclerList.setAdapter(userAdapter);
//                            Config.cancelDialog();


                        }


                    } else {
//                        Config.cancelDialog();
//                        llNoFriendFound.setVisibility(View.VISIBLE);
//                        recyclerParticipants.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Config.cancelDialog();
                }
            });

        }
    }

    private void insertexpenseEqualDistribution(String expensename, String distributiontype, String groupname, String totalexpense,String totalValue) {
        ref = new Firebase(Config.FIREBASE_URL);

        final Firebase postRefEqualExpense = ref.child(Config.EQUAL_EXPENSE_TABLE);
        key = postRefEqualExpense.child(Config.EQUAL_EXPENSE_TABLE).push().getKey();

        Map<String, String> post1 = new HashMap<String, String>();
        post1.put(Config.EXPENSE_ID, key);
        post1.put(Config.EXPENSE_NAME, expensename);
        post1.put(Config.EXPENSE_DISTRIBUTION_TYPE, distributiontype);
        post1.put(Config.EXPENSE_GROUP_NAME, groupname);
        post1.put(Config.EXPENSE_TOTAL_AMOUNT, totalexpense);
        post1.put(Config.EXPENSE_EQUALLY_DISTRIBUTED_AMOUNT,totalValue);

        postRefEqualExpense.push().setValue(post1);

    }

//    private void updateexpenseEqualDistribution(String expensename, String distributiontype, String groupname, String mappingvalue,String totalValue) {
//        ref = new Firebase(Config.FIREBASE_URL);
//
//        final Firebase postRefEqualExpense = ref.child(Config.EXPENSE_EQUAL_TABLE);
//        key = postRefEqualExpense.child(Config.EXPENSE_EQUAL_TABLE).push().getKey();
//
//        Map<String, String> post1 = new HashMap<String, String>();
//        post1.put(Config.EXPENSE_ID, key);
//        post1.put(Config.EXPENSE_NAME, expensename);
//        post1.put(Config.EXPENSE_DISTRIBUTION_TYPE, distributiontype);
//        post1.put(Config.EXPENSE_GROUP_NAME, groupname);
//        post1.put(Config.EXPENSE_TOTAL_AMOUNT, mappingvalue);
//        post1.put(Config.EXPENSE_EQUALLY_DISTRIBUTED_AMOUNT,totalValue);
//
//        postRefEqualExpense.push().setValue(post1);
//
//    }

}














