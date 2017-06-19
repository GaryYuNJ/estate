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
// 组织架构树点击事件
$('#jstree_org').on(
		"changed.jstree",
		function(e, data) {
			$("#orgId").val(data.node.id);
			var data = {
					"id" : data.node.id
			};
			$.post(rootUri + "/organization/getOrgById.json", data,
					function(data, state) {
							console.log(data);
							$("#orgpId").val(data.pid);
							$("#orgPhoneId").val(data.phone);
							$("#orgAddressId").val(data.address);
							$("#orgNameId").val(data.name);
					});
		});

$("#saveOrgbuttonId").click(
		function() {
			$("#saveOrgbuttonId").attr('disabled', "true");
			if($("#orgNameId").val()==""){
				alert("名称不能为空");
				return;
			}
			var orgModel = {};
			$.each($("#orgFormId").serializeArray(), function(i, field) {
				orgModel[field.name] = field.value;
			});
			var data = {
				"orgModelJson" : JSON.stringify(orgModel)
			}
			$.post(rootUri + "/organization/createOrupdate.json", data,
					function(data, state) {
						// console.log(state);
						if (0 == data.status) {
							alert("保存失败");
						} else {
							if ($("#orgId").val() == "") {
								$("#orgId").val(data.message);
								var node = {
									"id" : $("#orgId").val(),
									"text" : $("#orgNameId").val()
								};
								nodeCreate(node);
							} else {
								nodeRename($("#orgNameId").val());
							}
						}
					});
			$("#saveOrgbuttonId").removeAttr('disabled');
		})
// 重命名节点
function nodeRename(name) {
	var ref = $('#jstree_org').jstree(true), 
	sel = ref.get_selected();
	if (!sel.length) {
		return false;
	}
	sel = sel[0];
	ref.edit(sel,name);
};
// 创建节点
function nodeCreate(node) {
	var ref = $('#jstree_org').jstree(true), sel = ref.get_selected();
	if (!sel.length) {
		return false;
	}
	sel = sel[0];
	sel = ref.create_node(sel, node);
};

function nodeCreatePre(node) {
	var ref = $('#jstree_org').jstree(true), sel = ref.get_selected();
	if (sel.length) {
		$("#orgpId").val(sel[0]);
	}
	$("#orgId").val("");
	$("#orgPhoneId").val("");
	$("#orgAddressId").val("");
	$("#orgNameId").val("");
};
//删除节点
function nodeDelete(){
	$("#nodeDeleteBId").attr('disabled', "true");
	var ref = $('#jstree_org').jstree(true), 
	sel = ref.get_selected();
	if (!sel.length) {
		return false;
	}
	sel = sel[0];
	$.post(rootUri + "/organization/deleteOrg.json", {"id":sel},
			function(data, state) {
				if (0 == data.status) {
					alert("删除失败，该组织架构包含子组织架构或者成员！");
				} else {
					ref.delete_node(sel);
				}
			});
	console.log(sel);
	$("#nodeDeleteBId").removeAttr('disabled');
}

