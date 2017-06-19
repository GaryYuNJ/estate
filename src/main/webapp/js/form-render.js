function calculateFormValues(data) {
	var data1;
	var dataVue = {};
	dataVue.tableName = data.filedsDefine.tableName;
	dataVue.formid = data.filedsDefine.id;
	dataVue.fieldsJson = eval('(' + data.filedsDefine.fieldsJson + ')');
	console.log(dataVue.fieldsJson);
	console.log(dataVue.tableName);
	var tempFields = dataVue.fieldsJson.fields;
	var tempFieldsEn = eval('(' + data.filedsEn + ')').fields;
	var tempValues = data.valueList[0];
	for (i = 0; i < tempFields.length; i++) {
		for (j = 0; j < tempFieldsEn.length; j++) {
			if (tempFieldsEn[j].cid == tempFields[i].cid) {
				var value = tempValues[tempFieldsEn[j].label.trim()];
				if (tempFields[i].field_type == "checkboxes") {
					// 当为多选字段时
					var valueArr = value.split(",");
					for (var p = 0; p < tempFields[i].field_options.options.length; p++) {
						var isChecked = false;// 防止数据错误，fields定义中checkbox的选型已经为checked
						for (var k = 0; k < valueArr.length; k++) {
							if (valueArr[k] == tempFields[i].field_options.options[p].label) {
								isChecked = true;
								break;
							}
						}
						tempFields[i].field_options.options[p].checked = isChecked;
					}
				}
				if (tempFields[i].field_type == "date") {
					value = new Date(value).Format("yyyy-MM-dd hh:mm");
				}
				tempFields[i].value = value;
				continue;
			}
		}
	}
	dataVue.fieldsJson = tempFields;
	data1 = {
		data : dataVue,
		values : tempValues
	};
	return data1;
};