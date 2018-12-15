<?php
class DB_Connect {
    private $conn;
 
    // Connecting to database
    public function connect() {
        require_once 'include/Config.php';
         
        // Connecting to mysql database
        $this->conn = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);

        if(mysqli_connect_errno()){
        	die("Database connnection failed " . "(" .
            	mysqli_connect_error() . " - " . mysqli_connect_errno() . ")"
                	);
    }
         
        // return database handler
        return $this->conn;
    }
}
 
?>