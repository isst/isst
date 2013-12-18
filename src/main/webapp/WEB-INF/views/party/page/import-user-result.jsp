<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<h3><c:out value="${title}" /></h3>

<div>
<c:if test="${error!=null}">
<p><c:out value="${error}" /></p>
</c:if>

<p>
	<c:out value="${contentType}" />
	解析出<c:out value="${total}" />位学生，已导入<c:out value="${count}" />位学生
</p>

</div>