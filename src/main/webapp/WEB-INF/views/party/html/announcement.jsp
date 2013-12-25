<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html lang="zh-cn">
<head>
<base href="<%=basePath%>" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>浙江大学软件学院元旦晚会说明</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">

<link href="resources/css/bootstrap.min.css"  type="text/css" rel="stylesheet"/>
<link href="resources/css/bootstrap-theme.min.css"  type="text/css" rel="stylesheet"/>

<script type="text/javascript" src="resources/js/jquery-2.0.3.min.js"></script>
<script type="text/javascript" src="resources/js/bootstrap.min.js"></script>

<style type="text/css">
body { padding: 1em;}
</style>
</head>
<body>
<%@ include file="/WEB-INF/views/party/block/announcement.jsp" %>
</body>
</html>
