<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<style type="text/css">
.ui-li-aside { margin-top:0;}
.spittle { position: relative;}
.spittle .spittle-delete-area { position: absolute; right: 5px; bottom:5px;}
</style>
<c:set var="activeNav" value="spittles" scope="request" />

<div data-role="navbar" data-id="spittles">
	<ul>
		<li><a href="<%=basePath%>party/spittles.html" data-ajax="false">新鲜出炉</a></li>
		<li><a href="<%=basePath%>party/spittles-likes.html" class="${isLike?'ui-btn-active ui-state-persist':''}" data-ajax="false">不明觉厉</a></li>
		<li><a href="<%=basePath%>party/spittles-dislikes.html" class="${isLike?'':'ui-btn-active ui-state-persist'}" data-ajax="false">累觉不爱</a></li>
	</ul>
</div>
<br />
<input type="button" data-role="button" id="refreshSpittles"  value="刷新" data-theme="b" />

<ul data-role="listview" data-split-theme="d" data-inset="true" data-count-theme="b" id="spittleList">
	<c:forEach var="spittle" items="${spittles}">
    <li class="spittle" data-spittleId="${spittle.id}">
       <h2>${user.id==1?spittle.id:''}${user.id==1?'.':''}${spittle.nickname}</h2>
        <p>${spittle.content}</p>
        <span class="ui-li-count spittle-likes" style="right:50px;">赞  <em>${spittle.likes}</em></span>
        <span class="ui-li-count spittle-dislikes">踩  <em>${spittle.dislikes}</em></span>
        <p class="ui-li-aside"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${spittle.postDate}" /></p>
        <div data-role="controlgroup" data-type="horizontal" style="margin-top:2em;">
        	<input type="button" data-role="button" class="spittle-like" ${spittle.isLiked==1||spittle.isDisliked==1?'disabled="disabled"':''} data-theme="${spittle.isLiked==1?'b':''}" value="赞" />
        	<input type="button" data-role="button" class="spittle-dislike" ${spittle.isLiked==1||spittle.isDisliked==1?'disabled="disabled"':''} value="踩" data-theme="${spittle.isDisliked==1?'b':''}" />
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
$(function() {
	var dislikeClick = function() {
		var $this = $(this);
		var $spittle = $this.parents('.spittle');
		var spittleId = $spittle.attr("data-spittleId");
		$.isst.api.dislike(spittleId, function(response) {
			if (response.code > 0) {
				$this.buttonMarkup({theme: 'b'}).button('refresh');
				$spittle.find('.spittle-like, .spittle-dislike').button('disable');
				$spittle.find('.spittle-dislikes em').text(parseInt($spittle.find('.spittle-dislikes em').text())+1);
			} else {
				alert(response.message);
			}
		});
	};
	
	var  likeClick = function() {
		var $this = $(this);
		var $spittle = $this.parents('.spittle');
		var spittleId = $spittle.attr("data-spittleId");
		$.isst.api.like(spittleId, function(response) {
			if (response.code > 0) {
				$this.buttonMarkup({theme: 'b'}).button('refresh');
				$spittle.find('.spittle-like, .spittle-dislike').button('disable');
				$spittle.find('.spittle-likes em').text(parseInt($spittle.find('.spittle-likes em').text())+1);
			} else {
				alert(response.message);
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
	
	$("#refreshSpittles").click(function() {
		$(this).prev('span').find('.ui-btn-text').text("刷新中...");
		$(this).button('disable');
		window.location.href="<%=basePath%>party/spittles-likes.html";
	});
});
</script>