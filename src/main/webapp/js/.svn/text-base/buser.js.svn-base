//查询部分
$('#jstree_org').jstree({
	"core" : {
		"multiple" : false,
		"animation" : 0,
		"check_callback" : true,
		"themes" : {
			"stripes" : true
		},
		'data' : {
			'url' : rootUri + "/organization/getAll.json",
			'data' : function(node) {
			}
		}
	},
	"types" : {
		"#" : {
			"max_children" : 1,
			"max_depth" : 6,
			"valid_children" : [ "root" ]
		},
		"root" : {
			"icon" : rootUri + "/js/themes/default/tree_icon.png",
			"valid_children" : [ "default" ]
		},
		"default" : {
			"valid_children" : [ "default", "file" ]
		},
		"file" : {
			"icon" : "glyphicon glyphicon-file",
			"valid_children" : []
		}
	},
	"plugins" : [ "search", "types", "wholerow" ],
});
// 显示用户详情内容
function showBuserDetail(userId) {
	/*$("#icon_group_list1").click();
	$('#myTab a:first').click();
	if ($("#icon_group_list2 i.icon-chevron-down").length > 0) {
		// console.log("1");
		$("#icon_group_list2").click();
	}*/
	if (null != userId && "" != userId) {
		userEditPre(userId);
	} else {
		initBUserForm();
	}
};
// 添加新用户
$("#addBuserButton").click(function() {
	showBuserDetail();
});

// 初始化FORM
function initBUserForm() {
	$("#loginId").val("");
	$("#nameId").val("");
	$("#phoneId").val("");
	$("#emailId").val("");
	$('#statusId').bootstrapSwitch('setState', true);
	$('#typeId').bootstrapSwitch('setState', false);
	$("#userId").val("");
	$("#orid").val("");
	$('#jstree_org_buser').jstree(true).deselect_all();
}
// 编辑资源
function userEditPre(row) {
	$.post(rootUri + "/buser/getUserById.json", {
		"id" : row
	}, function(data) {
		$("#loginId").val(data.loginid);
		$("#nameId").val(data.name);
		$("#phoneId").val(data.phone);
		$("#emailId").val(data.email);
		$("#statusId").val(data.status);
		$("#userId").val(data.id);
		$("#orid").val(data.orid);
		if(0 == data.status){
			$('#statusId').bootstrapSwitch('setState', false);
		}else{
			$('#statusId').bootstrapSwitch('setState',true);
		}
		if(1 == data.type){
			$('#typeId').bootstrapSwitch('setState', true);
		}else{
			$('#typeId').bootstrapSwitch('setState', false);
		}
		$('#jstree_org_buser').jstree(true).deselect_all();
		$('#jstree_org_buser').jstree(true).select_node(data.orid);
	});
}

var pageNumber = 1;
$('#buserTableId')
		.bootstrapTable(
				{
					method : 'get',
					url : rootUri + "/buser/buserSearch.json",
					dataType : "json",
					pageSize : 10,
					pageList : [ 10, 25, 50 ], // 可供选择的每页的行数（*）
					pageNumber : pageNumber,
					pagination : true, // 分页
					singleSelect : false,
					striped : true,
					idField : "id", // 标识哪个字段为id主键
					sidePagination : "server", // 服务端处理分页
					columns : [
							{
								title : '登陆名称',
								field : 'loginid',
								align : 'center',
								valign : 'middle'
							},
							{
								title : '显示名称',
								field : 'name',
								align : 'center',
								valign : 'middle',
							},
							{
								title : '电话',
								field : 'phone',
								align : 'center',
								valign : 'middle',
							},
							{
								title : '身份',
								field : 'type',
								align : 'center',
								formatter : function(value, row, index) {
									if (value == "1") {
										return '<span class="label btn-warning">主管</span>';
									} else {
										return '<span class="label label-success">成员</span>';
									}
								}
							},
							{
								title : '状态',
								field : 'status',
								align : 'center',
								formatter : function(value, row, index) {
									if (value == "1") {
										return '<span class="label label-success">可用</span>';
									} else {
										return '<span class="label label-danger">不可用</span>';
									}
								}
							},
							{
								title : '操作',
								field : 'id',
								align : 'center',
								formatter : function(value, row, index) {
									var e = '<button class="btn btn-xs btn-warning" onclick="showBuserDetail(\''
											+ value
											+ '\')" data-toggle="modal" data-target="#myModal"><i class="icon-pencil"></i> </button>  ';
									var d = '<button class="btn btn-xs btn-danger" onclick="deleteBuser(\''
											+ row.id
											+ '\')"><i class="icon-remove"></i> </button>';
									return e + d;
								}
							} ],
					formatLoadingMessage : function() {
						return "请稍等，正在加载中...";
					}
				});
