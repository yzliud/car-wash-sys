<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>交易流水管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
	        laydate({
	            elem: '#beginTDatetime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
	        laydate({
	            elem: '#endTDatetime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
					
		
		});
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>交易流水列表 </h5>
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
	<form:form id="searchForm" modelAttribute="washTFlow" action="${ctx}/account/washTFlow/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>交易时间：</span>
				<input id="beginTDatetime" name="beginTDatetime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${washTFlow.beginTDatetime}" pattern="yyyy-MM-dd"/>"/> - 
				<input id="endTDatetime" name="endTDatetime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${washTFlow.endTDatetime}" pattern="yyyy-MM-dd"/>"/>
			<span>交易状态：</span>
				<form:select path="ttype"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('flow_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			<span>会员：</span>
				<sys:gridselect url="${ctx}/account/washTFlow/selectmember" id="member" name="member"  value="${washTFlow.member.id}"  title="选择会员" labelName="member.mobile" 
					labelValue="${washTFlow.member.mobile}" cssClass="form-control required" fieldLabels="手机号|昵称" fieldKeys="mobile|nickName" searchLabel="手机号" searchKey="mobile" ></sys:gridselect>
			<span>第三方流水号：</span>
				<form:input path="tppSn" htmlEscape="false" maxlength="50"  class=" form-control input-sm"/>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="account:washTFlow:add">
				<table:addRow url="${ctx}/account/washTFlow/form" title="交易流水"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="account:washTFlow:edit">
			    <table:editRow url="${ctx}/account/washTFlow/form" title="交易流水" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="account:washTFlow:del">
				<table:delRow url="${ctx}/account/washTFlow/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="account:washTFlow:import">
				<table:importExcel url="${ctx}/account/washTFlow/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="account:washTFlow:export">
	       		<table:exportExcel url="${ctx}/account/washTFlow/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th  class="sort-column tsn">流水号</th>
				<th  class="sort-column tdatetime">交易时间</th>
				<th  class="sort-column ttype">交易状态</th>
				<th  class="sort-column member.id">会员</th>
				<th  class="sort-column tamount">交易金额</th>
				<th  class="sort-column tppSn">第三方流水号</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="washTFlow">
			<tr>
				<td> <input type="checkbox" id="${washTFlow.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看交易流水', '${ctx}/account/washTFlow/form?id=${washTFlow.id}','800px', '500px')">
					${washTFlow.tsn}
				</a></td>
				<td>
					<fmt:formatDate value="${washTFlow.tdatetime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${fns:getDictLabel(washTFlow.ttype, 'flow_type', '')}
				</td>
				<td>
					${washTFlow.member.mobile}
				</td>
				<td>
					${washTFlow.tamount}
				</td>
				<td>
					${washTFlow.tppSn}
				</td>
				<td>
					<shiro:hasPermission name="account:washTFlow:view">
						<a href="#" onclick="openDialogView('查看交易流水', '${ctx}/account/washTFlow/form?id=${washTFlow.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="account:washTFlow:edit">
    					<a href="#" onclick="openDialog('修改交易流水', '${ctx}/account/washTFlow/form?id=${washTFlow.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="account:washTFlow:del">
						<a href="${ctx}/account/washTFlow/delete?id=${washTFlow.id}" onclick="return confirmx('确认要删除该交易流水吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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