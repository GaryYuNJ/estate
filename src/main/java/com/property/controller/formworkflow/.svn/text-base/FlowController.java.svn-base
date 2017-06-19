package com.property.controller.formworkflow;

import javax.annotation.Resource;
import java.io.InputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.property.service.*;
import com.property.business.*;
import com.property.data.APIMessage;
import com.property.data.FormDefineFlowConfig;
import com.property.model.*;
import com.property.utils.ActivityUtil;
import com.property.utils.BPMModelHelper;
import com.property.utils.CommonHelper;

import org.activiti.engine.runtime.ProcessInstance;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.IdentityService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.SubProcessActivityBehavior;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.ReadOnlyProcessDefinition;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.jobexecutor.*;
import org.activiti.engine.impl.task.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 流程图 跟踪相关接口
 */
@Controller
@RequestMapping(value = "Flow")
public class FlowController {
	@Resource
	private FormFlowHelper formFlowHelper;
	@Resource
	private IFlowConfigService flowconfigService;
	@Resource
	private IFormDynService formDynService;
	@Resource
	private IFormService formService;
	@Resource
	private IActivitiService activitiService;
	@Resource
	private IBuserService buserService;
	@Resource
	private ActivitiHelper activitiHelper;
	@Resource
	private BPMModelHelper bpmmodelHelper;
	@Autowired
	private RepositoryService aRepositoryService;
	@Autowired
	private RuntimeService aRuntimeService;
	@Autowired
	private TaskService aTaskService;
	@Autowired
	private IdentityService aIdentityService;

	/**
	 * 部署 使用设计器设计的流程；
	 */
	@RequestMapping(value = "/deployModel/{formdefineid}/complex", method = RequestMethod.GET)
	@ResponseBody
	public APIMessage deployModel(@PathVariable String formdefineid) {
		FormTableDefine formTableDefine = formService.getFormTableDefineById(Integer.parseInt(formdefineid));
		FormDefineFlowConfig formDefineFlowConfig = formTableDefine.getFormDefineFlowConfig();
		APIMessage deployResult = formFlowHelper.deployProcess(formDefineFlowConfig);
		return deployResult;
	}

	/**
	 * 读取流程节点配置；
	 */
	@RequestMapping(value = "/UserTaskConfig.json", method = RequestMethod.POST)
	@ResponseBody
	public Object getUserTaskConfig(@RequestParam String processdefineid) {
		Map<String, Object> result = new HashMap<String, Object>();

		ProcessDefinition processDefinition = aRepositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processdefineid).singleResult();
		FormTableDefine formTableDefine = formService.getFormTableDefineByName(processDefinition.getKey());
		result.put("formtabledefine", formTableDefine.getFieldsJson());

		List<FlowConfig> flowConfiglist = flowconfigService.getFlowConfigByPRID(processdefineid);
		if (!flowConfiglist.isEmpty()) {
			FlowConfig flowConfig = flowConfiglist.get(0);
			result.put("result", flowConfig.getcontent());
		} else {
			result.put("result", "[]");
		}
		return result;
	}

	/**
	 * 保存流程节点配置；
	 */
	@RequestMapping(value = "/SaveUserTaskConfig.json", method = RequestMethod.POST)
	@ResponseBody
	public String saveUserTaskConfig(@RequestParam String processdefineid, @RequestParam String content) {
		List<FlowConfig> flowConfiglist = flowconfigService.getFlowConfigByPRID(processdefineid);
		FlowConfig flowConfig = null;
		if (flowConfiglist.isEmpty()) {
			flowConfig = new FlowConfig();
			ProcessDefinition processDefinition = aRepositoryService.createProcessDefinitionQuery()
					.processDefinitionId(processdefineid).singleResult();
			flowConfig.setconfig_type("default");
			flowConfig.setcontent(content);
			flowConfig.setcreatetime(new Date());
			flowConfig.setcreateuser("admin");
			flowConfig.setprocessdefine_id(processdefineid);
			flowConfig.setprocessdefine_key(processDefinition.getKey());
			flowConfig.setprocessdefine_version(processDefinition.getVersion());
			flowConfig.setstatus(1);
			flowConfig.setUpdatetime(new Date());
			flowConfig.setupdateuser("admin");

			flowconfigService.insertFlowConfig(flowConfig);
			return "{\"result\":\"0\"}";
		} else {
			flowConfig = flowConfiglist.get(0);
			flowConfig.setcontent(content);
			flowConfig.setstatus(1);
			flowConfig.setUpdatetime(new Date());
			flowconfigService.updateFlowConfigById(flowConfig);
			return "{\"result\":\"0\"}";
		}
	}
}
