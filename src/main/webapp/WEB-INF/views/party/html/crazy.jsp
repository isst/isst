<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html lang="zh-cn">
<head>
<base href="<%=basePath%>">
<title>点赞狂人 | 点踩狂人 排行榜</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">    
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link type="text/css" rel="stylesheet" href="resources/jquery.jqplot/jquery.jqplot.css" />
<link href="resources/css/party-screen.css"  type="text/css" rel="stylesheet"/>
</head>
<body>

<div class="top">
	<div class="logo"></div>
</div>

<div class="screen">
	<div id="crazy" class="crazy-bg">
		<ul class="crazy-list">
			<li class="crazy-heading">点赞狂人排行榜</li>
		<c:forEach var="crazyUser" items="${likeCrazyUsers}" varStatus="status">
			<li class="crazy-user ${status.index==0?'first':''}">
				<div class="rank">
					<em>${status.index+1}</em>
					<strong>${crazyUser.count}</strong>
				</div>
				<div class="user">
					<label>${crazyUser.name}</label>
					<span>${crazyUser.fullname}</span>
				</div>
			</li>
		</c:forEach>
		</ul>
		
		<ul class="crazy-list last">
			<li class="crazy-heading">点踩狂人排行榜</li>
		<c:forEach var="crazyUser" items="${dislikeCrazyUsers}" varStatus="status">
			<li class="crazy-user ${status.index==0?'first':''}">
				<div class="rank">
					<em>${status.index+1}</em>
					<strong>${crazyUser.count}</strong>
				</div>
				<div class="user">
					<label>${crazyUser.name}</label>
					<span>${crazyUser.fullname}</span>
				</div>
			</li>
		</c:forEach>
		</ul>
	</div>
	
	
	<div class="controls hide" id="controls">
		<a href="<%=basePath%>party/screen.html">评论</a>
		<a href="<%=basePath%>party/lottery.html">抽奖</a>
		<a href="<%=basePath%>party/votes.html">投票</a>
		<div class="bg"></div>
	</div>
</div>

<script type="text/javascript" src="resources/js/jquery-2.0.3.min.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/js/party-screen.js"></script>
</body>
</html>
