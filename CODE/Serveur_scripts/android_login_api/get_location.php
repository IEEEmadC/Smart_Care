<?php
 
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
 
if (isset($_POST['email_u'])) {

 
    // receiving the post params
    $email_u = $_POST['email_u'];
    $email = $_POST['email'];
    
 
        
    $location = $db->getLocationByUidEmail($email_u);
    $location1 = $db->getLocationsByUidEmail($email);

        if ($location != false) {
            $response["error"] = FALSE;
            $response["lat"] = $location["lat"];
            $response["lng"] = $location["lng"];
            $response["lat_s"] = $location1["lat_s"];
            $response["lng_s"] = $location1["lng_s"];
            echo json_encode($response);
        }
        else{
        $response["error"] = TRUE;
        $response["error_msg"] = "Unknown Error";
        echo json_encode($response);     
    }
}
  else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters !";
    echo json_encode($response);
}
?>