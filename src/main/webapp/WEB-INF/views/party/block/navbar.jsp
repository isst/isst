<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<div data-role="navbar">
	<ul>
		<li>
			<a href="<%=basePath%>party/spittles.html" data-icon="grid" class="${activeNav == 'spittles' ? 'ui-btn-active ui-state-persist' : ''}">我要吐槽</a>
		</li>
		<li><a href="<%=basePath%>party/shows.html"  class="${activeNav == 'shows' ? 'ui-btn-active ui-state-persist' : ''}" data-icon="check">节目投票</a></li>
		<li><a href="<%=basePath%>party/spittles.html"  class="${activeNav == 'introduction' ? 'ui-btn-active ui-state-persist' : ''}" data-icon="info">晚会说明</a></li>
		<c:if test="${user.id==1}">
			<li><a href="<%=basePath%>party/admin.html"  class="${activeNav == 'admin' ? 'ui-btn-active ui-state-persist' : ''}" data-icon="gear">系统设置</a></li>
		</c:if>
	</ul>
</div>
