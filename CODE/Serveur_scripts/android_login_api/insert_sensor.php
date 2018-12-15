<?php
 
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
 
if (isset($_GET['v']) ) {

 
    // receiving the post params
    $v = $_GET['v'];
    $i = $_GET['i'];
    $email = $_GET['email'];

  
        // user already existed
    $emergency = $db->updateSensor($v, $i,$email);

        if ($emergency) {
            $response["error"] = FALSE;
            echo json_encode($response);
        }
        else{
        $response["error"] = TRUE;
        echo json_encode($response);}
   }
  else {
    $response["error"] = TRUE;
    echo json_encode($response);
}
?>