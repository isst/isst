<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<form action="<%=basePath%>party/userForm" method="post" id="userForm">
	<fieldset>
		<input name="id" type="hidden" value="${user.id}" />
		
		<label for="name" class="ui-hidden-accessible">用户名</label>
		<input type="text" name="name" id="name" value="${user.name}" placeholder="用户名" />
		
		<label for="password" class="ui-hidden-accessible">密码</label>
		<input type="text" name="password" value="" placeholder="密码" />
		
		<label for="fullname" class="ui-hidden-accessible">真实姓名</label>
		<input type="text" name="fullname" value="${user.fullname}" placeholder="真实姓名" />
		
		<label for="nickname" class="ui-hidden-accessible">昵称</label>
		<input type="text" name="nickname" value="${user.nickname}" placeholder="昵称" />

		<fieldset data-role="controlgroup" data-type="horizontal">
		    <input name="type" id="type-0" value="0" ${user.type==0?'checked="checked"':''} type="radio" />
		    <label for="type-0">学生</label>
		    <input name="type" id="type-1" value="1" ${user.type==1?'checked="checked"':''} type="radio" />
		    <label for="type-1">老师</label>
		</fieldset>
		
		<input type="submit" value="保存" data-theme="b" />
	</fieldset>
</form>

<script type="text/javascript">
$(function() {
	$("#userForm").submit(function() {
		var name = $.trim($("#name").val());
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