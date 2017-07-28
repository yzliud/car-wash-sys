<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>设备故障管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>设备故障列表 </h5>
		<div class="ibox-tools">
			<a class="collapse-link">
				<i class="fa fa-chevron-up"></i>
			</a>
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
	<form:form id="searchForm" modelAttribute="washDeviceFault" action="${ctx}/device/washDeviceFault/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>设备MAC：</span>
				<form:input path="mac" htmlEscape="false" maxlength="32"  class=" form-control input-sm"/>
			<span>设备名称：</span>
				<form:input path="deviceName" htmlEscape="false" maxlength="200"  class=" form-control input-sm"/>
			<span>设备地址：</span>
				<form:input path="deviceAddress" htmlEscape="false" maxlength="200"  class=" form-control input-sm"/>
			<!--  <span>状态（0-未处理 1已处理）：</span>
				<form:select path="status"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('device_fault_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>-->
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="device:washDeviceFault:add">
				<table:addRow url="${ctx}/device/washDeviceFault/form" title="设备故障"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="device:washDeviceFault:edit">
			    <table:editRow url="${ctx}/device/washDeviceFault/form" title="设备故障" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="device:washDeviceFault:del">
				<table:delRow url="${ctx}/device/washDeviceFault/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="device:washDeviceFault:import">
				<table:importExcel url="${ctx}/device/washDeviceFault/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="device:washDeviceFault:export">
	       		<table:exportExcel url="${ctx}/device/washDeviceFault/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th> <input type="checkbox" class="i-checks"></th>
				<th  class="sort-column mac">设备MAC</th>
				<th  class="sort-column deviceName">设备名称</th>
				<th  class="sort-column deviceAddress">设备地址</th>
				<th  class="sort-column faultTime">故障时间</th>
				<th  class="sort-column faultDesc">故障问题</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="washDeviceFault">
			<tr>
				<td> <input type="checkbox" id="${washDeviceFault.id}" class="i-checks"></td>
				<td>
					${washDeviceFault.mac}
				</td>
				<td>
					${washDeviceFault.deviceName}
				</td>
				<td>
					${washDeviceFault.deviceAddress}
				</td>
				<td>
					<fmt:formatDate value="${washDeviceFault.faultTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${washDeviceFault.faultDesc}
				</td>
				
				<td>
					<shiro:hasPermission name="device:washDeviceFault:view">
						<a href="#" onclick="openDialogView('查看设备故障', '${ctx}/device/washDeviceFault/form?id=${washDeviceFault.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="device:washDeviceFault:edit">
    					<a href="#" onclick="openDialog('修改设备故障', '${ctx}/device/washDeviceFault/form?id=${washDeviceFault.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="device:washDeviceFault:del">
						<a href="${ctx}/device/washDeviceFault/delete?id=${washDeviceFault.id}" onclick="return confirmx('确认要删除该设备故障吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
					</shiro:hasPermission>
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