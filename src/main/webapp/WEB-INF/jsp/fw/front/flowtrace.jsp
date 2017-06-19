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
							<div class="pull-left">流程图</div>
							<div class="widget-icons pull-right">
								<a href="#" class="wminimize"><i class="icon-chevron-up"></i></a>
							</div>
							<div class="clearfix"></div>
						</div>
						<div class="widget-content">
							<div class="test">
							<img id="processDiagram" src="${rootUri}/Flow/getFlowImg/20017" />
							</div>
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

		if ($('#processDiagram').length == 1) {
			showActivities();
		}
	});

	function showActivities() {
		$
				.getJSON(
						rootUri + '/Flow/trace/data/20017',
						function(infos) {

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
									top : (v.y +43),
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
									left : (v.x +18),
									top : (v.y +42),
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
																	if (varValue) {
																		tipContent += "<tr><td>"
																				+ varKey
																				+ "</td><td>"
																				+ varValue
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
																tipContent).attr('data-html','true');
											}).tooltip();
						});
	}
</script>
<%@ include file="/common/footer.html"%>