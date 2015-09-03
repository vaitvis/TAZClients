<?php

    class Database {
        private $dbHost;
        private $dbName;
        private $dbUser;
        private $dbPass;
        
        private $conn;
        
        function __construct($database) {
        	$config = parse_ini_file($database . "_db.ini");
        	$this->dbHost = $config['dbHost'];
        	$this->dbName = $config['dbName'];
        	$this->dbUser = $config['dbUser'];
        	$this->dbPass = $config['dbPass'];
        }
        
        function open() {
            $this->conn = new mysqli($this->dbHost, $this->dbUser, $this->dbPass, $this->dbName);
        }
        
        function conn() {
            return $this->conn;
        }
        
        function close() {
            $this->conn->close();
        }
    }

?>