<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
    
	<div class="control-area" id="controlArea">
		<a href="<%=basePath%>partyLottery.jsp" class="lottery" id="lottery" >抽奖</a>
		<a href="javascript:void(0);" class="start" id="pause" >暂停</a>
	</div>
</div>

<script type="text/javascript" src="resources/js/jquery-2.0.3.min.js"></script>
<script type="text/javascript" src="resources/js/ajax-pushlet-client.js"></script>
<script type="text/javascript">

Date.prototype.format = function(fmt)   {
  var o = {   
    "M+" : this.getMonth()+1, 
    "d+" : this.getDate(),
    "h+" : this.getHours(),
    "m+" : this.getMinutes(),
    "s+" : this.getSeconds(),
    "q+" : Math.floor((this.getMonth()+3)/3),
    "S"  : this.getMilliseconds()
  };   
  if(/(y+)/.test(fmt))   
    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
  for(var k in o)   
    if(new RegExp("("+ k +")").test(fmt))   
  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
  return fmt;   
};

var spittles = [];
var $ul = $("#spittleList");
var doing = false;
var timer = null;

function newTemplate(spittle) {
	var dateString = (new Date(spittle.postTime * 1000)).format("yyyy-MM-dd hh:mm:ss");
	var template = '<li id="'+spittle.spittleId+'">' +
		'<div class="spittle">' +
			'<div class="hd">' +
				'<h3>'+spittle.nickname+'</h3>' +
				'<p>'+dateString+'</p>' +
			'</div>' +
			'<div class="cnt ">'+spittle.content+'</div>' +
		'</div>' +
	'</li>';
    return $(template);
}

function deQuene() {
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
    		timer = setTimeout(deQuene, 5000);
		});
    } else if (spittles.length == 0) {
    	timer = null;
    }
}

PL.webRoot = "<%=basePath%>";
PL._init();
PL.joinListen("/party/spittles");
function onData(event) {
	var spittle = event.get("spittle");
	if (spittle) {
		spittle = $.parseJSON(spittle);
	    if (spittle) {
			for (var key in spittle) {
				spittle[key] = decodeURIComponent(spittle[key]);
			}
	    	spittles.push(spittle);
	    	if (!doing && !timer) {
	    		deQuene();
	    	}
	    }
	}
}
    
$(function() {
	$("#control_area").hover(
		function() {
			var $this = $(this);
			if ($this.hasClass("undis")) {
				$(this).removeClass("undis");
				$(this).addClass("dis");
			} else {
				$(this).removeClass("dis");
				$(this).addClass("undis");
			}
		}
	);
	
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
