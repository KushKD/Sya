package com.dit.himachal.rms.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dit.himachal.rms.R;
import com.dit.himachal.rms.adapter.ArchivedCaseListAdapter;
import com.dit.himachal.rms.databases.DatabaseHandler;
import com.dit.himachal.rms.enums.TaskType;
import com.dit.himachal.rms.generic.Generic_Async_Get;
import com.dit.himachal.rms.interfaces.AsyncTaskListenerObjectGet;
import com.dit.himachal.rms.modal.AdvocateListPojo;
import com.dit.himachal.rms.modal.ArchivedCasePojo;
import com.dit.himachal.rms.modal.GetDataPojo;
import com.dit.himachal.rms.modal.OfflineDataModel;
import com.dit.himachal.rms.presentation.CustomDialog;
import com.dit.himachal.rms.utilities.AppStatus;
import com.dit.himachal.rms.utilities.CommonUtils;
import com.dit.himachal.rms.utilities.Econstants;
import com.dit.himachal.rms.utilities.Preferences;
import com.dit.himachal.rms.utilities.PreventScreenshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

public class ArchiveCaseActivity extends AppCompatActivity implements AsyncTaskListenerObjectGet {

    private EditText edit_text_search;
    private ListView list;
    CustomDialog CD = new CustomDialog();
    List<ArchivedCasePojo> archivedCasePojoList = null;
    ArchivedCaseListAdapter archivedCaseListAdapter = null;
    SwipeRefreshLayout pullToRefresh;
    TextView header;


    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive_case);

        PreventScreenshot.on(ArchiveCaseActivity.this);

        list = findViewById(R.id.list);
        edit_text_search = findViewById(R.id.edit_text_search);
        pullToRefresh = findViewById(R.id.pullToRefresh);
        header = findViewById(R.id.heading);


        header.setText("Archive Cases");
        if (AppStatus.getInstance(ArchiveCaseActivity.this).isOnline()) {
            GetDataPojo object = new GetDataPojo();
            object.setUrl(Econstants.url);
            object.setMethord(Econstants.methordGetArchiveCases);
            object.setMethordHash(Econstants.encodeBase64(Econstants.methordGetArchiveCasesToken + Econstants.seperator + CommonUtils.getTimeStamp())); //Encode Base64 TODO
            object.setTaskType(TaskType.Get_Archive_Cases);
            object.setTimeStamp(CommonUtils.getTimeStamp());
            List<String> parameters = new ArrayList<>();
            parameters.add(Preferences.getInstance().role_id);
            parameters.add(Preferences.getInstance().user_id);
            object.setParameters(parameters);
            object.setBifurcation("Get_Archive_Cases"+Preferences.getInstance().user_id);

            new Generic_Async_Get(
                    ArchiveCaseActivity.this,
                    ArchiveCaseActivity.this,
                    TaskType.Get_Archive_Cases).
                    execute(object);
        } else {
            //  CD.showDialog(ArchiveCaseActivity.this,"Internet Not Available");
            DatabaseHandler DB = new DatabaseHandler(ArchiveCaseActivity.this);
            Log.e("Get_Archive_Cases Start", Integer.toString(DB.GetAllOfflineDataViaFunction(TaskType.GET_PENDING_MEMO_LIST_CABINET.toString(), Preferences.getInstance().user_id, Preferences.getInstance().role_id, "Get_Archive_Cases"+Preferences.getInstance().user_id).size()));
            if (DB.GetAllOfflineDataViaFunction(TaskType.Get_Archive_Cases.toString(), Preferences.getInstance().user_id, Preferences.getInstance().role_id, "Get_Archive_Cases"+Preferences.getInstance().user_id).size() > 0) {
                //Show Events
                try {

                    showData(DB.GetAllOfflineDataViaFunction(TaskType.Get_Archive_Cases.toString(), Preferences.getInstance().user_id, Preferences.getInstance().role_id, "Get_Archive_Cases"+Preferences.getInstance().user_id).get(0));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                CD.showDialogCloseActivity(ArchiveCaseActivity.this, Econstants.NO_DATA);
            }
            Toast.makeText(getApplicationContext(), "Application running in Offline Mode.", Toast.LENGTH_LONG).show();

        }





        edit_text_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                archivedCaseListAdapter.getFilter().filter(s.toString(), new Filter.FilterListener() {
                    public void onFilterComplete(int count) {
                        if (count == 0) {
                            list.setVisibility(View.GONE);
                        } else {
                            list.setVisibility(View.VISIBLE);
                        }

                    }
                });
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                // PSDashboardDetailsManufacturing.this.adapter.getFilter().filter(s);


            }
        });


        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (AppStatus.getInstance(ArchiveCaseActivity.this).isOnline()) {
                    GetDataPojo object = new GetDataPojo();
                    object.setUrl(Econstants.url);
                    object.setMethord(Econstants.methordGetArchiveCases);
                    object.setMethordHash(Econstants.encodeBase64(Econstants.methordGetArchiveCasesToken + Econstants.seperator + CommonUtils.getTimeStamp())); //Encode Base64 TODO
                    object.setTaskType(TaskType.Get_Archive_Cases);
                    object.setTimeStamp(CommonUtils.getTimeStamp());
                    List<String> parameters = new ArrayList<>();
                    parameters.add(Preferences.getInstance().role_id);
                    parameters.add(Preferences.getInstance().user_id);
                    object.setParameters(parameters);

                    new Generic_Async_Get(
                            ArchiveCaseActivity.this,
                            ArchiveCaseActivity.this,
                            TaskType.Get_Archive_Cases).
                            execute(object);
                } else {
                    //  CD.showDialog(ArchiveCaseActivity.this,"Internet Not Available");
                    DatabaseHandler DB = new DatabaseHandler(ArchiveCaseActivity.this);
                    Log.e("Get_Archive_Cases Start", Integer.toString(DB.GetAllOfflineDataViaFunction(TaskType.GET_PENDING_MEMO_LIST_CABINET.toString(), Preferences.getInstance().user_id, Preferences.getInstance().role_id, "Get_Archive_Cases"+Preferences.getInstance().user_id).size()));
                    if (DB.GetAllOfflineDataViaFunction(TaskType.Get_Archive_Cases.toString(), Preferences.getInstance().user_id, Preferences.getInstance().role_id, "Get_Archive_Cases"+Preferences.getInstance().user_id).size() > 0) {
                        //Show Events
                        try {

                            showData(DB.GetAllOfflineDataViaFunction(TaskType.Get_Archive_Cases.toString(), Preferences.getInstance().user_id, Preferences.getInstance().role_id, "Get_Archive_Cases"+Preferences.getInstance().user_id).get(0));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        CD.showDialogCloseActivity(ArchiveCaseActivity.this, Econstants.NO_DATA);
                    }
                    Toast.makeText(getApplicationContext(), "Application running in Offline Mode.", Toast.LENGTH_LONG).show();

                }


                pullToRefresh.setRefreshing(false);

            }

        });

        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition = (list == null || list.getChildCount() == 0) ? 0 : list.getChildAt(0).getTop();
                pullToRefresh.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArchivedCasePojo archiveCase = (ArchivedCasePojo) parent.getItemAtPosition(position);
               CD.showDialog_moredetails_causeList(ArchiveCaseActivity.this,archiveCase);

            }
        });
    }

    private void showData (OfflineDataModel forwarded) throws JSONException {

        if (forwarded.getFunctionName().equalsIgnoreCase(TaskType.Get_Archive_Cases.toString())) {

            Log.e("Result == ", forwarded.getResponse());
            Object json = new JSONTokener(forwarded.getResponse()).nextValue();
            if (json instanceof JSONObject) {
                Log.e("Json Object", "Object");
            } else if (json instanceof JSONArray) {
                Log.e("Json Object", "Object");
                JSONArray arrayReports = new JSONArray(forwarded.getResponse());
                Log.e("arrayReports", arrayReports.toString());
                //No Record Found  StatusMessage
                if (arrayReports.length() > 0) {
                    //ReportsModelPojo
                    JSONObject object = arrayReports.getJSONObject(0);
                    //  if (!Econstants.decodeBase64(object.optString("StatusMessage")).equalsIgnoreCase("Incorrect OTP, please enter correct OTP!!.")) {
                    archivedCasePojoList = new ArrayList<>();

                    for (int i = 0; i < arrayReports.length(); i++) {
                        ArchivedCasePojo memoPojo = new ArchivedCasePojo();
                        JSONObject objectx = arrayReports.getJSONObject(i);


                        memoPojo.setCaseId(Econstants.decodeBase64(objectx.optString("CaseId")));
                        memoPojo.setCaseNo(Econstants.decodeBase64(objectx.optString("CaseNo")));
                        memoPojo.setCaseSubType(Econstants.decodeBase64(objectx.optString("CaseSubType")));
                        memoPojo.setCaseTitle(Econstants.decodeBase64(objectx.optString("CaseTitle")));
                        memoPojo.setCaseType(Econstants.decodeBase64(objectx.optString("CaseType")));
                        memoPojo.setCaseYear(Econstants.decodeBase64(objectx.optString("CaseYear")));
                        memoPojo.setCourtId(Econstants.decodeBase64(objectx.optString("CourtId")));
                        memoPojo.setInstitutionDate(Econstants.decodeBase64(objectx.optString("InstitutionDate")));
                        memoPojo.setStatusMessage(Econstants.decodeBase64(objectx.optString("StatusMessage")));

                        if (!memoPojo.getStatusMessage().equalsIgnoreCase("No Record Found")) {
                            archivedCasePojoList.add(memoPojo);
                        }


                    }

                    if (archivedCasePojoList.size() > 0) {
                        archivedCaseListAdapter = new ArchivedCaseListAdapter(ArchiveCaseActivity.this, archivedCasePojoList);
                        list.setAdapter(archivedCaseListAdapter);
                        list.setTextFilterEnabled(true);
                        edit_text_search.setVisibility(View.VISIBLE);

                        Log.e("Data", archivedCasePojoList.toString());
                    } else {
                        CD.showDialogCloseActivity(ArchiveCaseActivity.this, "The Request was Successful. No Records Found.");
                    }


                } else {
                    CD.showDialogCloseActivity(ArchiveCaseActivity.this, "The Request was Successful. No Records Found.");
                }
            }


        }
    }




    @Override
    protected void onStop () {
        // PreventScreenshot.on(CabinetMemoListByRoleActivity.this);
        super.onStop();
    }

    @Override
    public void onSaveInstanceState (@NonNull Bundle outState, @NonNull PersistableBundle
            outPersistentState){
        // PreventScreenshot.on(CabinetMemoListByRoleActivity.this);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onPause () {
        // PreventScreenshot.on(CabinetMemoListByRoleActivity.this);
        super.onPause();

    }

    @Override
    public void onTaskCompleted (OfflineDataModel result, TaskType taskType) throws
            JSONException {


        if (taskType == TaskType.Get_Archive_Cases) {

            if (result.getHttpFlag().equalsIgnoreCase(Econstants.success)) {
                //Save the rsult to Database
                DatabaseHandler DH = new DatabaseHandler(ArchiveCaseActivity.this);
                //Check weather the Hash is Present in the DB or not
                Log.e("??Total Numner of Rows", Integer.toString(DH.getNoOfRowsBeforeOfflineSave(Preferences.getInstance().user_id, Preferences.getInstance().role_id, TaskType.GET_PENDING_MEMO_LIST_CABINET.toString(), "Get_Archive_Cases"+Preferences.getInstance().user_id)));
                if (DH.getNoOfRowsBeforeOfflineSave(Preferences.getInstance().user_id, Preferences.getInstance().role_id, TaskType.Get_Archive_Cases.toString(), "Get_Archive_Cases"+Preferences.getInstance().user_id) == 1) {
                    //Update the Earlier Record
                    DH.updateData(result);
                    Log.e("Updated Row", Boolean.toString(DH.updateData(result)));
                } else if (DH.getNoOfRowsBeforeOfflineSave(Preferences.getInstance().user_id, Preferences.getInstance().role_id, TaskType.Get_Archive_Cases.toString(), "Get_Archive_Cases"+Preferences.getInstance().user_id) == 0) {
                    DH.addOfflineAccess(result);
                    Log.e("Added Row", Boolean.toString(DH.addOfflineAccess(result)));
                } else {
                    //DELETE ALL THE RECORDS
                    DH.deleteAllExistingOfflineData(Preferences.getInstance().user_id, Preferences.getInstance().role_id, TaskType.Get_Archive_Cases.toString(), "Get_Archive_Cases"+Preferences.getInstance().user_id);
                    Log.e("Total Records Deleted:-", Integer.toString(DH.deleteAllExistingOfflineData(Preferences.getInstance().user_id, Preferences.getInstance().role_id, TaskType.GET_PENDING_MEMO_LIST_CABINET.toString(), "Get_Archive_Cases"+Preferences.getInstance().user_id)));
                    //Add the Latest Record
                    DH.addOfflineAccess(result);
                }

                showData(result);
            } else {
                CD.showDialogCloseActivity(ArchiveCaseActivity.this, result.getResponse());
            }


        }
        else {
            CD.showDialogCloseActivity(ArchiveCaseActivity.this, "Something bad happened. Please connect to the ADMIN team.");
        }
    }

}

