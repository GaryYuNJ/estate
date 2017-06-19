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
			<a><i class="icon-list-alt"></i> 提交表单</a>
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
										<%@include file="/common/form-workflow/form-render.jsp"%>
										<div class="form-group">
											<div class="col-lg-6 col-lg-offset-1">
												<a type="submit" class="btn btn-success"
													onclick="submitForm()">提交</a>
											</div>
										</div>
									</form>
								</div>
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
	var id = getQueryStringByName("id");
	var user = getQueryStringByName("user");

	var vue;

	var submitForm = function() {
		var message = validateFieldsValue(vue.data.fieldsJson);
		if (message.length > 0) {
			alert(message);
			return;
		}

		var dataUpload = {
			'id' : id,
			'user' : decodeURI(user),
			'form' : JSON.stringify(vue.data.fieldsJson)
		};
		console.log(dataUpload);
		$.ajax({
			type : 'POST',
			dataType : 'Json',
			url : rootUri + '/FormFlowBase/insertForm.json',
			data : dataUpload,
			success : function(data) {
				console.log(data);
				if (data.error) {
					alert("无法提交：\r\n" + data.error);
					return;
				}
				window.location.href = rootUri
						+ '/fwpages/myapplylist.html?user=' + user;
			},
			error : function(err) {
				console.log(err);
				alert("无法提交：系统出错");
			}
		});
	};

	$(function() {
		console.log(id);
		if (id) {
			$
					.ajax({
						type : 'POST',
						dataType : 'Json',
						async : false,
						url : rootUri + '/FormFlowBase/getFormById.json',
						data : {
							id : id,
							taskid : "userinputtask"
						},
						success : function(data) {
							console.log(data);
							var fieldsJson = data.fieldsJson;

							for (var i = 0; i < fieldsJson.length; i++) {
								if (fieldsJson[i].field_type == 'fileupload') {
									fieldsJson[i].field_options.options = [];
								}
								if (fieldsJson[i].field_type == 'table') {
									fieldsJson[i].value = [];
								}
								if (fieldsJson[i].field_type == 'linkdropdown') {
									$
											.ajax({
												type : 'GET',
												dataType : 'Json',
												async : false,
												url : fieldsJson[i].field_options.options[0].label,//rootUri + '/dropdowndata/getProvince.json',
												success : function(data) {
													fieldsJson[i].field_options.options[0].datalist = data;
												},
												error : function(err) {
													console.log(err);
													alert("系统出错！")
												}
											});
								}
							}

							vue = new Vue({
								el : '#main',
								data : {
									data : {
										tableName : data.tableName,
										fieldsJson : fieldsJson
									},
									user : user
								}
							});

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
			alert("表单id不存在");
		}

		vue.$nextTick(function() {
			for (var i = 0; i < this.data.fieldsJson.length; i++) {
				if (this.data.fieldsJson[i].field_type == 'timespend') {
					$('#countdown_' + i).parent().parent().parent().hide();
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
							if (index >= 0) {
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

		var changeUserName1=function(){
			var valueUser = $("#username1").val();
			if(valueUser){
				tempAssign = tempAssignText = valueUser;
				tempType="0";
				tempvalue=tempKey="";
				$("#tempAssign").val(tempAssignText);
			}
		};
		

		$('#memberSelDetial').on(
						'shown.bs.modal',
						function(e) {
							vueMemberSelDetial = null;

							var btn = $(e.relatedTarget);
							$('#memberSelHidden').val(btn.data("index"));

							$.ajax({
										type : 'POST',
										dataType : 'Json',
										url : rootUri
												+ '/organization/getNodes.json',
										success : function(data) {
											$('#tree')
													.treeview(
															{
																data : data,
																onNodeSelected : function(event,data) {
																	var urlApiJson = rootUri
																			+ '/buser/buserSearch.json?search={"loginid":"","orid":"'
																			+ $('#tree').treeview('getSelected',1)[0].id
																			+ '"}&order=asc&offset=0&limit=100';
																	$.ajax({
																				type : 'GET',
																				dataType : 'Json',
																				url : urlApiJson,
																				success : function(data) {
																					console.log(data);
																					var tempOptionsHtml = "<option value=''>请选择审批人</option>";
																					for (var i = 0; i < data.rows.length; i++) {
																						tempOptionsHtml += "<option>" + data.rows[i].loginid + "</option>";
																					}
																					$("#memberSel").html(tempOptionsHtml);
																				},
																				error : function(err) {
																					console.log(err);
																					alert("系统出错！")
																				}
																			});
																},
																onNodeUnselected : function(event,data) {
																	var urlApiJson = rootUri+ '/buser/buserSearch.json?search={"loginid":"","orid":""}&order=asc&offset=0&limit=100';
																	$.ajax({
																				type : 'GET',
																				dataType : 'Json',
																				url : urlApiJson,
																				success : function(data) {
																					console.log(data);
																					var tempOptionsHtml = "<option value=''>请选择审批人</option>";
																					for (var i = 0; i < data.rows.length; i++) {
																						tempOptionsHtml += "<option>" + data.rows[i].loginid + "</option>";
																					}
																					$("#memberSel").html(tempOptionsHtml);
																				},
																				error : function(err) {
																					console.log(err);
																					alert("系统出错！");
																				}
																			});
																}
															});
										},
										error : function(err) {
											console.log(err);
											alert("系统出错！")
										}
									});

						});
	});
</script>
<%@ include file="/common/footer.html"%>