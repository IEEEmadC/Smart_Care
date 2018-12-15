<?php


 
/**
 * @author Hergli Sedki
 */
 
class DB_Functions {
 
    private $conn;
 
    // constructor
    function __construct() {
        require_once 'DB_Connect.php';
        // connecting to database
        $db = new Db_Connect();
        $this->conn = $db->connect();
    }
 
    // destructor
    function __destruct() {
         
    }
 
    /**
     * Storing new user
     * returns user details
     */
    public function storeUser($name, $email, $phone, $password, $lat, $lng, $sexe, $name_s, $email_s, $phone_s) {
        $uuid = uniqid('', true);
        $hash = $this->hashSSHA($password);
        $password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt
 
        $stmt = $this->conn->prepare("INSERT INTO users(unique_id, name, email, phone, password, salt, lat, lng, created_at, sexe, name_s, email_s, phone_s) VALUES(?, ?, ?, ?, ?, ?, ?, ?, NOW(), ?, ?, ?, ?)");
        $stmt->bind_param("ssssssssssss", $uuid, $name, $email, $phone, $password, $salt, $lat, $lng, $sexe, $name_s, $email_s, $phone_s);
        $result = $stmt->execute();
        $stmt->close();
 
        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM users WHERE email = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $user;
        } else {
            return false;
        }
    }
 

   /**
     * Storing new photo
     * returns photo details
     */
    public function storePhoto($img_name, $file_url, $caption) {
       
        $stmt = $this->conn->prepare("INSERT INTO photos(photo_name, photo_url, caption) VALUES(?, ?, ?)");
        $stmt->bind_param("sss", $img_name, $file_url, $caption);
        $result = $stmt->execute();
        $stmt->close();
 
        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM photos WHERE photo_name = ?");
            $stmt->bind_param("s", $img_name);
            $stmt->execute();
            $photo = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $photo;
        } else {
            return false;
        }
    }


 /**
     * Storing new balance
     * returns balance details
     */
    public function storeBalance($email_u, $unique_id_u, $X, $Y, $Z) {
       
        $stmt = $this->conn->prepare("INSERT INTO balances(email_u, unique_id_u, X, Y, Z, created_at) VALUES(?, ?, ?, ?, ?, NOW())");
        $stmt->bind_param("sssss", $email_u, $unique_id_u, $X, $Y, $Z);
        $result = $stmt->execute();
        $stmt->close();
 
        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM balances WHERE email_u = ?");
            $stmt->bind_param("s", $email_u);
            $stmt->execute();
            $balance = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $balance;
        } else {
            return false;
        }
    }

 /**
     * Storing new emergency
     * returns emergency details
     */
    public function storeEmergency($email_u, $unique_id_u, $status) {
       
        $stmt = $this->conn->prepare("INSERT INTO emergencys(email_u, unique_id_u, status, created_at) VALUES(?, ?, ?, NOW())");
        $stmt->bind_param("sss", $email_u, $unique_id_u, $status);
        $result = $stmt->execute();
        $stmt->close();
 
        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM emergencys WHERE email_u = ?");
            $stmt->bind_param("s", $email_u);
            $stmt->execute();
            $emergency = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $emergency;
        } else {
            return false;
        }
    }



    /**
     * Storing new location
     * returns location details
     */
    public function storeLocation($lat, $lng, $uid, $email) {
       
        $stmt = $this->conn->prepare("INSERT INTO locations(lat, lng, unique_id_u, email_u, created_at) VALUES(?, ?, ?, ?, NOW())");
        $stmt->bind_param("ssss", $lat, $lng, $uid, $email);
        $result = $stmt->execute();
        $stmt->close();
 
        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM locations WHERE unique_id_u = ? AND email_u = ?");
            $stmt->bind_param("ss", $uid, $email);
            $stmt->execute();
            $location = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $location;
        } else {
            return false;
        }
    }
 

 /**
     * Storing new supervisor
     */
    public function storeSuper($name, $email, $phone, $password, $unique_id, $name_u, $email_u, $phone_u, $lat, $lng) {
       
        $uuid = uniqid('', true);
        $hash = $this->hashSSHA($password);
        $password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt
 
        $stmt = $this->conn->prepare("INSERT INTO supers(unique_id, name, email, phone, password, salt, unique_id_u, name_u, email_u, phone_u, lat, lng, lat_s, lng_s, created_at) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())");
        $stmt->bind_param("ssssssssssssss", $uuid, $name, $email, $phone, $password, $salt, $unique_id, $name_u, $email_u, $phone_u, $lat, $lng, $lat, $lng);
        $result = $stmt->execute();
        $stmt->close();
 
        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM supers WHERE email = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $super = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $super;
        } else {
            return false;
        }
    }


    /**
     * Storing new sensor
     * returns sensor details
     */
    public function storeSensor($uid, $email, $h, $t, $c, $v, $b_mah, $m_v, $mm_v) {
       
        $stmt = $this->conn->prepare("INSERT INTO sensors(sensor_uid, email_u, humidity, temperature, current, voltage, battery_mah, max_v, min_v, created_at) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())");
        $stmt->bind_param("sssssssss", $uid, $email, $h, $t, $c, $v, $b_mah, $m_v, $mm_v);
        $result = $stmt->execute();
        $stmt->close();
 
        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM sensors WHERE email_u = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $sensor = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $sensor;
        } else {
            return false;
        }
    }


    /**
     * Storing new message
     * returns message details
     */
    public function storeMessage($message, $email) {
       
        $stmt = $this->conn->prepare("INSERT INTO message_u(message,email,created_at) VALUES(?, ?, NOW())");
        $stmt->bind_param("ss", $message, $email);
        $result = $stmt->execute();
        $stmt->close();
 
        // check for successful store
        if ($result) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get user by email and password
     */
    public function getUserByEmailAndPassword($email, $password1) {
 
        $stmt = $this->conn->prepare("SELECT * FROM users WHERE email = ?");
 
        $stmt->bind_param("s", $email);
 
        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            // verifying user password
            $salt = $user['salt'];
            $password2 = $user['password'];
            $hash = $this->checkhashSSHA($salt, $password1);
            // check for password equality
            if ($password2 == $hash) {
                // user authentication details are correct
                return $user;
            }
        } else {
            return NULL;
        }
    }
 
 /**
     * Get user by email and password
     */
    public function getUserByEmail($email) {
 
        $stmt = $this->conn->prepare("SELECT * FROM users WHERE email = ?");
 
        $stmt->bind_param("s", $email);
 
        if ($stmt->execute()) {
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
           
                return $user;
        } else {
            return NULL;
        }
    }

