<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/common/header.jsp"%>
<%@ include file="/common/nav.jsp"%>
<!-- Main bar -->
<!-- Main bar -->
<div class="mainbar" id="main">

	<!-- Page heading -->
	<div class="page-head">
		<h2 class="pull-left">
			<i class="icon-home"></i> 首页
		</h2>

		<!-- Breadcrumb -->
		<div class="bread-crumb pull-right">
			<a href="index.html"><i class="icon-home"></i> 首页</a>
			<!-- Divider -->
			<span class="divider">/</span> <a href="#" class="bread-current">控制台</a>
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
							<div class="pull-left">设置审批人</div>
							<div class="widget-icons pull-right">
								<a href="#" class="wminimize"><i class="icon-chevron-up"></i></a>
								<a href="#" class="wclose"><i class="icon-remove"></i></a>
							</div>
							<div class="clearfix"></div>
						</div>
						<div class="widget-content">
							<div class="widget-foot">
								<a type="button" class="btn btn-success">提交</a>
								<div class="clearfix"></div>
							</div>
						</div>
					</div>
					<div class="widget">
						<div class="widget-head">
							<div class="pull-left">Tables</div>
							<div class="widget-icons pull-right">
								<a href="#" class="wminimize"><i class="icon-chevron-up"></i></a>
								<a href="#" class="wclose"><i class="icon-remove"></i></a>
							</div>
							<div class="clearfix"></div>
						</div>
						<div class="widget-content">
							<div class="widget-foot">
								<a type="button" class="btn btn-success">提交</a>
								<div class="clearfix"></div>
							</div>
						</div>
					</div>
					<div class="widget">
						<div class="widget-head">
							<div class="pull-left">通过xml创建流程</div>
							<div class="widget-icons pull-right">
								<a href="#" class="wminimize"><i class="icon-chevron-up"></i></a>
							</div>
							<div class="clearfix"></div>
						</div>
						<div class="widget-content">
							<div class="padd">
								<h6>请输入xml字符串：</h6>
								<hr />
								<textarea class="form-control" rows="10" placeholder="xml"
									v-model="xmlStr"></textarea>
							</div>
							<div class="widget-foot">
								<a type="submit" class="btn btn-success" onclick="submit_xml()">提交</a>
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
</div>

<%@ include file="/common/script.jsp"%>
<%@ include file="/common/footer.html"%>
<!-- Script for this page -->
<script
	src="http://cdnjs.cloudflare.com/ajax/libs/vue/1.0.26/vue.min.js"></script>
<script type="text/javascript">
	var submit_xml = function() {
		$.ajax({
			type : 'POST',
			dataType : 'Json',
			url : rootUri + '/Form/newWorkFlow.json',
			data : {
				xmlStr : vue.xmlStr
			},
			success : function(data) {
				console.log(data);
			},
			error : function(err) {
				console.log(err);
			}
		});
	};
	var vue;
	$(function() {
		vue = new Vue({
			el : '#main',
			data : {
				xmlStr : ''
			}
		});
	});
</script>
