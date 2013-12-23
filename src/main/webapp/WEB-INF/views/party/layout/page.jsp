<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
	<tiles:insertAttribute name="head" />
</head>
<body>
<div id="page" data-role="page">
	<tiles:insertAttribute name="script" />
	<div data-role="header" data-theme="c" data-position="fixed">
		<a class="ui-btn-left" href="<%=basePath%>party/nickname.html" data-role="button" data-rel="dialog" data-icon="edit" id="nickname">${user.nickname}</a>
		<h1>编译青春，驱动未来</h1>
		<a class="ui-btn-right" href="<%=basePath%>party/logout" data-role="button" data-icon="arrow-r" data-iconpos="right" id="logout" data-ajax="false">退出</a>
	</div>
	<div data-role="content" class="content">
	    <tiles:insertAttribute name="content" />
	</div>
  	<div data-role="footer" data-id="page-footer" data-position="fixed" data-theme="d">
  		<c:if test="${activeNav=='spittles'}">
<form action="" method="post" id="spittleForm">
<div class="ui-grid-a">
<div class="ui-block-a" style="width:90%"><input type="text" id="spittleContent" placeholder="请输入内容..." /></div>
<div class="ui-block-b" style="width:8%; margin-left:1%;"><input type="submit" id="spittleSubmit" value="发布"  data-theme="b" /></div>
</div>
</form>
<script type="text/javascript">
$(function() {
	$("#spittleForm").submit(function() {
		var $spittleContent = $("#spittleContent");
		var content = $spittleContent.val();
		if (content) {
			$.isst.api.postSpittle(content, function(response) {
				if (response.code > 0) {
					$spittleContent.val("");
					window.location.href = "<%=basePath%>party/spittles.html";
				} else {
					alert(response.message);
				}
			});
		}
		return false;
	});
});
</script>
  		</c:if>
	    <tiles:insertAttribute name="footer" />
	</div>
</div>
</body>
</html>