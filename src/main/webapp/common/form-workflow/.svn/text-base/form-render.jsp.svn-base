<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="form-group" v-for="item in data.fieldsJson">
	<div v-if="item.field_type=='text'">
		<label class="control-label col-lg-3" for="name1">{{item.label}}</label>
		<div class="col-lg-6">
			<input type="text" class="form-control" v-model="item.value"
				v-bind:disabled="item.isDisable">
		</div>
	</div>
	<div v-if="item.field_type=='paragraph'">
		<label class="control-label col-lg-3" for="address">{{item.label}}</label>
		<div class="col-lg-6">
			<textarea class="form-control" v-model="item.value"
				v-bind:disabled="item.isDisable"></textarea>
		</div>
	</div>
	<div v-if="item.field_type=='checkboxes'">
		<label class="control-label col-lg-3" for="address">{{item.label}}</label>
		<div class="col-lg-6">
			<div class="check-box"
				v-for="checkboxItem in item.field_options.options">
				<label class="fb-option"> <input v-if="checkboxItem.checked"
					v-model="checkboxItem.checked" type="checkbox" checked="checked"
					v-bind:disabled="item.isDisable"> <input
					v-else="checkboxItem.checked" v-model="checkboxItem.checked"
					type="checkbox" v-bind:disabled="item.isDisable">
					{{checkboxItem.label}}
				</label>
			</div>
		</div>
	</div>
	<div v-if="item.field_type=='radio'">
		<label class="control-label col-lg-3" for="address">{{item.label}}</label>
		<div class="col-lg-6">
			<div class="radio" v-for="checkboxItem in item.field_options.options">
				<label class="fb-option"> <input v-if="checkboxItem.checked"
					value={{checkboxItem.label}} v-model="item.value" type="radio"
					name={{item.label}} checked="checked"
					v-bind:disabled="item.isDisable"> <input
					v-else="checkboxItem.checked" value={{checkboxItem.label}}
					v-model="item.value" type="radio" name={{item.label}}
					v-bind:disabled="item.isDisable"> {{checkboxItem.label}}
				</label>
			</div>
		</div>
	</div>
	<div v-if="item.field_type=='dropdown'">
		<label class="control-label col-lg-3">{{item.label}}</label>
		<div class="col-lg-6">
			<select class="form-control" v-model="item.value"
				v-bind:disabled="item.isDisable">
				<option value="">--- Please Select ---</option>
				<option v-for="selectItem in item.field_options.options">{{selectItem.label}}</option>
			</select>
		</div>
	</div>
	<div v-if="item.field_type=='linkdropdown'">
		<div class="col-lg-8"
			v-for="linkdropdownitem in item.field_options.options">
			<label class="control-label col-lg-3">{{linkdropdownitem.checked}}</label>
			<div class="col-lg-4">
				<select class="form-control" v-model="linkdropdownitem.selectKey"
					id="linkselect_{{$parent.$index}}_{{$index}}"
					onchange="linkdropdown_change({{$index}},{{$parent.$index}},this.options[this.options.selectedIndex])"
					v-bind:disabled="item.isDisable">
					<option value="">--- Please Select ---</option>
					<option v-for="linkdropdownselectItem in linkdropdownitem.datalist"
						value="{{linkdropdownselectItem.key}}">{{linkdropdownselectItem.value}}</option>
				</select>
			</div>
		</div>
	</div>
	<div v-if="item.field_type=='date'">
		<label class="control-label col-lg-3" for="address">{{item.label}}</label>
		<div class="col-lg-6">
			<input size="16" type="text" readonly class="form_datetime"
				v-model="item.value" v-bind:disabled="item.isDisable">
		</div>
	</div>
	<div v-if="item.field_type=='number'">
		<label class="control-label col-lg-3" for="name1">{{item.label}}</label>
		<div class="col-lg-6">
			<input type="text" class="form-control" v-model="item.value"
				v-bind:disabled="item.isDisable">
		</div>
	</div>
	<div v-if="item.field_type=='price'">
		<label class="control-label col-lg-3" for="name1">{{item.label}}</label>
		<div class="col-lg-6">
			<input type="text" class="form-control" v-model="item.value | price"
				v-bind:disabled="item.isDisable">
		</div>
	</div>
	<div v-if="item.field_type=='fileupload'">
		<label class="control-label col-lg-3">{{item.label}}</label>
		<div class="col-lg-6">
			<input type="file" id="file" name="file"
				onchange="ajaxFileUpload({{$index}},this.value);"
				v-bind:disabled="item.isDisable" /> <input type="hidden" id="pic"
				name="pic" />
			<table class="table table-striped table-bordered table-hover">
				<thead>
					<tr>
						<th>编号</th>
						<th>文件名称</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody>
					<tr v-for="itemfile in item.field_options.options">
						<td>{{$index+1}}</td>
						<td>{{itemfile.name}}</td>
						<td><a
							href="${rootUri}/fileupload/fileDownload?filename={{itemfile.name}}&filepath={{itemfile.path}}"><i>查看</i></a>
							<a v-if="!item.isDisable" style="padding-left: 10px;"
							onclick="deletefile({{$parent.$index}},{{$index}})"><i>删除</i></a></td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	<div v-if="item.field_type=='timespend'">
		<label class="control-label col-lg-3">{{item.label}}</label>
		<div class="col-lg-6">
			<div id="countdown_{{$index}}"></div>
		</div>
	</div>
	<div v-if="item.field_type=='table'">
		<label class="control-label col-lg-3">{{item.label}}</label>
		<div class="col-lg-6">
			<table class="table table-striped table-bordered table-hover">
				<thead>
					<tr>
						<th v-for="itemfield in item.field_options.options">{{itemfield.label}}</th>
						<th v-if="!item.isDisable">操作</th>
					</tr>
				</thead>
				<tbody>
					<tr v-for="itemvalues in item.value">
						<td v-for="itemvalue in itemvalues">{{itemvalue.value}}</td>
						<td v-if="!item.isDisable"><a v-if="!item.isDisable"
							href="#tableFieldDetial" data-pindex="{{$parent.$index}}"
							data-index="{{$index}}" data-toggle="modal"> <i></i>修改
						</a><a v-if="!item.isDisable" style="padding-left: 10px;"
							onclick="deleteTableValue({{$parent.$index}},{{$index}})"><i>删除</i></a></td>
					</tr>
				</tbody>
			</table>
			<a href="#tableFieldDetial" data-pindex="{{$index}}"
				data-toggle="modal" class="btn btn-xs btn-success"
				v-if="!item.isDisable"> <i class="icon-plus"></i> 增加
			</a>

		</div>
	</div>
	<div v-if="item.field_type=='member'">
		<label class="control-label col-lg-3">{{item.label}}</label>
		<div class="col-lg-6">
			<input type="text" class="form-control"
				style="width: 50%; display: inline; margin-right: 3px;"
				v-model="item.value" disabled><a v-if="!item.isDisable" href="#memberSelDetial"
				class="btn btn-info" data-toggle="modal"
				data-index="{{$index}}" style="color: #fafafa">选择用户</a>
		</div>
	</div>
