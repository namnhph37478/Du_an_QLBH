<?php

	$host='127.0.0.1';
	$uname='root';
	$pwd='';
	$db="qlnh";
    
	$con = mysqli_connect($host,$uname,$pwd, $db) or die("connection failed");
    mysqli_set_charset($con, 'utf8');
    
    if(isset($_REQUEST['so_ban'])) {
        $so_ban = $_REQUEST['so_ban'];
        $r = mysqli_query($con, "SELECT * FROM orders WHERE id= (SELECT id FROM orders WHERE so_ban= '$so_ban' ORDER BY id DESC LIMIT 1)");
		$row = mysqli_fetch_array($r, MYSQL_ASSOC);
        foreach($row as $key => $value){
			$flag[$key] = $value;
		}
		echo json_encode($flag);
	   
	   mysqli_close($con);
    }



?>