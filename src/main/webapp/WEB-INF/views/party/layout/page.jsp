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
<tiles:insertAttribute name="head-script" />
</head>
<body>
<div id="page" data-role="page">
	<tiles:insertAttribute name="script" />
	<div data-role="header" data-theme="c" data-position="fixed" data-tap-toggle="false">
		<a class="ui-btn-left" href="<%=basePath%>party/nickname.html" data-role="button" data-rel="dialog" data-icon="edit" id="nickname">${user.nickname}</a>
		<h1>编译青春，驱动未来</h1>
		<a class="ui-btn-right" href="<%=basePath%>party/logout" data-role="button" data-icon="arrow-r" data-iconpos="right" id="logout" data-ajax="false">退出</a>
	</div>
	<div data-role="content" class="content">
	    <tiles:insertAttribute name="content" />
	    <div id="toTop"><input type="button" value="Top" data-icon="arrow-u" data-mini="true" /></div>
	</div>
  	<div data-role="footer" data-id="page-footer" data-position="fixed" data-theme="d" data-tap-toggle="false">
  		<c:if test="${activeNav=='spittles'}">

<style type="text/css">
.spittle-form #spittleContentBlock {
	width:89%; 
	margin-left:1%;
	vertical-align : middle;
}
.spittle-form #spittleSubmitBlock {
	width:9%; 
	margin-left:1%;
	vertical-align : middle;
}
@media all and (max-width:900px) {
    .spittle-form #spittleContentBlock {
        width   : 79%;
    }
    .spittle-form #spittleSubmitBlock {
        width   : 19%;
    }
}
</style>
<form action="" method="post" id="${spittlePage}SpittleForm" class="spittle-form">
<div class="ui-grid-a">
<div class="ui-block-a" id="spittleContentBlock"><input type="text" class="spittle-content" placeholder="小伙伴们快来吐槽吧..." /></div>
<div class="ui-block-b" id="spittleSubmitBlock"><input type="submit" class="spittle-submit" value="发布"  data-theme="b" /></div>
</div>
</form>
<script type="text/javascript">
$(function() {
	$("#${spittlePage}SpittleForm").submit(function() {
		var $spittleContent = $(this).find('.spittle-content');
		var content = $spittleContent.val();
		var $spittleSubmit = $(this).find('.spittle-submit');
		if (content.length >=5 && content.length <=140) {
			$spittleSubmit.prev('span').find('.ui-btn-text').text("发布中...");
			$spittleSubmit.button('disable');
			$.isst.api.postSpittle(content, function(response) {
				if (response.code > 0) {
					$spittleContent.val("");
					if (window.location.href.indexOf('spittles.html') > 0) {
						$("#refreshSpittles").click();
					} else {
						window.location.href = "<%=basePath%>party/spittles.html";
					}
				} else {
					alert(response.message);
				}
				$spittleSubmit.prev('span').find('.ui-btn-text').text("发布");
				$spittleSubmit.button('enable');
			});
		} else {
			alert("请输入5~140字数的吐槽内容");
		}
		return false;
	});
});
</script>
  		</c:if>
	    <tiles:insertAttribute name="footer" />
	</div>
	<div data-role="popup" id="alert" class="ui-content" data-position-to="window" data-theme="e" style="max-width:350px;"><p></p></div>
</div>
</body>
</html>