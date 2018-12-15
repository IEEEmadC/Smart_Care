<?php
 
require_once 'include/DB_Functions.php';
$db = new DB_Functions();
 
// json response array
$response = array();
$result = $db->getUsers();
            while($row = mysqli_fetch_array($result)){
                array_push($response,array('uid' => $row[1],'name' => $row[2],'email' => $row[3],'phone' => $row[4],'name_s' => $row[12], 'email_s' => $row[13],'phone_s' => $row[14],'lat' => $row[7],'lng' => $row[8],'status' => $row[18]));
            }
/*
$response1 = array();
$result = $db->getEmergencys();
            while($row = mysqli_fetch_array($result)){
                array_push($response1,array('status' => $row[3], 'email_u' => $row[1]));
            }*/

            echo json_encode(array('serveur_response' => $response) );

?>