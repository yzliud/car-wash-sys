<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>会员管理</title>
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
		<h5>会员列表 </h5>
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
	<form:form id="searchForm" modelAttribute="washMember" action="${ctx}/member/washMember/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>昵称：</span>
				<form:input path="nickName" htmlEscape="false" maxlength="20"  class=" form-control input-sm"/>
			<span>手机号：</span>
				<form:input path="mobile" htmlEscape="false" maxlength="20"  class=" form-control input-sm"/>
			<span>用户状态 ：</span>
				<form:select path="status"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('member_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			<span>用户类型：</span>
				<form:select path="userType"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('user_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
			<shiro:hasPermission name="member:washMember:add">
				<table:addRow url="${ctx}/member/washMember/form" title="会员"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="member:washMember:edit">
			    <table:editRow url="${ctx}/member/washMember/form" title="会员" id="contentTable"></table:editRow><!-- 编辑按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="member:washMember:del">
				<table:delRow url="${ctx}/member/washMember/deleteAll" id="contentTable"></table:delRow><!-- 删除按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="member:washMember:import">
				<table:importExcel url="${ctx}/member/washMember/import"></table:importExcel><!-- 导入按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="member:washMember:export">
	       		<table:exportExcel url="${ctx}/member/washMember/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th  class="sort-column nickName">昵称</th>
				<th  class="sort-column mobile">手机号</th>
				<th  class="sort-column status">用户状态</th>
				<th  class="sort-column userType">用户类型</th>
				<th  class="sort-column carModel">车辆型号</th>
				<th  class="sort-column carNumber">车牌号</th>
				<th  class="sort-column sex">性别</th>
				<th  class="sort-column country">国家</th>
				<th  class="sort-column province">省</th>
				<th  class="sort-column city">市</th>
				<th  class="sort-column img">头像</th>
				<th  class="sort-column lastTime">最后登陆时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="washMember">
			<tr>
				<td> <input type="checkbox" id="${washMember.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看会员', '${ctx}/member/washMember/form?id=${washMember.id}','800px', '500px')">
					${washMember.nickName}
				</a></td>
				<td>
					${washMember.mobile}
				</td>
				<td>
					${fns:getDictLabel(washMember.status, 'member_status', '')}
				</td>
				<td>
					${fns:getDictLabel(washMember.userType, 'user_type', '')}
				</td>
				<td>
					${washMember.carModel}
				</td>
				<td>
					${washMember.carNumber}
				</td>
				<td>
					${fns:getDictLabel(washMember.sex, 'sex', '')}
				</td>
				<td>
					${washMember.country}
				</td>
				<td>
					${washMember.province}
				</td>
				<td>
					${washMember.city}
				</td>
				<td>
					<img src="${washMember.img}" style="width:20px">
				</td>
				<td>
					<fmt:formatDate value="${washMember.lastTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
    					<a href="#" onclick="openDialog('设置', '${ctx}/member/washMember/forwardSet?id=${washMember.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i>设置</a>
					<shiro:hasPermission name="member:washMember:view">
						<a href="#" onclick="openDialogView('查看会员', '${ctx}/member/washMember/form?id=${washMember.id}','800px', '500px')" class="btn btn-info btn-xs" ><i class="fa fa-search-plus"></i> 查看</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="member:washMember:edit">
    					<a href="#" onclick="openDialog('修改会员', '${ctx}/member/washMember/form?id=${washMember.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="member:washMember:del">
						<a href="${ctx}/member/washMember/delete?id=${washMember.id}" onclick="return confirmx('确认要删除该会员吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
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