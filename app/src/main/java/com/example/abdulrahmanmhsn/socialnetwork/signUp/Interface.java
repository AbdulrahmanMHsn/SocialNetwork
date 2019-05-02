package com.example.abdulrahmanmhsn.socialnetwork.signUp;

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
     * Validation view
     * save user data in firebase
     --*/
    void registerNewUser();

    /*--
     * Authentication with email and password
     --*/
    void createUserWithEmail(String email, String password);

    /*--
    * check password is equal confirmPassword or not
     --*/
    void checkPassword(String password, String conPassword);

    /*--
    * check access permission or not
    * permission access gallery from device
     --*/
    void checkAndRequestForPermission();

    /*--
    * Open gallery from device
     --*/
    void openGallery();

    /*--
     * show message in activity using test
     */
    void showMessage(String message);
}