</div>


<!-- modal -->
<div id="tableFieldDetial" class="modal fade" tabindex="-1"
	role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">×</button>
				<h4 class="modal-title">详细</h4>
			</div>
			<div class="modal-body">
				<div style="margin-bottom: 50%;">
					<div v-for="item in value">
						<label class="control-label col-lg-4" for="name1">{{item.label}}</label>
						<div class="col-lg-8">
							<input type="text" v-model="item.value"
								style="margin-bottom: 10px;">
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal"
					aria-hidden="true">取消</button>
				<button type="button" onclick="setTableValue()"
					class="btn btn-primary">确定</button>
			</div>
		</div>
	</div>
</div>

<div id="memberSelDetial" class="modal fade" tabindex="-1" role="dialog"
	aria-labelledby="myModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal"
					aria-hidden="true">×</button>
				<h4 class="modal-title">选择用户</h4>
			</div>
			<div class="modal-body">
				<div id="tree"></div>
				<hr style="border-bottom: 1px solid #928F8F">
				<label class="col-lg-2 control-label">用户：</label><select
					class="form-control" id="memberSel">
				</select>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal"
					aria-hidden="true">取消</button>
				<button type="button" onclick="setMemberSel()"
					class="btn btn-primary">确定</button>
				<input type="hidden" id="memberSelHidden">
			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	function setTableValue(){
		if(!vue.data.fieldsJson[vueTableFieldDetial.pindex].value){
			vue.data.fieldsJson[vueTableFieldDetial.pindex].value = [];
		}
		if(vue.data.fieldsJson[vueTableFieldDetial.pindex].value.length == vueTableFieldDetial.index){
			vue.data.fieldsJson[vueTableFieldDetial.pindex].value.push(vueTableFieldDetial.value);
		}
		$("#tableFieldDetial").modal('hide');
	}
	
	function deletefile(pindex,index){
		vue.data.fieldsJson[pindex].field_options.options.splice(index,1);
	}
	
	function deleteTableValue(pindex,index){
		vue.data.fieldsJson[pindex].value.splice(index,1);
	}
	
	function setMemberSel(){
		var index = $('#memberSelHidden').val();
		if(index){
			vue.data.fieldsJson[index].value = $('#memberSel').val();
		}
	}
</script>