<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/common/header.jsp"%>
<%@ include file="/common/nav.jsp"%>
<!-- Main bar -->
<div id="main" class="mainbar" style="background: none;">

	<!-- Page heading -->
	<div class="page-head">
		<h2 class="pull-left">
			<i class="icon-list-alt"></i> 新建审批
		</h2>

		<!-- Breadcrumb -->
		<div class="bread-crumb pull-right">
			<a href="index.html"><i class="icon-list-alt"></i> 审批管理</a>
			<!-- Divider -->
			<span class="divider">/</span> <a href="#" class="bread-current">新建审批</a>
		</div>

		<div class="clearfix"></div>

	</div>
	<!-- Page heading ends -->

	<!-- Matter -->

	<div class="matter">
		<div class="container">
			<ul id="myTab" class="nav nav-tabs">
				<li class="active"><a href="#approlSet" data-toggle="tab">审批设置</a></li>
				<li><a href="#formSet" data-toggle="tab">表单编辑</a></li>
			</ul>
			<div id="myTabContent" class="tab-content">
				<div class="tab-pane fade in active" id="approlSet">
					<label style="margin-top: 30px;" class="col-lg-6 control-label">审批名称：</label>
					<div class="col-lg-8">
						<input id="shenpiName" type="text" class="form-control"
							placeholder="请输入审批名称">
					</div>
					<label style="margin-top: 30px;" class="col-lg-6 control-label">审批描述：</label>
					<div class="col-lg-8">
						<input type="text" id="shenpiDEC" class="form-control"
							placeholder="请输入审批描述">
					</div>
					<input type=hidden id="hideFormID">
				</div>
				<div id="formSet" class="fb-main tab-pane fade"></div>
			</div>
		</div>
	</div>

	<!-- Matter ends -->

</div>
<!-- Mainbar ends -->
<div class="clearfix"></div>
</div>

<%@ include file="/common/script.jsp"%>
<!-- Script for this page -->
<script src="<c:url value="/vendor/js/vendor.js" />"></script>
<script src="<c:url value="/dist/formbuilder.js" />"></script>

<script type="text/javascript">
	$(function() {
		var id = getQueryStringByName("id");
		if (id) {
			$.ajax({
				type : 'POST',
				dataType : 'Json',
				url : rootUri + '/Form/queryFoemDefine.json',
				data : {
					id : id
				},
				success : function(data) {
					console.log(data);
					$("#hideFormID").val(data.id);
					$("#shenpiName").val(data.tableName);
					$("#shenpiDEC").val(data.description);
					var fields = eval('(' + data.fieldsJson + ')');
					console.log(fields);
					fb = new Formbuilder({
						selector : '.fb-main',
						bootstrapData : fields.fields
					});
				},
				error : function(err) {
					fb = new Formbuilder({
						selector : '.fb-main'
					});
				}
			});
		} else {
			fb = new Formbuilder({
				selector : '.fb-main'
			});
		}
	});
</script>
<%@ include file="/common/footer.html"%>