<?php

    include_once "webscript.inc";
    include_once "database.inc";

    class LoginWebscript extends Webscript {

        /** @Get */
        function login($args, $ticket) {
            $db = new Database('tazapi');
            $db->open();
            
            $object = json_decode($args);
            
            $sql = "SELECT user, role FROM users WHERE user=? AND password=?";
            $stmt = $db->conn()->prepare($sql);
            $stmt->bind_param("ss", $user, $password);
            $user = $object->userName;
            $password = $object->password;
            $stmt->execute();
            $stmt->bind_result($user, $role);
            
            while($stmt->fetch()) {
                $count++;
            }
            $stmt->close();
            if($count < 1) {
                $db->close();
                return $this->authenticationFailed();
            }
            
            $loginData = new LoginData();
            $loginData->ticket = uniqid();
            $loginData->role = $role;
            $dateTime = new DateTime('+1 hour', new DateTimeZone('Europe/Vilnius'));
            $loginData->expires = $dateTime->format('Y-m-d H:i:s');
            
            $sql = "INSERT INTO tickets (user, ticket, expires) VALUES ( ?, ?, ?)";
            $stmt = $db->conn()->prepare($sql);
            $stmt->bind_param("sss", $user, $ticket, $expires);
            $user = $user;
            $ticket = $loginData->ticket;
            $expires = $loginData->expires;
            if ($stmt->execute() != TRUE) {
                $stmt->close();
                $db->close();
                return $this->authenticationFailed();
            }
            $stmt->close();
            $db->close();
            
            return $this->deliver_response($loginData);
        }

    }
    
    class LoginData {
        public $ticket;
        public $role;
        public $expires;
    }

?>