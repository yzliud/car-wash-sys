<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>优惠卷管理</title>
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
		<h5>优惠卷列表 </h5>
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
	<form:form id="searchForm" modelAttribute="washCoupon" action="${ctx}/coupon/washCoupon/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>优惠卷名称：</span>
				<form:input path="name" htmlEscape="false" maxlength="32"  class=" form-control input-sm"/>
			<span>类型：</span>
				<form:select path="flag"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('coupon_flag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
			<shiro:hasPermission name="coupon:washCoupon:add">
				<table:addRow url="${ctx}/coupon/washCoupon/form" title="优惠卷"></table:addRow><!-- 增加按钮 -->
			</shiro:hasPermission>
			<shiro:hasPermission name="coupon:washCoupon:export">
	       		<table:exportExcel url="${ctx}/coupon/washCoupon/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th  class="sort-column name">优惠卷名称</th>
				<th  class="sort-column flag">类型</th>
				<th  class="sort-column quantity">数量</th>
				<th  class="sort-column totalAmount">满足金额</th>
				<th  class="sort-column discountAmount">抵用金额</th>
				<th  class="sort-column effectiveTime">生效时间</th>
				<th  class="sort-column failureTime">失效时间</th>
				<th  class="sort-column status">状态</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="washCoupon">
			<tr>
				<td> <input type="checkbox" id="${washCoupon.id}" class="i-checks"></td>
				<td><a  href="#" onclick="openDialogView('查看优惠卷', '${ctx}/coupon/washCoupon/form?id=${washCoupon.id}','800px', '500px')">
					${washCoupon.name}
				</a></td>
				<td>
					${fns:getDictLabel(washCoupon.flag, 'coupon_flag', '')}
				</td>
				<td>
					${washCoupon.quantity}
				</td>
				<td>
					${washCoupon.totalAmount}
				</td>
				<td>
					${washCoupon.discountAmount}
				</td>
				<td>
					<fmt:formatDate value="${washCoupon.effectiveTime}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<fmt:formatDate value="${washCoupon.failureTime}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					${fns:getDictLabel(washCoupon.status, 'coupon_public_status', '')}
				</td>
				<td>
				<c:if test="${ washCoupon.status == 0}">
					<shiro:hasPermission name="coupon:washCoupon:edit">
    					<a href="#" onclick="openDialog('修改优惠卷', '${ctx}/coupon/washCoupon/form?id=${washCoupon.id}','800px', '500px')" class="btn btn-success btn-xs" ><i class="fa fa-edit"></i> 修改</a>
    				</shiro:hasPermission>
    				<shiro:hasPermission name="coupon:washCoupon:del">
						<a href="${ctx}/coupon/washCoupon/delete?id=${washCoupon.id}" onclick="return confirmx('确认要删除该优惠卷吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-trash"></i> 删除</a>
					</shiro:hasPermission>
					<shiro:hasPermission name="coupon:washCoupon:release">
						<a href="${ctx}/coupon/washCoupon/release?id=${washCoupon.id}" onclick="return confirmx('确认要发布该优惠卷吗？', this.href)"   class="btn btn-danger btn-xs"><i class="fa fa-hand-spock-o"></i> 发布</a>
					</shiro:hasPermission>
				</c:if>
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