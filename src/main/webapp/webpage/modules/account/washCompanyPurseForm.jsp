<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>公司账户管理</title>
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
			            elem: '#tDatetime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
			            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
			        });
		});
	</script>
</head>
<body class="hideScroll">
		<form:form id="inputForm" modelAttribute="washCompanyPurse" action="${ctx}/account/washCompanyPurse/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">会员：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/account/washCompanyPurse/selectmember" id="member" name="member.id"  value="${washCompanyPurse.member.id}"  title="选择会员" labelName="member.mobile" 
						 labelValue="${washCompanyPurse.member.mobile}" cssClass="form-control required" fieldLabels="手机号|昵称|车牌号|车辆型号" fieldKeys="mobile|nickName|carNumber|carModel" searchLabel="手机号" searchKey="mobile" ></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right">流水号：</label></td>
					<td class="width-35">
						<form:input path="tflowNo" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">交易类型（00 正常10 冻结90 异常）：</label></td>
					<td class="width-35">
						<form:select path="ttype" class="form-control ">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('flow_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>交易时间：</label></td>
					<td class="width-35">
						<input id="tdatetime" name="tdatetime" type="text" maxlength="20" class="laydate-icon form-control layer-date required"
							value="<fmt:formatDate value="${washCompanyPurse.tdatetime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">描述：</label></td>
					<td class="width-35">
						<form:input path="comment" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">收入：</label></td>
					<td class="width-35">
						<form:input path="income" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">支出：</label></td>
					<td class="width-35">
						<form:input path="pay" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">余额：</label></td>
					<td class="width-35">
						<form:input path="balance" htmlEscape="false"    class="form-control "/>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>