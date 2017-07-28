<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>订单评价管理</title>
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
		<h5>订单评价列表 </h5>
		<div class="ibox-tools">
			<a class="collapse-link">
				<i class="fa fa-chevron-up"></i>
			</a>
			<a class="dropdown-toggle" data-toggle="dropdown" href="#">
				<i class="fa fa-wrench"></i>
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
	<form:form id="searchForm" modelAttribute="washOrdEvaluate" action="${ctx}/order/washOrdEvaluate/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>订单：</span>
				<form:input path="orderNo" htmlEscape="false" maxlength="32"  class=" form-control input-sm"/>
			<span>标识：</span>
				<form:select path="flag"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('evaluate_flag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
			<shiro:hasPermission name="order:washOrdEvaluate:add">
				<table:addRow url="${ctx}/order/washOrdEvaluate/form" title="订单评价"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="order:washOrdEvaluate:edit">
			    <table:editRow url="${ctx}/order/washOrdEvaluate/form" title="订单评价" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="order:washOrdEvaluate:del">
				<table:delRow url="${ctx}/order/washOrdEvaluate/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="order:washOrdEvaluate:import">
				<table:importExcel url="${ctx}/order/washOrdEvaluate/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="order:washOrdEvaluate:export">
	       		<table:exportExcel url="${ctx}/order/washOrdEvaluate/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th  class="sort-column orderNo">订单号</th>
				<th  class="sort-column flag">标识</th>
				<th  class="sort-column evaluate">评价</th>
				<th  class="sort-column addEvaluate">追加评价</th>
				<th  class="sort-column updateDate">update_date</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="washOrdEvaluate">
			<tr>
				<td>${washOrdEvaluate.orderNo}</td>

				<td>
					${fns:getDictLabel(washOrdEvaluate.flag, 'evaluate_flag', '')}
				</td>
				<td>
					${washOrdEvaluate.evaluate}
				</td>
				<td>
					${washOrdEvaluate.addEvaluate}
				</td>
				<td>
					<fmt:formatDate value="${washOrdEvaluate.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
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