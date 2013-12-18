<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="activeNav" value="spittles" scope="request" />

<form action="" method="post" id="spittleForm">
<input type="text" id="spittleContent" placeholder="请输入内容..." />
<input type="submit" value="发布"  data-inline="true" data-theme="b" />
</form>


<script type="text/javascript">
$(function() {
	$("#spittleForm").submit(function() {
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
		return false;
	});
});
</script>