/**
     * Get all users 
     */
    public function getUsers() {
 
        $query = "SELECT * FROM users u JOIN emergencys e ON e.email_u = u.email";
        $result = mysqli_query($this->conn,$query);
        return  $result;
    }

    /**
     * Get all emergencys 
     */
    public function getEmergencys() {
 
        $query = "SELECT * FROM emergencys;";
        $result = mysqli_query($this->conn,$query);
        return  $result;
    }


/**
     * Get super by email and password
     */
    public function getSuperByEmailAndPassword($email, $password1) {
 
        $stmt = $this->conn->prepare("SELECT * FROM supers WHERE email = ?");
 
        $stmt->bind_param("s", $email);
 
        if ($stmt->execute()) {
            $super = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            // verifying super password
            $salt = $super['salt'];
            $password2 = $super['password'];
            $hash = $this->checkhashSSHA($salt, $password1);
            // check for password equality
            if ($password2 == $hash) {
                // super authentication details are correct
                return $super;
            }
        } else {
            return NULL;
        }
    }

/**
     * Get super by email and password
     */
    public function getSuperByEmail($email) {
 
        $stmt = $this->conn->prepare("SELECT * FROM supers WHERE email = ?");
 
        $stmt->bind_param("s", $email);
 
        if ($stmt->execute()) {
            $super = $stmt->get_result()->fetch_assoc();
            $stmt->close();

                return $super;
            
        } else {
            return NULL;
        }
    }



 /**
     * Get photo by email
     */
    public function getPhotoByUid($caption) {
 
        $stmt = $this->conn->prepare("SELECT * FROM photos WHERE caption = ?");
 
        $stmt->bind_param("s", $caption);
 
        if ($stmt->execute()) {
            $photo = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $photo;
        } else {
            return NULL;
        }
    }
 

