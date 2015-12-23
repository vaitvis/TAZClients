<?php

    include_once "webscript.inc";
    include_once "login.inc";
    include_once "clients.inc";
    include_once "history.inc";
    include_once "stats.inc";
    include_once "tazintegration.inc";
    include_once "database.inc";

    class Index extends Webscript {

        function process($webscript, $method) {
            $this->deleteInvalidTickets();
            try {
                $reflectedWebscript = new ReflectionAnnotatedClass(ucfirst($webscript)."Webscript");
                if($reflectedWebscript->hasMethod($method)) {
                    $methodInstance = $reflectedWebscript->getMethod($method);
                    if($methodInstance->isPublic()) {
                        $args = "";
                        $ticket = "";
                        if($methodInstance->hasAnnotation("Get")) {
                            $args = $_GET['data'];
                            $ticket = $_GET['ticket'];
                        }
                        if($methodInstance->hasAnnotation("Post")) {
                            $args = $_POST['data'];
                            $ticket = $_POST['ticket'];
                        }
                        if($methodInstance->hasAnnotation("RequiresAuthentication")) {
                            if($this->checkAuthentication($ticket, $methodInstance->getAnnotation("RequiresAuthentication"))) {
                                $this->extendAuthentication($ticket);
                            } else {
                                return $this->authenticationFailed();
                            }
                        }
                        return $methodInstance->invoke($reflectedWebscript->newInstance(), $args, $ticket);
                    }
                } else {
                    return $this->notFound();
                }
            } catch (ReflectionException $e) {
                return $this->notFound();
            }
        }
        
        function notFound() {
            $response = $this->http_response_code[404];            
            return $this->deliver_response($response, null, 404);
        }
        
        function deleteInvalidTickets() {
            $db = new Database('tazapi');
            $db->open();
            $sql = "DELETE FROM tickets WHERE expires < NOW()";
            $db->conn()->query($sql);
            $db->close();
        }
        
        function checkAuthentication($ticket, $annotation) {
            $db = new Database('tazapi');
            $db->open();
            $sql = "SELECT role FROM tickets INNER JOIN users ON tickets.user = users.user WHERE ticket = '".$ticket."';";
            $result = $db->conn()->query($sql);
            if ($result->num_rows > 0) {
                if($annotation->role) {
                    while($row = $result->fetch_assoc()) {
                        if($annotation->role != $row['role']) {
                            return false;
                        }
                    }
                }
                $db->close();
                return true;
            } else {
                $db->close();
                return false;
            }
        }
        
        function extendAuthentication($ticket) {
            $db = new Database('tazapi');
            $db->open();
            
            $dateTime = new DateTime('+1 hour', new DateTimeZone('Europe/Vilnius'));
            $expires = $dateTime->format('Y-m-d H:i:s');
            $sql = "UPDATE tickets SET expires='".$expires."' WHERE ticket = '".$ticket."';";
            $db->conn()->query($sql);
            $db->close();
        }
    }

    $indexWebscript = new Index();
    $indexWebscript->process($_GET['webscript'], $_GET['method']);
 
?>
             
