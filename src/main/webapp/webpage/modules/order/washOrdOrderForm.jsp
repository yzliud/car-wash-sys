<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>订单管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		var validateForm;
		function doSubmit(){//回调函数，在编辑和保存动作时，供openDialog调用提交表单。
		  if(validateForm.form()){
			  $("#inputForm").submit();
			  return true;
		  }
	
		  return false;
		}
		$(document).ready(function() {
			validateForm = $("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
			
					laydate({
			            elem: '#orderTime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
			            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
			        });
					laydate({
			            elem: '#payTime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
			            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
			        });
					laydate({
			            elem: '#endTime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
			            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
			        });
		});
	</script>
</head>
<body class="hideScroll">
		<form:form id="inputForm" modelAttribute="washOrdOrder" action="${ctx}/order/washOrdOrder/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">订单号：</label></td>
					<td class="width-35">
						<form:input path="orderNo" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">车主：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/order/washOrdOrder/selectcarPerson" id="carPerson" name="carPerson.id"  value="${washOrdOrder.carPerson.id}"  title="选择车主" labelName="carPerson.mobile" 
						 labelValue="${washOrdOrder.carPerson.mobile}" cssClass="form-control required" fieldLabels="手机号|昵称" fieldKeys="mobile|nickName" searchLabel="手机号" searchKey="mobile" ></sys:gridselect>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">洗车工：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/order/washOrdOrder/selectwashPerson" id="washPerson" name="washPerson.id"  value="${washOrdOrder.washPerson.id}"  title="选择洗车工" labelName="washPerson.mobile" 
						 labelValue="${washOrdOrder.washPerson.mobile}" cssClass="form-control required" fieldLabels="手机号|昵称" fieldKeys="mobile|nickName" searchLabel="手机号" searchKey="mobile" ></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right">设备：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/order/washOrdOrder/selectwashDevice" id="washDevice" name="washDevice.id"  value="${washOrdOrder.washDevice.id}"  title="选择设备" labelName="washDevice.name" 
						 labelValue="${washOrdOrder.washDevice.name}" cssClass="form-control required" fieldLabels="名称|MAC|地址" fieldKeys="name|mac|address" searchLabel="名称" searchKey="name" ></sys:gridselect>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">订单手机号：</label></td>
					<td class="width-35">
						<form:input path="mobile" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">车牌号：</label></td>
					<td class="width-35">
						<form:input path="carNumber" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">总费用：</label></td>
					<td class="width-35">
						<form:input path="totalFee" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">优惠金额：</label></td>
					<td class="width-35">
						<form:input path="discountFee" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">实际费用：</label></td>
					<td class="width-35">
						<form:input path="realFee" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">下单时间：</label></td>
					<td class="width-35">
						<input id="orderTime" name="orderTime" type="text" maxlength="20" class="laydate-icon form-control layer-date "
							value="<fmt:formatDate value="${washOrdOrder.orderTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">付款时间：</label></td>
					<td class="width-35">
						<input id="payTime" name="payTime" type="text" maxlength="20" class="laydate-icon form-control layer-date "
							value="<fmt:formatDate value="${washOrdOrder.payTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
					</td>
					<td class="width-15 active"><label class="pull-right">结束时间：</label></td>
					<td class="width-35">
						<input id="endTime" name="endTime" type="text" maxlength="20" class="laydate-icon form-control layer-date "
							value="<fmt:formatDate value="${washOrdOrder.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">订单状态：</label></td>
					<td class="width-35">
						<form:select path="orderStatus" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('order_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right">支付方式：</label></td>
					<td class="width-35">
						<form:select path="payMode" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('pay_mode')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">支付流水号：</label></td>
					<td class="width-35">
						<form:input path="paySerialNumber" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">删除标识：</label></td>
					<td class="width-35">
						<form:select path="delFlag" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('del_flag')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
		  		</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>