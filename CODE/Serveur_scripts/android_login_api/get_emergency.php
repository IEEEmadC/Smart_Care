<?php
 
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
 
if (isset($_POST['email_u'])) {

 
    // receiving the post params
    $email_u = $_POST['email_u'];
    
 
        
    $emergency = $db->getEmergencyByEmail($email_u);

        if ($emergency != false) {
            $response["error"] = FALSE;
            $response["status"] = $emergency["status"];
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