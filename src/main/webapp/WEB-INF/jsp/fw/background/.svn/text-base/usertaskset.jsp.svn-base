<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/common/header.jsp"%>
<%@ include file="/common/nav.jsp"%>

<!-- Main bar -->
<div class="mainbar" id="main">

	<!-- Page heading -->
	<%@ include file="/common/breadcrumb.jsp"%>
	<!-- Page heading ends -->

	<!-- Matter -->
	<div class="matter">
		<div class="container">
			<div class="row">
				<div class="col-md-12">
					<div class="widget">
						<div class="widget-head">
							<div class="pull-left">流程图</div>
							<div class="widget-icons pull-right">
								<a href="#" class="wminimize"><i class="icon-chevron-up"></i></a>
							</div>
							<div class="clearfix"></div>
						</div>
						<div class="widget-content">
							<div class="test">
								<img id="processDiagram"
									src="${rootUri}/FlowTrace/getFlowImgByProcessDefineID/${processdefineid}" />
							</div>
							<div class="widget-foot">
								<button type="button" onclick="saveUserTaskConfig()"
									class="btn btn-primary">保存修改</button>
								<div class="clearfix"></div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- modal -->
	<div id="usertask-set-1" class="modal fade" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true"></div>
	<div id="usertask-set-2" class="modal fade" tabindex="1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true"></div>
	<!-- Matter ends -->
</div>
<!-- Mainbar ends -->
<div class="clearfix"></div>

<%@ include file="/common/script.jsp"%>

