<?php

	$host='127.0.0.1';
	$uname='root';
	$pwd='';
	$db="qlnh";
    
	$con = mysqli_connect($host,$uname,$pwd, $db) or die("connection failed");
    mysqli_set_charset($con, 'utf8');
    
    if(isset($_REQUEST['so_ban_bill'])) {
        $so_ban_bill = $_REQUEST['so_ban_bill'];
        $r = mysqli_query($con, "SELECT tong_cong FROM orders WHERE so_ban = '$so_ban_bill'");
        while($row = mysqli_fetch_array($r)) {
            $flag['tong_cong'] = $row['tong_cong'];
        }
	   echo json_encode($flag);
	   mysqli_close($con);
    }



?>