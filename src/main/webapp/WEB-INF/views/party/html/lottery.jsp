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
<title>抽奖</title>
<link href="resources/css/party-screen.css"  type="text/css" rel="stylesheet"/>
</head>
<body class="lottery-page">
<div class="top">
	<div class="logo">
		<div class="lottery-info">抽奖池中共有<em id="spittleCount">0</em>条评论</div>
	</div>
</div>

<div class="screen">
	
	<div class="spittle-bg">
		<div class="spittle-wrapper">
			<ul id="spittleList" class="spittle-list">
			</ul>
		</div>
    </div>
    
	<div class="controls hide" id="controls">
		<!-- <a href="<%=basePath%>party/screen.html">评论</a>
		<a href="<%=basePath%>party/votes.html">投票</a>-->
		<a href="javascript:void(0);" class="prize" data-prize="0" id="prize0">特等奖</a>
		<a href="javascript:void(0);" class="prize" data-prize="1" id="prize1">一等奖</a>
	    <a href="javascript:void(0);" class="prize" data-prize="2" id="prize2">二等奖</a>
	    <a href="javascript:void(0);" class="prize" data-prize="3" id="prize3">三等奖</a>
	    <a href="javascript:void(0);" class="prize" data-prize="4" id="prize4">幸运奖</a>
	    <a href="javascript:void(0);" id="popup" class="popup" style="display:none">抽取</a>
	    <a href="javascript:void(0);" id="end" class="end" style="display:none">停止</a>
		<div class="bg"></div>
	</div>
</div>

<div id="overlay">
</div>

<div id="winnerPopup">
	<p><span id="winnerUserName"></span>：<label id="winnerFullName"></label></p>
</div>

<div id="winnerSpittlePopup"></div>

<div id="winnerList">
	<h2></h2>
	<div id="winnerWrapper"></div>
</div>

<div id="winnerControl">
	<a href="javascript:void(0);" class="prize-confirm" id="prizeConfirm">确认</a>
	<a href="javascript:void(0);" class="prize-cancel" id="prizeCancel">取消</a>
</div>

<script type="text/javascript" src="resources/js/jquery-2.0.3.min.js"></script>
<script type="text/javascript" src="resources/js/jquery.easing.1.3.js"></script>
<script type="text/javascript" src="resources/js/party-screen.js"></script>
<script type="text/javascript">

var Scrolling = function($ul) {
	var offsetTopIndexes = [];
	var weightIndexes = [];
	var winnerIndexMap = [];
	var isEnd = false;
	var isStop = false;
	var isPopup = false;
	var popupCallback = null;
	var endScrollingNum = 0;
	var endCallback = null;
	var stopCallback = null;
	var endScrollLength = [2, 1, 1, 0, 0];
	var ulHeight = 0;
	var liCount = 0;

	var winner;
	var prizeType;
	
	var isParsed = true;
	
	var populateWinner = function(curTop) {
		var positiveCurTop = 0 - curTop;
		var endTop = 0;
		
		for (var i=0; i<offsetTopIndexes.length; i++) {
			if (!winnerIndexMap[i] && positiveCurTop < offsetTopIndexes[i]) {
				break;
			}
		}
		
		if (i >= offsetTopIndexes.length) {
			for (var i=0; i<offsetTopIndexes.length; i++) {
				if (!winnerIndexMap[i]) {
					break;
				}
			}
		}

		var endIndex = (i + endScrollingNum) % liCount;
		var maxWeight = 0;
		var winnerIndex = i;
		var startIndex = i;
		while (startIndex != endIndex) {
			if (!winnerIndexMap[startIndex] && weightIndexes[startIndex] > maxWeight) {
				maxWeight = weightIndexes[startIndex];
				winnerIndex = startIndex;
			}
			startIndex = (startIndex+1) % liCount;
		}
		
		winnerIndexMap[winnerIndex] = true;
		//console.log("i:" + i + "; winnerIndex: " + winnerIndex + "; endIndex: " + endIndex);
		return {
			index: i,
			top: 0 - offsetTopIndexes[i],
			winnerIndex: winnerIndex,
			winnerTop: 0 - (winnerIndex < i ? (offsetTopIndexes[liCount-1] + offsetTopIndexes[winnerIndex+1]) : offsetTopIndexes[winnerIndex])
		};
	};

	var parseWinner = function(info) {
		var $li = $ul.find('li').eq(info.winnerIndex);
		return {
			index: info.winnerIndex,
			$li: $li,
			spittleId: $li.attr("data-id"),
			userName: $li.attr("data-name"),
			fullName: $li.attr("data-fullname"),
			prizeType: prizeType
		};
	};
	
	var doScrolling = function(easingString, toTop) {
		var curOffsetTop = 0;
		$ul.animate({top: (0 - ulHeight) + "px"}, {
			duration: liCount * 20,
			easing: easingString,
			step: function(curTop, fx) {
				if (isEnd) {
					$ul.stop();
					endCallback && endCallback.call();
				} else if (isStop && isParsed) {
					isParsed = false;
					$ul.stop();
					var info = populateWinner(curTop);
					var duration = ((1 + info.winnerIndex-info.index + liCount) % liCount)*1000;
					$ul.animate({top: info.winnerTop + "px"}, {
						duration: duration,
						easing: "easeOutCirc",
						complete: function() {
							isStop = false;
							stopCallback && stopCallback.call(null, winner = parseWinner(info));
						}
					});
				} else if (isPopup && isParsed) {
					isParsed = false;
					isPopup = false;
					popupCallback && popupCallback.call(null, winner = parseWinner(populateWinner(curTop)));
				}
			},
			complete: function() {
				if (!isEnd && !isStop) {
					$ul.css({top: '0px'});
					$ul.find("li:lt("+liCount+")").appendTo($ul);
					doScrolling("linear");
				}
			}
		});
	};
	
	this.isParsed = function() {
		return isParsed;
	};
	
	this.parsed = function() {
		isParsed = true;
	};
	
	this.init = function() {
		$ul.find("li").each(function(i) {
			var $li = $(this);
			var offsetTop = $li.position().top;
			$li.attr({
				"data-offset-top": offsetTop,
				"data-index": i
			});
			
			offsetTopIndexes[i] = offsetTop;
			winnerIndexMap[i] = false;
			weightIndexes[i] = $li.attr("data-weight");
		});
		liCount = $ul.find('li').length;
		ulHeight = $ul.outerHeight(true);
		$ul.append($ul.html());
	};

	this.start = function(prize) {
		prizeType = prize;
		endScrollingNum = endScrollLength[prize];
		isEnd = false;
		doScrolling("easeInCirc");
	};
	
	this.resume = function() {
		isEnd = false;
		doScrolling("easeInCirc");
	};
	
	this.stop = function(callback) {
		stopCallback = callback;
		isStop = true;
	};
	
	this.popup = function(callback) {
		popupCallback = callback;
		isPopup = true;
	};
	
	this.end = function(callback) {
		endCallback = callback;
		isEnd = true;
	};
	
	this.winner = function() {
		return winner;
	};
	
	this.prizeType = function() {
		return prizeType;
	};
	
	this.removeWinner = function(winner) {
		winnerIndexMap[winner.index] = false;
	};
	
	this.removeAllWinners = function() {
		for (var i=0; i<winnerIndexMap.length; i++) {
			winnerIndexMap[i] = false;
		}
	};
};

