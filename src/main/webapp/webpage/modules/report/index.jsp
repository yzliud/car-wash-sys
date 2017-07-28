<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<%@ include file="/webpage/include/echarts.jsp"%>
<meta name="decorator" content="default"/>
	<div id="line_normal"  class="main000"></div>
    <echarts:line 
        id="line_normal"
		title="近一月销售曲线图" 
		subtitle=""
		xAxisData="${xLineAxisData}" 
		yAxisData="${yLineAxisData}" 
		xAxisName="年月"
		yAxisName="金额（元）" />
		
	