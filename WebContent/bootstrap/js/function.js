//首页登录时使用的异步验证
function check_login_form(){
{
    $("loginbutton").attr({"disabled":"disabled"});
    var flag = false;
    username = $("#Usernane").val();
    pwd =  $("#password").val();
    var reg = new RegExp("^\\w*$");

    if (!reg.test(username)) 
    {
        toastr.warning("username contains illegal characters", "Warning");
        $("#Usernane").focus();
        return false;
    }
    if (!reg.test(pwd)) 
    {
        toastr.warning("password contains illegal characters", "Warning");
        $("#password").focus();
        return false;
    }

    data = {'u_name':username,'u_pw_hash':$.md5(pwd)};
    $.ajax({
        type : "post",
        data : data,
        async: false,
        url : "/ccpx/user/login",
        success : function(result){
            if (result.errno==0) {
                toastr.success(result.rsm.token, "info");
                $.cookie('u_id',result.rsm.id);
                $.cookie('u_token',result.rsm.token);
                
                var i = $.cookie('u_id');
                var t = $.cookie('u_token');
                alert(i);
                alert(t);
            }else{
                toastr.warning(result.err, "Warning:CODE "+result.errno);
            }
        },
        error:function(){
            toastr.error("error", "error");
        }
    });
    return false;
    
    //toastr.warning(flag.toString(), "DEBUG");
    $("#loginButton").removeAttr("disabled");
    return flag;
}
}



















//以下函数不要使用，开发完成后请删除
function checkform_nav()
{
    $("#phoneNumber").attr({"disabled":"disabled"});
    var flag = false;
    phoneNumber = $("#phoneNumber").val();
    pwd = $("#pwd").val();
    var regPwd = new RegExp("^\\w*$");
    var regPh = new RegExp("^\\d*$");

    if (!regPh.test(phoneNumber)) 
    {
        toastr.warning("手机号不符合规范", "警告");
        //toastr.info(phoneNumber, "DEBUG");
        $("#phoneNumber").focus();
        return false;
    }
    if (!regPwd.test(pwd)) 
    {
        toastr.warning("密码含有非法字符", "警告");
        $("#pwd").focus();
        return false;
    }

    data = {'u_phone':phoneNumber,'u_passwd':pwd};
    $.ajax({
        type: 'POST',
        url: '/TVCalendarAPI/index.php/UI/ajaxCheckPw',
        data: data,
        async:false,
        error: function(XMLHttpRequest, textStatus, errorThrown)
        {
          alert(XMLHttpRequest.status);
          alert(XMLHttpRequest.readyState);
          alert(textStatus);
        },
        success: function(result)
        {
            if (result == "OK") 
            {
                toastr.success("验证成功", "信息");
                //window.location.href("/TVCalendarAPI/index.php/UI/index");
                flag = true;
            }
            else if(result == 'WrongPW')
            {
                toastr.error("用户名或密码错误", "错误");
                flag = false;
            }
            else
            {
                toastr.error("参数错误", "错误");
                flag = false;
            } 
        },
    });
    //toastr.warning(flag.toString(), "DEBUG");
    $("#phoneNumber").removeAttr("disabled");
    return flag;
}



