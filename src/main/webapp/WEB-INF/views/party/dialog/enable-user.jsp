<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<form action="<%=basePath%>party/enableUser" method="post" id="enabledUserForm">
<input type="hidden" value="${userId}" name="userId" />
<input type="submit" value="取消禁言" data-theme="b" />
</form>
<script type="text/javascript">
$(function() {
	$("#enabledUserForm").submit(function() {
		$.post($(this).attr("action"), $(this).serialize(), function(response) {
				if (response.code > 0) {
					window.location.href = "<%=basePath%>party/admin.html";
				} else {
					alert(response.message);
				}
			});
		return false;
	});
});
</script>