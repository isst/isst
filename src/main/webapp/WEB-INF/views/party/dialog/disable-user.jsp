<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<form action="<%=basePath%>party/disableUser" method="post" id="disabledUserForm">
<input type="text" id="disabledUserText" name="userName" placeholder="请输入要禁言的用户名" />
<input type="submit" value="确定"  data-inline="true" data-theme="b" />
</form>
<script type="text/javascript">
$(function() {
	$("#disabledUserForm").submit(function() {
		var name = $.trim($("#disabledUserText").val());
		if (name) {
			$.post($(this).attr("action"), $(this).serialize(), function(response) {
				if (response.code > 0) {
					window.location.href = "<%=basePath%>party/admin.html";
				} else {
					alert(response.message);
				}
			});
		}
		return false;
	});
});
</script>