<?php
 
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
 
if (isset($_POST['email_u'])) {

 
    // receiving the post params
    $email_u = $_POST['email_u'];
    
 
        
    $sensor = $db->getSensorByUidEmail($email_u);

        if ($sensor != false) {
            $response["error"] = FALSE;
            $response["humidity"] = $sensor["humidity"];
            $response["temperature"] = $sensor["temperature"];
            $response["current"] = $sensor["current"];
            $response["voltage"] = $sensor["voltage"];
            $response["battery_mah"] = $sensor["battery_mah"];
            $response["max_v"] = $sensor["max_v"];
            $response["min_v"] = $sensor["min_v"];
            $response["created_at"] = $sensor["created_at"];
            
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