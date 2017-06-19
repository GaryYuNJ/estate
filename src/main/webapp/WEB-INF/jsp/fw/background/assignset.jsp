<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/common/header.jsp"%>
<%@ include file="/common/nav.jsp"%>

<div id="main" class="mainbar">

	<!-- Page heading -->
	<div class="page-head">
		<h2 class="pull-left">
			<i class="icon-home"></i> 审批人设置
		</h2>

		<!-- Breadcrumb -->
		<div class="bread-crumb pull-right">
			<a href="#"><i class="icon-home"></i> 审批管理</a>
			<!-- Divider -->
			<span class="divider">/</span> <a href="#" class="bread-current">审批人设置</a>
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
						<div class="widget-content">
							<a style="margin: 10px;" class="btn btn-xs btn-success"
								onclick="submit()"> <i class="icon-search"></i>提交
							</a>
						</div>
					</div>
					<div class="widget">
						<div class="widget-head">
							<div class="pull-left">审批人设置</div>
							<div class="widget-icons pull-right"></div>
							<div class="clearfix"></div>
						</div>
						<div class="widget-content">
							<div class="radio" style="margin-left: 20px">
								<label> <input type="radio" name="optionsRadios"
									id="optionsRadios1" value="option1" v-model="type">
									不分条件设置审批人
								</label>
							</div>
							<div v-show="type=='option1'" class="widget"
								style="margin-left: 40px; margin-right: 10px;">
								<div class="widget-head">
									<div class="pull-left">不分条件设置审批人</div>
									<div class="widget-icons pull-right">
										<a href="#myModal" class="btn btn-info" data-toggle="modal"
											style="color: #fafafa">增加审批人</a>
										<!-- Modal -->
										<div id="myModal" class="modal fade" tabindex="-1"
											role="dialog" aria-labelledby="myModalLabel"
											aria-hidden="true">
											<div class="modal-dialog">
												<div class="modal-content">
													<div class="modal-header">
														<button type="button" class="close" data-dismiss="modal"
															aria-hidden="true">×</button>
														<h4 class="modal-title">选择审批人</h4>
													</div>
													<div class="modal-body">
														<div style="margin-bottom: 57px;">
															<label class="col-lg-2 control-label">已选审批人：</label>
															<div class="col-lg-6">
																<input id="tempAssign" type="text" disabled=""
																	class="form-control" placeholder="">
															</div>
														</div>
														<hr style="border-bottom: 1px solid #928F8F">
														<a class="btn btn-info" onclick="addNextLevel1()"
															style="color: #fafafa">上一级主管</a> <a class="btn btn-info"
															onclick="addNextLevel2()" style="color: #fafafa">上二级主管</a>
														<a class="btn btn-info" onclick="addNextLevel3()"
															style="color: #fafafa">上三级主管</a>
														<hr style="border-bottom: 1px solid #928F8F">
														<div id="tree"></div>
														<hr style="border-bottom: 1px solid #928F8F">
														<select onchange="changeUserName1()" class="form-control"
															id="username1">
														</select>
													</div>
													<div class="modal-footer">
														<button type="button" class="btn btn-default"
															data-dismiss="modal" aria-hidden="true">取消</button>
														<button type="button" onclick="addAssign()"
															class="btn btn-primary">确定</button>
													</div>
												</div>
											</div>
										</div>
									</div>
									<div class="clearfix"></div>
								</div>
								<div class="widget-content">
									<table class="table table-striped table-bordered table-hover">
										<thead>
											<tr>
												<th>#编号</th>
												<th>审批人</th>
												<th>操作</th>
											</tr>
										</thead>
										<tbody>
											<tr v-for="item in option1">
												<td>{{$index+1}}</td>
												<td>{{item.text}}</td>
												<td><a class="btn btn-xs btn-danger"
													onclick="deleteAssigner1({{$index}})"> <i
														class="icon-remove"></i>删除
												</a> <!-- <a class="btn btn-xs btn-warning"
															href="NewFormEntity.html?id={{item.id}}"> <i
																class="icon-pencil">新建</i>
														</a> --></td>
											</tr>
										</tbody>
									</table>
								</div>
							</div>
							<div class="radio" style="margin-left: 20px">
								<label> <input type="radio" name="optionsRadios"
									id="optionsRadios2" value="option2" v-model="type">
									分条件设置审批人
								</label>
							</div>
							<div class="widget" v-show="type=='option2'"
								style="margin-left: 40px; margin-right: 10px;">
								<div class="widget-head">
									<div class="pull-left">分条件设置审批人</div>
									<div class="widget-icons pull-right">
										<a href="#conditionModal" class="btn btn-info"
											data-toggle="modal" style="color: #fafafa">修改条件</a>
										<!-- Modal -->
										<div id="conditionModal" class="modal fade" tabindex="-1"
											role="dialog" aria-labelledby="myModalLabel"
											aria-hidden="true">
											<div class="modal-dialog">
												<div class="modal-content">
													<div class="modal-header">
														<button type="button" class="close" data-dismiss="modal"
															aria-hidden="true">×</button>
														<h4 class="modal-title">选择审批条件</h4>
													</div>
													<div class="modal-body" id="conditionsModalBody"></div>
													<div class="modal-footer">
														<button type="button" class="btn btn-default"
															data-dismiss="modal" aria-hidden="true">取消</button>
														<button type="button" onclick="changeCondition()"
															class="btn btn-primary">确定</button>
													</div>
												</div>
											</div>
										</div>
										<div id="conditionvalue" class="modal fade" tabindex="-1"
											role="dialog" aria-labelledby="myModalLabel"
											aria-hidden="true">
											<div class="modal-dialog">
												<div class="modal-content">
													<div class="modal-header">
														<button type="button" class="close" data-dismiss="modal"
															aria-hidden="true">×</button>
														<h4 class="modal-title">选择审批条件</h4>
													</div>
													<div class="modal-body" id="conditionValueModalBody"></div>
													<div class="modal-footer">
														<button type="button" class="btn btn-default"
															data-dismiss="modal" aria-hidden="true">取消</button>
														<button type="button" onclick="changeConditionValue()"
															class="btn btn-primary">确定</button>
													</div>
												</div>
											</div>
										</div>
										<div id="myModal2" class="modal fade" tabindex="-1"
											role="dialog" aria-labelledby="myModalLabel"
											aria-hidden="true">
											<div class="modal-dialog">
												<div class="modal-content">
													<div class="modal-header">
														<button type="button" class="close" data-dismiss="modal"
															aria-hidden="true">×</button>
														<h4 class="modal-title">选择审批人</h4>
													</div>
													<div class="modal-body">
														<div style="margin-bottom: 57px;">
															<label class="col-lg-2 control-label">已选审批人：</label>
															<div class="col-lg-6">
																<input id="tempAssign2" type="text" disabled=""
																	class="form-control" placeholder="">
															</div>
														</div>
														<hr style="border-bottom: 1px solid #928F8F">
														<a class="btn btn-info" onclick="addNextLevel21()"
															style="color: #fafafa">上一级主管</a> <a class="btn btn-info"
															onclick="addNextLevel22()" style="color: #fafafa">上二级主管</a>
														<a class="btn btn-info" onclick="addNextLevel23()"
															style="color: #fafafa">上三级主管</a>
														<hr style="border-bottom: 1px solid #928F8F">
														<div id="tree2"></div>
														<hr style="border-bottom: 1px solid #928F8F">
														<select onchange="changeUserName2()" class="form-control"
															id="username2">
														</select>
													</div>
													<div class="modal-footer">
														<button type="button" class="btn btn-default"
															data-dismiss="modal" aria-hidden="true">取消</button>
														<button type="button" onclick="addAssign2()"
															class="btn btn-primary">确定</button>
													</div>
												</div>
											</div>
										</div>
									</div>
									<div class="clearfix"></div>
								</div>
								<div class="widget-content">
									<div class="widget"
										style="margin-left: 10px; margin-right: 10px;">
										<div class="widget-head">
											<div class="pull-left">
												当前选择条件：<input style="background-color: #eee" type="label"
													readonly v-model="option2.conditionTyepName">
											</div>
											<div class="widget-icons pull-right">
												<a href="#conditionvalue" class="btn btn-info"
													data-toggle="modal" style="color: #fafafa">增加分支</a>
											</div>
											<div class="clearfix"></div>
										</div>
										<div class="widget-content">
											<div v-for="item in option2.conditions" class="widget"
												style="margin-left: 10px; margin-right: 10px;">
												<div class="widget-head">
													<div class="pull-left">{{item.text}}</div>
													<div class="widget-icons pull-right">
														<a href="#myModal2" class="btn btn-info"
															onclick="openMyModal2({{$index}})" data-toggle="modal"
															style="color: #fafafa">增加审批人</a> <a href="#"
															class="wminimize"><i class="icon-chevron-up"></i></a>
													</div>
													<div class="clearfix"></div>
												</div>
												<div class="widget-content">
													<table
														class="table table-striped table-bordered table-hover">
														<thead>
															<tr>
																<th>#编号</th>
																<th>审批人</th>
																<th>操作</th>
															</tr>
														</thead>
														<tbody>
															<tr v-for="itemAssigners in item.assigners">
																<td>{{$index+1}}</td>
																<td>{{itemAssigners.text}}</td>
																<td><a class="btn btn-xs btn-danger"
																	onclick="deleteAssigner2({{$parent.$index}},{{$index}})">
																		<i class="icon-remove"></i>删除
																</a></td>
															</tr>
														</tbody>
													</table>
												</div>
											</div>
										</div>
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
</div>
<div class="clearfix"></div>
</div>
<%@ include file="/common/script.jsp"%>

