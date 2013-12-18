<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<form id="importUserForm" method="post" enctype="multipart/form-data" data-ajax="false" action="<%=basePath%>party/importUserForm">
	<input type="file" name="xlsFile" />
	<input type="submit" value="导入" data-theme="b" />
</form>