function check_form_usercenter() 
{
    $("#updateButton").attr({"disabled":"disabled"});
    var flag = false;
    var exName = $("#exName").val();
    var name = $("#inputName").val();
    var pwd = $("#inputPassword").val();
    var pwdNew = $("#inputPasswordNew").val();
    var pwdNewRe = $("#inputPasswordNewRe").val();

    if ((exName == name) && (pwdNew) == "")
    {
        toastr.warning("您并未对个人信息作出修改","警告");
        flag = false;
        $("#updateButton").removeAttr("disabled");
        return false;
    }
    if (pwd == "") 
    {
        toastr.warning("请输入密码","警告");
        flag = false;
        $("#updateButton").removeAttr("disabled");
        return false;
    }
    var data = {'name':'','pwd':'','pwdNew':''};
    if (pwdNew != "") 
    {
        if (pwdNew != pwdNewRe) 
        {
            toastr.warning("新密码输入不一致","警告");
            flag = false;
            $("#updateButton").removeAttr("disabled");
            return false;
        }
        if (pwd == pwdNew) 
        {
            toastr.warning("您未更改密码","警告");
            flag = false;
            $("#updateButton").removeAttr("disabled");
            return false;
        }
        var regPwd = new RegExp("^\\w*$");
        if (!regPwd.test(pwd) || !regPwd.test(pwdNew)) 
        {
            toastr.error("密码存在不合法字符", "错误");
            flag = false;
            $("#updateButton").removeAttr("disabled");
            return false;
        }
        var data = {'name':name,'pwd':pwd,'pwdNew':pwdNew}
    }
    else
    {
        var regName = new RegExp("'");
        if (regName.test(name)) 
        {
          toastr.warning("新昵称存在敏感字符", "警告");
          flag = false;
          $("#updateButton").removeAttr("disabled");
          $("#inputName").focus();
          return false;
        }
        var data = {'name':name,'pwd':pwd,'pwdNew':pwd}
    }
        
    $.ajax({
        type: 'POST',
        url: '/TVCalendarAPI/index.php/UI/ajaxUpdateUser',
        data: data,
        async:false,
        error: function(XMLHttpRequest, textStatus, errorThrown)
        {
          toastr.info("Status:"+XMLHttpRequest.status+"\nreadyState:"+XMLHttpRequest.readyState+"\ntext:"+textStatus, "DEBUG");
          toastr.error("Ajax错误", "错误");
        },
        success: function(result)
        {
          if (result == "OK") 
          {
            $("#inputPassword").val('');
            $("#inputPasswordNew").val('');
            $("#inputPasswordNewRe").val('');
            $("#username_h").html(name+'<span class="caret"></span>');
            toastr.success("更新个人资料成功", "信息");
          }
          else if(result == 'WrongPw')
          {
            toastr.warning("原密码不正确", "警告");
          }
          else
          {
            toastr.info(result, "DEBUG");
            toastr.error("参数错误", "错误");
          }
        },
      });
    
    $("#updateButton").removeAttr("disabled");
    return false;
}

function subscribe(s_id,u_id,button_name)
{
    var flag = false;
    var ids = new RegExp("^\\d*$");
    if (!ids.test(s_id)||!ids.test(u_id))
    {
      toastr.error("参数传递错误", "错误");
      //toastr.info(phoneNumber, "DEBUG");
      $("button[name=s"+button_name+"]").focus();
      return false;
    }
    $("button[name=s"+button_name+"]").attr({"disabled":"disabled"});
    data = {'u_id':u_id,'s_id':s_id};
    $.ajax({
      type: 'POST',
      url: '/TVCalendarAPI/index.php/UI/ajaxSubscribe',
      data: data,
      async:true,
      error: function(XMLHttpRequest, textStatus, errorThrown)
      {
        toastr.error("Ajax故障", "错误");
        toastr.info("Status:"+XMLHttpRequest.status+"\nreadyState:"+XMLHttpRequest.readyState+"\ntext:"+textStatus, "DEBUG");
      },
      success: function(result)
      {
        if (result.substr(0,3) == "OK:") 
        {
          toastr.success("订阅:"+result.substr(3), "操作成功");
          //window.location.href("/TVCalendarAPI/index.php/UI/index");
          $("button[name=s"+button_name+"]").removeAttr("disabled");
          $("button[name=s"+button_name+"]").hide();
          $("button[name=u"+button_name+"]").show();
          flag = true;
        }
        else if(result == "Repeat")
        {
            $("button[name=s"+button_name+"]").removeAttr("disabled");
            toastr.warning("您已订阅过:"+result.substr(3), "警告");
            $("button[name=s"+button_name+"]").hide();
            $("button[name=u"+button_name+"]").show();
            flag = true;
        }
        else
        {
          $("button[name=s"+button_name+"]").removeAttr("disabled");
          toastr.error("未知错误", "错误");
          flag = false;
        }
      },
    });
    //toastr.warning(flag.toString(), "DEBUG");
    return flag;
}

function unsubscribe(s_id,u_id,button_name) 
{
    var flag = false;
    var ids = new RegExp("^\\d*$");
    if (!ids.test(s_id)||!ids.test(u_id))
    {
      toastr.error("参数传递错误", "错误");
      //toastr.info(phoneNumber, "DEBUG");
      $("#"+button_name).focus();
      return false;
    }
    $("button[name=u"+button_name+"]").attr({"disabled":"disabled"});
    data = {'u_id':u_id,'s_id':s_id};
    $.ajax({
      type: 'POST',
      url: '/TVCalendarAPI/index.php/UI/ajaxUnsubscribe',
      data: data,
      async:true,
      error: function(XMLHttpRequest, textStatus, errorThrown)
      {
        toastr.error("Ajax故障", "错误");
        toastr.info("Status:"+XMLHttpRequest.status+"\nreadyState:"+XMLHttpRequest.readyState+"\ntext:"+textStatus, "DEBUG");
      },
      success: function(result)
      {
        if (result.substr(0,3) == "OK:") 
        {
          toastr.success("取消订阅:"+result.substr(3), "操作成功");
          //window.location.href("/TVCalendarAPI/index.php/UI/index");
          $("button[name=u"+button_name+"]").removeAttr("disabled");
          $("button[name=u"+button_name+"]").hide();
          $("button[name=s"+button_name+"]").show();
          flag = true;
        }
        else if(result == "None")
        {
            toastr.warning("您还未订阅过:"+result.substr(3), "警告");
            $("button[name=u"+button_name+"]").removeAttr("disabled");
            $("button[name=u"+button_name+"]").hide();
            $("button[name=s"+button_name+"]").show();
            flag = true;
        }
        else
        {
          $("button[name=u"+button_name+"]").removeAttr("disabled");
          toastr.error("未知错误", "错误");
          flag = false;
        }
      },
    });
    //toastr.warning(flag.toString(), "DEBUG");
    return flag;
}

