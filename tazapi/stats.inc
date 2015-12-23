<?php

    include_once "webscript.inc";
    include_once "database.inc";

    class StatsWebscript extends Webscript {
        
         /** 
        *   @Get
        *   @RequiresAuthentication(role = "admin")
        */
        function countClientsByState($args, $ticket) {
            $db = new Database('tazapi');
            $db->open();
            
            $object = json_decode($args);
            if ($object->state) {
            	$sql = "SELECT COUNT(*) FROM clients WHERE CLIENTSTATE = ?";
            } else {
            	$sql = "SELECT COUNT(*) FROM clients";
            }
            $stmt = $db->conn()->prepare($sql);
            if ($object->state) {
	            $stmt->bind_param("i", $state);
	            $state = $object->state;
            }
            
            $stmt->execute();
            

            $count = null;
            $stmt->bind_result($count);
            $stmt->fetch();
            
            $stmt->close();
            $db->close();
            $this->deliver_response($count, $ticket);
        }
        
        /**
         *   @Get
         *   @RequiresAuthentication(role = "admin")
         */
        function countClientsByDateState($args, $ticket) {
        	$db = new Database('tazapi');
        	$db->open();
        	
        	$object = json_decode($args);
        	
        	$wheres = array();
        	$wheres[] = "CLIENTSTATE = ".$object->state;
        	if($object->dateFrom) {
        		$wheres[] = "VALIDTO >= '".$object->dateFrom."'";
        	}
        	if($object->dateTo) {
        		$wheres[] = "VALIDTO <= '".$object->dateTo."'";
        	}
        	$sql = "SELECT COUNT(*) FROM clients";
        	$where_string = implode(' AND ', $wheres);
        	if ($where_string) {
        		$sql .= " WHERE " . $where_string;
        	}
        	$stmt = $db->conn()->prepare($sql);
        	
        	$stmt->execute();
        	
        	$count = null;
        	$stmt->bind_result($count);
        	$stmt->fetch();
        	
        	$stmt->close();
        	$db->close();
        	$this->deliver_response($count, $ticket);
        }
    
    }
    
?>