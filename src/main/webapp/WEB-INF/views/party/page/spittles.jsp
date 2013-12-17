<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="activeNav" value="spittles" scope="request" />

<input type="text" id="spittleContent" placeholder="请输入内容..." />
<input type="button" id="spittleSubmit" value="发布"  data-inline="true" data-theme="b" />

<script type="text/javascript">
$(function() {
	$("#spittleSubmit").click(function() {
		var $spittleContent = $("#spittleContent");
		var content = $spittleContent.val();
		if (content) {
			$.isst.api.postSpittle(content, function(response) {
				if (response.code > 0) {
					$spittleContent.val("");
				} else {
					alert(response.message);
				}
			});
		}
	});
});
</script>