<?php
 
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
 
if (isset($_POST['status']) && isset($_POST['unique_id_u']) && isset($_POST['email_u'])) {

 
    // receiving the post params
    $status = $_POST['status'];
    $email_u = $_POST['email_u'];
    $unique_id_u = $_POST['unique_id_u'];
   

 
   
    // check if user is already existed with the same email
    if ($db->isEmergencyExisted($email_u)) {
        // user already existed
    $emergency = $db->updateEmergency($email_u, $status);

        if ($emergency) {
            $response["error"] = FALSE;
            $response["status"] = $emergency["status"];
            $response["email_u"] = $emergency["email_u"];
            echo json_encode($response);
        }
        else{
        $response["error"] = TRUE;
        $response["error_msg"] = "Unknown Error";
        echo json_encode($response);}
    }
    else{

         $emergency = $db->storeEmergency($email_u, $unique_id_u, $status);
        if($emergency){
            $response["error"] = FALSE;
            $response["status"] = $emergency["status"];
            $response["email_u"] = $emergency["email_u"];
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
    $response["error_msg"] = "Required parameters status is missing!";
    echo json_encode($response);
}
?>