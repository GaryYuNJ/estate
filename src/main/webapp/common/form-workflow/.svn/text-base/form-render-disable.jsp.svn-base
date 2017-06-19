<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="form-group">
	<div>
		<label class="control-label col-lg-3" for="name1">申请人</label>
		<div class="col-lg-6">
			<input type="text" class="form-control" v-model="values.inputUser"
				disabled>
		</div>
	</div>
</div>
<div class="form-group" v-for="item in data.fieldsJson">
	<div v-if="item.field_type=='text'">
		<label class="control-label col-lg-3" for="name1">{{item.label}}</label>
		<div class="col-lg-6">
			<input type="text" class="form-control" v-model="item.value" disabled>
		</div>
	</div>
	<div v-if="item.field_type=='paragraph'">
		<label class="control-label col-lg-3" for="address">{{item.label}}</label>
		<div class="col-lg-6">
			<textarea class="form-control" v-model="item.value" disabled></textarea>
		</div>
	</div>
	<div v-if="item.field_type=='checkboxes'">
		<label class="control-label col-lg-3" for="address">{{item.label}}</label>
		<div class="col-lg-6">
			<div class="check-box"
				v-for="checkboxItem in item.field_options.options">
				<label class="fb-option"> <input disabled
					v-if="checkboxItem.checked" v-model="checkboxItem.checked"
					type="checkbox" checked="checked"> <input disabled
					v-else="checkboxItem.checked" v-model="checkboxItem.checked"
					type="checkbox"> {{checkboxItem.label}}
				</label>
			</div>
		</div>
	</div>
	<div v-if="item.field_type=='radio'">
		<label class="control-label col-lg-3" for="address">{{item.label}}</label>
		<div class="col-lg-6">
			<div class="radio" v-for="checkboxItem in item.field_options.options">
				<label class="fb-option"> <input disabled
					v-if="checkboxItem.checked" value={{checkboxItem.label}}
					v-model="item.value" type="radio" name={{item.label}}
					checked="checked"> <input disabled
					v-else="checkboxItem.checked" value={{checkboxItem.label}}
					v-model="item.value" type="radio" name={{item.label}}>
					{{checkboxItem.label}}
				</label>
			</div>
		</div>
	</div>
	<div v-if="item.field_type=='dropdown'">
		<label class="control-label col-lg-3">{{item.label}}</label>
		<div class="col-lg-6">
			<select disabled class="form-control" v-model="item.value">
				<option value="">--- Please Select ---</option>
				<option v-for="selectItem in item.field_options.options">{{selectItem.label}}</option>
			</select>
		</div>
	</div>
	<div v-if="item.field_type=='date'">
		<label class="control-label col-lg-3" for="address">{{item.label}}</label>
		<div class="col-lg-6">
			<input disabled size="16" type="text" readonly class="form_datetime"
				v-model="item.value">
		</div>
	</div>
	<div v-if="item.field_type=='number'">
		<label class="control-label col-lg-3" for="name1">{{item.label}}</label>
		<div class="col-lg-6">
			<input disabled type="text" class="form-control" v-model="item.value">
		</div>
	</div>
	<div v-if="item.field_type=='price'">
		<label class="control-label col-lg-3" for="name1">{{item.label}}</label>
		<div class="col-lg-6">
			<input disabled type="text" class="form-control"
				v-model="item.value | price">
		</div>
	</div>
</div>
