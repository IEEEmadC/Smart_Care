<?php
 
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
 
if (isset($_POST['lat']) && isset($_POST['lng']) && isset($_POST['email_u'])) {

 
    // receiving the post params
    $email = $_POST['email_u'];
    $lat = $_POST['lat'];
    $lng = $_POST['lng'];
   

 
   
    // check if user is already existed with the same email
    if ($db->isLocationExisted($email)) {
        // user already existed
        $location = $db->updateLocation($lat, $lng, $email);
        if ($location)
        {
            $response["error"] = FALSE;
            $response["lat"] = $location["lat"];
            $response["lng"] = $location["lng"];
            $response["email_u"] = $location["email_u"];
            echo json_encode($response);
        }
        else{
        $response["error"] = TRUE;
        $response["error_msg"] = "Unknown Error";
        echo json_encode($response);}
    }
}
  else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters lat and lng is missing!";
    echo json_encode($response);
}
?>