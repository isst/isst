<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<base href="<%=basePath%>" />
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title><c:out value="${title}" /></title>
<link rel="stylesheet"  href="<%=basePath%>resources/jquery.mobile/themes/default/jquery.mobile-1.3.2.min.css" />
<link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Open+Sans:300,400,700">
<script src="<%=basePath%>resources/js/jquery-2.0.3.min.js"></script>
<script src="<%=basePath%>resources/jquery.mobile/jquery.mobile-1.3.2.min.js"></script>
<script type="text/javascript">
		window.alert = function(message) {
			$('#alert').find('p').html(message);
			$('#alert').popup("open");
		};
		
		$(document).bind("mobileinit", function () {
    		//$.mobile.ajaxEnabled = false;
    		//$.mobile.defaultPageTransition = 'none';
      		//$.mobile.defaultDialogTransition = 'none';
      		//$.mobile.useFastClick = true;
		});
		
		$.isst = {
			url: '<%=basePath%>',
			userId: <c:out value="${user.id}" />
		};
		
		$.isst.party = {
			url: '<%=basePath%>party',
			createUrl: function(path) {
				if (path.substr(0, 1) != '/') {
					path = $.isst.party.url + '/' + path;
				}
				return path;
			},
			activeNav: function(id) {
				var $nav = $("#nav-" + id);
				if ($nav.length !=0) {
					$nav.addClass("ui-btn-active ui-state-persist");
				}
			}
		};
		
		$.isst.api = {
			url: '<%=basePath%>api',
			get: function(path, data, callback, complete) {
				$.isst.api._ajax(path, data, callback, complete, 'get');
			},
			post: function(path, data, callback, complete) {
				$.isst.api._ajax(path, data, callback, complete, 'post');
			},
			put: function(path, data, callback, complete) {
				data['_method'] = 'PUT';
				$.isst.api._ajax(path, data, callback, complete, 'post');
			},
			del: function(path, data, callback, complete) {
				data['_method'] = 'DELETE';
				$.isst.api._ajax(path, data, callback, complete, 'post');
			},
			_ajax: function(path, data, callback, complete, type) {
				data._path = path;
				$.ajax({
					url: $.isst.party.createUrl("api"),
					data: data || {},
					type: type,
					cache: false,
					dataType: 'json',
					success: callback,
					complete: complete || function() {},
					error: function() {
						alert("服务器错误");
					}
				});
			},
			login: function(name, password, callback, complete) {
				$.isst.api.post('/users/validation', {name: name, password: password}, callback, complete);
			},
			postSpittle: function(content, callback, complete) {
				$.isst.api.post('/users/{userId}/spittles', {content: content, userId: $.isst.userId}, callback, complete);
			},
			updateNickname: function(nickname, callback, complete) {
				$.isst.api.put('/users/{userId}', {nickname: nickname, userId: $.isst.userId}, callback, complete);
			},
			getShows: function(callback, complete) {
				$.isst.api.get('/users/{userId}/shows', {userId: $.isst.userId}, callback, complete);
			},
			showVote: function(showId, callback, complete) {
				$.isst.api.post('/users/{userId}/shows/{showId}/votes', {userId: $.isst.userId, showId: showId}, callback, complete);
			},
			like: function(spittleId, callback, complete, isLike) {
				isLike = isLike == undefined ? 1 : isLike;
				$.isst.api.post('/users/{userId}/spittles/{spittleId}/likes', {userId: $.isst.userId, spittleId: spittleId, isLike: isLike}, callback);
			},
			dislike: function(spittleId, callback, complete) {
				$.isst.api.like(spittleId, callback, complete, 0);
			},
			getSpittles: function(id, page, pageSize, callback, complete) {
				$.isst.api.get('/users/{userId}/spittles', {userId: $.isst.userId, id: id, page: page, pageSize: pageSize}, callback, complete);
			},
			getLikeSpittles: function(callback, complete) {
				$.isst.api.get('/users/{userId}/spittles/likes', {userId: $.isst.userId, isLike: 1}, callback, complete);
			},
			getDislikeSpittles: function(callback, complete) {
				$.isst.api.get('/users/{userId}/spittles/likes', {userId: $.isst.userId, isLike: 0}, callback, complete);
			},
			deleteSpittle: function(spittleId, callback, complete) {
				$.isst.api.del('/users/{userId}/spittles/{spittleId}', {userId: $.isst.userId, spittleId: spittleId}, callback, complete);
			}
		};
		
		$(function() {
	    $(window).scroll(function() {
	        if($(this).scrollTop() != 0) {
	            $('#toTop').fadeIn();    
	        } else {
	            $('#toTop').fadeOut();
	        }
	    });
	
	    $('#toTop').click(function() {
	        $('body,html').animate({scrollTop:0},800);
	    });    
	});
</script>
<style type="text/css">
	.ui-header .ui-title { white-space: normal; margin: 0.6em 0 0.8em; text-align: center;}
	.ui-li-heading, .ui-li-desc { white-space: normal; }
	.content { position: relative;}
	#toTop {position: fixed; bottom: 130px; right: 20px; width: 70px; cursor: pointer; display: none; z-index:1000;}
</style>