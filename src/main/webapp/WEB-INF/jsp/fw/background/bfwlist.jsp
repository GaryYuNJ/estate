<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ include file="/common/header.jsp"%>
<%@ include file="/common/nav.jsp"%>
<!-- Main bar -->
<div class="mainbar">
	<!-- Page heading -->
	<%@ include file="/common/breadcrumb.jsp"%>
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
										<td><a class="btn btn-xs btn-success"
											href="newformtable?id={{item.id}}"> <i
												class="icon-search"></i>编辑表单
										</a> <a data-toggle="modal"
											href="${rootUri}/common/bootstrap-modal/assign-set-button-2.html?{{$index}}"
											data-target="#assignSetSelectModal"
											class="btn btn-xs btn-warning" data-index="{{$index}}"><i
												class="icon-pencil">设置流程</i></a> <a
											v-show="item.state==1||item.state==3||item.state==0"
											class="btn btn-xs btn-success"
											onclick="startFormFlow({{item.id}})"> <i
												class="icon-search"></i>启用
										</a> <a v-show="item.state==2" class="btn btn-xs btn-warning"
											onclick="stopFormFlow({{item.id}})"> <i class="icon-search"></i>停用
										</a></td>
									</tr>
								</tbody>
							</table>

							<div class="widget-foot">
								<div class="clearfix"></div>
							</div>
						</div>

					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- modal -->
	<div id="assignSetSelectModal" class="modal fade" tabindex="-1"
		role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"></div>
	<!-- Matter ends -->

</div>
<!-- Mainbar ends -->
<div class="clearfix"></div>
</div>

<%@ include file="/common/script.jsp"%>

<script type="text/javascript">


var startFormFlow=function(id){
	$.ajax({
		type: 'POST',
  		dataType: 'Json',
  		url: rootUri + '/FormFlowBase/startFormFlow/'+id ,
  		success: function(data){
  			console.log(data);
  			if(data.status==0){
  				alert(data.message);
  			}
  			location.reload();
  		},
  		error:function(err){
  			console.log(err);
  			alert("系统出错");
  		}				
	});
};

var stopFormFlow=function(id){
	$.ajax({
		type: 'POST',
  		dataType: 'Json',
  		url: rootUri + '/FormFlowBase/stopFormFlow/'+id ,
  		success: function(data){
  			console.log(data);
  			if(data.status==0){
  				alert(data.message);
  			}
  			location.reload();
  		},
  		error:function(err){
  			alert("系统出错");
  			console.log(err);
  		}				
	});
};

var vueAssignSetSelectModal;
//设置流程弹窗打开事件
$('#assignSetSelectModal').on('shown.bs.modal', function(e) {
	 var btn = $(e.relatedTarget),
     index = btn.data("index"); 
	 var obj=vue.list[index];
	 
	 console.log(obj);
	 
	 if (!vueAssignSetSelectModal) {
		 vueAssignSetSelectModal = new Vue({
				el : '#assignSetSelectModal',
				data : {
					index :index,
					id:obj.id
				}
			});
		} else{
			vueAssignSetSelectModal.index = index;
	 		vueAssignSetSelectModal.id = obj.id;
		}
	 });

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