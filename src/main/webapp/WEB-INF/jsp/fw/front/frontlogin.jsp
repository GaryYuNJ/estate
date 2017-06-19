<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="utf-8">
<!-- Title and other stuffs -->
<title>登陆页面</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0 ,user-scalable=no">
<meta name="author" content="">
<!-- Stylesheets -->

<link href="<c:url value="/style/bootstrap.css" />" rel="stylesheet">
<link href="<c:url value="/style/font-awesome.css" />" rel="stylesheet">
<link href="<c:url value="/style/style.css" />" rel="stylesheet">

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

	<!-- Form area -->
	<div class="admin-form">
		<div class="container">

			<div class="row">
				<div class="col-md-12">
					<!-- Widget starts -->
					<div class="widget worange">
						<!-- Widget head -->
						<div class="widget-head">
							<i class="icon-lock"></i> Login
						</div>

						<div class="widget-content">
							<div class="padd">
								<!-- Login form -->
								<form class="form-horizontal">
									<!-- Email 
									<div class="form-group">
										<label class="control-label col-lg-3" for="inputEmail">账号</label>
										<div class="col-lg-9">
											<input type="text" class="form-control" id="inputEmail"
												placeholder="账号">
										</div>
									</div>-->
									<div class="form-group">
										<label class="col-lg-4 control-label" style="width: 100px;">用户</label>
										<div class="col-lg-8">
											<select onchange="changeUserName()" class="form-control"
												id="username">
												<option>admin</option>
												<option>CEO</option>
												<option>Manager1</option>
												<option>Manager2</option>
												<option>PM1</option>
												<option>PM2</option>
												<option>PM3</option>
												<option>employee1</option>
												<option>employee2</option>
												<option>employee3</option>
												<option>employee4</option>
												<option>employee5</option>
												<option>employee6</option>
												<option>employee7</option>
											</select>
										</div>
									</div>
									<!-- Password 
									<div class="form-group">
										<label class="control-label col-lg-3" for="inputPassword">密码</label>
										<div class="col-lg-9">
											<input type="password" class="form-control"
												id="inputPassword" placeholder="密码">
										</div>
									</div>-->
									<!-- Remember me checkbox and sign in button 
									<div class="form-group">
										<div class="col-lg-9 col-lg-offset-3">
											<div class="checkbox">
												<label> <input type="checkbox"> Remember me
												</label>
											</div>
										</div>
									</div>-->
									<div class="col-lg-9 col-lg-offset-2">
										<a class="btn btn-danger" id="denglu">登录</a>
										<!--<button type="reset" class="btn btn-default">Reset</button>-->
									</div>
									<br />
								</form>

							</div>
						</div>

						<div class="widget-foot">
							<!--Not Registred? <a href="#">Register here</a>-->
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<script src="<c:url value="/js/jquery.js" />"></script>
	<script src="<c:url value="/js/bootstrap.js" />"></script>
	<script>
		$(function() {
			$.ajax({
				type : 'GET',
				dataType : 'Json',
				url : rootUri + '/buser/buserSearchAll.json',
				success : function(data) {
					console.log(data);
					var tempOptionsHtml = "<option value＝''>请选择用户</option>";
					for (var i = 0; i < data.length; i++) {
						tempOptionsHtml += "<option>" + data[i].loginid
								+ "</option>";
					}
					$("#username").html(tempOptionsHtml);
				},
				error : function(err) {
					console.log(err);
				}
			});
			changeUserName();
		});

		var changeUserName = function() {
			var username = $("#username").val();
			$("#denglu").attr("href",
					"/property/fwpages/ffwlist?user=" + username);
		};
	</script>
</body>

</html>