<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp"%>
<html>
<head>
	<title>内部车牌管理</title>
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
			jQuery.validator.addMethod("checkCarNumber", function(value, element) {
				
				var checkFlag = 1;
				$.ajax({
		 			url:"${ctx}/member/washPlateNumber/checkCarNumber",
		 			dataType:'json',
		 			async:false,
		 			data:{
		 				"carNumber":$('#carNumber').val(),
		 				"oldCarNumber":"${washPlateNumber.carNumber}"
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
			
			}, "此车牌已存在,请重新输入！");
			
			validateForm = $("#inputForm").validate({
				rules: {
					carNumber:{
						checkCarNumber: true
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
		<form:form id="inputForm" modelAttribute="washPlateNumber" action="${ctx}/member/washPlateNumber/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>	
		<table class="table table-bordered  table-condensed dataTables-example dataTable no-footer">
		   <tbody>
				<tr>
					<td class="width-15 active"><label class="pull-right">车牌号：</label></td>
					<td class="width-35">
						<form:input path="carNumber" htmlEscape="false" maxlength="10"  minlength="1"   class="form-control required"/>
					</td>
					<td class="width-15 active"><label class="pull-right">内部费用：</label></td>
					<td class="width-35">
						<form:input path="price" htmlEscape="false" maxlength="10"  minlength="1" min="0.01"  class="form-control required number"/>
					</td>
				</tr>
		 	</tbody>
		</table>
	</form:form>
</body>
</html>