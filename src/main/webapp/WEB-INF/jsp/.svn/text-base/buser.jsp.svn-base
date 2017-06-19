<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/common/header.jsp"%>
<%@ include file="/common/nav.jsp"%>
<!-- Main bar -->
<div class="mainbar">
	<!-- Page heading -->
	<div class="page-head">
		<h2 class="pull-left">
			<i class="icon-home"></i> 用户管理页面
		</h2>
		<!-- Breadcrumb -->
		<div class="bread-crumb pull-right">
			<a href="index.html"><i class="icon-home"></i> 用户管理页面</a>
			<!-- Divider -->
			<span class="divider">/</span> <a href="#" class="bread-current">用户列表</a>
		</div>
		<div class="clearfix"></div>
	</div>
	<!-- Page heading ends -->

	<!-- Matter -->
	<div class="matter">
		<div class="container">
			<div class="row">
				<div class="col-md-12">
					<div class="widget">
						<div class="widget-head">
							<div class="pull-left">用户列表</div>
							<div class="widget-icons pull-right">
								<a href="#" class="wminimize" id="icon_group_list1"><i
									class="icon-chevron-up"></i></a>
							</div>
							<div class="clearfix"></div>
						</div>
						<div class="widget-content">
							<div class="col-lg-3">
								<hr>
								<div class="widget treeMinHeight" id="jstree_org"></div>
							</div>
							<div class="col-lg-9">
								<hr>
								<form class="form-horizontal" role="form" id="searchform">
									<div class="form-group">
										<div class="col-lg-2">
											<input type="text" class="form-control" placeholder="登录账号"
												name="loginid">
										</div>
										<div class="col-lg-2">
											<button type="button" class="btn btn-primary" id="dosearch">
												<i class="icon-search"></i> 查询
											</button>
										</div>
										<div class="col-lg-2">
											<button type="button" class="btn btn-primary"
												id="addBuserButton" data-toggle="modal" data-target="#myModal">
												<i class="icon-plus"></i> 新增用户
											</button>
										</div>
									</div>
									<input type="hidden" name="orid" id="queryOrgId">
								</form>
								<table class="table table-striped table-bordered table-hover"
									id="buserTableId">
								</table>
							</div>
							<div class="clearfix"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- Matter ends -->
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
			<div class="modal-dialog" role="document"  style="width:1000px"> 
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						<h4 class="modal-title" id="myModalLabel">用户详情</h4>
					</div>
					<div class="modal-body">
					<div class="widget">
						<div class="widget-content"
							id="groupGroupDetailsTable">
							<ul class="nav nav-tabs" id="myTab">
								<li class="active"><a href="#home">用户详情</a></li>
							</ul>
							<div class="tab-content">
								<div class="tab-pane active" id="home">
									<div class="col-lg-3">
										<div class="widget treeMinHeight" id="jstree_org_buser"></div>
									</div>
									<div class="col-lg-9">
										<form class="form-horizontal" role="form"
											id="userFormId">
											<div class="form-group">
												<label class="col-lg-2 control-label" style="width: 100px;">登陆名称</label>
												<div class="col-lg-4">
													<input type="text" class="form-control" placeholder="登陆名称"
														name="loginid" id="loginId">
												</div>
												<label class="col-lg-2 control-label" style="width: 100px;">显示名称</label>
												<div class="col-lg-4">
													<input type="text" class="form-control" placeholder="1000"
														name="name" id="nameId">
												</div>
											</div>
											<div class="form-group">
												<label class="col-lg-2 control-label"  style="width: 100px;">联系电话</label>
												<div class="col-lg-4">
													<input type="text" class="form-control" placeholder="1000"
														name="phone" id="phoneId">
												</div>
												<label class="col-lg-2 control-label"  style="width: 100px;">电子邮件</label>
												<div class="col-lg-4">
													<input type="text" class="form-control"
														placeholder="xx@xx.com" name="email" id="emailId">
												</div>
											</div>
											<div class="form-group">
												<label class="col-lg-2 control-label" style="width: 100px;">可用状态</label>
												<div class="col-lg-4">
													<div id="statusId" class="make-switch" data-on="success" data-off="warning" 
														data-on-label="启用" data-off-label="禁用" >
														<input type="checkbox"  name="status" id="statusId2">
													</div>
												</div>
												<label class="col-lg-2 control-label"  style="width: 100px;">是否主管</label>
												<div class="col-lg-4">
													<div id="typeId" class="make-switch" data-on="success" data-off="warning" 
														data-on-label="主管" data-off-label="成员" >
														<input type="checkbox" name="type" id="typeId2">
													</div>
												</div>
											</div>
											<input type="hidden" name="id" id="userId">
											<input type="hidden" name="orid" id="orid">
										</form>
										<hr>
										<form class="form-horizontal" role="form"
											id="ResourceKeyFormId">
											
										</form>
									<div class="center">
									<button type="button" class="btn btn-primary"
										id="BuserButtonId" onclick="saveUser()">保存</button>
										</div>
									</div>
								</div>
							</div>
							<div class="clearfix"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- Mainbar ends -->
<div class="clearfix"></div>
</div>

<%@ include file="/common/script.jsp"%>
<script src="<c:url value="/js/buser.js" />"></script>
<%@ include file="/common/footer.html"%>

