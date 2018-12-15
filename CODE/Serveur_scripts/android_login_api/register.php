<?php
 
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array("error" => FALSE);
 
if (isset($_POST['name']) && isset($_POST['email']) && isset($_POST['password']) && isset($_POST['password_s'])) {

 
    // receiving the post params
    $name = $_POST['name'];
    $email = $_POST['email'];
    $password = $_POST['password'];
    $sexe = $_POST['sexe'];
    $phone = $_POST['phone'];
    $lat = $_POST['lat'];
    $lng = $_POST['lng'];
    $name_s = $_POST['name_s'];
    $email_s = $_POST['email_s'];
    $phone_s = $_POST['phone_s'];
    $password_s = $_POST['password_s'];
    $message = "Welcome";
   
 
   
    // check if user is already existed with the same email
    if ($db->isUserExisted($email) ) {
        // user already existed
        $response["error"] = TRUE;
        $response["error_msg"] = "User  already existed with " . $email;
        echo json_encode($response);
    }
    elseif ($db->isUserExisted($email_s) ) {
        // user already existed
        $response["error"] = TRUE;
        $response["error_msg"] = "Supervisor  already existed with " . $email;
        echo json_encode($response);
    }

      elseif ($db->isSuperExisted($email) ) {
        // user already existed
        $response["error"] = TRUE;
        $response["error_msg"] = "User  already existed with " . $email;
        echo json_encode($response);
    }

    elseif ($db->isSuperExisted($email_s) ) {
        // user already existed
        $response["error"] = TRUE;
        $response["error_msg"] = "Supervisor already existed with " . $email;
        echo json_encode($response);
    }

     else {


       
        // create a new user
        $user = $db->storeUser($name, $email, $phone, $password, $lat, $lng, $sexe, $name_s, $email_s, $phone_s);

           
     
        if ($user) {
            // user stored successfully
            $super = $db->storeSuper($name_s, $email_s, $phone_s, $password_s, $user["unique_id"],$name, $email, $phone, $lat, $lng);

            if ($super) {

                $location = $db->storeLocation($lat, $lng, $user["unique_id"], $email);
                $emergency = $db->storeEmergency($email, $user["unique_id"], "OK");
                $sensor = $db->storeSensor($user["unique_id"], $email, "0", "0", "0", "0", "0", "0", "0");
                $message1 = $db->storeMessage($message, $email);
                
            $response["error"] = FALSE;
            $response["uid"] = $user["unique_id"];
            $response["user"]["name"] = $user["name"];
            $response["user"]["email"] = $user["email"];
            $response["user"]["phone"] = $user["phone"];
            $response["user"]["lat"] = $user["lat"];
            $response["user"]["lng"] = $user["lng"];
            $response["user"]["created_at"] = $user["created_at"];
            $response["user"]["updated_at"] = $user["updated_at"];
            $response["super"]["uid"] = $super["unique_id"];
            $response["super"]["name"] = $super["name"];
            $response["super"]["email"] = $super["email"];
            $response["super"]["phone"] = $super["phone"];
            $response["super"]["name_u"] = $super["name_u"];
            $response["super"]["email_u"] = $super["email_u"];
            $response["super"]["phone_u"] = $super["phone_u"];
            $response["super"]["created_at"] = $super["created_at"];
            $response["super"]["updated_at"] = $super["updated_at"];
            echo json_encode($response);
        }
            else {
            // user failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in registration!";

            echo json_encode($response);
        }
        } else {
            // user failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in registration!";
            echo json_encode($response);
        }
    }
} else {
    $response["error"] = TRUE;
    $response["error_msg"] = "Required parameters (name, email or password) is missing!";
    echo json_encode($response);
}
?>