<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
	<tiles:insertAttribute name="head" />
	<style type="text/css">
		p {font-size: 1.5em;font-weight: bold;}
	</style>
</head>
<body>
<div id="page" data-role="page">
	<script type="text/javascript">
	jQuery(document).ready(function() {
		$("#loginform").submit(function() {
			var name = $('#username').val();
			var password = $('#password').val();
			
			if (!name || !password) {
				return false;
			}
			
			var returnUrl = '<c:out value="${returnUrl}" />';
			returnUrl = returnUrl ? $.isst.party.createUrl(returnUrl) : null;
			var $submit = $('#loginSubmit');
			$submit.prev('span').find('.ui-btn-text').text("登录中...");
			$submit.button('disable');
			$.isst.api.login(name, password, function(response) {
				if (response.code > 0) {
					window.location.href = returnUrl ? returnUrl : $.isst.party.createUrl("index.html");
				} else {
					$submit.prev('span').find('.ui-btn-text').text("登录");
					$submit.button('enable');
					alert(response.message);
				}
			});
			
			return false;
		});
	});
	</script>
	<div data-role="header" data-theme="d" data-position="fixed">
		<h1>编译青春，驱动未来</h1>
	</div>
	<div data-role="content" class="content">
	    <p style="backg"><font color="#2EB1E8" >登录</font></p>
		    <form method="post" id="loginform">
		    	<label for="username" class="ui-hidden-accessible">用户名</label>
		    	<input type="text" name="username" id="username" placeholder="用户名" value="" class="required" /><br>
		    	<label for="password" class="ui-hidden-accessible">密码</label>
		    	<input type="password" name="password" id="password" placeholder="密码" value="" class="required" /><br>
			    <input type="submit" data-role="button" data-theme="b" id="loginSubmit" value="登录" />
		    </form>
	  </div>
  	<div data-role="footer" data-position="fixed" data-theme="a">
  		<h1>©2013&nbsp;&nbsp;ISST项目组</h1>
  	</div>
  	<div data-role="popup" id="alert" class="ui-content" data-position-to="window" data-theme="e" style="max-width:350px;"><p></p></div>
</div>

</body>
</html>