/**
     * Get location by email
     */
    public function getLocationByUidEmail($email) {
 
        $stmt = $this->conn->prepare("SELECT * FROM locations WHERE email_u = ?");
 
        $stmt->bind_param("s", $email);
 
        if ($stmt->execute()) {
            $location = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $location;
        } else {
            return NULL;
        }
    }
 

    /**
     * Get location by email
     */
    public function getLocationsByUidEmail($email) {
 
        $stmt = $this->conn->prepare("SELECT lat_s , lng_s FROM supers WHERE email = ?");
 
        $stmt->bind_param("s", $email);
 
        if ($stmt->execute()) {
            $location = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $location;
        } else {
            return NULL;
        }
    }
 

   /**
     * Get sensor by uid
     */
    public function getSensorByUidEmail($email) {
 
        $stmt = $this->conn->prepare("SELECT * FROM sensors WHERE  email_u = ?");
 
        $stmt->bind_param("s", $email);
 
        if ($stmt->execute()) {
            $sensor = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $sensor;
        } else {
            return NULL;
        }
    }

/**
     * Get message by email 
     */
    public function getMessageeByEmail($email_u) {
 
        $stmt = $this->conn->prepare("SELECT * FROM message_u WHERE email_u = ?");
 
        $stmt->bind_param("s", $email_u);
 
        if ($stmt->execute()) {
            $balance = $stmt->get_result()->fetch_assoc();
            $stmt->close();
           
                return $balance;
        } else {
            return NULL;
        }
    }

/**
     * Get balance by email and password
     */
    public function getBalanceByEmail($email_u) {
 
        $stmt = $this->conn->prepare("SELECT * FROM balances WHERE email_u = ?");
 
        $stmt->bind_param("s", $email_u);
 
        if ($stmt->execute()) {
            $balance = $stmt->get_result()->fetch_assoc();
            $stmt->close();
           
                return $balance;
        } else {
            return NULL;
        }
    }


/**
     * Get emergency by email 
     */
    public function getEmergencyByEmail($email_u) {
 
        $stmt = $this->conn->prepare("SELECT * FROM emergencys WHERE email_u = ?");
 
        $stmt->bind_param("s", $email_u);
 
        if ($stmt->execute()) {
            $emergency = $stmt->get_result()->fetch_assoc();
            $stmt->close();
           
                return $emergency;
        } else {
            return NULL;
        }
    }

    /**
     * Check user is existed or not
     */
    public function isUserExisted($email) {
        $stmt = $this->conn->prepare("SELECT email from users WHERE email = ?");
 
        $stmt->bind_param("s", $email);
 
        $stmt->execute();
 
        $stmt->store_result();
 
        if ($stmt->num_rows > 0) {
            // user existed 
            $stmt->close();
            return true;
        } else {
            // user not existed
            $stmt->close();
            return false;
        }
    }
 

    /**
     * Check super is existed or not
     */
    public function isSuperExisted($email) {
        $stmt = $this->conn->prepare("SELECT email from supers WHERE email = ?");
 
        $stmt->bind_param("s", $email);
 
        $stmt->execute();
 
        $stmt->store_result();
 
        if ($stmt->num_rows > 0) {
            // user existed 
            $stmt->close();
            return true;
        } else {
            // user not existed
            $stmt->close();
            return false;
        }
    }

/**
     * Check message is existed or not
     */
    public function isMessageExisted($email) {
        $stmt = $this->conn->prepare("SELECT created_at from message_u WHERE email = ?");
        $stmt->bind_param("s", $email);
 
        $stmt->execute();
 
        $stmt->store_result();
 
        if ($stmt->num_rows > 0) {
            // user existed 
            $stmt->close();
            return true;
        } else {
            // user not existed
            $stmt->close();
            return false;
        }
    }

