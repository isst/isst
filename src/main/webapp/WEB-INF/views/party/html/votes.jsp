<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html lang="zh-cn">
<head>
<base href="<%=basePath%>">
<title>节目投票统计</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">    
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<link type="text/css" rel="stylesheet" href="resources/jquery.jqplot/jquery.jqplot.css" />
<link href="resources/css/party-screen.css"  type="text/css" rel="stylesheet"/>
</head>
<body>

<div class="top">
	<div class="logo"></div>
</div>

<div class="screen">
	<div id="votes"></div>
	
	<div class="controls hide" id="controls">
		<a href="<%=basePath%>party/screen.html">评论</a>
		<a href="<%=basePath%>party/lottery.html">抽奖</a>
		<div class="bg"></div>
	</div>
</div>
<!--[if lt IE 9]><script language="javascript" type="text/javascript" src="resources/jquery.jqplot/excanvas.js"></script><![endif]-->
<script type="text/javascript" src="resources/js/jquery-2.0.3.min.js"></script>
<script type="text/javascript" src="resources/jquery.jqplot/jquery.jqplot.min.js"></script>
<script type="text/javascript" src="resources/jquery.jqplot/jqplot.logAxisRenderer.min.js"></script>
<script type="text/javascript" src="resources/jquery.jqplot/jqplot.canvasTextRenderer.min.js"></script>
<script type="text/javascript" src="resources/jquery.jqplot/jqplot.canvasAxisLabelRenderer.min.js"></script>
<script type="text/javascript" src="resources/jquery.jqplot/jqplot.categoryAxisRenderer.min.js"></script>
<script type="text/javascript" src="resources/jquery.jqplot/jqplot.barRenderer.min.js"></script>
<script type="text/javascript" src="resources/jquery.jqplot/jqplot.pointLabels.min.js"></script>
<script type="text/javascript" src="resources/jquery.jqplot/jqplot.canvasAxisTickRenderer.min.js"></script>
<script type="text/javascript" src="resources/js/ajax-pushlet-client.js"></script>
<script type="text/javascript" src="resources/js/party-screen.js"></script>
<script type="text/javascript">
	var showMapper = <c:out value="${showNameMapper}" escapeXml="false" />;
	var jqplotOptions = {
		animate: true,
		title:'节目投票统计',
  		axes: {yaxis:{renderer: $.jqplot.LogAxisRenderer}},
 		 seriesDefaults:{
            renderer:$.jqplot.BarRenderer,
 		 	pointLabels: {show: true, formatString: '%d'},
            rendererOptions: {
               	varyBarColor: true
            }
        },
		axes: {
			yaxis:{
				ticks: [0, 100, 200, 300, 400, 500, 600, 700],
				tickOptions: {
					formatString: '%d'
				},
				label: '投票数'
			},
			xaxis: {
				label: '节目单',
				renderer: $.jqplot.CategoryAxisRenderer,
				tickRenderer: $.jqplot.CanvasAxisTickRenderer,
				ticks: <c:out value="${showNames}" escapeXml="false" />,
				tickOptions: {
					angle: -30,
					textColor: '#ffffff',
					fontSize: '18px',
					fontWeight: 'bold'
				}
			}
		}
	};
	var $plot = null;
	var dataQueue = [];
	var latestData = null;
	var pushVoteData = function(votes) {
		var data = [];
		for (var i = 0; i < showMapper.length; i++) {
			if (votes[showMapper[i]] == undefined) {
				data[i] = 0;
			} else {
				data[i] = votes[showMapper[i]];
			}
		}
		if (data.length>0) {
			var hasDataChanged = false;
			if (latestData) {
				for (var i = 0; i<data.length; i++) {
					if (data[i] != latestData[i]) {
						hasDataChanged = true;
						break;
					}
				}
			}
			if (!latestData || hasDataChanged) {
				latestData = data;
				dataQueue.push(data);
			}
		}
	};
	var plotTimer = null;
	var isReploting = false;
	var doReplot = function() {
		if (!isReploting && dataQueue.length > 0) {
			isReploting = true;
			var data = dataQueue.shift();
			if (!$plot) {
				$plot = $.jqplot('votes', [data], jqplotOptions);
			} else {
				for (var i = 0; i < $plot.series.length; i++) {  
        			for (var j = 0; j < $plot.series[i].data.length; j++) {  
            			$plot.series[i].data[j][1] = data[j];    
        			}  
    			}
				$plot.replot();
			}
			isReploting = false;
			plotTimer = setTimeout(doReplot, 3000);
		} else {
			plotTimer = null;
		}
	};
	
	pushVoteData(<c:out value="${voteData}" escapeXml="false" />);
	doReplot();
	
	//PL.webRoot = "<%=basePath%>";
	//PL._init();
	//PL.joinListen("/party/votes");
	
	function onData(event) {
		var votes = event.get("votes");
		if (votes) {
			votes = $.parseJSON(votes);
		    if (votes) {
				pushVoteData(votes);
				if (!isReploting && !plotTimer) {
		    		doReplot();
		    	}
		    }
		}
	}
</script>
</body>
</html>
