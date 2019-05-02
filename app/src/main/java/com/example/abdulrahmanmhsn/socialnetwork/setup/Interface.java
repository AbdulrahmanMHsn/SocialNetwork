package com.example.abdulrahmanmhsn.socialnetwork.setup;

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
    * edit information user
    * and save edit information user in firebase
     --*/
    void saveAccountSetupInformation();
}
