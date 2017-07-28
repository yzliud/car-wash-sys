<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>设备使用记录管理</title>
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
			            elem: '#operatorTime', //目标元素。由于laydate.js封装了一个轻量级的选择器引擎，因此elem还允许你传入class、tag但必须按照这种方式 '#id .class'
			            event: 'focus' //响应事件。如果没有传入event，则按照默认的click
			        });
		});
	</script>
</head>
<body class="hideScroll">
		<form:form id="inputForm" modelAttribute="washDeviceRecord" action="${ctx}/device/washDeviceRecord/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">设备：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/device/washDeviceRecord/selectwashDevice" id="washDevice" name="washDevice.id"  value="${washDeviceRecord.washDevice.id}"  title="选择设备" labelName="washDevice.name" 
						 labelValue="${washDeviceRecord.washDevice.name}" cssClass="form-control required" fieldLabels="名称|MAC|地址" fieldKeys="name|mac|address" searchLabel="名称" searchKey="name" ></sys:gridselect>
					</td>
					<td class="width-15 active"><label class="pull-right">洗车工：</label></td>
					<td class="width-35">
						<sys:gridselect url="${ctx}/device/washDeviceRecord/selectwashMember" id="washMember" name="washMember.id"  value="${washDeviceRecord.washMember.id}"  title="选择洗车工" labelName="washMember.mobile" 
						 labelValue="${washDeviceRecord.washMember.mobile}" cssClass="form-control required" fieldLabels="手机号|昵称" fieldKeys="mobile|nickName" searchLabel="手机号" searchKey="mobile" ></sys:gridselect>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right">发送指令：</label></td>
					<td class="width-35">
						<form:input path="msg" htmlEscape="false"    class="form-control "/>
					</td>
					<td class="width-15 active"><label class="pull-right">操作时间：</label></td>
					<td class="width-35">
						<input id="operatorTime" name="operatorTime" type="text" maxlength="20" class="laydate-icon form-control layer-date "
							value="<fmt:formatDate value="${washDeviceRecord.operatorTime}" pattern="yyyy-MM-dd HH:mm:ss"/>"/>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>