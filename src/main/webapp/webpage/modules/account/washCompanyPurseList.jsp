<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>公司账户管理</title>
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
		<h5>公司账户列表 </h5>
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
	<form:form id="searchForm" modelAttribute="washCompanyPurse" action="${ctx}/account/washCompanyPurse/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>会员：</span>
				<sys:gridselect url="${ctx}/account/washCompanyPurse/selectmember" id="member" name="member"  value="${washCompanyPurse.member.id}"  title="选择会员" labelName="member.mobile" 
					labelValue="${washCompanyPurse.member.mobile}" cssClass="form-control required" fieldLabels="手机号|昵称|车牌号|车辆型号" fieldKeys="mobile|nickName|carNumber|carModel" searchLabel="手机号" searchKey="mobile" ></sys:gridselect>
			<span>流水号：</span>
				<form:input path="tflowNo" htmlEscape="false" maxlength="32"  class=" form-control input-sm"/>
			<span>交易类型：</span>
				<form:select path="ttype"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('flow_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
			<shiro:hasPermission name="account:washCompanyPurse:add">
				<table:addRow url="${ctx}/account/washCompanyPurse/form" title="公司账户"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="account:washCompanyPurse:edit">
			    <table:editRow url="${ctx}/account/washCompanyPurse/form" title="公司账户" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="account:washCompanyPurse:del">
				<table:delRow url="${ctx}/account/washCompanyPurse/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="account:washCompanyPurse:import">
				<table:importExcel url="${ctx}/account/washCompanyPurse/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="account:washCompanyPurse:export">
	       		<table:exportExcel url="${ctx}/account/washCompanyPurse/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th  class="sort-column member.id">会员</th>
				<th  class="sort-column tflowNo">流水号</th>
				<th  class="sort-column ttype">交易类型</th>
				<th  class="sort-column tdatetime">交易时间</th>
				<th  class="sort-column comment">描述</th>
				<th  class="sort-column income">收入</th>
				<th  class="sort-column pay">支出</th>
				<th  class="sort-column balance">余额</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="washCompanyPurse">
			<tr>
				<td>
					${washCompanyPurse.member.mobile}
				</td>
				<td>
					${washCompanyPurse.tflowNo}
				</td>
				<td>
					${fns:getDictLabel(washCompanyPurse.ttype, 'flow_type', '')}
				</td>
				<td>
					<fmt:formatDate value="${washCompanyPurse.tdatetime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${washCompanyPurse.comment}
				</td>
				<td>
					${washCompanyPurse.income}
				</td>
				<td>
					${washCompanyPurse.pay}
				</td>
				<td>
					${washCompanyPurse.balance}
				</td>
				<td>
					<shiro:hasPermission name="account:washCompanyPurse:view">
						<a href="#" onclick="openDialogView('查看公司账户', '${ctx}/account/washCompanyPurse/form?id=${washCompanyPurse.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="account:washCompanyPurse:edit">
    					<a href="#" onclick="openDialog('修改公司账户', '${ctx}/account/washCompanyPurse/form?id=${washCompanyPurse.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="account:washCompanyPurse:del">
						<a href="${ctx}/account/washCompanyPurse/delete?id=${washCompanyPurse.id}" onclick="return confirmx('确认要删除该公司账户吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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