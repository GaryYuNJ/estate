<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="uft-8">
<head>
<meta charset="utf-8">
<!-- Title and other stuffs -->
<title>后台登陆页面</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="author" content="">
<!-- Stylesheets -->
<link href="<c:url value="/style/bootstrap.css" />" rel="stylesheet">
<link rel="stylesheet" href="<c:url value="/style/font-awesome.css" />">
<link href="<c:url value="/style/style.css" />" rel="stylesheet">
<!-- HTML5 Support for IE -->
<!--[if lt IE 9]>
  <script src="js/html5shim.js"></script>
  <![endif]-->
<!-- Favicon -->
<link rel="shortcut icon"
	href="<c:url value="/img/favicon/favicon.png" />">
</head>
<body>
	<div class="login-header"></div>
	<!-- Form area -->
	<div class="login-page">
		<div class="admin-form">
			<div class="container">
				<div class="row">
					<div class="col-md-12">
						<!-- Widget starts -->
						<div class="widget worange">
							<!-- Widget head -->
							<div class="widget-head">
								<i class="icon-lock"></i> 登录
							</div>
							<div class="widget-content">
								<div class="padd">
									<!-- Login form -->
									<form class="form-horizontal" method="post"
										action="<c:url value="/login" />">
										<!-- Email -->
										<div class="form-group">
											<label class="control-label col-lg-3" style="width: 100px;" for="userName">用戶名</label>
											<div class="col-lg-9">
												<input type="text" class="form-control" id="userNameId"
													name="userName" placeholder="用戶名">
											</div>
										</div>
										<!-- Password -->
										<div class="form-group">
											<label class="control-label col-lg-3" style="width: 100px;" for="inputPassword">密码</label>
											<div class="col-lg-9">
												<input type="password" class="form-control"
													id="inputPassword" name="password" placeholder="密码">
											</div>
										</div>
										<!-- Remember me checkbox and sign in button -->
										<div class="form-group"></div>
										<div class="col-lg-9 col-lg-offset-2">
											<button type="submit" class="btn btn-danger login-button">登录</button>
										</div>
										<br />
									</form>

								</div>
							</div>

							<div class="widget-foot">为保证后台使用体验流畅，推荐在以下浏览器上运行：IE9、IE10、Firefox、chrome，其他浏览器不推荐使用。
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="login-footer">物业云© 版权所有</div>
	<!-- JS -->
	<script src="<c:url value="/js/jquery.js" />"></script>
	<script src="<c:url value="/js/bootstrap.js" />"></script>

	<script>
		if ("${userVerifyResult}" == 'false') {
			alert("用户名密码错误！");
		}

	</script>
</body>
</html>