<script type="text/javascript">
var fieldsJson;
var formID;//审批表单ID，从querystring获取
var submit = function() {
	if (!formID) {
		alert("系统错误，无审批ID");
		return;
	};
	if ((vue.type == "option1" && vue.option1.length <= 0)
			|| (vue.type == "option2" && vue.option2.length <= 0)) {
		alert("没有设置审批人");
		return;
	};
	var activitiJson = {
		type : vue.type,
		option1 : vue.option1,
		option2 : vue.option2
	}
	$.ajax({
		type : 'POST',
		dataType : 'Json',
		url : rootUri + '/FormFlowBase/setWorkFlow.json',
		data : {
			formID : formID,
			activitiJson : JSON.stringify(activitiJson)
		},
		success : function(data) {
			console.log(data);
			window.location.href = rootUri + '/fwpages/bfwlist';
		},
		error : function(err) {
			console.log(err);
			alert("系统出错！")
		}
	});
};

var vue;
var tempAssign = "";
var tempAssignText = "";
var tempType="";
var tempKey="";
var tempvalue="";
var addNextLevel1 = function() {
	tempAssign = "${'${'}AssignToNextLevel_1}";
	tempAssignText = "上一级主管";
	tempType="1";
	tempKey="AssignToNextLevel_1";
	tempvalue="AssignToNextLevel_1";
	$("#tempAssign").val(tempAssignText);
};
var addNextLevel2 = function() {
	tempAssign = "${'${'}AssignToNextLevel_2}";
	tempAssignText = "上二级主管";
	tempType="1";
	tempKey="AssignToNextLevel_2";
	tempvalue="AssignToNextLevel_2";
	$("#tempAssign").val(tempAssignText);
};
var addNextLevel3 = function() {
	tempAssign = "${'${'}AssignToNextLevel_3}";
	tempAssignText = "上三级主管";
	tempType="1";
	tempKey="AssignToNextLevel_3";
	tempvalue="AssignToNextLevel_3";
	$("#tempAssign").val(tempAssignText);
};
var addAssign = function() {
	vue.option1.push({
		assign : tempAssign,
		text : tempAssignText,
		type : tempType,
		value : tempvalue,
		key : tempKey
	});
	$("#myModal").modal('hide');
};

