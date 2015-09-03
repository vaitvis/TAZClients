<?php

include_once "annotations.php";
include_once "database.inc";

    class Webscript {
        
         public $http_response_code = array(
                200 => 'OK',
                400 => 'Bad Request',
                401 => 'Unauthorized',
                403 => 'Forbidden',
                404 => 'Not Found'
            );
        
        function deliver_response($api_response, $ticket = null, $code = 200){
            header($_SERVER["SERVER_PROTOCOL"]." ".$code.' '.$this->http_response_code[ $code ]);
            header('Content-Type: application/json; charset=utf-8');
            
            if($ticket) {
                $db = new Database('tazapi');
                $db->open();
                $sql = "SELECT expires FROM tickets WHERE ticket=?";
                $stmt = $db->conn()->prepare($sql);
                $stmt->bind_param("s", $ticket);
                $stmt->execute();
                $stmt->bind_result($expires);
                $stmt->fetch();
                $response['expires'] = $expires;
                $stmt->close();
                $db->close();
            }
            
            $response['status'] = $code;
            $response['data'] = $api_response;
            $json_response = json_encode($response);
            echo $json_response;
                
            return;
        }
        
        function authenticationFailed() {
            $response = $this->http_response_code[ 401 ];
            return $this->deliver_response($response, null, 401);
        }
        
        function isAdmin($ticket) {
            $db = new Database('tazapi');
            $db->open();
            $sql = "SELECT role FROM users INNER JOIN tickets ON users.user = tickets.user WHERE ticket=?";
            $stmt = $db->conn()->prepare($sql);
            $stmt->bind_param("s", $ticket);
            $stmt->execute();
            $stmt->bind_result($role);
            $stmt->fetch();
            if($role == "admin") {
                return true;
            } else {
                return false;
            }
        }
    }

    class RequiresAuthentication extends Annotation {
        public $role;
    }
    
    class Post extends Annotation {}
    
    class Get extends Annotation {}
    
    class Delete extends Annotation {}

?>