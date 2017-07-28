<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>工作记录管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
	        laydate({
	            elem: '#beginWorkOnTime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
	        laydate({
	            elem: '#endWorkOnTime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
					
		
		});
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>工作记录列表 </h5>
		<div class="ibox-tools">
			<a class="collapse-link">
				<i class="fa fa-chevron-up"></i>
			</a>
			<a class="dropdown-toggle" data-toggle="dropdown" href="#">
				<i class="fa fa-wrench"></i>
			</a>
			<ul class="dropdown-menu dropdown-user">
				<li><a href="#">选项1</a>
				</li>
				<li><a href="#">选项2</a>
				</li>
			</ul>
			<a class="close-link">
				<i class="fa fa-times"></i>
			</a>
		</div>
	</div>
    
    <div class="ibox-content">
	<sys:message content="${message}"/>
	
	<!--查询条件-->
	<div class="row">
	<div class="col-sm-12">
	<form:form id="searchForm" modelAttribute="washWorkDeviceRecord" action="${ctx}/wash/washWorkDeviceRecord/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>洗车工：</span>
				<sys:gridselect url="${ctx}/wash/washWorkDeviceRecord/selectwashPerson" id="washPerson" name="washPerson"  value="${washWorkDeviceRecord.washPerson.id}"  title="选择洗车工" labelName="washPerson.mobile" 
					labelValue="${washWorkDeviceRecord.washPerson.mobile}" cssClass="form-control required" fieldLabels="手机号|昵称" fieldKeys="mobile|nickName" searchLabel="手机号" searchKey="mobile" ></sys:gridselect>
			<span>设备：</span>
				<sys:gridselect url="${ctx}/wash/washWorkDeviceRecord/selectwashDevice" id="washDevice" name="washDevice"  value="${washWorkDeviceRecord.washDevice.id}"  title="选择设备" labelName="washDevice.name" 
					labelValue="${washWorkDeviceRecord.washDevice.name}" cssClass="form-control required" fieldLabels="名称|MAC|地址" fieldKeys="name|mac|address" searchLabel="名称" searchKey="name" ></sys:gridselect>
			<span>上班时间：</span>
				<input id="beginWorkOnTime" name="beginWorkOnTime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${washWorkDeviceRecord.beginWorkOnTime}" pattern="yyyy-MM-dd"/>"/> - 
				<input id="endWorkOnTime" name="endWorkOnTime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${washWorkDeviceRecord.endWorkOnTime}" pattern="yyyy-MM-dd"/>"/>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="wash:washWorkDeviceRecord:add">
				<table:addRow url="${ctx}/wash/washWorkDeviceRecord/form" title="工作记录"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="wash:washWorkDeviceRecord:edit">
			    <table:editRow url="${ctx}/wash/washWorkDeviceRecord/form" title="工作记录" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="wash:washWorkDeviceRecord:del">
				<table:delRow url="${ctx}/wash/washWorkDeviceRecord/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="wash:washWorkDeviceRecord:import">
				<table:importExcel url="${ctx}/wash/washWorkDeviceRecord/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="wash:washWorkDeviceRecord:export">
	       		<table:exportExcel url="${ctx}/wash/washWorkDeviceRecord/export"></table:exportExcel><!-- 导出按钮 -->
	       	</shiro:hasPermission>
	       <button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="sortOrRefresh()" title="刷新"><i class="glyphicon glyphicon-repeat"></i> 刷新</button>
		
			</div>
		<div class="pull-right">
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="search()" ><i class="fa fa-search"></i> 查询</button>
			<button  class="btn btn-primary btn-rounded btn-outline btn-sm " onclick="reset()" ><i class="fa fa-refresh"></i> 重置</button>
		</div>
	</div>
	</div>
	
	<!-- 表格 -->
	<table id="contentTable" class="table table-striped table-bordered table-hover table-condensed dataTables-example dataTable">
		<thead>
			<tr>
				<th  class="sort-column washPerson.id">洗车工</th>
				<th  class="sort-column washDevice.id">设备</th>
				<th  class="sort-column workOnTime">上班时间</th>
				<th  class="sort-column workOffTime">下班时间</th>
				<th  class="sort-column remarks">remarks</th>
				<th  class="sort-column updateDate">update_date</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="washWorkDeviceRecord">
			<tr>
				<td>
					${washWorkDeviceRecord.washPerson.mobile}
				</td>
				<td>
					${washWorkDeviceRecord.washDevice.name}
				</td>
				<td>
					<fmt:formatDate value="${washWorkDeviceRecord.workOnTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${washWorkDeviceRecord.workOffTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${washWorkDeviceRecord.remarks}
				</td>
				<td>
					<fmt:formatDate value="${washWorkDeviceRecord.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				
			</tr>
		</c:forEach>
		</tbody>
	</table>
	
		<!-- 分页代码 -->
	<table:page page="${page}"></table:page>
	<br/>
	<br/>
	</div>
	</div>
</div>
</body>
</html>