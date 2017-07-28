<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>个人信息</title>
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
		
		// 手机号码验证
		jQuery.validator.addMethod("isMobile", function(value, element) {
		    var length = value.length;
		    var mobile = /^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$/;
		    return this.optional(element) || (length == 11 && mobile.test(value));
		}, "请正确填写您的手机号码");
		
		$(document).ready(function() {
			$("#no").focus();
			validateForm = $("#inputForm").validate({
				rules: {
					mobile:{isMobile:true, minlength : 11,remote: "${ctx}/sys/user/checkMobile?oldMobile=${user.mobile}"}
				},
				messages: {
					mobile: {remote: "手机号已存在"},
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

			//在ready函数中预先调用一次远程校验函数，是一个无奈的回避案。(刘高峰）
			//否则打开修改对话框，不做任何更改直接submit,这时再触发远程校验，耗时较长，
			//submit函数在等待远程校验结果然后再提交，而layer对话框不会阻塞会直接关闭同时会销毁表单，因此submit没有提交就被销毁了导致提交表单失败。
			$("#inputForm").validate().element($("#mobile"));
		});

	
	</script>
</head>
<body>
	<form:form id="inputForm" modelAttribute="user" action="${ctx}/sys/user/infoEdit"  method="post" class="form-horizontal form-group">
		<div class="control-group">
		</div>
		<div class="control-group">
			<label class="col-sm-3 control-label">姓名:</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="50"  class="form-control  max-width-250 required" />
			</div>
		</div>
		<div class="control-group">
			<label class="col-sm-3 control-label">邮箱:</label>
			<div class="controls">
				<form:input path="email" htmlEscape="false" maxlength="50" class="form-control  max-width-250 email"/>
			</div>
		</div>
		<div class="control-group">
			<label class="col-sm-3 control-label">电话:</label>
			<div class="controls">
				<form:input path="phone" htmlEscape="false" class="form-control  max-width-250 " maxlength="50"/>
			</div>
		</div>
		<div class="control-group">
			<label class="col-sm-3 control-label">手机:</label>
			<div class="controls">
				<form:input path="mobile" class="form-control  max-width-250 required" htmlEscape="false" maxlength="50"/>
			</div>
		</div>
		<div class="control-group">
			<label class="col-sm-3 control-label">备注:</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="form-control  max-width-250 "/>
			</div>
		</div>
	</form:form>
</body>
</html>