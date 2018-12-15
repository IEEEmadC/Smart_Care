<?php
 
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);

 
if (isset($_POST['name_u']) && isset($_POST['email_u']) ) {

 
    // receiving the post params
    $name = $_POST['name_s'];
    $email = $_POST['email_s'];
    $phone = $_POST['phone_s'];
    $lat = $_POST['lat'];
    $lng = $_POST['lng'];
    $name_u = $_POST['name_u'];
    $email_u = $_POST['email_u'];
    $phone_u = $_POST['phone_u'];
    $email_n = $_POST['email_n'];


    if($email_n != "null"){
         if ($db->isSuperExisted($email_n) ) {
        // user already existed
        $response["error"] = TRUE;
        $response["error_msg"] = "Suservisor  already existed with " . $email;
        echo json_encode($response);
    }
    else{

            $user = $db->updateSuper($name, $email_n, $phone, $lat, $lng, $name_u, $email_u, $phone_u, $email);
            if ($user) {
                $response["error"] = FALSE;
                echo json_encode($response);
            }
        else{
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown Error In Registeration New email ";
            echo json_encode($response);
        }
    }     

    }
    else{
    
        $user = $db->updateSuper($name, $email, $phone, $lat, $lng, $name_u, $email_u, $phone_u, $email);
        if ($user) {
            $response["error"] = FALSE;
            echo json_encode($response);
        }
        else{
            $response["error"] = TRUE;
            $response["error_msg"] = $user;
            echo json_encode($response);
        }
    }
   
     } else {
            // user failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in registration!";
            echo json_encode($response);
        }
   
?>