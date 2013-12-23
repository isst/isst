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
</head>
<body>
<p>晚会说明</p>
<a href="http://www.baidu.com">跳转到百度</a>
<script type="text/javascript" src="resources/js/jquery-2.0.3.min.js"></script>
</body>
</html>
