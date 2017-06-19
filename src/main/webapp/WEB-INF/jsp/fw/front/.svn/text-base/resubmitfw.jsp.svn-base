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
												<label class="control-label col-lg-3">申请人</label>
												<div class="col-lg-6">
													<input type="text" class="form-control"
														v-model="data.inputUser" disabled>
												</div>
											</div>
										</div>
										<%@include file="/common/form-workflow/form-render.jsp"%>
										<!-- Buttons -->
										<div class="form-group">
											<!-- Buttons -->
											<div class="col-lg-6 col-lg-offset-1">
												<a type="submit" class="btn btn-success"
													onclick="submitForm()">重新提交</a> <a
													style="margin-left: 20px;" type="submit"
													class="btn btn-danger" onclick="cancelForm()">撤销</a>
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

<!-- Mainbar ends -->
<div class="clearfix"></div>

<!-- Footer starts -->
<%@ include file="/common/front-script.jsp"%>
<script src="<c:url value="/js/form-render.js" />"></script>

<script type="text/javascript">
	var vue;

	var user = decodeURI(getQueryStringByName("user"))
	var objId = getQueryStringByName("objId");
	var taskid = getQueryStringByName("taskid");
	var piId = getQueryStringByName("piId");

	var submitForm = function() {
		console.log(vue.data.fieldsJson);
		var message = validateFieldsValue(vue.data.fieldsJson);
		if (message.length > 0) {
			alert(message);
			return;
		}
		var dataUpload = {
			'taskid' : taskid,
			'remark' : '',
			'user' : decodeURI(user),
			'form' : JSON.stringify(vue.data.fieldsJson),
			'objId' : objId,
			'isReSubmit' : true
		};
		console.log(dataUpload);
		$.ajax({
			type : 'POST',
			dataType : 'Json',
			url : rootUri + '/FormFlowBase/pushWorkFlow.json',
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

	var cancelForm = function() {
		var dataUpload = {
			'taskid' : taskid,
			'user' : decodeURI(user)
		};
		console.log(dataUpload);
		$.ajax({
			type : 'POST',
			dataType : 'Json',
			url : rootUri + '/Form/cancelWorkFlow.json',
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
		vue = new Vue({
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
									data.fieldsJson[i].value = eval('('
											+ data.fieldsJson[i].value + ')');
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
							initTimeSpend(
									i,
									this.data.fieldsJson[i].field_options.time_spend_type,
									this.data.taskStartTime,
									this.data.processInstanceStartTime);
						}
					}
				});

		$("#tableFieldDetial").on("hidden.bs.modal", function() {
			$(this).removeData("bs.modal");
		});

		$('#tableFieldDetial')
				.on(
						'shown.bs.modal',
						function(e) {
							vueTableFieldDetial = null;
							var tableFieldDetialHtml = "<div style='margin-bottom: 50%;'>"
									+ "<div v-for='item in value'>"
									+ "<label class='control-label col-lg-4' for='name1'>{{item.label}}</label>"
									+ "<div class='col-lg-8'>"
									+ "<input type='text' v-model='item.value'"
									+ "style='margin-bottom: 10px;'>"
									+ "</div>" + "</div>" + "</div>";
							$('#tableFieldDetial .modal-body').html(
									tableFieldDetialHtml);
							var btn = $(e.relatedTarget), pindex = btn
									.data("pindex"), index = btn.data("index");

							var tableFields = vue.data.fieldsJson[pindex].field_options.options;
							var newValue = [];

							var valueList = vue.data.fieldsJson[pindex].value;
							if (index) {
								newValue = valueList[index];
							} else {
								for (var i = 0; i < tableFields.length; i++) {
									newValue.push({
										'label' : tableFields[i].label,
										'value' : ''
									});
								}
								if (valueList) {
									index = valueList.length;
								} else {
									index = 0;
								}
							}

							vueTableFieldDetial = new Vue({
								el : '#tableFieldDetial',
								data : {
									pindex : pindex,
									index : index,
									value : newValue
								}
							});

						});
	});
</script>
<%@ include file="/common/footer.html"%>