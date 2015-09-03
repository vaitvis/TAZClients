<?php

    include_once "webscript.inc";
    include_once "database.inc";

    class ClientsWebscript extends Webscript {
        
        /** 
        *   @Post
        *   @RequiresAuthentication
        */
        function update($args, $ticket) {
            $db = new Database('tazapi');
            $db->open();
            
            $object = json_decode($args);
            if($object->id) {
                $sql = "UPDATE clients SET ADDRESSEE=?, COMPANYTITLE=?, COMPANYCODE=?, COMPANYVATCODE=?, ADDRESS=?, PHONENUMBER=?, EMAIL=?, VALIDFROM=?, VALIDTO=?, CLIENTSTATE=?, COMMENT=? WHERE ID = ?";
                $stmt = $db->conn()->prepare($sql);
                $stmt->bind_param("sssssssssisi", $addressee, $companyTitle, $companyCode, $companyVatCode, $address, $phoneNumber, $email, $validFrom, $validTo, $clientState, $comment, $id);
            } else {
                $sql = "INSERT INTO clients (ADDRESSEE, COMPANYTITLE, COMPANYCODE, COMPANYVATCODE, ADDRESS, PHONENUMBER, EMAIL, VALIDFROM, VALIDTO, CLIENTSTATE, COMMENT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                $stmt = $db->conn()->prepare($sql);
                $stmt->bind_param("sssssssssis", $addressee, $companyTitle, $companyCode, $companyVatCode, $address, $phoneNumber, $email, $validFrom, $validTo, $clientState, $comment);
            }
            $addressee = $object->addressee;
            $companyTitle = $object->companyTitle;
            $companyCode = $object->companyCode;
            $companyVatCode = $object->companyVatCode;
            $address = $object->address;
            $phoneNumber = $object->phoneNumber;
            $email = $object->email;
            $validFrom = $object->validFrom;
            $validTo = $object->validTo;
            $clientState = $object->clientState;
            $comment = $object->comment;
            if($object->id) {
                $id = $object->id;
            }
            $stmt->execute();
            $stmt->close();
            $api_result = new InsertResult();
            if($object->id) {
                $api_result->id = $object->id;
            } else {
                $api_result->id = $db->conn()->insert_id;
            }
            $db->close();
            $this->deliver_response($api_result, $ticket);
        }
        
         /** 
        *   @Get
        *   @RequiresAuthentication
        */
        function get($args, $ticket) {
            $db = new Database('tazapi');
            $db->open();
            
            $object = json_decode($args);
            $sql = "SELECT ID, ADDRESSEE, COMPANYTITLE, COMPANYCODE, COMPANYVATCODE, ADDRESS, PHONENUMBER, EMAIL, VALIDFROM, VALIDTO, CLIENTSTATE, COMMENT FROM clients WHERE ID = ?";
            $stmt = $db->conn()->prepare($sql);
            $stmt->bind_param("i", $id);
            $id = $object->id;
            $stmt->execute();
            $client = new Client();
            $stmt->bind_result($client->id, $client->addressee, $client->companyTitle, $client->companyCode, $client->companyVatCode, $client->address, $client->phoneNumber, $client->email, $client->validFrom, $client->validTo, $client->clientState, $client->comment);
            $stmt->fetch();
            $stmt->close();
            $db->close();
            $this->deliver_response($client, $ticket);
        }
        
         /** 
        *   @Get
        *   @RequiresAuthentication(role = "admin")
        */
        function delete($args, $ticket) {
            $db = new Database('tazapi');
            $db->open();
            
            $object = json_decode($args);
            $sql = "DELETE FROM clients WHERE ID = ?";
            $stmt = $db->conn()->prepare($sql);
            $stmt->bind_param("i", $id);
            $id = $object->id;
            $stmt->execute();
            $stmt->close();
            $db->close();
        }
        
         /** 
        *   @Get
        *   @RequiresAuthentication
        */
        function filter($args, $ticket) {
            $db = new Database('tazapi');
            $db->open();
            
            $object = json_decode($args);
            
            $wheres = array();
            if($object->id) {
                $wheres[] = "ID = ".$object->id;
                if(!$this->isAdmin($ticket)) {
                    $wheres[] = "CLIENTSTATE <> 4";
                }
            } else {
                if($object->filter) {
                    $wheres[] = "(ADDRESSEE like '%".$object->filter."%' OR COMPANYTITLE like '%".$object->filter."%' OR COMPANYCODE like '%".$object->filter."%' OR COMPANYVATCODE like '%".$object->filter."%' OR ADDRESS like '%".$object->filter."%' OR PHONENUMBER like '%".$object->filter."%' OR EMAIL like '%".$object->filter."%')";
                }
                if($object->validFrom) {
                    $wheres[] = "VALIDTO >= '".$object->validFrom."'";
                }
                if($object->validTo) {
                    $wheres[] = "VALIDTO <= '".$object->validTo."'";
                }
                if($object->state) {
                    $wheres[] = "CLIENTSTATE = ".$object->state;
                } else if(!$this->isAdmin($ticket)) {
                    $wheres[] = "CLIENTSTATE <> 4";
                }
            }
            
            $sql = "SELECT ID, ADDRESSEE, COMPANYTITLE, COMPANYCODE, COMPANYVATCODE, ADDRESS, PHONENUMBER, EMAIL, VALIDFROM, VALIDTO, CLIENTSTATE, COMMENT FROM clients";
            $where_string = implode(' AND ', $wheres);
            if ($where_string) {
                $sql .= " WHERE " . $where_string;
            }
            $result = $db->conn()->query($sql);
            $totalRows = $result->num_rows;
            
            $pagedResult = new PagedResult();
            $pagedResult->totalRows = $totalRows;
            $pagedResult->rows = array();
            if($totalRows > 0) {
                $sql .= " LIMIT ".$object->page->startAt.", ".$object->page->size;
                $result = $db->conn()->query($sql);
                if($result->num_rows > 0) {
                    while($row = $result->fetch_assoc()) {
                        $client = new Client();
                        $client->id = $row["ID"];
                        $client->addressee = $row["ADDRESSEE"];
                        $client->companyTitle = $row["COMPANYTITLE"];
                        $client->companyCode = $row["COMPANYCODE"];
                        $client->companyVatCode = $row["COMPANYVATCODE"];
                        $client->address = $row["ADDRESS"];
                        $client->phoneNumber = $row["PHONENUMBER"];
                        $client->email = $row["EMAIL"];
                        $client->validFrom = $row["VALIDFROM"];
                        $client->validTo = $row["VALIDTO"];
                        $client->clientState = $row["CLIENTSTATE"];
                        $client->comment = $row["COMMENT"];
                        $pagedResult->rows[] = $client;
                    }
                }
            }
            
            $db->close();
            $this->deliver_response($pagedResult, $ticket);
        }
    }
    
    class InsertResult {
        public $id;
    }
    
    class PagedResult {
        public $totalRows;
        public $rows;
    }
    
    class Client {
        public $id;
        public $addressee;
        public $companyTitle;
        public $companyCode;
        public $companyVatCode;
        public $address;
        public $phoneNumber;
        public $email;
        public $validFrom;
        public $validTo;
        public $clientState;
        public $comment;
    }
    
?>