// 自定义查询
$('#dosearch').click(function() {
	$("#queryOrgId").val("");
	refreshSearch();
});
// 组织架构树数查询
$('#jstree_org').on("changed.jstree", function(e, data) {
	$("#queryOrgId").val(data.node.id);
	//console.log("dddd");
	refreshSearch();
});

// 查询刷新
function refreshSearch() {
	var params = $('#buserTableId').bootstrapTable('getOptions')
	params.queryParams = function(params) {
		// 定义参数
		var search = {};
		// 遍历form 组装json
		$.each($("#searchform").serializeArray(), function(i, field) {
			// console.info(field.name + ":" + field.value + " ");
			// 可以添加提交验证
			search[field.name] = field.value;
		});
		// 参数转为json字符串，并赋给search变量 ,JSON.stringify <ie7不支持，有第三方解决插件
		params.search = JSON.stringify(search);
		return params;
	}
	//console.info(params);
	$('#buserTableId').bootstrapTable('refresh', params)
}
// 维护部分
$('#jstree_org_buser').jstree({
	"core" : {
		"multiple" : false,
		"animation" : 0,
		"check_callback" : true,
		"themes" : {
			"stripes" : true
		},
		'data' : {
			'url' : rootUri + "/organization/getAll.json",
			'data' : function(node) {
			}
		}
	},
	"types" : {
		"#" : {
			"max_children" : 1,
			"max_depth" : 6,
			"valid_children" : [ "root" ]
		},
		"root" : {
			"icon" : rootUri + "/js/themes/default/tree_icon.png",
			"valid_children" : [ "default" ]
		},
		"default" : {
			"valid_children" : [ "default", "file" ]
		},
		"file" : {
			"icon" : "glyphicon glyphicon-file",
			"valid_children" : []
		}
	},
	"plugins" : [ "search", "types", "wholerow" ],
});

// 组织架构树点击事件
$('#jstree_org_buser').on("select_node.jstree", function(e, node) {
	$("#orid").val(node.node.id);
});
// 添加资源
function saveUser() {
	$("#BuserButtonId").attr('disabled', "true");
	if ($("#loginId").val().length < 1) {
		alert("登陆名不能为空");
		$("#BuserButtonId").removeAttr('disabled');
		return;
	}
	var userModel = {};
	$.each($("#userFormId").serializeArray(), function(i, field) {
		if(field.name=="status"){
			if($('#statusId2').is(':checked')){
				userModel["status"] =1;
			}else{
				userModel["status"] =0;
			}
		}else if(field.name=="type"){
			if($('#typeId2').is(':checked')){
				userModel["type"] =1;
			}else{
				userModel["type"] =0;
			}
		}else{
			userModel[field.name] = field.value;
		}
	});
	var data = {
		"bUserModelJson" : JSON.stringify(userModel)
	}
	$.post(rootUri + "/buser/createOrupdate.json", data, function(data, state) {
		console.log(state);
		if (0 < data.status) {
			$("#userId").val(data.message);
			alert("保存成功！");
		} else {
			alert("后台异常！");
		}
	});
	$("#BuserButtonId").removeAttr('disabled');
	refreshSearch();
	/*if ($("#icon_group_list1 i.icon-chevron-down").length > 0) {
		// console.log("1");
		$("#icon_group_list1").click();
	}*/
}

function deleteBuser(id) {
	if (confirm("是否确认删除此用户？")) {
		$.post(rootUri + "/buser/delete.json", {"id":id}, function(data, state) {
			// console.log(state);
			if (0 < data.status) {
				alert("删除成功！");
				refreshSearch();
			} else {
				alert("删除失败！");
			}
		});
	}
}
