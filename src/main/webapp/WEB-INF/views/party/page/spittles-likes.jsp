<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<style type="text/css">
.spittle { position: relative;}
.spittle .spittle-delete-area { position: absolute; right: 5px; bottom:5px;}
.spittle .spittle-post-time { position: absolute; right: 0.5em; top: 1em;}
.spittle .spittle-content { margin:1em 0.5em 0.5em 1em;}
.spittle-likes a.ui-link, .spittle-dislikes a.ui-link { color: #fff;}
</style>
<c:set var="activeNav" value="spittles" scope="request" />
<c:set var="spittlePage" value="${isLike?'kike':'dislike'}" scope="request" />

<div data-role="navbar" data-id="spittles">
	<ul>
		<li><a href="<%=basePath%>party/spittles.html" data-ajax="false">新鲜出炉</a></li>
		<li><a href="<%=basePath%>party/spittles-likes.html" data-ajax="false" class="${isLike?'ui-btn-active ui-state-persist':''}">不明觉厉</a></li>
		<li><a href="<%=basePath%>party/spittles-dislikes.html" data-ajax="false" class="${isLike?'':'ui-btn-active ui-state-persist'}">累觉不爱</a></li>
	</ul>
</div>
<br />
<input type="button" data-role="button" id="refresh${isLike?'Like':'Dislike'}Spittles"  value="刷新" data-theme="b" />

<ul data-role="listview" data-split-theme="d" data-inset="true" data-count-theme="b" id="${isLike?'like':'dislike'}SpittleList">
	<c:forEach var="spittle" items="${spittles}">
    <li class="spittle" data-spittleId="${spittle.id}">
       <h2>${user.id==1?spittle.id:''}${user.id==1?'.':''}${spittle.nickname}</h2>
        <p class="spittle-content">${spittle.content}</p>
        <p class="spittle-post-time"><fmt:formatDate pattern="MM-dd HH:mm:ss" value="${spittle.postDate}" /></p>
        <div class="spittle-control">
        	<a href="#" data-role="button" data-mini="true" data-inline="true" class="spittle-like ${spittle.isLiked==1||spittle.isDisliked==1?'ui-disabled':''}" data-theme="${spittle.isLiked==1?'b':''}">赞&nbsp;<em>${spittle.likes}</em></a>
        	<a href="#" data-role="button" data-mini="true" data-inline="true" class="spittle-dislike ${spittle.isLiked==1||spittle.isDisliked==1?'ui-disabled':''}" data-theme="${spittle.isDisliked==1?'b':''}">踩&nbsp;<em>${spittle.dislikes}</em></a>
        </div>
        <c:if test="${user.id==1}">
        <div class="spittle-delete-area">
        <input type="button" value="删除" data-inline="true" data-mini="true" class="spittle-delete" data-icon="delete" />
        </div>
        </c:if>
    </li>
    </c:forEach>
</ul>

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
$(function() {
	var dislikeClick = function() {
		var $this = $(this);
		var $spittle = $this.parents('.spittle');
		var spittleId = $spittle.attr("data-spittleId");
		$spittle.find('.spittle-like, .spittle-dislike').addClass('ui-disabled');
		$.isst.api.dislike(spittleId, function(response) {
			if (response.code > 0) {
				$this.buttonMarkup({theme: 'b'});
				$spittle.find('.spittle-dislike em').text(parseInt($spittle.find('.spittle-dislike em').text())+1);
			} else {
				alert(response.message);
				//$spittle.find('.spittle-like, .spittle-dislike').removeClass('ui-disabled');
			}
		});
	};
	
	var likeClick = function() {
		var $this = $(this);
		var $spittle = $this.parents('.spittle');
		var spittleId = $spittle.attr("data-spittleId");
		$spittle.find('.spittle-like, .spittle-dislike').addClass('ui-disabled');
		$.isst.api.like(spittleId, function(response) {
			if (response.code > 0) {
				$this.buttonMarkup({theme: 'b'});
				$spittle.find('.spittle-like em').text(parseInt($spittle.find('.spittle-like em').text())+1);
			} else {
				alert(response.message);
				//$spittle.find('.spittle-like, .spittle-dislike').removeClass('ui-disabled');
			}
		});
	};
	
	var deleteClick = function() {
		if (confirm('确定要删除吗？')) {
			var $this = $(this);
			var $spittle = $this.parents('.spittle');
			$.isst.api.deleteSpittle($spittle.attr("data-spittleId"), function(response) {
				if (response.code > 0) {
					$spittle.remove();
				} else {
					alert(response.message);
				}
			});
		}
	};
	
	$('.spittle-dislike').click(dislikeClick);
	$('.spittle-like').click(likeClick);
	$('.spittle-delete').click(deleteClick);
	
	var $spittleList = $("#${isLike?'like':'dislike'}SpittleList");
	
	var renderSpittles = function(spittles) {
		for (var i=0; i<spittles.length; i++) {
				var spittle = spittles[i];
				var $li = $('<li class="spittle" data-spittleId="'+spittle.id+'">' +
       '<h2>'+ spittle.nickname+'</h2>' +
        '<p class="spittle-content">'+spittle.content+'</p>' +
        '<p class="spittle-post-time">'+new Date(spittle.postDate).format("MM-dd hh:mm:ss")+'</p>' +
        '<div class="spittle-control">' +
        	'<a href="javascript:;" data-role="button" data-mini="true" data-inline="true" class="spittle-like">赞&nbsp;<em>'+spittle.likes+'</em></a>' +
        	'<a href="javascript:;" data-role="button" data-mini="true" data-inline="true" class="spittle-dislike">踩&nbsp;<em>'+spittle.dislikes+'</em></a>' +
        '</div>' +
    '</li>');
				if (spittle.isLiked == 1 || spittle.isDisliked == 1) {
					$li.find('.spittle-like, .spittle-dislike').addClass('ui-disabled');
				}
				
				if (spittle.isLiked == 1) {
					$li.find('.spittle-like').attr('data-theme', 'b');
				}
				
				if (spittle.isDisliked == 1) {
					$li.find('.spittle-dislike').attr('data-theme', 'b');
				}
				
				$spittleList.append($li);
				$li.find('.spittle-dislike').click(dislikeClick);
				$li.find('.spittle-like').click(likeClick);
				$li.find('.spittle-control a').button();
				
				if ($.isst.userId == 1) {
					$li.append('<div class="spittle-delete-area"><input type="button" value="删除" data-inline="true" data-mini="true" class="spittle-delete" data-icon="delete" /></div>');
					$li.find('.spittle-delete').click(deleteClick);
					$li.find('h2').prepend(spittle.id + '.');
					$li.find('.spittle-delete').button();
				}
				$spittleList.listview("refresh");
			}
	};
	
	
	$("#refresh${isLike?'Like':'Dislike'}Spittles").click(function() {
		var $this = $(this);
		$this.prev('span').find('.ui-btn-text').text("刷新中...");
		$this.button('disable');
		$.isst.api.${isLike?'getLikeSpittles':'getDislikeSpittles'}(function(spittles) {
			$spittleList.html('');
			renderSpittles(spittles);
			$this.prev('span').find('.ui-btn-text').text("刷新");
			$this.button('enable');
		});
	});
});
</script>