/**
     * Check location is existed or not
     */
    public function isLocationExisted($email) {
        $stmt = $this->conn->prepare("SELECT unique_id_u from locations WHERE email_u = ?");
 
        $stmt->bind_param("s", $email);
 
        $stmt->execute();
 
        $stmt->store_result();
 
        if ($stmt->num_rows > 0) {
            // user existed 
            $stmt->close();
            return true;
        } else {
            // user not existed
            $stmt->close();
            return false;
        }
    }

    /**
     * Check sensor is existed or not
     */
    public function isSensorExisted($email) {
        $stmt = $this->conn->prepare("SELECT uid_sensor from sensors WHERE email_u = ?");
 
        $stmt->bind_param("s", $email);
 
        $stmt->execute();
 
        $stmt->store_result();
 
        if ($stmt->num_rows > 0) {
            // sensor existed 
            $stmt->close();
            return true;
        } else {
            // sensor not existed
            $stmt->close();
            return false;
        }
    }


     /**
     * Check balance is existed or not
     */
    public function isBalanceExisted($email_u) {
        $stmt = $this->conn->prepare("SELECT unique_id_u from balances WHERE email_u = ?");
 
        $stmt->bind_param("s", $email_u);
 
        $stmt->execute();
 
        $stmt->store_result();
 
        if ($stmt->num_rows > 0) {
            // sensor existed 
            $stmt->close();
            return true;
        } else {
            // sensor not existed
            $stmt->close();
            return false;
        }
    }

/**
     * Check emergency is existed or not
     */
    public function isEmergencyExisted($email_u) {
        $stmt = $this->conn->prepare("SELECT unique_id_u from emergencys WHERE email_u = ?");
 
        $stmt->bind_param("s", $email_u);
 
        $stmt->execute();
 
        $stmt->store_result();
 
        if ($stmt->num_rows > 0) {
            // sensor existed 
            $stmt->close();
            return true;
        } else {
            // sensor not existed
            $stmt->close();
            return false;
        }
    }

/**
     * update password
     * returns $table details
     */
    public function updatePassword($table, $password, $email) {
        $hash = $this->hashSSHA($password);
        $password = $hash["encrypted"]; // encrypted password
        $salt = $hash["salt"]; // salt
 
        $stmt = $this->conn->prepare("UPDATE $table SET password=?, salt=?, updated_at=NOW() WHERE email=?");
        $stmt->bind_param("sss", $password, $salt, $email);
        $result = $stmt->execute();
        $stmt->close();
 
        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM users WHERE email = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $tabl = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $tabl;
        } else {
            return false;
        }
    }


