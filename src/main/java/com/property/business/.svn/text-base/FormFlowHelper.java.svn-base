package com.property.business;

import java.math.BigInteger;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.property.data.APIMessage;
import com.property.data.FormDefineFlowConfig;
import com.property.utils.BPMModelHelper;

@Component("formHelper")
public class FormFlowHelper {
	@Resource
	private BPMModelHelper bpmmodelHelper;

	/**
	 * 部署 流程
	 */
	public APIMessage deployProcess(FormDefineFlowConfig formdefineflowconfig) {
		APIMessage result = new APIMessage();
		switch (formdefineflowconfig.getFlowType()) {
		case "complex":
			deployProcessComplex(formdefineflowconfig.getContent());
			result.setStatus(1);
			break;
		case "simple":
			deployProcessSimple(formdefineflowconfig.getContent());
			result.setStatus(1);
			break;
		default:
			result.setStatus(0);
			result.setMessage("流程设置错误");
			break;
		}
		return result;
	}

	private void deployProcessComplex(JSONObject bpmModelID){
		BigInteger modelid = bpmModelID.getBigInteger("BPMModelID");
		bpmmodelHelper.deploy(modelid.toString());
	}
	
	private void deployProcessSimple(JSONObject bpmModelID){
		
	}
}
