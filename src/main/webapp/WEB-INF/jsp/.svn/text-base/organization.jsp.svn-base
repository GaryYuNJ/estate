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
			<i class="icon-home"></i> 组织结构管理
		</h2>
		<!-- Breadcrumb -->
		<div class="bread-crumb pull-right">
			<a href="index.html"><i class="icon-home"></i> 组织结构管理</a>
			<!-- Divider -->
			<span class="divider">/</span> <a href="#" class="bread-current">组织结构列表</a>
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
							<div class="pull-left">组织结构列表</div>
							<div class="widget-icons pull-right">
								<a href="#" class="wminimize" id="icon_group_list1"><i
									class="icon-chevron-up"></i></a>
							</div>
							<div class="clearfix"></div>
						</div>
						<div class="widget-content">
							<div class="col-lg-5">
								<hr>
								<button type="button" class="btn btn-primary"
									onclick="nodeCreatePre()">
									<i class="icon-plus"></i> 添加
								</button>
								<!-- <button type="button" class="btn btn-warning"
									onclick="nodeRename()">
									<i class="icon-pencil"></i> 修改
								</button> -->
								<button type="button" class="btn btn-danger"
									onclick="nodeDelete()" id="nodeDeleteBId">
									<i class="icon-remove"></i> 删除
								</button>
								<hr>
								<div class="widget treeMinHeight" id="jstree_org"></div>
							</div>
							<div class="col-lg-7">
								<hr>
								<form class="form-horizontal" role="form" id="orgFormId">
									<div class="form-group">
												<label class="col-lg-2 control-label" style="width: 100px;">名称</label>
												<div class="col-lg-8">
													<input type="text" class="form-control" placeholder="名称"
														name="name" id="orgNameId">
												</div></div>
												<div class="form-group">
												<label class="col-lg-2 control-label" style="width: 100px;">电话</label>
												<div class="col-lg-8">
													<input type="text" class="form-control" placeholder="电话"
														name="phone" id="orgPhoneId">
												</div>
											</div>
									<div class="form-group">
										<label class="col-lg-2 control-label" style="width: 100px;">地址</label>
										<div class="col-lg-8">
											<input type="text" class="form-control" placeholder="地址"
														name="address" id="orgAddressId">
										</div>
									</div>
									<input type="hidden" name="id" id="orgId">
									<input type="hidden" name="pid" id="orgpId">
								</form>
								<div class="center">
									<button type="button" class="btn btn-primary"
										id="saveOrgbuttonId">保存</button>
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
	<%@ include file="/common/script.jsp"%>
	<%@ include file="/common/footer.html"%>
	<script src="<c:url value="/js/organization.js" />"></script>