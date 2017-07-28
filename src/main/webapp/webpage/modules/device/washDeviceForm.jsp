<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>设备管理</title>
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

			
			jQuery.validator.addMethod("checkMac", function(value, element) {
				
				var checkFlag = 1;
				$.ajax({
		 			url:"${ctx}/device/washDevice/checkMac",
		 			dataType:'json',
		 			async:false,
		 			data:{
		 				"mac":$('#mac').val(),
		 				"oldMac":"${washDevice.mac}"
					},
		 			type:'post',
		 			success:function(data){
		 				if(data.rtnCode == '0'){
		 					checkFlag = 0;
		 				}
		 			}
			    });
				 if(checkFlag == 1){
			        return true;
			    }else{
			        return false;
			    }
			
			}, "此MAC已存在,请重新输入！");
			
			jQuery.validator.addMethod("checkName", function(value, element) {
				
				var checkFlag = 1;
				$.ajax({
		 			url:"${ctx}/device/washDevice/checkName",
		 			dataType:'json',
		 			async:false,
		 			data:{
		 				"name":$('#name').val(),
		 				"oldName":"${washDevice.name}"
					},
		 			type:'post',
		 			success:function(data){
		 				if(data.rtnCode == '0'){
		 					checkFlag = 0;
		 				}
		 			}
			    });
				 if(checkFlag == 1){
			        return true;
			    }else{
			        return false;
			    }
			
			}, "此名称已存在,请重新输入！");
			
			validateForm = $("#inputForm").validate({
				rules: {
					mac:{
						checkMac: true
		            },
		            name:{
						checkName: true
		            }
				},

				
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
		<form:form id="inputForm" modelAttribute="washDevice" action="${ctx}/device/washDevice/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>mac：</label></td>
					<td class="width-35">
						<form:input path="mac" htmlEscape="false" maxlength="32"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>名称：</label></td>
					<td class="width-35">
						<form:input path="name" htmlEscape="false" maxlength="100"    class="form-control required"/>
					</td>
				</tr>
				<tr>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>地址：</label></td>
					<td class="width-35">
						<form:input path="address" htmlEscape="false" maxlength="200"    class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right"><font color="red">*</font>状态：</label></td>
					<td class="width-35">
						<form:select path="status" class="form-control required">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('device_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>