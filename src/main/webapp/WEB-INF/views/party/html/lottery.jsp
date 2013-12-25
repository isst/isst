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
    
	<div class="controls hide" id="controls">
		<a href="<%=basePath%>party/screen.html">评论</a>
		<a href="<%=basePath%>party/votes.html">投票</a>
		<a href="javascript:void(0);" class="prize" data-prize="1" id="prize1">一等奖</a>
	    <a href="javascript:void(0);" class="prize" data-prize="2" id="prize2">二等奖</a>
	    <a href="javascript:void(0);" class="prize" data-prize="3" id="prize3">三等奖</a>
	    <a href="javascript:void(0);" class="prize" data-prize="4" id="prize4">幸运奖</a>
	    <a href="javascript:void(0);" id="stop" class="stop" style="display:none">停止</a>
		<div class="bg"></div>
	</div>
</div>

<div id="overlay">
	<a href="javascript:void(0);" class="prizeconfirm" id="prize-confirm" style="position: absolute; right: 100px; bottom: 10px;">确认</a>
	<a href="javascript:void(0);" class="prize-cancel" id="prize-cancel" style="position: absolute; right: 10px; bottom: 10px;">取消</a>
</div>

<div id="winner">
	<p><label id="winnerPrizeType"></label>：<span id="winnerUserName"></span></p>
</div>

<div id="winSpittle"></div>

<div id="winnerList">
</div>

<script type="text/javascript" src="resources/js/jquery-2.0.3.min.js"></script>
<script type="text/javascript" src="resources/js/jquery.easing.1.3.js"></script>
<script type="text/javascript" src="resources/js/party-screen.js"></script>
<script type="text/javascript">

var Scrolling = function($ul) {
	var speed = 0;
	var scrollIndex = 1;
	var timer = null;
	var scrollMaxSpeed;
	
	var offsetTopIndexes = [];
	var $lists = [];
	
	var isEnd = false;
	var endScrollingNum = 5;
	var endCallback = null;
	
	var ulHeight = 0;
	var liCount = 0;

	var populateEnd = function(curTop) {
		var positiveCurTop = 0 - curTop;
		var endTop = 0;
		for (var i=0; i<offsetTopIndexes.length; i++) {
			if (positiveCurTop < offsetTopIndexes[i]) {
				var endIndex = (i + endScrollingNum) % liCount;
				return {
					index: i,
					top: 0 - offsetTopIndexes[i],
					endIndex: endIndex,
					endTop: 0 - (endIndex < i ? (offsetTopIndexes[liCount] + offsetTopIndexes[endIndex]) : offsetTopIndexes[endIndex])
				};
			}
		}
	};

	var doScrolling = function(easingString, toTop) {
		var curOffsetTop = 0;
		$ul.animate({top: (0 - ulHeight) + "px"}, {
			duration: liCount * 20,
			easing: easingString,
			step: function(curTop, fx) {
				if (isEnd) {
					$ul.stop();
					var info = populateEnd(curTop);
					$ul.animate({top: info.endTop + "px"}, {
						duration: endScrollingNum*400,
						easing: "easeOutCirc",
						complete: function() {
							isEnd = false;
							endCallback && endCallback.call($lists[info.endIndex], info);
						}
					});
				}
			},
			complete: function() {
				if (!isEnd) {
					$ul.css({top: '0px'});
					$ul.find("li:lt("+liCount+")").appendTo($ul);
					doScrolling("linear");
				}
			}
		});
	};
		
	this.init = function() {
		$ul.find("li").each(function(i) {
			var $li = $(this);
			var offsetTop = $li.position().top;
			$li.attr({
				"data-offset-top": offsetTop,
				"data-index": i
			});
			
			$lists[i] = $li;
			offsetTopIndexes[i] = offsetTop;
		});
		liCount = $ul.find('li').length;
		ulHeight = $ul.outerHeight(true);
		$ul.append($ul.html());
	};
	
	this.start = function() {
		doScrolling("easeInCirc");
	};
	
	this.stop = function(callback) {
		endCallback = callback;
		isEnd = true;
	};
};
	
$(function() {
	var $ul = $("#spittleList");
	var $winnerList = $("#winnerList");
	var $overlay = $('#overlay');
	var prizeType = 0;
	var spittleId = 0;
	var userName;
	var fullName;
	var sl = new Scrolling($ul);
	$overlay.css('opacity', 0.75);
	
	$.getJSON("party/admin/getLotterySpittles.json", function(spittles) {
		var offsetTop = 0;
		var index = 0;
		//for (var j=0; j<250; j++) {
		for (var i=0; i<spittles.length; i++) {
			var spittle = spittles[i];
			var $li = newTemplate(spittle);
			$li.attr({
				'data-user-id': spittle.userId,
				'data-fullname': spittle.fullname
			});
			$ul.append($li);
			index++;
		}
		//}
		sl.init();
	});
	
	$('.prize').click(function() {
		prizeType = $(this).attr('data-prize');
		$(this).addClass("prize-start");
		$('#stop').show();
		sl.start();
		return false;
	});
	
	$('#stop').click(function() {
		if (sl) {
			sl.stop(function(info) {
				$(this).hide();
				$('.prize-start').removeClass("prize-start");
				$overlay.show();
				popWinner($ul.find("li:first"));
			});
		}
	});
	
	var $winner = $("#winner");
	var winnerHeight = $winner.outerHeight(true);
	var $winSpittle = $("#winSpittle");
	
	function popWinner($li) {
		spittleId = $li.attr("data-id");
		userName = $li.attr('data-name');
		fullName = $li.attr('data-fullname');
		$winner.find("#winnerUserName").text($li.attr("data-name"));
		$winner.find("#winnerPrizeType").text($("#prize"+prizeType).text());
		
		$winSpittle.html($li.html());
		$winSpittle.width($li.width());
		$winSpittle.height($li.height());
		
		var toWidth = $winSpittle.outerWidth(true);
		var toHeight = $winSpittle.outerHeight(true);
		var toLeft = parseInt($(window).scrollLeft() + ( $(window).width() - toWidth ) * 0.5, 10);
		var toTop = parseInt($(window).scrollTop() + ( $(window).height() - toHeight ) * 0.45, 10);
		var offset = $(".spittle-wrapper").offset();
		$winSpittle.css({
			left: offset.left + 'px',
			top: (offset.top+10) + 'px'
		});
		$winSpittle.show();
		
		$winSpittle.animate({left: toLeft + 'px', top: toTop + 'px'}, 1000, function(){
			$winner.css({left: toLeft + 'px', top: (toTop - winnerHeight - 20) + 'px'});
			$winner.show();
		});
	}
	
	var close = function() {
		$winSpittle.hide();
		$winner.hide();
		$overlay.hide();
	};
	
	$('#prize-confirm').click(function() {
		if (spittleId == 0) {
			return false;
		}
		
		$.post("party/admin/winPrize", {spittleId: spittleId, prizeType: prizeType}, function(response) {
			if (response.code == 0) {
				alert(response.message);
			} else {
				spittleId = 0;
				var $s = $('<div class="spittle-winner"><h4>'+userName+'</h4><p>'+fullName+'</p></div>');
				$s.hide();
				$winnerList.append($s);
				$s.fadeIn('slow');
				close();
			}
		});
	});
	
	$('#prize-cancel').click(function() {
		close();
	});
});
</script>
</body>
</html>
