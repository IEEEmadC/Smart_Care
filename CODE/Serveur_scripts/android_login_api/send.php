<?php
 
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
 
if (isset($_POST['message']) && isset($_POST['email_u'])  ) {

 
    // receiving the post params
    $email_u = $_POST['email_u'];
    $message = $_POST['message'];
   if (!$db->isMessageExisted($email_u)) {
        $m = $db->storeMessage($message, $email_u);
        if($m){
            $response["error"] = FALSE;
            echo json_encode($response);
        }
         else{
            $response["error"] = TRUE;
            $response["error_msg"] = "g";
            echo json_encode($response);}
    }
        else{
            $m = $db->updateMessage($message, $email_u);
            if($m){
            $response["error"] = FALSE;
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
    $response["error_msg"] = "Required parameters email is missing!";
    echo json_encode($response);
}
?>