/**
     * update user
     * returns user details
     */
    public function updateUser($name, $email, $phone, $lat, $lng ,$name_s,$email_s,$phone_s,$email1) {

        $stmt = $this->conn->prepare("UPDATE users SET name=?, phone=?, lat=?, lng=?, updated_at=NOW(), name_s=?, email_s=?, phone_s=?, email=? WHERE email=?");
        $stmt->bind_param("sssssssss", $name, $phone, $lat, $lng, $name_s, $email_s, $phone_s, $email, $email1);
        $result = $stmt->execute();
        $stmt->close();

        $stmt = $this->conn->prepare("UPDATE supers SET name=?, phone=?,name_u=?, phone_u=?, lat=?, lng=?, updated_at=NOW(), email_u=? WHERE email=?");
        $stmt->bind_param("ssssssss", $name_s, $phone_s, $name, $phone, $lat, $lng, $email, $email_s);
        $result = $stmt->execute();
        $stmt->close();

        $stmt = $this->conn->prepare("UPDATE sensors SET email_u=?, updated_at=NOW() WHERE email_u = ?");
        $stmt->bind_param("ss", $email, $email1);
        $result = $stmt->execute();
        $stmt->close();

        $stmt = $this->conn->prepare("UPDATE emergencys SET email_u=?, updated_at = NOW() WHERE email_u = ?");
        $stmt->bind_param("ss", $email,$email1);
        $result = $stmt->execute();
        $stmt->close();

        $stmt = $this->conn->prepare("UPDATE locations SET email_u=?, updated_at=NOW() WHERE email_u = ?");
        $stmt->bind_param("ss", $email, $email1);
        $result = $stmt->execute();
        $stmt->close();

        $stmt = $this->conn->prepare("UPDATE message_u SET email=?, updated_at=NOW() WHERE email = ?");
        $stmt->bind_param("ss", $email, $email1);
        $result = $stmt->execute();
        $stmt->close();
 
 

 
        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM users WHERE email = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $user;
        } else {
            return false;
        }
    }


  /**
     * delete user
     * 
     */
    public function deleteUser($email) {
       
        $stmt = $this->conn->prepare("DELETE FROM users WHERE email=?");
        $stmt->bind_param("s", $email);
        $result = $stmt->execute();
        $stmt->close();
        if ($result) 
            return true;
        else
            return false;
 
    }


    /**
     * update Sensor
     * returns Sensor details
     */
    public function updateSensor($v,$i,$email) {
      
        $stmt = $this->conn->prepare("UPDATE sensors SET voltage=?, current=?, updated_at=NOW() WHERE email_u = ?");
        $stmt->bind_param("sss", $v, $i, $email);
        $result = $stmt->execute();
        $stmt->close();
 
        // check for successful store
        if ($result) {
            
            return true;
        } else {
            return false;
        }
    }

    /**
     * delete sensor
     * 
     */
    public function deleteSensor($email) {
       
        $stmt = $this->conn->prepare("DELETE FROM sensors WHERE email_u = ?");
        $stmt->bind_param("s", $email);
        $stmt->execute();
        $stmt->close();
 
    }

      /**
     * updating new location
     * returns location details
     */
    public function updateLocation($lat, $lng, $email) {
       
        $stmt = $this->conn->prepare("UPDATE locations SET lat=?, lng=?, updated_at=NOW() WHERE email_u = ?");
        $stmt->bind_param("sss", $lat, $lng, $email);
        $result = $stmt->execute();
        $stmt->close();
 
        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM locations WHERE email_u = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $location = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $location;
        } else {
            return false;
        }
    }
 
      /**
     * updating new location
     * returns location details
     */
    public function updateLocations($lat, $lng, $email) {
       
        $stmt = $this->conn->prepare("UPDATE supers SET lat_s=?, lng_s=?, updated_at=NOW() WHERE email = ?");
        $stmt->bind_param("sss", $lat, $lng, $email);
        $result = $stmt->execute();
        $stmt->close();
 
        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM supers WHERE email = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $location = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $location;
        } else {
            return false;
        }
    }

    /**
     * delete location
     * 
     */
    public function deleteLocation($email) {
       
        $stmt = $this->conn->prepare("DELETE FROM locations WHERE  email_u = ?");
        $stmt->bind_param("s", $email);
        $stmt->execute();
        $stmt->close();
 
    }

    /**
     * update user
     * returns user details
     */
    public function updateSuper($name, $email, $phone, $lat, $lng ,$name_u,$email_u,$phone_u,$email1) {

        $stmt = $this->conn->prepare("UPDATE supers SET name=?, phone=?, lat=?, lng=?, updated_at=NOW(), name_u=?, email_u=?, phone_u=?, email=? WHERE email=?");
        $stmt->bind_param("sssssssss", $name, $phone, $lat, $lng, $name_u, $email_u, $phone_u, $email, $email1);
        $result = $stmt->execute();
        $stmt->close();

        $stmt = $this->conn->prepare("UPDATE users SET name=?, phone=?, lat=?, lng=?, updated_at=NOW(), name_s=?, email_s=?, phone_s=?, email=? WHERE email=?");
        $stmt->bind_param("sssssssss", $name_u, $phone_u, $lat, $lng, $name, $email, $phone, $email_u, $email_u);
        $result = $stmt->execute();
        $stmt->close();
 
        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM supers WHERE email = ?");
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $user = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $user;
        } else {
            return false;
        }
    }


     /**
     * delete Supervisor
     * 
     */
    public function deleteSupervisor($email) {
       
        $stmt = $this->conn->prepare("DELETE FROM supers WHERE email=?");
        $stmt->bind_param("s", $email);
        $stmt->execute();
        $stmt->close();
 
    }


    /**
     * update balance
     * returns balance details
     */
    public function updateBalance($email_u, $X, $Y, $Z) {

        $stmt = $this->conn->prepare("UPDATE balances SET X=?, Y=?, Z=?, updated_at = NOW() WHERE email_u = ?");
        $stmt->bind_param("ssss", $X, $Y, $Z, $email_u);
        $result = $stmt->execute();
        $stmt->close();
 
        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM balances WHERE email_u = ?");
            $stmt->bind_param("s", $email_u);
            $stmt->execute();
            $balance = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $balance;
        } else {
            return false;
        }
    }

    /**
     * update message
     */
    public function updateMessage($message,$email_u) {

        $stmt = $this->conn->prepare("UPDATE message_u SET message=?, updated_at = NOW() WHERE email = ?");
        $stmt->bind_param("ss", $message, $email_u);
        $result = $stmt->execute();
        $stmt->close();
 
        // check for successful store
        if ($result) {
             $stmt = $this->conn->prepare("SELECT * FROM message_u WHERE email = ?");
            $stmt->bind_param("s", $email_u);
            $stmt->execute();
            $balance = $stmt->get_result()->fetch_assoc();
            $stmt->close();
            return $balance;
 
        } else {
            return false;
        }
    }

     /**
     * delete Balance
     * 
     */
    public function deleteBalance($email_u) {
       
        $stmt = $this->conn->prepare("DELETE FROM balances WHERE email_u = ?");
        $stmt->bind_param("s", $email_u);
        $stmt->execute();
        $stmt->close();
 
    }

      /**
     * delete message
     * 
     */
    public function deleteMessage($email_u) {
       
        $stmt = $this->conn->prepare("DELETE FROM message_u WHERE email_u = ?");
        $stmt->bind_param("s", $email_u);
        $stmt->execute();
        $stmt->close();
 
    }


    /**
     * update emergency
     * returns emergency details
     */
    public function updateEmergency($email_u, $status) {

        $stmt = $this->conn->prepare("UPDATE emergencys SET status=?, updated_at = NOW() WHERE email_u = ?");
        $stmt->bind_param("ss", $status,$email_u);
        $result = $stmt->execute();
        $stmt->close();
 
        // check for successful store
        if ($result) {
            $stmt = $this->conn->prepare("SELECT * FROM emergencys WHERE email_u = ?");
            $stmt->bind_param("s", $email_u);
            $stmt->execute();
            $emergency = $stmt->get_result()->fetch_assoc();
            $stmt->close();
 
            return $emergency;
        } else {
            return false;
        }
    }


     /**
     * delete emergency
     * 
     */
    public function deleteEmergency($email_u) {
       
        $stmt = $this->conn->prepare("DELETE FROM emergencys WHERE email_u = ?");
        $stmt->bind_param("s", $email_u);
        $stmt->execute();
        $stmt->close();
 
    }



    /**
     * delete photo
     * 
     */
    public function deletePhoto($caption) {
       
        $stmt = $this->conn->prepare("DELETE FROM photos WHERE  caption = ?");
        $stmt->bind_param("s", $caption);
        $stmt->execute();
        $stmt->close();
 
    }


    /**
     * Encrypting password
     * @param password
     * returns salt and encrypted password
     */
    public function hashSSHA($password) {
 
        $salt = sha1(rand());
        $salt = substr($salt, 0, 10);
        $encrypted = base64_encode(sha1($password . $salt, true) . $salt);
        $hash = array("salt" => $salt, "encrypted" => $encrypted);
        return $hash;
    }
 
    /**
     * Decrypting password
     * @param salt, password
     * returns hash string
     */
    public function checkhashSSHA($salt, $password) {
 
        $hash = base64_encode(sha1($password . $salt, true) . $salt);
 
        return $hash;
    }
      /**
     * here the function delete and update
     */
 
}
 
?>