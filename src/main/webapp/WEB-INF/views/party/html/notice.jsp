<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html lang="zh-cn">
<head>
<base href="<%=basePath%>" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>晚会说明</title>
<link href="resources/css/party-screen.css"  type="text/css" rel="stylesheet"/>
<style type="text/css">
.notice p { color: #fff; font-size: 36px; font-weight: bold; word-break: break-all; word-wrap: break-word; line-height: 1.5em; clear: both; }
.ew-android, .ew-web { position: absolute; top: 33%;}
.ew-android h2, .ew-web h2 { text-align: center;}
.ew-android { left: 20px; }
.ew-web { right: 20px;}
</style>
</head>
<body>
<div class="top">
	<div class="logo"></div>
</div>

<div class="screen">
	<div class="spittle-bg">
		<div class="spittle-wrapper notice">
			<p>欢迎下载Android版本：<em>http://www.zjucst.com/downloads/isst-1.0.0.pak</em>，
			或登录<em>http://www.zjucst.com/isst/party/，</em>
			嘉宾登录用户名：姓名全拼，密码为指定密码；学生登录用户名：学号，密码：身份证后六位（若不能登录请使用“123456”登录），参与元旦晚会在线评论及投票，赢取丰厚礼品！机会就在身边，小伙伴请快快拿起手机登录吧！</p>
		</div>
    </div>
</div>
<div class="ew-android"><img src="resources/images/ew-android.jpg" width="250" /><h2>Android版</h2></div>
<div class="ew-web"><img src="resources/images/ew-web.jpg" width="250" /><h2>Web版</h2></div>

<script type="text/javascript" src="<%=basePath%>resources/js/jquery-2.0.3.min.js"></script>
</body>
</html>
