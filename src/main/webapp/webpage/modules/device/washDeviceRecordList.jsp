<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>设备使用记录管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
	        laydate({
	            elem: '#beginOperatorTime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
	        laydate({
	            elem: '#endOperatorTime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
					
		
		});
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>设备使用记录列表 </h5>
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
	<form:form id="searchForm" modelAttribute="washDeviceRecord" action="${ctx}/device/washDeviceRecord/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>设备：</span>
				<sys:gridselect url="${ctx}/device/washDeviceRecord/selectwashDevice" id="washDevice" name="washDevice"  value="${washDeviceRecord.washDevice.id}"  title="选择设备" labelName="washDevice.name" 
					labelValue="${washDeviceRecord.washDevice.name}" cssClass="form-control required" fieldLabels="名称|MAC|地址" fieldKeys="name|mac|address" searchLabel="名称" searchKey="name" ></sys:gridselect>
			<span>洗车工：</span>
				<sys:gridselect url="${ctx}/device/washDeviceRecord/selectwashMember" id="washMember" name="washMember"  value="${washDeviceRecord.washMember.id}"  title="选择洗车工" labelName="washMember.mobile" 
					labelValue="${washDeviceRecord.washMember.mobile}" cssClass="form-control required" fieldLabels="手机号|昵称" fieldKeys="mobile|nickName" searchLabel="手机号" searchKey="mobile" ></sys:gridselect>
			<span>操作时间：</span>
				<input id="beginOperatorTime" name="beginOperatorTime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${washDeviceRecord.beginOperatorTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/> - 
				<input id="endOperatorTime" name="endOperatorTime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${washDeviceRecord.endOperatorTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="device:washDeviceRecord:add">
				<table:addRow url="${ctx}/device/washDeviceRecord/form" title="设备使用记录"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="device:washDeviceRecord:edit">
			    <table:editRow url="${ctx}/device/washDeviceRecord/form" title="设备使用记录" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="device:washDeviceRecord:del">
				<table:delRow url="${ctx}/device/washDeviceRecord/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="device:washDeviceRecord:import">
				<table:importExcel url="${ctx}/device/washDeviceRecord/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="device:washDeviceRecord:export">
	       		<table:exportExcel url="${ctx}/device/washDeviceRecord/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th  class="sort-column washDevice.id">设备</th>
				<th  class="sort-column washMember.id">洗车工</th>
				<th  class="sort-column msg">发送指令</th>
				<th  class="sort-column operatorTime">操作时间</th>
				<th  class="sort-column updateDate">update_date</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="washDeviceRecord">
			<tr>
				<td> <input type="checkbox" id="${washDeviceRecord.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看设备使用记录', '${ctx}/device/washDeviceRecord/form?id=${washDeviceRecord.id}','800px', '500px')">
					${washDeviceRecord.washDevice.name}
				</a></td>
				<td>
					${washDeviceRecord.washMember.mobile}
				</td>
				<td>
					${washDeviceRecord.msg}
				</td>
				<td>
					<fmt:formatDate value="${washDeviceRecord.operatorTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${washDeviceRecord.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<shiro:hasPermission name="device:washDeviceRecord:view">
						<a href="#" onclick="openDialogView('查看设备使用记录', '${ctx}/device/washDeviceRecord/form?id=${washDeviceRecord.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="device:washDeviceRecord:edit">
    					<a href="#" onclick="openDialog('修改设备使用记录', '${ctx}/device/washDeviceRecord/form?id=${washDeviceRecord.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="device:washDeviceRecord:del">
						<a href="${ctx}/device/washDeviceRecord/delete?id=${washDeviceRecord.id}" onclick="return confirmx('确认要删除该设备使用记录吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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