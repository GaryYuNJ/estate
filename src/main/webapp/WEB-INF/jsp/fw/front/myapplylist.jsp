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
			<i class="icon-list-alt"></i> 我申请的
		</h2>
		<!-- Breadcrumb -->
		<div class="bread-crumb pull-right">
			<a><i class="icon-list-alt"></i> 审批</a>
			<!-- Divider -->
			<span class="divider">/</span> <a href="#" class="bread-current">我申请的</a>
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
										<th>状态</th>
										<th>申请时间</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody>
									<tr v-for="item in list">
										<td>{{$index+1}}</td>
										<td>{{item.tablename}}</td>
										<td>{{item.state}}</td>
										<td>{{item.StartTime | time}}</td>
										<td><a class="btn btn-xs btn-success"
											href="fwinstancedetial?objId={{item.objId}}&piId={{item.piId}}&user={{user}}">
												<i class="icon-search"></i>查看
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
			url : rootUri + '/Form/shenpiApplyByUser.json',
			data : {
				user : user
			},
			success : function(data) {
				console.log(data);
				new Vue({
					el : '#main',
					data : {
						user : user,
						list : data
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