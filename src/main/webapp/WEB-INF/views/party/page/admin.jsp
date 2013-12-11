<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:set var="activeNav" value="admin" scope="request" />

<div data-role="collapsible" data-collapsed-icon="arrow-r" data-expanded-icon="arrow-d" data-collapsed="false" data-content-theme="c">
	<h3>队列</h3>
	<p>
		<label>普通队列中条数：</label>
		<label class="ui-li-count" id="admin-queue-count">0</label>
	</p>
	<input type="button" id="initSpittleQueue" value="初始化队列" data-theme="c" data-inline="true" />
	<input type="button" id="updateSpittleQueueCount" value="更新队列" data-theme="c" data-inline="true" />
</div>

<script type="text/javascript">
$(function() {
	$('#initSpittleQueue').click(function() {
		$.getJSON($.isst.party.createUrl("admin/pushAllSpittles"), function(response) {
			$('#admin-queue-count').text(response.size);
		});
	});
	
	$('#updateSpittleQueueCount').click(function() {
		$.getJSON($.isst.party.createUrl("admin/spittleQueueSize"), function(response) {
			$('#admin-queue-count').text(response.size);
		});
	}).click();
});
</script>