<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="/common/front-header.jsp"%>
<%@ include file="/common/front-nav.jsp"%>

<!-- Main bar -->
<div class="mainbar">

	<!-- Page heading -->
	<div class="page-head">
		<h2 class="pull-left">
			<i class="icon-list-alt"></i> 审批列表
		</h2>
		<!-- Breadcrumb -->
		<div class="bread-crumb pull-right">
			<a><i class="icon-list-alt"></i> 审批管理</a>
			<!-- Divider -->
			<span class="divider">/</span> <a href="#" class="bread-current">审批列表</a>
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
							<div class="pull-left">审批列表</div>
							<div class="widget-icons pull-right">
								<a href="#" class="wminimize"><i class="icon-chevron-up"></i></a>
							</div>
							<div class="clearfix"></div>
						</div>
						<div class="widget-content">
							<table class="table table-striped table-bordered table-hover">
								<thead>
									<tr>
										<th>#编号</th>
										<th>审批名称</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody>
									<tr v-for="item in list">
										<td>{{item.id}}</td>
										<td>{{item.tableName}}</td>
										<td><a class="btn btn-xs btn-warning"
											href="newfwinstance?id={{item.id}}&user={{user}}"> <i
												class="icon-pencil">新建审批</i>
										</a></td>
									</tr>
								</tbody>
							</table>

							<div class="widget-foot">
								<!-- <ul class="pagination pull-right">
											<li><a href="#">Prev</a></li>
											<li><a href="#">1</a></li>
											<li><a href="#">2</a></li>
											<li><a href="#">3</a></li>
											<li><a href="#">4</a></li>
											<li><a href="#">Next</a></li>
										</ul> -->
								<div class="clearfix"></div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- Matter ends -->
</div>
<!-- Mainbar ends -->
<div class="clearfix"></div>

<%@ include file="/common/front-script.jsp"%>

<script type="text/javascript">
	var user = decodeURI(getQueryStringByName("user"))

	$(function() {
		$.ajax({
			type : 'POST',
			dataType : 'Json',
			url : rootUri + '/Form/queryformlist.json',
			success : function(data) {
				console.log(data);
				var list = [];
				for (i = 0; i < data.length; i++) {
					if (data[i].state == 2)
						list.push(data[i]);
				}
				new Vue({
					el : '#main',
					data : {
						user : user,
						list : list
					}
				});
			},
			error : function(err) {
				new Vue({
					el : '#main'
				});
				console.log(err);
			}
		});
	});
</script>
<%@ include file="/common/footer.html"%>