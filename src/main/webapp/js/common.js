//判断是否是流程变量占位符
function isExpression(str){
	var pattern = /^\$\{(.*)\}$/;
	return pattern.test(str);
};

// 正则表达式判断 字符串是否只有字母数字和下划线，无特殊字符
function regStrName(str) {
	var pattern2 = /^[\u4E00-\u9FA5\w\d]+$/;
	return pattern2.test(str);
};
// 根据QueryString参数名称获取值
function getQueryStringByName(name) {
	var result = location.search.match(new RegExp(
			"[\?\&]" + name + "=([^\&]+)", "i"));
	if (result == null || result.length < 1) {
		return "";
	}
	return result[1];
};

// 流程参数中英转换
var ACTIVITY_TYPE={// 任务类型
		"userTask": "用户任务",
		"serviceTask": "系统任务",
		"startEvent": "开始节点",
		"endEvent":"结束节点",
		"exclusiveGateway": "条件判断节点(系统自动根据条件处理)",
		"inclusiveGateway": "并行处理任务",
		"callActivity": "调用活动",
		"subProcess":"子流程",
		// key
		"name":"任务名称",
		"type":"任务类型"
		};

function getZhActivityType(key){
	var temp=ACTIVITY_TYPE[key];
	if(!temp)
		temp=key;
	return temp;
};

// 表单字段验证
function validateFieldsValue(fields){
	var resultMessage="";
	for(var i=0;i<fields.length;i++){
		// 是否必填项
		if(fields[i].required){
			if(fields[i].field_type=="checkboxes"){
				var ischecked=false;
				for(var j=0;j<fields[i].field_options.options.length;j++){
					if(fields[i].field_options.options[j].checked)
						ischecked=true;
				}
				if(!ischecked)
					resultMessage+=fields[i].label+" 必填\r\n" ;
			}else if(fields[i].field_type=="linkdropdown"){
				var isSelect=true;
				var labelTemp="";
				for(var j=0;j<fields[i].field_options.options.length;j++){
					if(!fields[i].field_options.options[j].selectKey){
						isSelect=false;
					}
					labelTemp+=fields[i].field_options.options[j].checked;
					if(j<fields[i].field_options.options.length-1)
						labelTemp+="|";
				}
				if(!isSelect)
					resultMessage+=labelTemp+" 必填\r\n" ;
			}else if(!fields[i].value){
				resultMessage+=fields[i].label+" 必填\r\n" ;
			}
		}
		if(fields[i].field_type=="text" && fields[i].value && fields[i].value.length>50){
			resultMessage+=fields[i].label+" 长度不能超过50\r\n" ;
		}
		if(fields[i].field_type=="paragraph" && fields[i].value && fields[i].value.length>250){
			resultMessage+=fields[i].label+" 长度不能超过250\r\n" ;
		}
		if((fields[i].field_type=="price"||fields[i].field_type=="number") && fields[i].value && isNaN(fields[i].value)){
			resultMessage+=fields[i].label+" 必须为数字\r\n" ;
		}
		if(fields[i].field_type=="Date" && fields[i].value && isNaN(Date.parse(fields[i].value))){
			resultMessage+=fields[i].label+" 日期不正确\r\n" ;
		}
	}
	return resultMessage;
}

function fmoney(s, n) {   
   n = n > 0 && n <= 20 ? n : 2;   
   s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";   
   var l = s.split(".")[0].split("").reverse(),   
   r = s.split(".")[1];   
   t = "";   
   for(i = 0; i < l.length; i ++ )   
   {   
      t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");   
   }   
   return t.split("").reverse().join("") + "." + r;   
} 

function rmoney(s) {   
   return parseFloat(s.replace(/[^\d\.-]/g, ""));   
} 

Date.prototype.Format = function (fmt) { // author: meizz
    var o = {
        "M+": this.getMonth() + 1, // 月份
        "d+": this.getDate(), // 日
        "h+": this.getHours(), // 小时
        "m+": this.getMinutes(), // 分
        "s+": this.getSeconds(), // 秒
        "q+": Math.floor((this.getMonth() + 3) / 3), // 季度
        "S": this.getMilliseconds() // 毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};
	
Vue.filter('time', function (value) {
	return new Date(value).Format("yyyy-MM-dd hh:mm:ss")
});

Vue.filter('price', function (s) { 
	if(s&&!isNaN(s)){
	   s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(2) + "";   
	   var l = s.split(".")[0].split("").reverse(),   
	   r = s.split(".")[1];   
	   t = "";   
	   for(i = 0; i < l.length; i ++ )   
	   {   
	      t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");   
	   }   
	   return t.split("").reverse().join("") + "." + r;   
	  }else return s;
});

var linkdropdown_change = function(index, pindex, option) {
	vue.data.fieldsJson[pindex].field_options.options[index].selectKey = option.value;
	vue.data.fieldsJson[pindex].field_options.options[index].selectValue = option.text;
	if (index == vue.data.fieldsJson[pindex].field_options.options.length - 1) {
		return;
	}

	$.ajax({
				type : 'GET',
				dataType : 'Json',
				async : false,
				url : vue.data.fieldsJson[pindex].field_options.options[index + 1].label
						+ option.value,// rootUri +
										// '/dropdowndata/getProvince.json',
				success : function(data) {
					var selectDom = $("#linkselect_" + pindex + "_"
							+ (index + 1));
					var optionsHtml = '<option value="">--- Please Select ---</option>';
					for (var k = 0; k < data.length; k++) {
						optionsHtml += '<option value="'+data[k].key+'">'
								+ data[k].value + '</option>';
					}
					selectDom.html(optionsHtml);
				},
				error : function(err) {
					console.log(err);
					alert("系统出错！")
				}
			});
};

function ajaxFileUpload(index,file) {
	//var picpath="";
	var filename = file.split('\\').pop();
	$.ajaxFileUpload({
		url : rootUri + '/fileupload/fileUpload',
		fileElementId:'file',
		dataType : "json",
		success: function(data){
			 alert(data.message);
			 //if(!vue.data.fieldsJson[index].field_options.options)
				 //vue.data.fieldsJson[index].field_options.options = [];
			 vue.data.fieldsJson[index].field_options.options.push({name:filename,path:data.fileName});
			 //vue.data.fieldsJson[index].field_options.options.push({name:filename,path:data.fileName});
			 vue.data.fieldsJson[index].value = "true";
			 // picpath = $("#pic").val() + data.fileName+",";
			 // $("#pic").val(picpath);
		},
		error: function(data){
		     alert("上传失败");
		}
	});
}

function initTimeSpend(index,timeSpendType,dateTask,dateProcessInstance){
	var ts;
	var newYear = true;
	if(timeSpendType=='task'){
		if(dateTask)
			ts = new Date(dateTask);
		else
			// return;
			ts = new Date(2016,11,12);
	}
	if(timeSpendType=='processinstance'){
		if(dateProcessInstance)
			ts = new Date(dateProcessInstance);
		else
			// return;
			ts = new Date(2016,11,13);
	}
	vue.data.fieldsJson[index].value = "true";
	$('#countdown_'+index).countdown({
		timestamp : ts,
		callback : function(days, hours, minutes, seconds) {
		}
	});
}