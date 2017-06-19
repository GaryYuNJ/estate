<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<!-- Title and other stuffs -->
<title>表单审批流引擎系统</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="author" content="">
<!-- Stylesheets -->
<link href="<c:url value="/style/bootstrap.css" />" rel="stylesheet">
<!-- Font awesome icon -->
<link rel="stylesheet" href="<c:url value="/style/font-awesome.css" />">
<!-- Date picker -->
<link rel="stylesheet"
	href="<c:url value="/style/bootstrap-datetimepicker.min.css" />">
<!-- Bootstrap toggle -->
<link href="<c:url value="/style/style.css" />" rel="stylesheet">
<link href="<c:url value="/js/themes/default/style.min.css" />"
	rel="stylesheet">
<link href="<c:url value="/style/bootstrap-table.min.css" />"
	rel="stylesheet">
<link href="<c:url value="/style/bootstrap-switch.css" />"
	rel="stylesheet">
<link href="<c:url value="/style/bootstrapValidator.min.css" />"
	rel="stylesheet">
<link href="<c:url value="/style/fileinput.min.css" />" rel="stylesheet">

<link href="<c:url value="/vendor/css/vendor.css" />" rel="stylesheet">
<link href="<c:url value="/dist/formbuilder.css" />" rel="stylesheet">

<!-- HTML5 Support for IE -->
<!--[if lt IE 9]>
  <script src="js/html5shim.js"></script>
  <![endif]-->
<!-- Favicon -->
<link rel="shortcut icon"
	href="<c:url value="/img/favicon/favicon.png" />">
<c:set var="rootUri" value="${pageContext.request.contextPath}" />
<script type="text/javascript">
	var rootUri = "${rootUri}";
</script>
</head>

<body>
	<!-- Header starts -->
	<header>
		<div class="container">
			<div class="row">
				<div class="col-md-4">
					<div class="login-header"></div>
				</div>
				<div class="col-md-4" style="padding-top: 10px">
					<h1>表单审批流引擎</h1>
				</div>
				<ul class="nav navbar-nav pull-right">
					<li class="dropdown pull-right"><a data-toggle="dropdown"
						class="dropdown-toggle" href="#"> <i class="icon-user"></i> ${ sessionScope.user.name}
							<b class="caret"></b>
					</a> <!-- Dropdown menu -->
						<ul class="dropdown-menu">
							<!-- <li><a href="#"><i class="icon-user"></i> 资料</a></li> -->
							<li><a data-toggle="modal" href="#changePasswordModal"><i
									class="icon-cogs"></i> 密码修改</a></li>
							<li><a href="<c:url value="/logOut" />"><i
									class="icon-off"></i> 退出</a></li>
						</ul></li>
				</ul>
			</div>
	</header>
	<!-- Header ends -->

	<div class="modal fade" id="changePasswordModal" tabindex="-1"
		role="dialog" aria-labelledby="changePasswordModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="changePasswordModalLabel">修改密码</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form" id="changePasswordForm">
						<div class="form-group">
							<label class="col-lg-3 control-label" style="width: 120px">当前密码</label>
							<div class="col-lg-4">
								<input type="password" class="form-control" placeholder="当前密码"
									name="curPassword" id="curPassword">
							</div>
						</div>
						<div class="form-group">
							<label class="col-lg-3 control-label" style="width: 120px">新密码</label>
							<div class="col-lg-4">
								<input type="password" class="form-control" placeholder="新密码"
									name="newPassword" id="newPassword">
							</div>
						</div>
						<div class="form-group">
							<label class="col-lg-3 control-label" style="width: 120px">确认新密码</label>
							<div class="col-lg-4">
								<input type="password" class="form-control"
									placeholder="再输入一次新密码" name="newPasswordConfirm"
									id="newPasswordCommit">
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal"
						id="closechangePasswordModal">关闭</button>
					<button type="button" class="btn btn-primary"
						onclick="changePassword();">保存</button>
				</div>
			</div>
		</div>
	</div>

	<script>
		//更新用户与资源组权限
		function changePassword() {
			//定义参数  
			var array = {};
			//遍历form 组装json  
			$.each($("#changePasswordForm").serializeArray(),
					function(i, field) {
						//可以添加提交验证  
						array[field.name] = field.value;
					});

			//参数转为json字符串，并赋给变量 ,JSON.stringify <ie7不支持，有第三方解决插件  
			//var modelJsonStr = JSON.stringify(array);

			$.ajax({
				url : "<c:url value='/manage/changePassword.json' />",
				data : array,
				type : 'get',
				cache : false,
				dataType : 'json',
				success : function(data) {
					if (data.status == 1) {
						alert("密码重置成功！");
						$("#closechangePasswordModal").click();
					} else if (data.status == 0) {
						alert("密码更新失败！");
					} else if (data.status == -1) {
						alert("当前密码不可为空！");
					} else if (data.status == -2) {
						alert("新密码不可为空！");
					} else if (data.status == -3) {
						alert("新密码与确认密码不匹配！");
					} else if (data.status == -4) {
						alert("用户未登陆！");
					} else if (data.status == -5) {
						alert("当前密码错误！");
					}
				},
				error : function() {
					alert("系统异常！");
				}
			});
		}
	</script>