<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<input type="text" id="nicknameText" placeholder="请输入昵称..." value="${user.nickname}" />
<input type="button" id="nicknameSubmit" value="修改"  data-inline="true" data-theme="b" />

<script type="text/javascript">
$(function() {
	$("#nicknameSubmit").click(function() {
		var nickname = $.trim($("#nicknameText").val());
		if (nickname) {
			$.isst.api.updateNickname(nickname, function(response) {
				if (response.code > 0) {
					$.post($.isst.party.url + '/login', {id: $.isst.userId}, function() {
						$('#nickname .ui-btn-text').text(nickname);
						$('.ui-dialog').dialog('close');
					});
				} else {
					alert(response.message);
				}
			});
		}
	});
});
</script>