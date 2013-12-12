<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<base href="<%=basePath%>" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>浙江大学软件学院元旦晚会</title>
<link href="resources/css/party.css"  type="text/css" rel="stylesheet"/>
<style type="text/css">
    .topbox .custom,.topbox .tencentLogo{ float:left;}
    .custom p{ font-size:35px; padding-left:50px;}
</style>
</head>
<body class="wall_screen">
<div class="wrapperSize1280">
<div class="microblog">
    <div id="topbox" class="topbox">
        <div class="tencentLogo">浙江大学软件学院</div>
                <div class="custom" id="picText"><p>浙江大学软件学院元旦晚会</p></div>
            </div>
    <div class="talkBg">
        <div class="talkList">
            <ul id="talkList" class="LC">
			</ul>
        </div>
    </div>
        <div class="control_area undis" id="control_area">
        <a href="javascript:void(0);" class="prize" data-prize="1">一等奖</a>
        <a href="javascript:void(0);" class="prize" data-prize="2">二等奖</a>
        <a href="javascript:void(0);" class="prize" data-prize="3">三等奖</a>
        <a href="javascript:void(0);" id="stop" class="stop" style="display:none">停止</a>
        <div class="bg"></div>
    </div>
    </div>
    </div>

<div class="imgPopup" id="imgPopup" style="display:none;">
<div class="blockLayer"></div>
<div class="bigPicBox" id="bigPic"></div>
</div>

<div id="overlay" style="display:none; position: fixed; background-color:#000; left:0; top:0; width:100%; height:100%;">
	<a href="javascript:void(0);" class="prizeconfirm" id="prize-confirm" style="position: absolute; right: 100px; bottom: 10px;">确认</a>
	<a href="javascript:void(0);" class="prize-cancel" id="prize-cancel" style="position: absolute; right: 10px; bottom: 10px;">取消</a>
</div>

<div id="winner" style="display:none; position: absolute;">
</div>

<script type="text/javascript" src="resources/js/jquery-2.0.3.min.js"></script>
<script type="text/javascript" src="resources/js/ajax-pushlet-client.js"></script>
<script type="text/javascript">
$(function() {
	var $ul = $("#talkList");
	var $overlay = $('#overlay');
	$overlay.css('opacity', 0.5);
	
	function newTemplate(spittle) {
		var template = '<li id="'+spittle.spittleId+'">' +
		'<div class="user_con cl" style="">' +
			'<div class="msgBox">' +
				'<div rel="abcd1152264185" class="userName" style="font-size: 32px; line-height: 49px; height: 47px;">' +
					'<strong><a title="">'+spittle.nickname+'</a></strong>' +
					'<span class="msgUserTo">:&nbsp;</span>' +
				'</div>' +
				'<div class="msgCnt " style="font-size: 32px; line-height: 49px;">'+spittle.content+'</div>' +
			'</div>' +
		'</div>' +
		'</li>';
	    return $(template);
	}

	$.getJSON("party/admin/getLotterySpittles.json", function(spittles) {
		for (var i=0; i<spittles.length; i++) {
			$ul.append(newTemplate(spittles[i]));
		}
	});
	
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
	
	var isScrolling = false;
	
	$('.prize').click(function() {
		$(this).addClass("prize-start");
		$('#stop').show();
		isScrolling = true;
		doScrolling();
		return false;
	});
	
	$('#stop').click(function() {
		isScrolling = false;
		$(this).hide();
		$('.prize-start').removeClass("prize-start");
	});
	
	var currentMarginTop = 0;
	function doScrolling() {
		if (isScrolling) {
			var $li = $ul.find('li:first');
			currentMarginTop -= $li.outerHeight(true);
			$ul.animate({marginTop : currentMarginTop +"px"}, 30, function(){
				$li.appendTo($ul);
				setTimeout(doScrolling, 10);
			});
		} else {
			$ul.css({marginTop:0});
			$overlay.show();
			popWinner($ul.find("li:first"));
		}
	}
	
	var $winner = $('#winner');
	
	function popWinner($li) {
		$winner.html($li.html());
		$winner.width($li.width());
		$winner.height($li.height());
		
		var toWidth = $winner.outerWidth(true);
		var toHeight = $winner.outerHeight(true);
		var toLeft = parseInt($(window).scrollLeft() + ( $(window).width() - toWidth ) * 0.5, 10);
		var toTop = parseInt($(window).scrollTop() + ( $(window).height() - toHeight ) * 0.45, 10);
		var offset = $li.offset();
		$winner.css({
			left: offset.left + 'px',
			top: offset.top + 'px'
		});
		$winner.show();
		
		$winner.animate({left: toLeft + 'px', top: toTop + 'px'}, 1000, function(){
			
		});
	}
	
	$('#prize-confirm').click(function() {
		
	});
	
	$('#prize-cancel').click(function() {
		$winner.hide();
		$overlay.hide();
	});
});
</script>
</body>
</html>