<script type="text/javascript">
	var vue;

	$(function() {
		$.ajax({
			type : 'POST',
			dataType : 'Json',
			url : rootUri + '/Flow/UserTaskConfig.json',
			data : {
				processdefineid : '${processdefineid}'
			},
			success : function(data) {
				console.log(data);

				var listTemp = eval('(' + data.result + ')');
				var formdefine = eval('(' + data.formtabledefine + ')').fields;

				var formfieldsVue = [];
				for (var i = 0; i < formdefine.length; i++) {
					formfieldsVue.push({
						cid : formdefine[i].cid,
						field_type : formdefine[i].field_type,
						label : formdefine[i].label
					});
				}

				for (var i = 0; i < listTemp.length; i++) {
					var formfieldsConfig = listTemp.formfields;
				}

				vue = new Vue({
					el : '#main',
					data : {
						infosIndex : 0,
						infos : [],
						formfields : formfieldsVue,
						list : listTemp
					}
				});

			},
			error : function(err) {
				console.log(err);
				alert("系统出错");
				vue = new Vue({
					el : '#main',
					data : {
						infosIndex : 0,
						infos : []
					}
				});
			}
		});

		if ($('#processDiagram').length == 1) {
			showActivities();
		}
	});

	var saveUserTaskConfig = function() {
		$.ajax({
			type : 'POST',
			dataType : 'Json',
			url : rootUri + '/Flow/SaveUserTaskConfig.json',
			data : {
				processdefineid : '${processdefineid}',
				content : JSON.stringify(vue.list)
			},
			success : function(data) {
				console.log(data);
				alert("保存成功");
			},
			error : function(err) {
				console.log(err);
				alert("系统出错，保存失败");
			}
		});
	};

	var vueusertaskset1 = null;

	//用户任务节点弹窗打开事件
	$('#usertask-set-1')
			.on(
					'shown.bs.modal',
					function() {

						var infosIndex = vue.infosIndex;//当前任务节点索引
						var info = vue.infos[infosIndex]['params'];//当前任务节点配置项
						var taskid = vue.infos[infosIndex]['vars'].id;//当前节点任务id

						var currentItem;//当前配置的节点
						if (vue.list && vue.list.length > 0) {
							//遍历vue的list，如果当前task已经配置过，赋值给currentItem
							for (var i = 0; i < vue.list.length; i++) {
								if (vue.list[i].taskid == taskid) {
									currentItem = vue.list[i];
								}
							}
						}

						var formfieldsTemp = [];
						if (currentItem && currentItem.formfields
								&& currentItem.formfields.length > 0) {
							var vueformfields = [];
							for (var i = 0; i < currentItem.formfields.length; i++) {
								vueformfields.push(currentItem.formfields[i]);
							}
							for (var i = 0; i < vue.formfields.length; i++) {
								var isExist = false;
								for (var j = 0; j < vueformfields.length; j++) {
									if (vue.formfields[i].cid == vueformfields[j].cid) {
										vueformfields[j].label = vue.formfields[i].label;
										formfieldsTemp.push(vueformfields[j]);
										vueformfields.splice(j, 1);
										isExist = true;
										break;
									} else {
										formfieldsTemp
												.push({
													cid : vue.formfields[i].cid,
													field_type : vue.formfields[i].field_type,
													label : vue.formfields[i].label
												});
										isExist = true;
										break;
									}
								}
								if (!isExist) {
									formfieldsTemp
											.push({
												cid : vue.formfields[i].cid,
												field_type : vue.formfields[i].field_type,
												label : vue.formfields[i].label
											});
								}
							}
						} else {
							for (var i = 0; i < vue.formfields.length; i++) {
								formfieldsTemp.push({
									cid : vue.formfields[i].cid,
									field_type : vue.formfields[i].field_type,
									label : vue.formfields[i].label
								});
							}
						}
						if (currentItem)
							currentItem.formfields = formfieldsTemp;

						var params = [];
						if (info && info.assign && isExpression(info.assign)) {
							var value = '';
							if (currentItem) {
								for (var i = 0; i < currentItem.params.length; i++) {
									if (currentItem.params[i].expression == info.assign) {
										value = currentItem.params[i].value;
									}
								}
							}
							params.push({
								type : 'assign',
								expression : info.assign,
								value : value
							});
						}
						if (info && info.duedate && isExpression(info.duedate)) {
							var value = '';
							if (currentItem) {
								for (var i = 0; i < currentItem.params.length; i++) {
									if (currentItem.params[i].expression == info.duedate) {
										value = currentItem.params[i].value;
									}
								}
							}
							params.push({
								type : 'duedate',
								expression : info.duedate,
								value : value
							});
						}
						if (!vueusertaskset1) {
							vueusertaskset1 = new Vue({
								el : '#usertask-set-1',
								data : {
									taskid : taskid,
									params : params,
									formfields : formfieldsTemp
								}
							});
						} else {
							vueusertaskset1.taskid = taskid;
							vueusertaskset1.params = params;
							vueusertaskset1.formfields = formfieldsTemp;
						}
					});

	var usertaskSet = function() {
		if (vue.list && vue.list.length > 0) {
			var isExist = false;
			for (var i = 0; i < vue.list.length; i++) {
				if (vue.list[i].taskid == vueusertaskset1.taskid) {
					vue.list[i].params = vueusertaskset1.params;
					vue.list[i].formfields = vueusertaskset1.formfields;
					isExist = true;
				}
			}
			if (!isExist) {
				vue.list.push({
					taskid : vueusertaskset1.taskid,
					params : vueusertaskset1.params,
					formfields : vueusertaskset1.formfields
				});
			}
		} else {
			vue.list = [];
			vue.list.push({
				taskid : vueusertaskset1.taskid,
				params : vueusertaskset1.params,
				formfields : vueusertaskset1.formfields
			});
		}
		$("#usertask-set-1").modal('hide');
	};

	/**
	 * 获取元素的outerHTML
	 */
	$.fn.outerHTML = function() {

		// IE, Chrome & Safari will comply with the non-standard outerHTML, all others (FF) will have a fall-back for cloning
		return (!this.length) ? this : (this[0].outerHTML || (function(el) {
			var div = document.createElement('div');
			div.appendChild(el.cloneNode(true));
			var contents = div.innerHTML;
			div = null;
			return contents;
		})(this[0]));

	};

	function showActivities() {
		$
				.getJSON(
						rootUri + '/FlowTrace/define/data/${processdefineid}',
						function(infos) {
							console.log(infos);
							var positionHtml = "";

							var diagramPositon = $('#processDiagram')
									.position();
							var varsArray = new Array();
							$.each(infos, function(i, v) {
								var $positionDiv = $('<div/>', {
									'class' : 'activity-attr'
								}).css({
									position : 'absolute',
									left : (v.x + 20),
									top : (v.y + 43),
									width : (v.width - 2),
									height : (v.height - 2),
									backgroundColor : 'black',
									opacity : 0
								});

								// 节点边框
								var $border = $('<div/>', {
									'class' : 'activity-attr-border'
								}).css({
									position : 'absolute',
									left : (v.x + 18),
									top : (v.y + 42),
									width : (v.width - 4),
									height : (v.height - 3)
								});

								if (v.currentActiviti) {
									$border.css({
										border : '3px solid red'
									}).addClass('ui-corner-all-12');
								}
								positionHtml += $positionDiv.outerHTML()
										+ $border.outerHTML();
								varsArray[varsArray.length] = v.vars;
							});

							$(positionHtml).appendTo('.test');

							// 鼠标移动到活动上提示
							$('.activity-attr-border')
									.each(
											function(i, v) {
												var tipContent = "<table class='table table-bordered'>";
												$
														.each(
																varsArray[i],
																function(
																		varKey,
																		varValue) {
																	if (varKey != "id"
																			&& varValue) {
																		tipContent += "<tr><td>"
																				+ getZhActivityType(varKey)
																				+ "</td><td>"
																				+ getZhActivityType(varValue)
																				+ "</td></tr>";
																	}
																});
												//tipContent += "</table>";
												$(this).data('vars',
														varsArray[i]).data(
														'toggle', 'tooltip')
														.data('placement',
																'bottom')
														.data('title', '活动属性')
														.attr('title',
																tipContent)
														.attr('data-html',
																'true');
											})
									.tooltip()
									.each(
											function(i) {
												if (infos[i]['vars']['type'] == "userTask") {
													$(this)
															.click(
																	function() {
																		vue.infosIndex = i;
																		vue.infos = infos;
																		$(
																				'#usertask-set-1')
																				.modal(
																						{
																							remote : '${rootUri}/common/bootstrap-modal/usertask-set-1.html?i'
																						});
																	});
												}
											});
						});
	}
</script>
<%@ include file="/common/footer.html"%>