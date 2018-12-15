<?php
 
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);

 
if (isset($_POST['password']) && isset($_POST['email']) ) {

 
    // receiving the post params
    $password = $_POST['password'];
    $email = $_POST['email'];
    $stat = $_POST['stat'];

    if($stat == "Supervisor")
        $stat1="supers";
    else
        $stat1="users";
   
    
    
        $user = $db->updatePassword($stat1, $password, $email);
        if ($user) {
            $response["error"] = FALSE;
            echo json_encode($response);
        }
        else{
            $response["error"] = TRUE;
            $response["error_msg"] = $user;
            echo json_encode($response);
        }
  
     } else {
            // user failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "require parameters email && password";
            echo json_encode($response);
        }
   
?>