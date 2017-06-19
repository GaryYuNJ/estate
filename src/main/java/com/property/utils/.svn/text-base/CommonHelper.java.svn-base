package com.property.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.hp.hpl.sparta.xpath.ThisNodeTest;
import com.property.data.FlowConfigContent;
import com.property.data.FormDefineFlowConfig;
import com.property.data.FormField;
import com.property.model.BUser;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class CommonHelper {
	/**
	 * 中文转拼音;不考虑多音字
	 */
	public static String getPinYin(String strs) {
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		char[] ch = strs.trim().toCharArray();
		StringBuffer buffer = new StringBuffer("");

		try {
			for (int i = 0; i < ch.length; i++) {
				// unicode，bytes应该也可以.
				if (Character.toString(ch[i]).matches("[\u4e00-\u9fa5]+")) {
					String[] temp = PinyinHelper.toHanyuPinyinStringArray(ch[i], format);
					buffer.append(temp[0]);// :结果"?"已经查出，但是音调是3声时不显示myeclipse8.6和eclipse
				} else {
					buffer.append(Character.toString(ch[i]));
				}
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

	
	/**
	 *序列化FlowConfigContent json 
	 */
	public static List<FlowConfigContent> getFlowConfigContent(String flowconfigStr) {
		List<FlowConfigContent> flowConfigContents= new ArrayList<FlowConfigContent>();
		if(flowconfigStr.isEmpty()||flowconfigStr==null){
			return flowConfigContents;
		}
		Object json = JSONArray.parse(flowconfigStr);		
		flowConfigContents = JSON.parseArray(json + "", FlowConfigContent.class);
		return flowConfigContents;
	}
	
	/**
	 *序列化FlowConfigContent json 
	 */
	public static FlowConfigContent getFlowConfigContent(String flowconfigStr,String taskid) {
		List<FlowConfigContent> flowConfigContents = getFlowConfigContent(flowconfigStr);
		FlowConfigContent flowConfigContent = new FlowConfigContent();
		for(FlowConfigContent item:flowConfigContents) {
			if(item.getTaskid().equals(taskid)){
				flowConfigContent = item;
			}
		}
		return flowConfigContent;
	}
}
