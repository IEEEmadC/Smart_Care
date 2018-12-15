<?php
 
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);

 
if (isset($_POST['name_u']) && isset($_POST['email_u']) ) {

 
    // receiving the post params
    $name = $_POST['name_u'];
    $email = $_POST['email_u'];
    $phone = $_POST['phone_u'];
    $lat = $_POST['lat'];
    $lng = $_POST['lng'];
    $name_s = $_POST['name_s'];
    $email_s = $_POST['email_s'];
    $phone_s = $_POST['phone_s'];
    $email_n = $_POST['email_n'];


    if($email_n != "null"){
         if ($db->isUserExisted($email_n) ) {
        // user already existed
        $response["error"] = TRUE;
        $response["error_msg"] = "User  already existed with " . $email;
        echo json_encode($response);
    }
    else{

            $user = $db->updateUser($name, $email_n, $phone, $lat, $lng, $name_s, $email_s, $phone_s, $email);
            if ($user) {
                $response["error"] = FALSE;
                $response["uid"] = $user["unique_id"];
                $response["name"] = $user["name"];
                $response["email"] = $user["email"];
                $response["phone"] = $user["phone"];
                $response["lat"] = $user["lat"];
                $response["lng"] = $user["lng"];
                $response["created_at"] = $user["created_at"];
                $response["updated_at"] = $user["updated_at"];
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
    
        $user = $db->updateUser($name, $email, $phone, $lat, $lng, $name_s, $email_s, $phone_s, $email);
        if ($user) {
            $response["error"] = FALSE;
            $response["uid"] = $user["unique_id"];
            $response["name"] = $user["name"];
            $response["email"] = $user["email"];
            $response["phone"] = $user["phone"];
            $response["lat"] = $user["lat"];
            $response["lng"] = $user["lng"];
            $response["created_at"] = $user["created_at"];
            $response["updated_at"] = $user["updated_at"];
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