var PopupWinner = function($winnerPopup, $winnerSpittlePopup, $overlay, $ul) {
	this.popup = function(winner) {
		var $li = winner.$li;
		
		$overlay.show();

		$winnerSpittlePopup.html($li.html());
		$winnerSpittlePopup.width($li.width());
		$winnerSpittlePopup.height($li.height());
		
		var toWidth = $winnerSpittlePopup.outerWidth(true);
		var toHeight = $winnerSpittlePopup.outerHeight(true);
		var toLeft = parseInt($(window).scrollLeft() + ( $(window).width() - toWidth ) * 0.5, 10);
		var toTop = parseInt($(window).scrollTop() + ( $(window).height() - toHeight ) * 0.45, 10);
		var offset = $(".spittle-wrapper").offset();
		
		$winnerSpittlePopup.css({
			left: offset.left + 'px',
			top: (offset.top+10) + 'px'
		});
		
		$winnerSpittlePopup.show();
		
		$winnerSpittlePopup.animate({left: toLeft + 'px', top: toTop + 'px'}, 1000, function() {
			$winnerPopup.find("#winnerUserName").text(winner.userName);
			$winnerPopup.find("#winnerFullName").text(winner.fullName);
			$winnerPopup.css({left: toLeft + 'px', top: (toTop - $winnerPopup.outerHeight(true) - 20) + 'px'});
			$winnerPopup.show();
		});
	};
	
	this.close = function() {
		$winnerSpittlePopup.hide();
		$winnerPopup.hide();
		$overlay.hide();
	};
};

var WinnerList = function($winnerList, $overlay, prizeType) {
	var spittleIds = {};
	var $winnerWrapper = $winnerList.find("#winnerWrapper");
	
	$winnerList.show();
	
	this.add = function(winner, callback) {
		spittleIds[winner.spittleId] = true;
		var $s = $('<div class="spittle-winner" id="spittleWinner'+winner.spittleId+'"><h4>'+winner.userName+'</h4><p>'+winner.fullName+'</p></div>');
		$s.hide();
		$winnerWrapper.append($s);
		$s.fadeIn('slow', function() {
			callback && callback.call();
		});
	};
	
	this.remove = function(winner) {
		$("#spittleWinner"+winner.spittleId).remove();
		delete spittleIds[winner.spittleId];
	};
	
	this.removeAll = function() {
		spittleIds = {};
		$winnerWrapper.html("");
	};
	
	this.getSpittleIds = function() {
		var arr = [];
		for (var id in spittleIds) {
			arr.push(id);
		}
		return arr;
	};
	
	this.moveToCenter = function() {
		$overlay.show();
		$winnerList.addClass("window");
		var len = $winnerWrapper.find(".spittle-winner").length;
		if (len < 4) {
			$winnerList.width(232 * len);
		}
		
		var toWidth = $winnerList.outerWidth(true);
		var toHeight = $winnerList.outerHeight(true);
		var toLeft = parseInt($(window).scrollLeft() + ( $(window).width() - toWidth ) * 0.5, 10);
		var toTop = parseInt($(window).scrollTop() + ( $(window).height() - toHeight ) * 0.45, 10);
		
		$winnerList.animate({left: toLeft + 'px', top: toTop + 'px'}, 1000, function() {
		});
	};
};

