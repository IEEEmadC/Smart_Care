<?php
 
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
 
if (isset($_POST['lat']) && isset($_POST['lng']) && isset($_POST['email'])) {

 
    // receiving the post params
    $email = $_POST['email'];
    $lat = $_POST['lat'];
    $lng = $_POST['lng'];
   

 

        // user already existed
        $location = $db->updateLocations($lat, $lng, $email);
        if ($location)
        {
            $response["error"] = FALSE;
            $response["lat"] = $location["lat_s"];
            $response["lng"] = $location["lng_s"];
            $response["email"] = $location["email"];
            echo json_encode($response);
        }
        else{
        $response["error"] = TRUE;
        $response["error_msg"] = "Unknown Error";
        echo json_encode($response);}
    
}
  else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters lat and lng is missing!";
    echo json_encode($response);
}
?>