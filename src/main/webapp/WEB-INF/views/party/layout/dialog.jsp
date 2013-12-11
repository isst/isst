<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="zh-cn">
<head>
	<tiles:insertAttribute name="head" />
</head>
<body>
<div data-role="page">
	<tiles:insertAttribute name="script" />
	<div data-role="header" data-theme="d">
		<h1><c:out value="${title}" /></h1>
	</div>
	<div data-role="content">
		<tiles:insertAttribute name="body" />
	</div>
</div>
</body>
</html>