$(function() {
	var prizeCount = [1, 2, 10, 10, 20];
	var winnerNum = 0;
	var $ul = $("#spittleList");
	var $winnerList = $("#winnerList");
	var $winnerPopup = $("#winnerPopup");
	var $winnerSpittlePopup = $("#winnerSpittlePopup");
	var $winnerControl = $("#winnerControl");
	var $overlay = $("#overlay");
	var lotteryPerScrollNum = 5;
	
	$overlay.css('opacity', 0.9);
	$overlay.html('<p class="lottery-loading">正在初始化抽奖池...</p>');
	$overlay.show();
	
	var sl = new Scrolling($ul);
	var pw = new PopupWinner($winnerPopup, $winnerSpittlePopup, $overlay, $ul);
	var wl;
	
	$.getJSON("party/admin/getLotterySpittles.json", function(spittles) {
		var offsetTop = 0;
		$("#spittleCount").text(spittles.length);

		for (var i=0; i<spittles.length; i++) {
			var spittle = spittles[i];
			var $li = newTemplate(spittle);
			$li.attr({
				'data-user-id': spittle.userId,
				'data-fullname': spittle.fullname,
				'data-weight': spittle.weight
			});
			$ul.append($li);
		}
		sl.init();
		$overlay.hide().html("");
	});
	
	$('.prize').click(function() {
		var prizeType = parseInt($(this).attr('data-prize'));
		$('.prize').hide();
		$('#end').show();
		$('#popup').show();
		$winnerList.find("h2").text($(this).text());
		
		wl = new WinnerList($winnerList, $overlay, prizeType);
		sl.start(prizeType);
		
		return false;
	});
	
	$('#popup').click(function() {
		if (!sl.isParsed()) {
			return false;
		}

 		var $this = $(this);
		var count = prizeCount[sl.prizeType()];
		//console.log(winnerNum);
		if (winnerNum > count-1) {
			return false;
		}
		
		$('#end').show();
		if (count < lotteryPerScrollNum) {
			if ($this.hasClass("start")) {
				$this.removeClass("start").text("抽取");
				sl.resume();
			} else {
				sl.stop(function(winner) {
					pw.popup(winner);
					wl.add(winner, function() {
						winnerNum++;
						$this.addClass("start").text("开始");
						if (winnerNum >= count) {
							$this.hide();
						}
						sl.parsed();
					});
					$winnerControl.show();
				});
			}
		} else {
			if ($this.hasClass("start")) {
				$this.removeClass("start").text("抽取");
				sl.resume();
			} else {
				if (winnerNum >= count-1) {
					sl.stop(function(winner) {
						wl.add(winner, function() {
							winnerNum++;
							$this.addClass("start").text("开始");
							sl.parsed();
						});
						$winnerControl.show();
					});
				} else {
					sl.popup(function(winner) {
						wl.add(winner, function() {
							winnerNum++;
							sl.parsed();
						});
					});
				}
			}
		}
	});
	
	$('#end').click(function() {
		sl.end(function() {
			close();
		});
	});
	
	var close = function() {
		$('#end').hide();
		$winnerControl.hide();
		
		pw.close();
	};
	
	$('#prizeConfirm').click(function() {
		var winner = sl.winner();
		var count = prizeCount[sl.prizeType()];
		var spittleId = count < lotteryPerScrollNum ? [winner.spittleId] : wl.getSpittleIds();
		$.post("party/admin/winPrize", {spittleId: spittleId, prizeType: winner.prizeType}, function(response) {
			if (response.code == 0) {
				alert(response.message);
			} else if (count < lotteryPerScrollNum) {
				close();
				if (winnerNum == count) {
					wl.moveToCenter();
				}
			} else {
				close();
				wl.moveToCenter();
			}
		});
	});
	
	$('#prizeCancel').click(function() {
		var count = prizeCount[sl.prizeType()];
		if (count < lotteryPerScrollNum) {
			var winner = sl.winner();
			if (winner) {
				winnerNum--;
				wl.remove(winner);
				sl.removeWinner(winner);
			}
			$('#popup').show();
		} else {
			winnerNum = 0;
			wl.removeAll();
			sl.removeAllWinners();
		}
		close();
	});
});
</script>
</body>
</html>
