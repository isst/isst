<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<input type="button" id="showVote" value="${hasVote?'已投过票':(canVote?'投上一票':'不能再投了')}" class="${hasVote||!canVote?'ui-disabled':''}" data-theme="b" />
<input type="button" id="showVoteCancel" value="取消" data-theme="c" />

<script type="text/javascript">
$(function() {
	$("#showVote").click(function() {
		var showId = <c:out value="${show.id}" />
		if (showId) {
			$.isst.api.showVote(showId, function(response) {
				if (response.code > 0) {
					window.location.href = "<%=basePath%>party/shows.html";
				} else {
					alert(response.message);
				}
			});
		}
	});
	
	$("#showVoteCancel").click(function() {
		$('.ui-dialog').dialog('close');
	});
});
</script>