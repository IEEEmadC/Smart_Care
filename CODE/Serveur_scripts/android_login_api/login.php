<?php
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
if( $_POST['status'] == "User"){ 
if (isset($_POST['email']) && isset($_POST['password'])) {
 
    // receiving the post params
    $email = $_POST['email'];
    $password = $_POST['password'];
 
    // get the user by email and password
    $user = $db->getUserByEmailAndPassword($email, $password);
 
    if ($user != false) {
        // use is found
      $super = $db->getSuperByEmail($user["email_s"]);  
        if ($super != false) {
            $location = $db->getLocationByUidEmail($user["email"]);
            $response["error"] = FALSE;
            $response["statuts"] = "User";
            $response["uid"] = $user["unique_id"];
            $response["user"]["name"] = $user["name"];
            $response["user"]["email"] = $user["email"];
            $response["user"]["phone"] = $user["phone"];
            $response["user"]["lat"] = $user["lat"];
            $response["user"]["lng"] = $user["lng"];
            $response["user"]["created_at"] = $user["created_at"];
            $response["user"]["updated_at"] = $user["updated_at"];
            $response["super"]["uid"] = $super["unique_id"];
            $response["super"]["name"] = $super["name"];
            $response["super"]["email"] = $super["email"];
            $response["super"]["phone"] = $super["phone"];
            $response["super"]["name_u"] = $super["name_u"];
            $response["super"]["email_u"] = $super["email_u"];
            $response["super"]["phone_u"] = $super["phone_u"];
            $response["super"]["created_at"] = $super["created_at"];
            $response["super"]["updated_at"] = $super["updated_at"];
            $response["location"]["lat"] = $location["lat"];
            $response["location"]["lng"] = $location["lng"];
        $photo = $db->getPhotoByUid($user["unique_id"]);
        if ($photo != false) {
            $response["photo"]["error"] =FALSE;
            $response["user"]["photo_name"] = $photo["photo_name"];
            $response["user"]["photo_url"] = $photo["photo_url"];
            $response["user"]["caption"] = $photo["caption"];
            
        }
        else{$response["photo"]["error"] =TRUE;}
            
    } else {
        // super is not found with the credentials
        $response["error"] = TRUE;
        $response["error_msg"] = "Login credentials are wrong. Please try again!";
        echo json_encode($response);
    }
        echo json_encode($response);       
}
     else {
        // user is not found with the credentials
        $response["error"] = TRUE;
        $response["error_msg"] = "Login credentials are wrong. Please try again!";
        echo json_encode($response);
    }
} else {
    // required post params is missing
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters email or password is missing!";
    echo json_encode($response);
}}
if( $_POST['status'] == "Supervisor"){ 

if (isset($_POST['email']) && isset($_POST['password'])) {
 
    // receiving the post params
    $email = $_POST['email'];
    $password = $_POST['password'];
 
    // get the user by email and password
    $super = $db->getSuperByEmailAndPassword($email, $password);
 
    if ( $super != false) {
        // super is found
        $user = $db->getUserByEmail($super["email_u"]);
        if ( $user != false) {
            $location = $db->getLocationByUidEmail($user["email"]);

            $response["statuts"] = "Supervisor";
            $response["error"] = FALSE;
            $response["uid"] = $user["unique_id"];
            $response["user"]["name"] = $user["name"];
            $response["user"]["email"] = $user["email"];
            $response["user"]["phone"] = $user["phone"];
            $response["user"]["lat"] = $user["lat"];
            $response["user"]["lng"] = $user["lng"];
            $response["user"]["created_at"] = $user["created_at"];
            $response["user"]["updated_at"] = $user["updated_at"];
            $response["super"]["uid"] = $super["unique_id"];
            $response["super"]["name"] = $super["name"];
            $response["super"]["email"] = $super["email"];
            $response["super"]["phone"] = $super["phone"];
            $response["super"]["name_u"] = $super["name_u"];
            $response["super"]["email_u"] = $super["email_u"];
            $response["super"]["phone_u"] = $super["phone_u"];
            $response["super"]["created_at"] = $super["created_at"];
            $response["super"]["updated_at"] = $super["updated_at"];
            $response["location"]["lat"] = $super["lat_s"];
            $response["location"]["lng"] = $super["lng_s"];
        $photo = $db->getPhotoByUid($super["unique_id"]);
        if ($photo != false) {
            $response["photo"]["error"] =FALSE;
            $response["user"]["photo_name"] = $photo["photo_name"];
            $response["user"]["photo_url"] = $photo["photo_url"];
            $response["user"]["caption"] = $photo["caption"];
            
        }
        else{$response["photo"]["error"] =TRUE;}
            
       
    } else {
        // user is not found with the credentials
        $response["error"] = TRUE;
        $response["error_msg"] = "Login credentials are wrong. Please try again!";
        echo json_encode($response);
    }
         echo json_encode($response);
}

    else {
        // user is not found with the credentials
        $response["error"] = TRUE;
        $response["error_msg"] = "Login credentials are wrong. Please try again!";
        echo json_encode($response);
}} else {
    // required post params is missing
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters email or password is missing!";
    echo json_encode($response);
}

}
else {
    // required post params is missing
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters Supervisor or User is missing!";
    echo json_encode($response);
}

?>