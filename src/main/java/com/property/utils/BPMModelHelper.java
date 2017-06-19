package com.property.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.persistence.entity.ModelEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service("bpmmodelHelper")
public class BPMModelHelper {

	@Autowired
	private RepositoryService aRepositoryService;

	@Autowired
	private ObjectMapper aObjectMapper;

	/**
	 * 新建bpmnmodel
	 * 
	 * @param name:流程名称
	 * @param key:流程key
	 * @param description:描述
	 */
	public Model newModel(String name, String key, String description) {
		Model model = new ModelEntity();
		try {
			model.setName(name);
			model.setKey(key);
			// model.setVersion(1);
			ObjectNode modelJson = (ObjectNode) aObjectMapper.readTree("{}");
			modelJson.put(org.activiti.editor.constants.ModelDataJsonConstants.MODEL_NAME, model.getName());
			modelJson.put(org.activiti.editor.constants.ModelDataJsonConstants.MODEL_DESCRIPTION, description);
			model.setMetaInfo(modelJson.toString());
			aRepositoryService.saveModel(model);

			// 保存模型资源
			aRepositoryService.addModelEditorSource(model.getId(),
					("{\"resourceId\":\"" + model.getId() + "\",\"properties\":{\"process_id\":\"" + key
							+ "\",\"name\":\"" + name + "\",\"documentation\":\"" + description
							+ "\",\"process_author\":\"\",\"process_version\":\"\",\"process_namespace\":\"http://www.activiti.org/processdef\",\"executionlisteners\":\"\",\"eventlisteners\":\"\",\"signaldefinitions\":\"\",\"messagedefinitions\":\"\"},\"stencil\":{\"id\":\"BPMNDiagram\"},\"childShapes\":[],\"bounds\":{\"lowerRight\":{\"x\":1200,\"y\":1050},\"upperLeft\":{\"x\":0,\"y\":0}},\"stencilset\":{\"url\":\"stencilsets/bpmn2.0/bpmn2.0.json\",\"namespace\":\"http://b3mn.org/stencilset/bpmn2.0#\"},\"ssextensions\":[]}")
									.getBytes("utf-8"));

			InputStream svgStream = new ByteArrayInputStream(
					"<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:oryx=\"http://oryx-editor.org\"  width=\"50\" height=\"50\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:svg=\"http://www.w3.org/2000/svg\"><defs/><svg id=\"underlay-container\"/><g stroke=\"none\" font-family=\"Verdana, sans-serif\" font-size-adjust=\"none\" font-style=\"normal\" font-variant=\"normal\" font-weight=\"normal\" line-heigth=\"normal\" font-size=\"12\"><g class=\"stencils\"><g class=\"me\"/><g class=\"children\"/><g class=\"edge\"/></g><g class=\"svgcontainer\"><g display=\"none\"><rect x=\"0\" y=\"0\" stroke-width=\"1\" stroke=\"#777777\" fill=\"none\" stroke-dasharray=\"2,2\" pointer-events=\"none\"/></g><g display=\"none\"><path stroke-width=\"1\" stroke=\"silver\" fill=\"none\" stroke-dasharray=\"5,5\" pointer-events=\"none\"/></g><g display=\"none\"><path stroke-width=\"1\" stroke=\"silver\" fill=\"none\" stroke-dasharray=\"5,5\" pointer-events=\"none\"/></g><g/></g></g></svg>"
							.getBytes("utf-8"));
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
			throw new ActivitiException("Error saving model", e);
		}
		return model;
	}

	/**
	 * 部署流程 ；将bpmnmodel转换成xml并部署
	 */
	public void deploy(String modelid) {
		try {
			Model modelData = aRepositoryService.getModel(modelid);
			ObjectNode modelNode = (ObjectNode) aObjectMapper
					.readTree(aRepositoryService.getModelEditorSource(modelData.getId()));
			byte[] bpmnBytes = null;
			BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
			bpmnBytes = new BpmnXMLConverter().convertToXML(model);
			String processName = modelData.getName() + ".bpmn20.xml";
			Deployment deployment = aRepositoryService.createDeployment().name(modelData.getName())
					.addString(processName, new String(bpmnBytes)).deploy();
		} catch (Exception e) {
			throw new ActivitiException("Error saving model", e);
		}
	}
}
