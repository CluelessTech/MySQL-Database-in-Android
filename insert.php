<?php
	$con = mysqli_connect("localhost", "root" ,"", "curdOperation");
	if(!$con)
		die('Could not connect '. mysqli_error());

	$id = $_REQUEST['inputID'];
	$name = $_REQUEST['inputName'];
	$programmingLanguage = $_REQUEST['inputProgLang'];

	// $id = "1";
	// $name = "P";
	// $programmingLanguage = "Java";

	if($id==NULL || $name==NULL || $programmingLanguage==NULL){
		$r["re"]="Fill all the field!!";
		print(json_encode($r));
	}
	else{
		$insert = mysqli_query($con,"select * from programmingTable where id=$id");
		$check='';
		while($row = mysqli_fetch_array($insert)){
			$check=$row['id'];
		}

		if($check==NULL){
			$query ="insert into programmingTable values('$id', '$name', '$programmingLanguage')";
			$conn = mysqli_query($con, $query);
			if($conn){

				$r["re"]="Record inserted successfully";
				print(json_encode($r));
			}else{
				$r["re"]="Record not inserted";
				print(json_encode($r));
			}
			
		}else{
			$r["re"]="Record ID already exists";
			print(json_encode($r));
		}
	}
	mysqli_close($con);
?>