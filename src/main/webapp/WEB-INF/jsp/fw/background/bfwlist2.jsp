<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/common/header.jsp"%>
<%@ include file="/common/nav.jsp"%>
<!-- Main bar -->
<div class="mainbar">

	<!-- Page heading -->
	<div class="page-head">
		<h2 class="pull-left">
			<i class="icon-list-alt"></i> 审批列表
		</h2>
		<!-- Breadcrumb -->
		<div class="bread-crumb pull-right">
			<a href="index.html"><i class="icon-list-alt"></i> 审批管理</a>
			<!-- Divider -->
			<span class="divider">/</span> <a href="#" class="bread-current">审批列表</a>
		</div>
		<div class="clearfix"></div>
	</div>
	<!-- Page heading ends -->

	<!-- Matter -->

	<div class="matter" id="main">
		<div class="container">
			<div class="row">
				<div class="col-md-12">
					<div class="widget">
						<div class="widget-content">
							<a href="newformtable" style="margin: 10px;"
								class="btn btn-xs btn-success"> <i class="icon-search"></i>新建审批
							</a>
						</div>
					</div>
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
										<th>更新时间</th>
										<th>操作</th>
									</tr>
								</thead>
								<tbody>
									<tr v-for="item in list">
										<td>{{item.id}}</td>
										<td>{{item.tableName}}</td>
										<td>{{item.cratetime | time}}</td>
										<td><a v-show="item.state<2"
											class="btn btn-xs btn-success"
											href="newformtable?id={{item.id}}"> <i
												class="icon-search"></i>编辑表单
										</a> <a class="btn btn-xs btn-warning"
											href="assignset?id={{item.id}}"> <i class="icon-pencil">设置审批人</i>
										</a> <a class="btn btn-xs btn-warning" href="javascript:0;"
											onclick="bpmNew({{$index}})"> <i class="icon-pencil">设置审批人2</i>
										</a> <a v-show="item.state==1" class="btn btn-xs btn-success"
											onclick="qiyong({{item.id}})"> <i class="icon-search"></i>启用
										</a> <a v-show="item.state==3" class="btn btn-xs btn-success"
											onclick="chongqiyong({{item.id}})"> <i
												class="icon-search"></i>启用
										</a> <a v-show="item.state==2" class="btn btn-xs btn-warning"
											onclick="tingyong({{item.id}})"> <i class="icon-search"></i>停用
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
</div>

<%@ include file="/common/script.jsp"%>

<script type="text/javascript">
Vue.filter('time', function (value) {
    return new Date(value).toLocaleString();
});

var qiyong=function(id){
	$.ajax({
		type: 'POST',
  		dataType: 'Json',
  		url: rootUri + '/Form/qiyong.json' ,
  		data:{id:id},
  		success: function(data){
  			console.log(data);
  			location.reload();
  		},
  		error:function(err){
  			console.log(err);
  			alert("系统出错");
  		}				
	});
};
var chongqiyong=function(id){
	$.ajax({
		type: 'POST',
  		dataType: 'Json',
  		url: rootUri + '/Form/chongqiyong.json' ,
  		data:{id:id},
  		success: function(data){
  			console.log(data);
  			location.reload();
  		},
  		error:function(err){
  			alert("系统出错");
  			console.log(err);
  		}				
	});
};

var tingyong=function(id){
	$.ajax({
		type: 'POST',
  		dataType: 'Json',
  		url: rootUri + '/Form/tingyong.json' ,
  		data:{id:id},
  		success: function(data){
  			console.log(data);
  			location.reload();
  		},
  		error:function(err){
  			alert("系统出错");
  			console.log(err);
  		}				
	});
};

var bpmNew=function(index){
	var obj=vue.list[index];
	$.ajax({
		type: 'POST',
  		dataType: 'Json',
  		url: rootUri + '/bpm/addModel' ,
  		data:{
			id:obj.id,
  			name:obj.tableName,
  			key:obj.tableNameEn,
  			description:obj.description
  		},
  		success: function(data){
  			console.log(data);
  			window.open(rootUri+data.url);
  		},
  		error:function(err){
  			console.log(err);
  		}				
	});
};

var vue;
$(function() {
	$.ajax({
		type: 'POST',
  		dataType: 'Json',
  		url: rootUri + '/Form/queryformlist.json' ,
  		success: function(data){
  			console.log(data);
  			vue=new Vue({
  				el:'#main',
  				data:{
  					list:data
  				}
  			});
  		},
  		error:function(err){
  			vue=new Vue({
  				el:'#main'
  			});
  			console.log(err);
  		}				
	});
});
</script>
<%@ include file="/common/footer.html"%>