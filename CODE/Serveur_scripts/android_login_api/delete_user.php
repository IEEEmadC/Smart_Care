<?php
 
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
 
if (isset($_POST['email_u'])) {

 
    // receiving the post params
    $email_u = $_POST['email_u'];
    $email_s = $_POST['email_s'];
    $caption = $_POST['caption'];
    
 
        
    $e = $db->deleteUser($email_u);
    $e1 = $db->deleteSupervisor($email_s);
    $e2 = $db->deleteSensor($email_u);
    $e3 = $db->deleteEmergency($email_u);
    $e4 = $db->deleteLocation($email_u);
    $e5 = $db->deletePhoto($caption);
    $e6 = $db->deleteMessage($email_u);
    

        if ($e != false && $e1 != false) {
            $response["error"] = FALSE;
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