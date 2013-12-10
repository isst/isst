<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<base href="<%=basePath%>" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>浙江大学软件学院元旦晚会</title>
<link href="resources/css/party.css"  type="text/css" rel="stylesheet"/>
<style type="text/css">
    .topbox .custom,.topbox .tencentLogo{ float:left;}
    .custom p{ font-size:35px; padding-left:50px;}
</style>
</head>
<body class="wall_screen">
<div class="wrapperSize1280">
<div class="microblog">
    <div id="topbox" class="topbox">
        <div class="tencentLogo">浙江大学软件学院</div>
                <div class="custom" id="picText"><p>浙江大学软件学院元旦晚会</p></div>
            </div>
    <div class="talkBg">
        <div class="talkList">
            <ul id="talkList" class="LC">
			</ul>
        </div>
    </div>
        <div class="control_area undis" id="control_area">
        <a href="javascript:void(0);" class="next" id="toNext" style="visibility:hidden;">下一条</a>
        <a href="javascript:void(0);" class="pre" id="toPrev" style="visibility:hidden;">上一条</a>
        <a href="javascript:void(0);" class="start" id="pause" >暂停</a>
        <a href="javascript:void(0);" class="lottery" id="lottery" >抽奖</a>
        <div class="bg"></div>
    </div>
    </div>
    </div>

<div class="imgPopup" id="imgPopup" style="display:none;">
<div class="blockLayer"></div>
<div class="bigPicBox" id="bigPic"></div>
</div>

<script type="text/javascript" src="resources/js/jquery-2.0.3.min.js"></script>
<script type="text/javascript" src="resources/js/ajax-pushlet-client.js"></script>
<script type="text/javascript">
var spittles = [];
var $ul = $("#talkList");
var doing = false;
var timer = null;

function newTemplate(spittle) {
	var template = '<li id="'+spittle.spittleId+'">' +
	'<div class="user_con cl" style="">' +
		'<div class="msgBox">' +
			'<div rel="abcd1152264185" class="userName" style="font-size: 32px; line-height: 49px; height: 47px;">' +
				'<strong><a title="">'+spittle.nickname+'</a></strong>' +
				'<span class="msgUserTo">:&nbsp;</span>' +
			'</div>' +
			'<div class="msgCnt " style="font-size: 32px; line-height: 49px;">'+spittle.content+'</div>' +
		'</div>' +
	'</div>' +
	'</li>';
    return $(template);
}

$(function() {
	$.getJSON("party/admin/getLotterySpittles.json");
});
</script>
</body>
</html>
