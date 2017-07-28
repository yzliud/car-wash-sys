<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>优惠卷明细管理</title>
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
		<h5>优惠卷明细列表 </h5>
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
	<form:form id="searchForm" modelAttribute="washCouponDetail" action="${ctx}/coupon/washCouponDetail/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>优惠卷编码：</span>
				<form:input path="couponNo" htmlEscape="false" maxlength="32"  class=" form-control input-sm"/>
			<span>优惠卷名称：</span>
				<form:input path="couponName" htmlEscape="false" maxlength="32"  class=" form-control input-sm"/>
			<span>类型：</span>
				<form:select path="flag"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('coupon_flag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			<span>会员：</span>
				<sys:gridselect url="${ctx}/coupon/washCouponDetail/selectwashMember" id="washMember" name="washMember"  value="${washCouponDetail.washMember.id}"  title="选择会员" labelName="washMember.mobile" 
					labelValue="${washCouponDetail.washMember.mobile}" cssClass="form-control required" fieldLabels="手机号|昵称|车牌号|车辆型号" fieldKeys="mobile|nickName|carNumber|carModel" searchLabel="手机号" searchKey="mobile" ></sys:gridselect>
		 </div>	
	</form:form>
	<br/>
	</div>
	</div>
	
	<!-- 工具栏 -->
	<div class="row">
	<div class="col-sm-12">
		<div class="pull-left">
			<shiro:hasPermission name="coupon:washCouponDetail:export">
	       		<table:exportExcel url="${ctx}/coupon/washCouponDetail/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th  class="sort-column couponNo">优惠卷编码</th>
				<th  class="sort-column couponName">优惠卷名称</th>
				<th  class="sort-column flag">类型</th>
				<th  class="sort-column washMember.id">会员</th>
				<th  class="sort-column totalAmount">满足金额</th>
				<th  class="sort-column discountAmount">抵用金额</th>
				<th  class="sort-column effectiveTime">生效时间</th>
				<th  class="sort-column failureTime">失效时间</th>
				<th  class="sort-column orderNo">订单号</th>
				<th  class="sort-column remarks">remarks</th>
				<th  class="sort-column updateTime">update_time</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="washCouponDetail">
			<tr>
				<td>
					${washCouponDetail.couponNo}
				</td>
				<td>
					${washCouponDetail.couponName}
				</td>
				<td>
					${fns:getDictLabel(washCouponDetail.flag, 'coupon_flag', '')}
				</td>
				<td>
					${washCouponDetail.washMember.mobile}
				</td>
				<td>
					${washCouponDetail.totalAmount}
				</td>
				<td>
					${washCouponDetail.discountAmount}
				</td>
				<td>
					<fmt:formatDate value="${washCouponDetail.effectiveTime}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<fmt:formatDate value="${washCouponDetail.failureTime}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					${washCouponDetail.orderNo}
				</td>
				<td>
					${washCouponDetail.remarks}
				</td>
				<td>
					<fmt:formatDate value="${washCouponDetail.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
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