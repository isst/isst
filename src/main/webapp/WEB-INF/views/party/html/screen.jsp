<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html lang="zh-cn">
<head>
<base href="<%=basePath%>" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>浙江大学软件学院元旦晚会</title>
<link href="resources/css/party-screen.css"  type="text/css" rel="stylesheet"/>
</head>
<body>
<div class="top">
	<div class="logo"></div>
</div>

<div class="screen">
	<div class="spittle-bg">
		<div class="spittle-wrapper">
			<ul id="spittleList" class="spittle-list">
			</ul>
		</div>
    </div>
    
	<div class="controls hide" id="controls">
		<a href="<%=basePath%>party/lottery.html">抽奖</a>
		<a href="<%=basePath%>party/votes.html">投票</a>
		<a href="javascript:void(0);" id="pause"><c:out value="${isPushingActived?'暂停':'播放'}" /></a>
		<div class="bg"></div>
	</div>
</div>

<script type="text/javascript" src="<%=basePath%>resources/js/jquery-2.0.3.min.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/js/ajax-pushlet-client.js"></script>
<script type="text/javascript" src="<%=basePath%>resources/js/party-screen.js"></script>
<script type="text/javascript">

var PushingSpittle = function($ul) {
	var spittles = [];
	var doing = false;
	var timer = null;
	var self = this;
	
	this.popup = function() {
		if (!doing && spittles.length>0) {
			doing = true;
			var spittle = spittles.shift();
			var $templ = $('#'+spittle.spittleId);
			if ($templ.length == 0) {
				$templ = newTemplate(spittle);
			}
	    	$ul.prepend($templ.hide());
	    	var liHeight = $templ.outerHeight(true);
	    	$ul.animate({marginTop : liHeight +"px"}, 1000, function(){
	    		$ul.css({marginTop:0});
	    		$templ.fadeIn(1000);
	    		doing = false;
	    		timer = setTimeout(function() {
	    			self.popup();
	    		}, 5000);
			});
	    } else if (spittles.length == 0) {
	    	timer = null;
	    }
	};
	
	this.push = function(spittle) {
		spittles.push(spittle);
		if (!doing && !timer) {
	    	this.popup();
	    }
	};
};

var ps = new PushingSpittle($("#spittleList"));

PL.webRoot = "<%=basePath%>";
PL._init();
PL.joinListen("/party/spittles");
function onData(event) {
	var spittle = event.get("spittle");
	if (spittle) {
		spittle = $.parseJSON(spittle);
	    if (spittle) {console.log(spittle);
			for (var key in spittle) {
				spittle[key] = decodeURIComponent(spittle[key]);
			}
	    	ps.push(spittle);
	    }
	}
}
    
$(function() {
	$("#pause").click(function() {
		var $this = $(this);
		if ($this.text() == "暂停") {
			$this.text("播放");
			$.post("party/admin/pausePushing");
		} else {
			$this.text("暂停");
			$.post("party/admin/resumePushing");
		}
	});
});
</script>
</body>
</html>
