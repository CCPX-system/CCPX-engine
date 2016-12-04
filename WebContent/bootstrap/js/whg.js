function validate_rname() {
		 var uname=document.getElementById("name").value;
		 if(uname.length < 3 || uname.length > 20) {
				$("#nameError").text("Length must be between 3~20!");
				showError($("#nameError"));
				return false;
			}
		 else{
$.ajax({
	 type: "POST",
    dataType:"JSON",
    url: "user/validate_name",
    async: false,
    data: {name: uname
    },
	success:function (data) {
		if(data.message == "exist") {
			$("#nameError").text("Name already exist!");
			document.getElementById("nameError").style.color="Red";
			return false;
		}else{
			$("#nameError").text("Name available.");
			document.getElementById("nameError").style.color="Green";
			return true;
		}
	}
});
		 }
}




function TipForname(){
$("#nameError").text("Input Name.");
document.getElementById("nameError").style.color="Black";
}
