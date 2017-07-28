<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>订单管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
	        laydate({
	            elem: '#beginPayTime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
	        laydate({
	            elem: '#endPayTime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
	            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
	        });
					
		
		});
	</script>
</head>
<body class="gray-bg">
	<div class="wrapper wrapper-content">
	<div class="ibox">
	<div class="ibox-title">
		<h5>订单列表 </h5>
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
	<form:form id="searchForm" modelAttribute="washOrdOrder" action="${ctx}/order/washOrdOrder/" method="post" class="form-inline">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<table:sortColumn id="orderBy" name="orderBy" value="${page.orderBy}" callback="sortOrRefresh();"/><!-- 支持排序 -->
		<div class="form-group">
			<span>订单号：</span>
				<form:input path="orderNo" htmlEscape="false" maxlength="50"  class=" form-control input-sm"/>
			<span>车主：</span>
				<sys:gridselect url="${ctx}/order/washOrdOrder/selectcarPerson" id="carPerson" name="carPerson"  value="${washOrdOrder.carPerson.id}"  title="选择车主" labelName="carPerson.mobile" 
					labelValue="${washOrdOrder.carPerson.mobile}" cssClass="form-control required" fieldLabels="手机号|昵称" fieldKeys="mobile|nickName" searchLabel="手机号" searchKey="mobile" ></sys:gridselect>
			<span>洗车工：</span>
				<sys:gridselect url="${ctx}/order/washOrdOrder/selectwashPerson" id="washPerson" name="washPerson"  value="${washOrdOrder.washPerson.id}"  title="选择洗车工" labelName="washPerson.mobile" 
					labelValue="${washOrdOrder.washPerson.mobile}" cssClass="form-control required" fieldLabels="手机号|昵称" fieldKeys="mobile|nickName" searchLabel="手机号" searchKey="mobile" ></sys:gridselect>
			<span>设备：</span>
				<sys:gridselect url="${ctx}/order/washOrdOrder/selectwashDevice" id="washDevice" name="washDevice"  value="${washOrdOrder.washDevice.id}"  title="选择设备" labelName="washDevice.name" 
					labelValue="${washOrdOrder.washDevice.name}" cssClass="form-control required" fieldLabels="名称|MAC|地址" fieldKeys="name|mac|address" searchLabel="名称" searchKey="name" ></sys:gridselect>
			<span>付款时间：</span>
				<input id="beginPayTime" name="beginPayTime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${washOrdOrder.beginPayTime}" pattern="yyyy-MM-dd"/>"/> - 
				<input id="endPayTime" name="endPayTime" type="text" maxlength="20" class="laydate-icon form-control layer-date input-sm"
					value="<fmt:formatDate value="${washOrdOrder.endPayTime}" pattern="yyyy-MM-dd"/>"/>
			<span>订单状态：</span>
				<form:select path="orderStatus"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('order_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			<span>删除标识：</span>
				<form:select path="delFlag"  class="form-control m-b">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('del_flag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
			<shiro:hasPermission name="order:washOrdOrder:export">
	       		<table:exportExcel url="${ctx}/order/washOrdOrder/export"></table:exportExcel><!-- 导出按钮 -->
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
				<th  class="sort-column carPerson.id">车主</th>
				<th  class="sort-column washPerson.id">洗车工</th>
				<th  class="sort-column washDevice.id">设备</th>
				<th  class="sort-column totalFee">总费用</th>
				<th  class="sort-column discountFee">优惠金额</th>
				<th  class="sort-column realFee">实际费用</th>
				<th  class="sort-column orderTime">下单时间</th>
				<th  class="sort-column payTime">付款时间</th>
				<th  class="sort-column endTime">结束时间</th>
				<th  class="sort-column orderStatus">订单状态</th>
				<th  class="sort-column carNumber">车牌号</th>
				<th  class="sort-column delFlag">删除标识</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="washOrdOrder">
			<tr>
				<td><a  href="#" onclick="openDialogView('查看订单', '${ctx}/order/washOrdOrder/form?id=${washOrdOrder.id}','800px', '500px')">
					${washOrdOrder.orderNo}
				</a></td>
				<td>
					${washOrdOrder.carPerson.mobile}
				</td>
				<td>
					${washOrdOrder.washPerson.mobile}
				</td>
				<td>
					${washOrdOrder.washDevice.name}
				</td>
				<td>
					${washOrdOrder.totalFee}
				</td>
				<td>
					${washOrdOrder.discountFee}
				</td>
				<td>
					${washOrdOrder.realFee}
				</td>
				<td>
					<fmt:formatDate value="${washOrdOrder.orderTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${washOrdOrder.payTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					<fmt:formatDate value="${washOrdOrder.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
				</td>
				<td>
					${fns:getDictLabel(washOrdOrder.orderStatus, 'order_status', '')}
				</td>
				<td>
					${washOrdOrder.carNumber}
				</td>
				<td>
					${fns:getDictLabel(washOrdOrder.delFlag, 'del_flag', '')}
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