function changeArrow(id) 
{
    $("#up"+id).toggle();
    $("#down"+id).toggle();
}

function syn(u_id,e_id) 
{
    var ids = new RegExp("^\\d*$");
    if (!ids.test(e_id)||!ids.test(u_id))
    {
        toastr.error("参数传递错误", "错误");
        //toastr.info(phoneNumber, "DEBUG");
        $("#sep"+e_id).focus();
    }
    $("button[id=s"+e_id+"]").attr({"disabled":"disabled"});
    $("#sep"+e_id).attr({"disabled":"disabled"});
    data = {'u_id':u_id,'e_id':e_id};
    $.ajax({
        type: 'POST',
        url: '/TVCalendarAPI/index.php/UI/ajaxSynchron',
        data: data,
        async:true,
        error: function(XMLHttpRequest, textStatus, errorThrown)
        {
            toastr.error("Ajax故障", "错误");
            toastr.info("Status:"+XMLHttpRequest.status+"\nreadyState:"+XMLHttpRequest.readyState+"\ntext:"+textStatus, "DEBUG");
        },
        success: function(result)
        {
            //toastr.info(result, "DEBUG");
            if (result.substr(0,3) == "OK:") 
            {
                toastr.success("您已观看:"+result.substr(3), "同步完成");
                $("button[id=s"+e_id+"]").removeAttr("disabled");
                $("#sep"+e_id).removeAttr("disabled");
                $("button[id=s"+e_id+"]").hide();
                $("button[id=u"+e_id+"]").show();
                $("#sep"+e_id).hide();
                $("#syned"+e_id).show();
            }
            else if(result == "Repeat")
            {
                toastr.warning("您曾观看过:"+result.substr(3), "警告");
                $("button[id=s"+e_id+"]").removeAttr("disabled");
                $("#sep"+e_id).removeAttr("disabled");
                $("button[id=s"+e_id+"]").hide();
                $("button[id=u"+e_id+"]").show();
                $("#sep"+e_id).hide();
                $("#syned"+e_id).show();
            }
            else
            {
                toastr.error("未知错误", "错误");
            }
        },
    });
    //toastr.warning(flag.toString(), "DEBUG");
}

