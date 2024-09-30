<?php
	$host='127.0.0.1';
	$uname='root';
	$pwd='';
	$db="qlnh";
    
	$con = mysqli_connect($host,$uname,$pwd, $db) or die("connection failed");
    mysqli_set_charset($con, 'utf8');
    
	$r = mysqli_query($con, "SELECT DISTINCT so_ban FROM orders");
    $data = array();
    while ($row = mysqli_fetch_assoc($r)) {
    $data[] = $row;
    }
    echo json_encode($data);
    mysqli_close($con);


?>