var tempAssign2 = "";
var tempAssignText2 = "";
var tempType2="";
var tempKey2="";
var tempvalue2="";
var addNextLevel21 = function() {
	tempAssign2 = "${'${'}AssignToNextLevel_1}";
	tempAssignText2 = "上一级主管";
	tempType2="1";
	tempKey2="AssignToNextLevel_1";
	tempvalue2="AssignToNextLevel_1";
	$("#tempAssign2").val(tempAssignText2);
};
var addNextLevel22 = function() {
	tempAssign2 = "${'${'}AssignToNextLevel_2}";
	tempAssignText2 = "上二级主管";
	tempType2="1";
	tempKey2="AssignToNextLevel_2";
	tempvalue2="AssignToNextLevel_2";
	$("#tempAssign2").val(tempAssignText2);
};
var addNextLevel23 = function() {
	tempAssign2 = "${'${'}AssignToNextLevel_3}";
	tempAssignText2 = "上三级主管";
	tempType2="1";
	tempKey2="AssignToNextLevel_3";
	tempvalue2="AssignToNextLevel_3";
	$("#tempAssign2").val(tempAssignText2);
};
var addAssign2 = function() {
	vue.option2.conditions[tempConditionIndex].assigners.push({
		assign : tempAssign2,
		text : tempAssignText2,
		type : tempType2,
		value : tempvalue2,
		key : tempKey2
	});
	$("#myModal2").modal('hide');
};

