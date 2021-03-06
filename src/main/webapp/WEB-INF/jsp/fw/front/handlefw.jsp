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
			<i class="icon-list-alt"></i> {{data.tableName}}
		</h2>

		<!-- Breadcrumb -->
		<div class="bread-crumb pull-right">
			<a><i class="icon-list-alt"></i> 审批</a>
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
									<!-- Edit profile form (not working)-->
									<form class="form-horizontal" style="display: none;">
										<div class="form-group">
											<div>
												<label class="control-label col-lg-3" for="name1">申请人</label>
												<div class="col-lg-6">
													<input type="text" class="form-control"
														v-model="data.inputUser" disabled>
												</div>
											</div>
										</div>
										<%@include file="/common/form-workflow/form-render.jsp"%>
										<div class="form-group">
											<label class="control-label col-lg-3" for="address">批注</label>
											<div class="col-lg-6">
												<textarea class="form-control" v-model="remark"></textarea>
											</div>
										</div>
										<!-- Buttons -->
										<div class="form-group">
											<div>
												<!-- Buttons -->
												<div class="col-lg-6 col-lg-offset-1">
													<a type="submit" class="btn btn-success"
														onclick="submitForm()">同意</a> <a class="btn btn-success"
														href="#myModal2" data-toggle="modal">改签</a>
													<div class="btn-group" style="margin-left: 20px;">
														<button class="btn btn-default dropdown-toggle"
															data-toggle="dropdown">
															驳回 <span class="caret"></span>
														</button>
														<ul class="dropdown-menu">
															<li><a onclick="rollBackWorkFlow()">驳回到上一步</a></li>
															<li><a onclick="rollBackToAssignWorkFlow()">驳回到提交人</a></li>
														</ul>
													</div>
												</div>
											</div>
										</div>
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

<div id="myModal2" class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">×</button>
				<h4 class="modal-title">选择审批人</h4>
			</div>
			<div class="modal-body">
				<label class="control-label col-lg-3" for="name1">审批人</label>
				<div class="col-lg-6">
					<input type="text" class="form-control" id="transferUser">
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal"
					aria-hidden="true">取消</button>
				<button type="button" onclick="transferAssignee()"
					class="btn btn-primary">确定</button>
			</div>
		</div>
	</div>
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

	//同意
	var submitForm = function() {
		var dataUpload = {
			'taskid' : taskid,
			'remark' : vue.remark,
			'user' : decodeURI(user),
			'form' : JSON.stringify(vue.data.fieldsJson),
			'objId' : objId,
			'isReSubmit' : false 
		};
		console.log(dataUpload);
		$.ajax({
			type : 'POST',
			dataType : 'Json',
			url : rootUri + '/FormFlowBase/pushWorkFlow.json',
			data : dataUpload,
			success : function(data) {
				window.location.href = rootUri + '/fwpages/ffwlist?user='
						+ user;
			},
			error : function(err) {
				console.log(err);
			}
		});
	};

	var transferAssignee = function() {
		var dataUpload = {
			'taskid' : taskid,
			'transferUser' : $("#transferUser").val()
		};
		console.log(dataUpload);
		$.ajax({
			type : 'POST',
			dataType : 'Json',
			url : rootUri + '/FormFlowBase/transferAssignee.json',
			data : dataUpload,
			success : function(data) {
				console.log(data);
				window.location.href = rootUri + '/fwpages/ffwlist?user='
						+ user;
			},
			error : function(err) {
				console.log(err);
			}
		});
	};

	//驳回到发起者
	var rollBackToAssignWorkFlow = function() {
		if (!vue.remark) {
			alert("请填写批注");
			return;
		}
		var dataUpload = {
			'taskid' : taskid,
			'user' : user,
			'remark' : vue.remark
		};
		console.log(dataUpload);
		$.ajax({
			type : 'POST',
			dataType : 'Json',
			url : rootUri + '/Form/rollBackToAssignWorkFlow.json',
			data : dataUpload,
			success : function(data) {
				console.log(data);
				window.location.href = rootUri + '/fwpages/ffwlist?user='
						+ user;
			},
			error : function(err) {
				console.log(err);
			}
		});
	};

	//退回到上一步
	var rollBackWorkFlow = function() {
		if (!vue.remark) {
			alert("请填写批注");
			return;
		}
		var dataUpload = {
			'taskid' : taskid,
			'user' : user,
			'remark' : vue.remark
		};
		console.log(dataUpload);
		$.ajax({
			type : 'POST',
			dataType : 'Json',
			url : rootUri + '/Form/rollBackWorkFlow.json',
			data : dataUpload,
			success : function(data) {
				console.log(data);
				window.location.href = rootUri + '/fwpages/ffwlist?user='
						+ user;
			},
			error : function(err) {
				console.log(err);
			}
		});
	};

	$(function() {
		vue = new Vue(
				{
					el : '#main',
					data : {
						user : user,
						commentsList : {},
						State : "",
						tableName : "",
						data : {},
						values : {},
						remark : "",
						transferUser : ""
					}
				});

		if (objId) {
			$
					.ajax({
						type : 'POST',
						dataType : 'Json',
						async : false,
						url : rootUri
								+ '/FormFlowBase/getFormByBusinessKey.json',
						data : {
							objId : objId,
							taskid : taskid
						},
						success : function(data) {
							console.log(data);

							for (var i = 0; i < data.fieldsJson.length; i++) {
								if (data.fieldsJson[i].access == "write")
									data.fieldsJson[i].isDisable = false;
								else
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

		vue.$nextTick(function() {
					for (var i = 0; i < this.data.fieldsJson.length; i++) {
						if (this.data.fieldsJson[i].field_type == 'timespend') {
							initTimeSpend(
									i,
									this.data.fieldsJson[i].field_options.time_spend_type,
									this.data.taskStartTime,
									this.data.processInstanceStartTime);
						}
					}
				});
	});
</script>
<%@ include file="/common/footer.html"%>