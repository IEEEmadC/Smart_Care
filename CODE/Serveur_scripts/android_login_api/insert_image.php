<?php
 
require_once 'include/DB_Functions.php';
 
$db = new DB_Functions();
$upload_path = 'uploads/'; //this is our upload folder
$server_ip = gethostbyname(gethostname()); //Getting the server ip
$upload_url = 'http://'.$server_ip.'/android_login_api/'.$upload_path; //upload url
 
//response array
$response = array();
 
 
if($_SERVER['REQUEST_METHOD']=='POST'){
 
    //checking the required parameters from the request
    if(isset($_POST['caption']))
    {
         
        $caption = $_POST['caption'];
        $fileinfo = pathinfo($_FILES['image']['name']);//getting file info from the request
        $extension = $fileinfo['extension']; //getting the file extension
        $file_url = $upload_url . $_POST['name-ph'] . '.' . $extension; //file url to store in the database
        $file_path = $upload_path .$_POST['caption'] . '.'. $extension; //file path to upload in the server
        $img_name =  $_POST['name-ph']; //file name to store in the database
 
        try{
            move_uploaded_file($_FILES['image']['tmp_name'],$file_path); //saving the file to the uploads folder;
           
            //adding the path and name to database   
            $photo = $db->storePhoto($img_name,$_POST['photo_url'], $caption);
        if ($photo) {
            // photo stored successfully
            $response["error"] = FALSE;
            
        } else {
            // photo failed to store
            $response["error"] = TRUE;
            $response["error_msg"] = "Unknown error occurred in registration!";
        }

            //if some error occurred
        }catch(Exception $e){
            $response['error']=true;
            $response['message']=$e->getMessage();
        }
        //displaying the response
        echo json_encode($response);
 
        //closing the connection
        mysqli_close($connection);
    }else{
        $response['error'] = true;
        $response['message']='Please choose a file';
        echo json_encode($response);
    }
}
 
?>