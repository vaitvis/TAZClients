<?php

    include_once "webscript.inc";
    include_once "database.inc";

    class HistoryWebscript extends Webscript {
    
        /** 
        *   @Post
        *   @RequiresAuthentication
        */
        function insert($args, $ticket) {
            $db = new Database('tazapi');
            $db->open();
            
            $object = json_decode($args);
            $sql = "INSERT INTO history (USER, CLIENT, DATE, EVENTTYPE, COMMENT) VALUES (?, ?, ?, ?, ?)";
            $stmt = $db->conn()->prepare($sql);
            $stmt->bind_param("sisis", $user, $clientId, $date, $eventType, $comment);
            $user = $object->user;
            $clientId = $object->id;
            $dateTime = new DateTime('now', new DateTimeZone('Europe/Vilnius'));
            $date = $dateTime->format('Y-m-d H:i:s');
            $eventType = $object->eventType;
            $comment = $object->comment;
            
            $stmt->execute();
            $stmt->close();
            $db->close();
            $this->deliver_response(null, $ticket);
        }
        
         /** 
        *   @Get
        *   @RequiresAuthentication(role = "admin")
        */
        function deleteClientHistory($args, $ticket) {
            $db = new Database('tazapi');
            $db->open();
            
            $object = json_decode($args);
            $sql = "DELETE FROM history WHERE CLIENT = ?";
            $stmt = $db->conn()->prepare($sql);
            $stmt->bind_param("i", $id);
            $id = $object->id;
            
            $stmt->execute();
            $stmt->close();
            $db->close();
            $this->deliver_response(null, $ticket);
        }
    
    }
    
?>