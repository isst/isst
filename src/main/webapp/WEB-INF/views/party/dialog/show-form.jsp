<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<form action="<%=basePath%>party/showForm" method="post" id="showForm">
	<fieldset>
		<input name="id" type="hidden" value="${show.id}" />
		
		<label for="name" class="ui-hidden-accessible">节目名称</label>
		<input type="text" name="name" id="showName" value="${show.name}" placeholder="节目名称" />
		
		<label for="organization" class="ui-hidden-accessible">节目团队</label>
		<input type="text" name="organization" value="${show.organization}" placeholder="节目团队" />
		
		<label for="category" class="ui-hidden-accessible">节目类型</label>
		<input type="text" name="category" value="${show.category}" placeholder="节目类型" />
		
		<label for="sortNum" class="ui-hidden-accessible">节目序号</label>
		<input type="text" name="sortNum" value="${show.sortNum}" placeholder="节目序号" />

		<fieldset data-role="controlgroup" data-type="horizontal">
		    <input name="status" id="status-pending" value="0" ${show.status==0?'checked="checked"':''} type="radio">
		    <label for="status-pending">未开始</label>
		    <input name="status" id="status-running" value="1" ${show.status==1?'checked="checked"':''} type="radio">
		    <label for="status-running">进行中</label>
		    <input name="status" id="status-end" value="2" ${show.status==2?'checked="checked"':''} type="radio">
		    <label for="status-end">已结束</label>
		</fieldset>
		
		<input type="submit" value="保存" data-theme="b" />
	</fieldset>
</form>

<script type="text/javascript">
$(function() {
	$("#showForm").submit(function() {
		var showName = $.trim($("#showName").val());
		if (showName) {
			$.post($(this).attr("action"), $(this).serialize(), function(response) {console.log(response);
				if (response.code > 0) {
					window.location.href = "<%=basePath%>party/admin.html";
				}
			});
		}
		return false;
	});
});
</script>