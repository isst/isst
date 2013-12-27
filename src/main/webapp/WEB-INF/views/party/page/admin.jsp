<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<c:set var="activeNav" value="admin" scope="request" />

<div data-role="collapsible" data-collapsed-icon="arrow-r" data-expanded-icon="arrow-d" data-collapsed="true" data-content-theme="c">
	<h3>节目单</h3>
	<ul data-role="listview" data-count-theme="c" data-inset="true" id="show-list">
		<c:forEach var="show" items="${shows}">
		<li>
			<a data-rel="dialog" href="<%=basePath%>party/showForm.html?showId=${show.id}">
				<h2>${show.sortNum}.${show.name}</h2>
				<p>${show.organization}&nbsp;&nbsp;|&nbsp;&nbsp;${show.category}</p>
				<span class="ui-li-count">${show.status==0?'未开始':(show.status==1?'进行中':'已结束')}</span>
			</a>
		</li>
		</c:forEach>
	</ul>
<c:if test="${user.id==1}">
<a href="<%=basePath%>party/showForm.html" data-rel="dialog" data-role="button" data-theme="b">添加节目</a>
</c:if>
</div>

<div data-role="collapsible" data-collapsed-icon="arrow-r" data-expanded-icon="arrow-d" data-collapsed="true" data-content-theme="c">
	<h3>推送消息</h3>
	<div>
		<input type="text" id="pushMessageTitle" placeholder="请输入标题..." value="" />
		<textarea id="pushMessageDescription" placeholder="请输入内容..."></textarea>
		<input type="button" id="pushMessage" value="发送" data-theme="b" />
	</div>
</div>

<div data-role="collapsible" data-collapsed-icon="arrow-r" data-expanded-icon="arrow-d" data-collapsed="true" data-content-theme="c">
	<h3>老师名单</h3>
	<ul data-role="listview" data-count-theme="c" data-inset="true" id="show-list">
		<c:forEach var="teacher" items="${teachers}">
		<li>
			<a data-rel="dialog" href="<%=basePath%>party/userForm.html?userId=${teacher.id}">
				<h2>${teacher.name}</h2>
				<p>${teacher.fullname}&nbsp;&nbsp;|&nbsp;&nbsp;${teacher.nickname}</p>
			</a>
		</li>
		</c:forEach>
	</ul>
<c:if test="${user.id==1}">
<a href="<%=basePath%>party/userForm.html" data-rel="dialog" data-role="button" data-theme="b">添加老师</a>
<a href="<%=basePath%>party/importUserForm.html" data-rel="dialog" data-role="button">导入学生</a>
</c:if>
</div>

<input type="button" value="清除缓存" id="clearCached" />
<input type="button" value="${isPartyEnded? '晚会结束':'晚会进行中'}" id="partyEnded" />

<script type="text/javascript">
$(function() {
	var $pushMessageTitle = $("#pushMessageTitle");
	var $pushMessageDescription = $("#pushMessageDescription");
	
	$("#pushMessage").click(function() {
		var title = $pushMessageTitle.val();
		var description = $pushMessageDescription.val();
		
		if (title && description) {
			$.post($.isst.party.createUrl("admin/pushMessage"), {title: title, description: description}, function(response) {
				if (response.code > 0) {
					$pushMessageTitle.val("");
					$pushMessageDescription.val("");
				}
			});
		}
	});
	
	$("#clearCached").click(function() {
		var $this = $(this);
		$this.button("disable");
		$.get($.isst.party.createUrl("admin/clearCached"), function() {
			$this.button("enable");
		});
	});
	
	$("#partyEnded").click(function() {
		var $this = $(this);
		$this.button("disable");
		$.get($.isst.party.createUrl("admin/partyEnded"), function() {
			var $span = $this.prev('.ui-btn-inner').find('.ui-btn-text');
			$span.text($span.text()=="晚会结束" ? "晚会进行中" : "晚会结束");
			$this.button("enable");
		});
	});
});
</script>