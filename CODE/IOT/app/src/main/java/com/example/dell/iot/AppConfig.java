package com.example.dell.iot;

public class AppConfig {

    //server url
    public static String SERVER_URL = "http://192.168.1.53:8080/android_login_api/";

    public static String API = "AIzaSyBLGGRvIaFkj57eGR9gGYtcsOQpdnQYgXc";

    // Server user login url
    public static String URL_LOGIN = SERVER_URL + "login.php";

    // Server user register url
    public static String URL_REGISTER = SERVER_URL + "register.php";

    // Server user password url
    public static String URL_PASS = SERVER_URL + "update_pass.php";

    //server upload photo url
    public static String UPLOAD_URL = SERVER_URL + "insert_image.php";

    //server get admin messages url
    public static String MES_URL = SERVER_URL + "get_message.php";

    public static String UPLOAD_URL1 = SERVER_URL + "insert_imagee.php";

    //location url
    public static String LOCATION_URL = SERVER_URL + "insert_location.php";

    //location url
    public static String LOCATIONS_URL = SERVER_URL + "insert_locations.php";

    //insert_emergency url
    public static String EMERGENCY_URL = SERVER_URL + "insert_emergency.php";

    //get_emergency url
    public static String EMERGENCYGET_URL = SERVER_URL + "get_emergency.php";

    //get_sensor url
    public static String SENSORGET_URL = SERVER_URL + "get_sensor.php";

    public static String LOCATIONGET_URL = SERVER_URL + "get_location.php";

    public static String LOCATIONSGET_URL = SERVER_URL + "get_locations.php";

    //get_all_users url
    public static String USERSGET_URL = SERVER_URL + "get_all.php";

    // edit user url
    public static String USEER_URL = SERVER_URL + "update_user.php";

    public static String SUPEER_URL = SERVER_URL + "update_super.php";

    //delete user url
    public static String DELETEUSER_URL = SERVER_URL + "delete_user.php";

    //send message url
    public static String SEND_URL = SERVER_URL + "send.php";

    //LOC url
    public static String LOC_URL = SERVER_URL + "update_loc.php";
}