var changeUserName1=function(){
	var valueUser = $("#username1").val();
	if(valueUser){
		tempAssign = tempAssignText = valueUser;
		tempType="0";
		tempvalue=tempKey="";
		$("#tempAssign").val(tempAssignText);
	}
};

var changeUserName2=function(){
	var valueUser = $("#username2").val();
	if(valueUser){
		tempAssign2 = tempAssignText2 = valueUser;
		tempType2="0";
		tempvalue2=tempKey2="";
		$("#tempAssign2").val(tempAssignText2);
	}
};

var changeCondition = function() {
	vue.option2.conditionTypeNo = $("#conditionType").val();
	vue.option2.conditionTypeCID = fieldsJson.fields[vue.option2.conditionTypeNo].cid;
	vue.option2.conditionTyep = fieldsJson.fields[vue.option2.conditionTypeNo].field_type;
	vue.option2.conditionTyepName = fieldsJson.fields[vue.option2.conditionTypeNo].label;
	vue.option2.conditionTyepOptions = fieldsJson.fields[vue.option2.conditionTypeNo].field_options.options;
	$("#conditionModal").modal('hide');
}

var changeConditionValue = function() {
	$("#conditionvalue").modal('hide');
}

var tempConditionIndex;

var openMyModal2 = function(index){
	tempConditionIndex = index;
}

var deleteAssigner1 = function(index){
	console.log(vue.option1);
	vue.option1.splice(index,1);
	console.log(vue.option1);
}

var deleteAssigner2 = function(Pindex,Cindex){
	console.log(vue.option2.conditions);
	vue.option2.conditions[Pindex].assigners.splice(Cindex,1)
	//vue.option1.splice(index,1);
	//console.log(vue.option1);
}

$('#conditionvalue')
		.on(
				'show.bs.modal',
				function() {
					$('#conditionvalue').off().on('hide.bs.modal');
					$('#conditionValueModalBody').html('');
					var conditionValueModalBodyHtml = '';

					if (vue.option2.conditionTyep == 'dropdown'||vue.option2.conditionTyep == 'radio') {
						conditionValueModalBodyHtml += "<label class='control-label'>当 "
								+ vue.option2.conditionTyepName
								+ " ＝ </label>"
								+ "<div>"
								+ "<select id='"+ vue.option2.conditionTypeCID +"' class='form-control'>"
								+ "<option value=''> --- Please Select --- </option>";
						for (var i = 0; i < vue.option2.conditionTyepOptions.length; i++) {
							conditionValueModalBodyHtml += "<option value='"+vue.option2.conditionTyepOptions[i].label+"'>"
									+ vue.option2.conditionTyepOptions[i].label
									+ "</option>";
						}
						conditionValueModalBodyHtml += "</select>"
								+ "</div>";

						$('#conditionValueModalBody').html(
								conditionValueModalBodyHtml);

						$('#conditionvalue')
								.on(
										'hide.bs.modal',
										function() {
											var tempvalue = $(
													"#"
															+ vue.option2.conditionTypeCID)
													.val();
											var tempCondition = {
												value : tempvalue,
												text : "当 "
														+ vue.option2.conditionTyepName
														+ "= "
														+ tempvalue,
												assigners : []
											};
											vue.option2.conditions
													.push(tempCondition);
										});
					}
				});

