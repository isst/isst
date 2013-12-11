<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
	<tiles:insertAttribute name="head" />
	<style type="text/css">
		p {font-size: 1.5em;font-weight: bold;}
		#loginSubmit{float:right; margin:10px; }
	</style>
</head>
<body>
<div id="page" data-role="page">
	<script type="text/javascript">
	jQuery(document).ready(function() {
		$("#loginSubmit").bind("click", function() {
			var name = $('#username').val();
			var password = $('#password').val();
			$.isst.api.login(name, password, function(response) {
				if (response.code > 0) {
					$.post($.isst.party.url + '/login', {id: response.code}, function() {
						window.location.href = $.isst.party.createUrl("spittles.html");
					});
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
		    	<input type="password" name="password" id="password" placeholder="密码" value="" class="required" />
			    <a data-role="button" id="loginSubmit" data-theme="b">登录</a>
		    </form>
	  </div>
  	<div data-role="footer" data-position="fixed" data-theme="a">
  		<h1>©2013&nbsp;&nbsp;ISST项目组（http://zjucst.com/isst）</h1>
  	</div>
</div>
</body>
</html>