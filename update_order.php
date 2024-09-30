<?php
	$host='127.0.0.1';
	$uname='root';
	$pwd='';
	$db="qlnh";
    
	$con = mysqli_connect($host,$uname,$pwd, $db) or die("connection failed");
    mysqli_set_charset($con, 'utf8');
    if(isset($_REQUEST['so_ban'])) {
        $so_ban=$_REQUEST['so_ban'];
        $ten_mon1=$_REQUEST['ten_mon1'];
        $sp_mon1 = $_REQUEST['sp_mon1'];
        $ten_mon2=$_REQUEST['ten_mon2'];
        $sp_mon2 = $_REQUEST['sp_mon2'];
        $ten_mon3=$_REQUEST['ten_mon3'];
        $sp_mon3 = $_REQUEST['sp_mon3'];
        $ten_mon4=$_REQUEST['ten_mon4'];
        $sp_mon4 = $_REQUEST['sp_mon4'];
        $ten_mon5=$_REQUEST['ten_mon5'];
        $sp_mon5 = $_REQUEST['sp_mon5'];
        $ten_mon6=$_REQUEST['ten_mon6'];
        $sp_mon6 = $_REQUEST['sp_mon6'];
        $ten_mon7=$_REQUEST['ten_mon7'];
        $sp_mon7 = $_REQUEST['sp_mon7'];
        $ten_mon8=$_REQUEST['ten_mon8'];
        $sp_mon8 = $_REQUEST['sp_mon8'];
        $tong_cong = $_REQUEST['tong_cong'];
	       
        $id = mysqli_query($con, "SELECT id FROM orders WHERE so_ban='".$so_ban."' ORDER BY id DESC LIMIT 1");
        $row=$id->fetch_assoc();
        $id_num= (int)$row['id'];
        
		$sql = mysqli_query($con, "UPDATE orders SET ten_mon1='".$ten_mon1."', sp_mon1='".$sp_mon1."', ten_mon2='".$ten_mon2."', sp_mon2='".$sp_mon2."', ten_mon3='".$ten_mon3."', sp_mon3='".$sp_mon3."', ten_mon4='".$ten_mon4."', sp_mon4='".$sp_mon4."', ten_mon5='".$ten_mon5."', sp_mon5='".$sp_mon5."', ten_mon6='".$ten_mon6."', sp_mon6='".$sp_mon6."', ten_mon7='".$ten_mon7."', sp_mon7='".$sp_mon7."', ten_mon8='".$ten_mon8."', sp_mon8='".$sp_mon8."', tong_cong='".$tong_cong."' WHERE id='".$id_num."'");
		if($sql){
		  echo 'success';
		}
		mysqli_close($con);
    }
    else {
        echo 'Fail';
    }

?>