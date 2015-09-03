<?php

    include_once "webscript.inc";
    include_once "database.inc";

    class TazIntegrationWebscript extends Webscript {
    	
    	/**
    	 *   @Get
    	 *   @RequiresAuthentication
    	 */
    	function getProformaInvoiceId($args, $ticket) {
    		$db = new Database('taz');
    		$db->open();
    		
    		$sql = "SELECT nextval('seq_saskaita')";
    		$result = $db->conn()->query($sql);
    		$nextId = $result->fetch_assoc()["nextval('seq_saskaita')"];
    		$db->close();
    		$this->deliver_response($nextId, $ticket);
    	}
    }
?>