$(function() {
	formID = getQueryStringByName("id");
	if (!formID) {
		alert("系统错误，无审批ID");
		return;
	}
	$.ajax({
				type : 'POST',
				dataType : 'Json',
				url : rootUri +'/Form/getFormById.json',
				data : {
					id : formID
				},
				success : function(data) {
					console.log(data);
					var activitiObj = eval('(' + data.activitiJson
							+ ')');
					if(activitiObj)
						activitiObj=activitiObj.content;
					fieldsJson = eval('(' + data.fieldsJson + ')');
					console.log(activitiObj);

					var conditionsHtml = "";
					for (var i = 0; i < fieldsJson.fields.length; i++) {
						if (fieldsJson.fields[i].field_type == 'radio'
								|| fieldsJson.fields[i].field_type == 'dropdown'
								//|| fieldsJson.fields[i].field_type == 'number'
								) {
							conditionsHtml += "<option value='"+i+"'>"
									+ fieldsJson.fields[i].label
									+ "</option>";
						}
					}
					if (conditionsHtml.length <= 0) {
						conditionsHtml = "没有可选择的条件，请修改表单";
					} else {
						conditionsHtml = "<select class='form-control' id='conditionType'>"
								+ "<option>--- Please Select ---</option>"
								+ conditionsHtml + "</select>"
					}
					$("#conditionsModalBody").html(conditionsHtml);

					if (activitiObj) {
						vue = new Vue({
							el : '#main',
							data : {
								type : activitiObj.type==null?"option1":activitiObj.type,
								option1 : activitiObj.option1==null?[]:activitiObj.option1,
								option2 : activitiObj.option2==null?[]:activitiObj.option2
							}
						});
					} else {
						vue = new Vue({
							el : '#main',
							data : {
								type : "option1",
								option1 : [],
								option2 : {
									conditionTyepName : '',
									conditions : []
								}
							}
						});
					}

				},
				error : function(err) {
					console.log(err);
					alert("系统出错！")
				}
			});

	$.ajax({
		type : 'GET',
		dataType : 'Json',
		url : rootUri + '/buser/buserSearchAll.json',
		success : function(data) {
			console.log(data);
			var tempOptionsHtml = "<option value=''>请选择审批人</option>";
			for (var i = 0; i < data.length; i++) {
				tempOptionsHtml += "<option>" + data[i].loginid
						+ "</option>";
			}
			$("#username1").html(tempOptionsHtml);
			$("#username2").html(tempOptionsHtml);
		},
		error : function(err) {
			console.log(err);
		}
	});
	
	$.ajax({
		type : 'POST',
		dataType : 'Json',
		url : rootUri +'/organization/getNodes.json',
		success : function(data) {
			$('#tree').treeview(
					{
						data : data,
						onNodeSelected : function(event, data) {
							var tempvarkey="Role_"+$('#tree').treeview('getSelected', 1)[0].id;
							tempvarkey=tempvarkey.slice(0,13);
							console.log(tempvarkey);
							tempAssignText = $('#tree').treeview('getSelected', 1)[0].text+" 主管";
							tempAssign = "${'${'}"+tempvarkey+"}";
							tempType = "2";
							tempKey = tempvarkey;
							tempvalue = $('#tree').treeview('getSelected', 1)[0].id;
							$("#tempAssign").val(tempAssignText);
							/*部门人员联动*/
							var urlApiJson=rootUri +'/buser/buserSearch.json?search={"loginid":"","orid":"'+$('#tree').treeview('getSelected', 1)[0].id+'"}&order=asc&offset=0&limit=100';
							$.ajax({
								type : 'GET',
								dataType : 'Json',
								url :urlApiJson ,
								success : function(data) {
									console.log(data);
									var tempOptionsHtml = "<option value=''>请选择审批人</option>";
									for (var i = 0; i < data.rows.length; i++) {
										tempOptionsHtml += "<option>" + data.rows[i].loginid
												+ "</option>";
									}
									$("#username1").html(tempOptionsHtml);
								},
								error : function(err) {
									console.log(err);
									alert("系统出错！")
								}
							});
						},
						onNodeUnselected : function(event, data) {
							tempAssignText = tempAssign = tempType=tempKey=tempvalue="";
							$("#tempAssign").val("");
							/*部门人员联动*/
							var urlApiJson=rootUri +'/buser/buserSearch.json?search={"loginid":"","orid":""}&order=asc&offset=0&limit=100';
							$.ajax({
								type : 'GET',
								dataType : 'Json',
								url :urlApiJson ,
								success : function(data) {
									console.log(data);
									var tempOptionsHtml = "<option value=''>请选择审批人</option>";
									for (var i = 0; i < data.rows.length; i++) {
										tempOptionsHtml += "<option>" + data.rows[i].loginid
												+ "</option>";
									}
									$("#username1").html(tempOptionsHtml);
								},
								error : function(err) {
									console.log(err);
									alert("系统出错！")
								}
							});
						}
					});

			$('#tree2').treeview(
					{
						data : data,
						onNodeSelected : function(event, data) {
							var tempvarkey="Role_"+$('#tree2').treeview('getSelected', 1)[0].id;
							tempvarkey=tempvarkey.slice(0,13);
							console.log(tempvarkey);
							tempAssignText2 = $('#tree2').treeview('getSelected', 1)[0].text+" 主管";
							tempAssign2 ="${'${'}"+tempvarkey+"}";
							tempType2 = "2";
							tempKey2 = tempvarkey
							tempvalue2 = $('#tree2').treeview('getSelected', 1)[0].id;
							$("#tempAssign2").val(tempAssignText2);
							/*部门人员联动*/
							var urlApiJson=rootUri +'/buser/buserSearch.json?search={"loginid":"","orid":"'+$('#tree2').treeview('getSelected', 1)[0].id+'"}&order=asc&offset=0&limit=100';
							$.ajax({
								type : 'GET',
								dataType : 'Json',
								url :urlApiJson ,
								success : function(data) {
									console.log(data);
									var tempOptionsHtml = "<option value=''>请选择审批人</option>";
									for (var i = 0; i < data.rows.length; i++) {
										tempOptionsHtml += "<option>" + data.rows[i].loginid
												+ "</option>";
									}
									$("#username2").html(tempOptionsHtml);
								},
								error : function(err) {
									console.log(err);
									alert("系统出错！")
								}
							});
						},
						onNodeUnselected : function(event, data) {
							tempAssignText2 = tempAssign2 = tempType2=tempKey2=tempvalue2="";
							$("#tempAssign2").val("");
							/*部门人员联动*/
							var urlApiJson=rootUri +'/buser/buserSearch.json?search={"loginid":"","orid":""}&order=asc&offset=0&limit=100';
							$.ajax({
								type : 'GET',
								dataType : 'Json',
								url :urlApiJson ,
								success : function(data) {
									console.log(data);
									var tempOptionsHtml = "<option value=''>请选择审批人</option>";
									for (var i = 0; i < data.rows.length; i++) {
										tempOptionsHtml += "<option>" + data.rows[i].loginid
												+ "</option>";
									}
									$("#username2").html(tempOptionsHtml);
								},
								error : function(err) {
									console.log(err);
									alert("系统出错！")
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
</script>
<%@ include file="/common/footer.html"%>
