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
	<base href="<%=basePath%>" />
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title><tiles:getAsString name="title" /></title>
	<tiles:insertAttribute name="header" />
	<script type="text/javascript">
		if ($.isst.userId == 0) {
			window.location.href = $.isst.party.createUrl("login.html");
		}
	</script>
</head>
<body>
<div id="page" data-role="page">
	<div data-role="header"  data-theme="b" data-position="fixed">
		<h1>浙江大学软件学院元旦晚会</h1>
	</div>
	<div data-role="content" class="content">
	    <tiles:insertAttribute name="content" />
	</div>
  	<div data-role="footer" data-id="foo1" data-position="fixed">
		<div data-role="navbar">
			<ul>
				<li>
					<a href="<%=basePath%>party/spittles.html" data-icon="grid" class="${activeNav == 'spittles' ? 'ui-btn-active ui-state-persist' : ''}">评论列表</a>
				</li>
				<li><a href="<%=basePath%>party/spittles.html"  class="${activeNav == 'shows' ? 'ui-btn-active ui-state-persist' : ''}" data-icon="edit">节目评分</a></li>
				<li><a href="<%=basePath%>party/spittles.html"  class="${activeNav == 'lottery' ? 'ui-btn-active ui-state-persist' : ''}" data-icon="star">奖品说明</a></li>
				<c:if test="${user.id==1}">
				<li><a href="<%=basePath%>party/admin.html"  class="${activeNav == 'admin' ? 'ui-btn-active ui-state-persist' : ''}" data-icon="gear">系统设置</a></li>
				</c:if>
			</ul>
		</div>
		
	</div>
</div>
</body>
</html>
