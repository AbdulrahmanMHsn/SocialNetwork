package com.example.abdulrahmanmhsn.socialnetwork.main;

import android.view.MenuItem;

interface Interface {

    /*--
    * init view
    * findViewById()
     --*/
    void initView();

    /*--
    * Add Listener to view
     --*/
    void addEventToView();

    /*--
    * Select item from navigation drawer
     --*/
    void userMenuSelector(MenuItem item);

    /*--
    * this is use move from MainActivity to LoginActivity
     --*/
    void sendUserToLoginActivity();

    /*--
   * this is use move from MainActivity to SetupActivity
    --*/
    void sendUserToSetupActivity();

    /*--
    * Check User Existence
    * Verify that the user has already registered and does not
     --*/
    void CheckUserExistence();

    /*--
    * get data from  firebase
     --*/
    void getDataFromServer();


}
