<?php
 
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
 
if (isset($_POST['email'])) {

 
    // receiving the post params
    $email_u = $_POST['email'];
    
 
        
    $location = $db->getLocationsByUidEmail($email_u);

        if ($location != false) {
            $response["error"] = FALSE;
            $response["lat"] = $location["lat_s"];
            $response["lng"] = $location["lng_s"];
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