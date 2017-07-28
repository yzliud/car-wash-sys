<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>设备管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
		});
		function zip(){
			 var params = $("#searchForm").serialize();
			 var url = '${ctx}/device/washDevice/zip?'+params;
			 $('<form method="post" action="' + url + '"></form>').appendTo('body').submit().remove();
		}
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>设备列表 </h5>
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
	<form:form id="searchForm" modelAttribute="washDevice" action="${ctx}/device/washDevice/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>mac：</span>
				<form:input path="mac" htmlEscape="false" maxlength="32"  class=" form-control input-sm"/>
			<span>名称：</span>
				<form:input path="name" htmlEscape="false" maxlength="32"  class=" form-control input-sm"/>
			<span>状态：</span>
				<form:select path="status"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('device_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="device:washDevice:add">
				<table:addRow url="${ctx}/device/washDevice/form" title="设备"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="device:washDevice:edit">
			    <table:editRow url="${ctx}/device/washDevice/form" title="设备" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="device:washDevice:del">
				<table:delRow url="${ctx}/device/washDevice/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="device:washDevice:import">
				<table:importExcel url="${ctx}/device/washDevice/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="device:washDevice:export">
	       		<table:exportExcel url="${ctx}/device/washDevice/export"></table:exportExcel><!-- 导出按钮 -->
	       	</shiro:hasPermission>
	       	<button class="btn btn-white btn-sm " data-toggle="tooltip" data-placement="left" onclick="zip()" title="批量下载二维码"><i class="fa fa-print"></i>批量下载二维码</button>
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
				<th  class="sort-column mac">mac</th>
				<th  class="sort-column name">名称</th>
				<th  class="sort-column address">地址</th>
				<th  class="sort-column status">状态</th>
				<th  class="sort-column remarks">remarks</th>
				<th  class="sort-column updateDate">update_date</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="washDevice">
			<tr>
				<td> <input type="checkbox" id="${washDevice.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看设备', '${ctx}/device/washDevice/form?id=${washDevice.id}','800px', '500px')">
					${washDevice.mac}
				</a></td>
				<td>
					${washDevice.name}
				</td>
				<td>
					${washDevice.address}
				</td>
				<td>
					${fns:getDictLabel(washDevice.status, 'device_status', '')}
				</td>
				<td>
					${washDevice.remarks}
				</td>
				<td>
					<fmt:formatDate value="${washDevice.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<shiro:hasPermission name="device:washDevice:view">
						<a href="#" onclick="openDialogView('查看设备', '${ctx}/device/washDevice/form?id=${washDevice.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="device:washDevice:edit">
    					<a href="#" onclick="openDialog('修改设备', '${ctx}/device/washDevice/form?id=${washDevice.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="device:washDevice:del">
						<a href="${ctx}/device/washDevice/delete?id=${washDevice.id}" onclick="return confirmx('确认要删除该设备吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
					</shiro:hasPermission>
					<a href="${ctx}/device/washDevice/img?mac=${washDevice.mac}"  class="btn btn-info btn-xs" ><i class="fa fa-print"></i> 下载二维码</a>
					
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