<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<c:set var="activeNav" value="shows" scope="request" />
<ul data-role="listview" data-count-theme="c" data-inset="true" id="show-list">
<c:forEach var="show" items="${shows}">
<li>
	<a data-rel="dialog" href="<%=basePath%>party/showVote.html?showId=${show.id}" class="${show.status==0?'ui-disabled':''}">
		<h2 >
			${show.sortNum}.${show.name}
			<c:if test="${show.voteTime>0}">
				<span style="color:#090;">√</span>
			</c:if>
		</h2>
		<p>${show.organization}&nbsp;&nbsp;|&nbsp;&nbsp;${show.category}</p>
		<span class="ui-li-count">${show.status==0?'未开始':(show.status==1?'进行中':'已结束')}</span>
	</a>
</li>
</c:forEach>
</ul>