function unsyn(u_id,e_id) 
{
    var ids = new RegExp("^\\d*$");
    if (!ids.test(e_id)||!ids.test(u_id))
    {
        toastr.error("参数传递错误", "错误");
        //toastr.info(phoneNumber, "DEBUG");
        $("#u"+e_id).focus();
    }
    $("#u"+e_id).attr({"disabled":"disabled"});
    data = {'u_id':u_id,'e_id':e_id};
    $.ajax({
        type: 'POST',
        url: '/TVCalendarAPI/index.php/UI/ajaxUnsynchron',
        data: data,
        async:true,
        error: function(XMLHttpRequest, textStatus, errorThrown)
        {
            toastr.error("Ajax故障", "错误");
            toastr.info("Status:"+XMLHttpRequest.status+"\nreadyState:"+XMLHttpRequest.readyState+"\ntext:"+textStatus, "DEBUG");
        },
        success: function(result)
        {
            if (result.substr(0,3) == "OK:") 
            {
                toastr.success("取消观看:"+result.substr(3), "操作完成");
                //window.location.href("/TVCalendarAPI/index.php/UI/index");
                //$("button[id=u"+e_id+"]").hide();
                //$("button[ud=s"+e_id+"]").show();
                $("#u"+e_id).removeAttr("disabled");
                $("#u"+e_id).hide();
                $("#s"+e_id).show();
            }
            else if(result == "None")
            {
                toastr.warning("您还未观看过:"+result.substr(3), "警告");
                $("#u"+e_id).removeAttr("disabled");
                $("#u"+e_id).hide();
                $("#s"+e_id).show();
            }
            else
            {
                toastr.error("未知错误", "错误");
            }
        },
    });
    //toastr.warning(flag.toString(), "DEBUG");
}
function checkform_reg()
{
    $("#regButton").attr({"disabled":"disabled"});
    var flag = false;
    var phoneNumber = document.getElementById("inputPhone").value;
    var usrName = document.getElementById("usrName").value;
    var pwd =  document.getElementById("inputPassword").value;
    var pwdc = document.getElementById("passwdRepeat").value;
    var captcha  = $("#captcha").val();
    var regPwd = new RegExp("^\\w*$");
    var regPh = new RegExp("^\\d{11,}$");
    var regName = new RegExp("'");
    if (!$("#checkAgree").is(":checked")) 
    {
        toastr.warning("您未接受协议", "警告");
        $("#checkAgree").focus();
        return false;
    }
    if (!regPh.test(phoneNumber)) 
    {
        toastr.warning("手机号不符合规范", "警告");
        document.getElementById("inputPhone").focus();
        return false;
    }
    if (!regPwd.test(pwd)) 
    {
        toastr.warning("密码含有非法字符", "警告");
        document.getElementById("inputPassword").focus();
        return false;
    }
    if (pwd != pwdc) 
    {
        toastr.warning("两次密码输入不一致", "警告");
        document.getElementById("passwdRepeat").focus();
        return false;
    }
    if (regName.test(usrName)) 
    {
        toastr.warning("昵称存在敏感字符", "警告");
        $("#usrName").focus();
        return false;
    }
    if (usrName == "") 
    {
        toastr.info("未输入昵称，默认使用手机号代替", "信息");
        usrName = phoneNumber;
    }
        data = {'u_phone':phoneNumber,'u_name':usrName,'u_passwd':pwd,'captcha':captcha};
        $.ajax({
        type: 'POST',
        url: '/TVCalendarAPI/index.php/UI/ajaxReg',
        data: data,
        async:false,
        error: function(XMLHttpRequest, textStatus, errorThrown)
        {
            alert(XMLHttpRequest.status);
            alert(XMLHttpRequest.readyState);
            alert(textStatus);
            toastr.error("网络错误", "错误");
        },
        success: function(result)
        {
            if (result == "OK") 
            {
                toastr.success("注册成功", "信息");
                //window.location.href("/TVCalendarAPI/index.php/UI/index");
                flag = true;
            }
            else if(result == 'WrongPh')
            {
                toastr.error("该手机号已被使用", "错误");
                flag = false;
            }
            else if(result == 'WrongCaptcha')
            {
                toastr.error("验证码不正确", "错误");
                change_captcha();
                flag = false;
            }
            else
            {
                toastr.info(result, "DEBUG");
                toastr.error("参数错误", "错误");
                flag = false;
            }
        },
    });
    //toastr.warning(flag.toString(), "DEBUG");
    $("#regButton").removeAttr("disabled");
    return flag;
}
function change_captcha() 
{
    var seed = parseInt(Math.random()*50000+1); 
    $("#captcha_img").attr('src','/TVCalendarAPI/index.php/UI/get_code?_='+seed);
}
function add_download_count(Durl,id) 
{
    x=getArgs();
    var data = {'e_id':x['e_id']};
    //debugger;
    $.ajax({
        type: 'POST',
        url: '/TVCalendarAPI/index.php/UI/ajaxCountDownload',
        data: data,
        async:true,
        error: function(XMLHttpRequest, textStatus, errorThrown)
        {
            toastr.error(XMLHttpRequest.status, "连接异常,请直接点击链接下载！");
        },
        success: function(result)
        {
            if (result == "OK") 
            {
                toastr.success("链接生成成功，如下载软件未启用，\n请点击或复制这个链接自行下载", "信息");
                $("#down"+id).attr('href',Durl);
                $("#down"+id).removeAttr("onclick");
                $("#down"+id).html(Durl); 
            }
            else
            {
                toastr.info(result, "DEBUG");
                toastr.success("链接生成成功，如下载软件未启用，\n请点击或复制这个链接自行下载", "信息");
            }
        },
    });
    window.location.href = Durl;
    
}

function getArgs(){
    var args = {};
    var match = null;
    var search = decodeURIComponent(location.search.substring(1));
    var reg = /(?:([^&]+)=([^&]+))/g;
    while((match = reg.exec(search))!==null){
        args[match[1]] = match[2];
    }
    return args;
}