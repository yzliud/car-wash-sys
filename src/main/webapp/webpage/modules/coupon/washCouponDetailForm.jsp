<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>优惠卷明细管理</title>
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
			
		});
	</script>
</head>
<body class="hideScroll">
		<form:form id="inputForm" modelAttribute="washCouponDetail" action="${ctx}/coupon/washCouponDetail/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">优惠卷编码：</label></td>
					<td class="width-35">
						<form:input path="couponNo" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">优惠卷名称：</label></td>
					<td class="width-35">
						<form:input path="couponName" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">类型(0-抵用卷 1-满减)：</label></td>
					<td class="width-35">
						<form:input path="flag" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">会员ID：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/coupon/washCouponDetail/selectwashMember" id="washMember" name="washMember.id"  value="${washCouponDetail.washMember.id}"  title="选择会员ID" labelName="washMember.mobile" 
						 labelValue="${washCouponDetail.washMember.mobile}" cssClass="form-control required" fieldLabels="手机号|昵称|车牌号|车辆型号" fieldKeys="mobile|nickName|carNumber|carModel" searchLabel="手机号" searchKey="mobile" ></sys:gridselect>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">满足金额：</label></td>
					<td class="width-35">
						<form:input path="totalAmount" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">抵用金额：</label></td>
					<td class="width-35">
						<form:input path="discountAmount" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">生效时间：</label></td>
					<td class="width-35">
						<form:input path="effectiveTime" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">失效时间：</label></td>
					<td class="width-35">
						<form:input path="failureTime" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">订单号：</label></td>
					<td class="width-35">
						<form:input path="orderNo" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"></td>
		   			<td class="width-35" ></td>
		  		</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>