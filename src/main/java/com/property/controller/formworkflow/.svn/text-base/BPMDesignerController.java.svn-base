package com.property.controller.formworkflow;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigInteger;

import javax.annotation.Resource;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.property.data.FormDefineFlowConfig;
import com.property.model.FormTableDefine;
import com.property.service.IFormService;
import com.property.utils.BPMModelHelper;
import com.property.utils.CommonHelper;

@Controller
@RequestMapping(value = "bpm")
public class BPMDesignerController {
	protected static final Logger LOGGER = LoggerFactory.getLogger(BPMDesignerController.class);
	@Autowired
	private RepositoryService aRepositoryService;

	@Autowired
	private ObjectMapper aObjectMapper;

	@Resource
	private IFormService formService;
	@Resource
	private BPMModelHelper bpmmodelHelper;

	/**
	 * 新建流程model
	 */
	@RequestMapping(value = "/addModel", method = RequestMethod.POST)
	@ResponseBody
	public String addModel(@RequestParam("id") Integer id, @RequestParam("name") String name,
			@RequestParam("key") String key, @RequestParam("description") String description) {
		String url = "/bpm/modeler.html?modelId=";
		Boolean isBpm = false;
		try {
			FormTableDefine formTableDefine = formService.getFormTableDefineById(id);
			FormDefineFlowConfig formDefineFlowConfig=new FormDefineFlowConfig();
			String activitiJson = formTableDefine.getActivitiJson();
			if (activitiJson != null && !activitiJson.isEmpty()) {
			    formDefineFlowConfig =formTableDefine.getFormDefineFlowConfig();
				if (formDefineFlowConfig.getFlowType().equals("complex")) {
					JSONObject jsonActiviti = formDefineFlowConfig.getContent();
					BigInteger modelid = jsonActiviti.getBigInteger("BPMModelID");
					url = url + modelid;
					isBpm = true;
				}
			}
			if (!isBpm) {
				// 保存模型
				Model model = bpmmodelHelper.newModel(name, key, description);
				if (model != null) {
					formDefineFlowConfig.setFlowStatus(0);
					formDefineFlowConfig.setFlowType("complex");
					formDefineFlowConfig.setContent(JSON.parseObject("{\"BPMModelID\":" + model.getId() + "}"));
					formTableDefine.setActivitiJson(JSON.toJSONString(formDefineFlowConfig));
					formService.updateFormTableDefineById(formTableDefine);
					url = url + model.getId();
				}
			}
		} catch (Exception e) {
			throw new ActivitiException("Error saving model", e);
		}
		return "{\"url\":\"" + url + "\"}";
	}

	/**
	 * 获取流程model
	 */
	@RequestMapping(value = "/model/{modelId}/json", method = RequestMethod.GET)
	@ResponseBody
	public ObjectNode getEditorJson(@PathVariable String modelId) {
		ObjectNode modelNode = null;
		Model model = aRepositoryService.getModel(modelId);

		if (model != null) {
			try {
				if (StringUtils.isNotEmpty(model.getMetaInfo())) {
					modelNode = (ObjectNode) aObjectMapper.readTree(model.getMetaInfo());
				} else {
					modelNode = aObjectMapper.createObjectNode();
					modelNode.put(org.activiti.editor.constants.ModelDataJsonConstants.MODEL_NAME, model.getName());
				}
				modelNode.put(org.activiti.editor.constants.ModelDataJsonConstants.MODEL_ID, model.getId());
				ObjectNode editorJsonNode = (ObjectNode) aObjectMapper
						.readTree(new String(aRepositoryService.getModelEditorSource(model.getId()), "utf-8"));
				modelNode.putPOJO("model", editorJsonNode);
			} catch (Exception e) {
				LOGGER.error("Error creating model JSON", e);
				throw new ActivitiException("Error creating model JSON", e);
			}
		}
		return modelNode;
	}

	/**
	 * 保存流程model
	 */
	@RequestMapping(value = "/model/{modelId}/save", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.OK)
	public void saveModel(@PathVariable String modelId, @RequestBody MultiValueMap<String, String> values) {
		try {
			Model model = aRepositoryService.getModel(modelId);

			ObjectNode modelJson = (ObjectNode) aObjectMapper.readTree(model.getMetaInfo());

			modelJson.put(org.activiti.editor.constants.ModelDataJsonConstants.MODEL_NAME, values.getFirst("name"));
			modelJson.put(org.activiti.editor.constants.ModelDataJsonConstants.MODEL_DESCRIPTION,
					values.getFirst("description"));
			model.setMetaInfo(modelJson.toString());
			model.setName(values.getFirst("name"));

			aRepositoryService.saveModel(model);

			aRepositoryService.addModelEditorSource(model.getId(), values.getFirst("json_xml").getBytes("utf-8"));

			InputStream svgStream = new ByteArrayInputStream(values.getFirst("svg_xml").getBytes("utf-8"));
			TranscoderInput input = new TranscoderInput(svgStream);

			PNGTranscoder transcoder = new PNGTranscoder();
			// Setup output
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			TranscoderOutput output = new TranscoderOutput(outStream);

			// Do the transformation
			transcoder.transcode(input, output);
			final byte[] result = outStream.toByteArray();
			aRepositoryService.addModelEditorSourceExtra(model.getId(), result);
			outStream.close();
		} catch (Exception e) {
			// LOGGER.error("Error saving model", e);
			throw new ActivitiException("Error saving model", e);
		}
	}

	/**
	 * 流程设计器资源文件（语言）
	 */
	@RequestMapping(value = { "/editor/stencilset" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET }, produces = {
					"application/json;charset=utf-8" })
	@ResponseBody
	public String getStencilset() {
		InputStream stencilsetStream = getClass().getClassLoader().getResourceAsStream("stencilset.json");
		try {
			return IOUtils.toString(stencilsetStream, "utf-8");
		} catch (Exception e) {
			throw new ActivitiException("Error while loading stencil set", e);
		}
	}
}
