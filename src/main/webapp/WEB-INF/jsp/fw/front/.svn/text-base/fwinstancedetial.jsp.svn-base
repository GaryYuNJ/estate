<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@include file="/common/front-header.jsp"%>
<%@include file="/common/front-nav.jsp"%>

<!-- Main bar -->
<div class="mainbar">

	<!-- Page heading -->
	<div class="page-head">
		<h2 class="pull-left">
			<i class="icon-list-alt"></i> {{data.tableName}}
		</h2>

		<!-- Breadcrumb -->
		<div class="bread-crumb pull-right">
			<a><i class="icon-list-alt"></i> 表单详情</a>
			<!-- Divider -->
			<span class="divider">/</span> <a href="#" class="bread-current">{{data.tableName}}</a>
		</div>

		<div class="clearfix"></div>

	</div>
	<!-- Page heading ends -->

	<!-- Matter -->

	<div class="matter">
		<div class="container">
			<div class="row">
				<div class="col-md-12">
					<div class="widget wred">
						<div class="widget-head">
							<div class="pull-left">{{data.tableName}}</div>
							<div class="widget-icons pull-right">
								<a href="#" class="wminimize"><i class="icon-chevron-up"></i></a>
							</div>
							<div class="clearfix"></div>
						</div>
						<div class="widget-content">
							<div class="padd">
								<!-- Profile form -->
								<div class="form profile">
									<form class="form-horizontal" style="display: none;">
										<!-- Edit profile form (not working)-->
										<%@include file="/common/form-workflow/form-render.jsp"%>
									</form>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="col-md-12">
					<div class="widget">
						<div class="widget-head">
							<div class="pull-left">审批记录</div>
							<div class="widget-icons pull-right">
								<a href="#" class="wminimize"><i class="icon-chevron-up"></i></a>
							</div>
							<div class="clearfix"></div>
						</div>
						<div class="widget-content">
							<%@include file="/common/form-workflow/workflow-comments-his.jsp"%>
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
<script src="<c:url value="/js/form-render.js" />"></script>
<script type="text/javascript">
	var vue;

	var user = decodeURI(getQueryStringByName("user"))
	var objId = getQueryStringByName("objId");
	var taskid = getQueryStringByName("taskid");
	var piId = getQueryStringByName("piId");

	$(function() {
		vue = new Vue({
			el : '#main',
			data : {
				user : user,
				commentsList : {},
				State : "",
				remark : "",
				tableName : "",
				data : {},
				values : {}
			}
		});

		if (objId) {
			$
					.ajax({
						type : 'POST',
						dataType : 'Json',
						async : false,
						url : rootUri
								+ '/FormFlowBase/getInstanceDetialByBusinessKey.json',
						data : {
							objId : objId,
							processinstanceid : piId,
							user : user
						},
						success : function(data) {
							console.log(data);
							
							for (var i = 0; i < data.fieldsJson.length; i++) {
								data.fieldsJson[i].isDisable = true;
								if (data.fieldsJson[i].field_type == 'table') {
									data.fieldsJson[i].value = eval('(' + data.fieldsJson[i].value + ')');
								}
								if (data.fieldsJson[i].field_type == 'linkdropdown') {
									$
											.ajax({
												type : 'GET',
												dataType : 'Json',
												async : false,
												url : data.fieldsJson[i].field_options.options[0].label,//rootUri + '/dropdowndata/getProvince.json',
												success : function(data1) {
													data.fieldsJson[i].field_options.options[0].datalist = data1;
													for (j = 1; j < data.fieldsJson[i].field_options.options.length; j++) {
														data.fieldsJson[i].field_options.options[j].datalist = [ {
															key : data.fieldsJson[i].field_options.options[j].selectKey,
															value : data.fieldsJson[i].field_options.options[j].selectValue
														} ]
													}
												},
												error : function(err) {
													console.log(err);
													alert("系统出错！");
													return;
												}
											});
								}
							}

							vue.data = data;

							$(".form_datetime").datetimepicker({
								format : 'yyyy-mm-dd hh:ii',
								autoclose : true
							});

							$(".form-horizontal").show();
						},
						error : function(err) {
							console.log(err);
							alert("系统出错！")
						}
					});
		} else {
			alert("objId不存在");
		}

		if (piId) {
			$.ajax({
				type : 'POST',
				dataType : 'Json',
				url : rootUri + '/Form/getHisByProcessInstaceId.json',
				data : {
					piId : piId
				},
				success : function(data) {
					console.log(data);
					vue.commentsList = data.commentsList.reverse();
					vue.State = data.State;
				},
				error : function(err) {
					console.log(err);
					alert("系统出错！")
				}
			});
		} else {
			alert("piId不存在");
		}
		
		vue
				.$nextTick(function() {
					for (var i = 0; i < this.data.fieldsJson.length; i++) {
						if (this.data.fieldsJson[i].field_type == 'timespend') {
							$('#countdown_' + i).parent().parent().parent().hide();
						}
					}
				});
	});
</script>
<%@ include file="/common/footer.html"%>