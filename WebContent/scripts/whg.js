function validate_name() {
		var uname=document.getElementById("Name").value;
		if(uname.length < 6 || uname.length > 8) {
				$("#nameError").text("Length must be between 6~8!");
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

function Regisubmit(){
	var name=$("#Name").val();
	var wechat=$("#WeChatID").val();
	var pwd=$("#userpassword").val();
	var fullname=$("#NickName").val(); 
	var email=$("#Email").val();
	$.ajax({
    type: "POST",
    dataType:"JSON",
    url: "",
    async: false,
    data: {'u_name':name,'u_wechat_id':wechat,'u_pw_hash':$.md5(pwd),'u_fullname':fullname,'u_email':email},
	success : function(result){
            if (result.errno==0) { // parameter in their response
                toastr.success("registration succeed!", "info");

            }else{
                toastr.warning(result.err, "Warning:CODE "+result.errno); //pop up
            }
        },
        error:function(){
            toastr.error("error", "error");
        }
    });
    
	
}


function TipForname(){
$("#nameError").text("Input Name.");
document.getElementById("nameError").style.color="Black";
}
