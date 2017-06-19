package com.property.controller.utils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Controller
@RequestMapping(value = "fileupload")
public class FileUploadController {
	// private static final Logger LOG =
	// Logger.getLogger(UploadController.class);
	private static String BASE_PATH = "/Users/Caspar/Documents/java/Upload";
	private static final HashMap<String, String> TypeMap = new HashMap<String, String>();
	// 设置文件允许上传的类型
	static {
		TypeMap.put("image", "gif,jpg,jpeg,png,bmp");
		TypeMap.put("flash", "swf,flv");
		TypeMap.put("media", "swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
		TypeMap.put("file",
				"doc,docx,xls,xlsx,ppt,pptx,htm,html,txt,dwg,pdf,xml,zip,gif,jpg,jpeg,png,bmp,swf,flv,swf,flv,mp3,wav,wma,wmv,mid,avi,mpg,asf,rm,rmvb");
	}

	// 设置文件上传大小
	public static long fileSize = 3 * 1024 * 1024;

	/**
	 * 文件上传
	 *
	 * @param file
	 * @param request
	 * @return message: -1 没有文件上传 0 上传成功 1 上传失败 2 文件超过上传大小 3 文件格式错误 4 上传文件路径非法 5
	 *         上传目录没有写权限
	 * 
	 *
	 */
	@RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
	public void imageUpload(@RequestParam("file") CommonsMultipartFile file,
			@RequestParam(required = false) String filePre, HttpServletRequest request, HttpServletResponse response) {
		// LOG.info("file name is :" + file.getOriginalFilename());
		if (!file.isEmpty()) {

			// 当文件超过设置的大小时，则不运行上传
			if (file.getSize() > fileSize) {
				backInfo(response, false, 2, "");
				return;
			}
			// 获取文件名后缀
			String OriginalFilename = file.getOriginalFilename();
			String fileSuffix = OriginalFilename.substring(OriginalFilename.lastIndexOf(".") + 1).toLowerCase();

			// String fileSuffix="jpeg";

			// 判断该类型的文件是否在允许上传的文件类型内
			if (!Arrays.asList(TypeMap.get("file").split(",")).contains(fileSuffix)) {
				backInfo(response, false, 3, "");
				return;
			}
			if (!ServletFileUpload.isMultipartContent(request)) {
				backInfo(response, false, -1, "");
				return;
			}
			// 检查上传文件的目录
			File uploadDir = new File(BASE_PATH);
			if (!uploadDir.isDirectory()) {
				if (!uploadDir.mkdir()) {
					backInfo(response, false, 4, "");
					return;
				}
			}
			// 是否有上传的权限
			if (!uploadDir.canWrite()) {
				backInfo(response, false, 5, "");
				return;
			}

			// 新文件名
			String newname = "";
			if (null != filePre) {
				newname += filePre;// 对应模块上传的文件名前缀
			}
			newname += UUID.randomUUID() + "." + fileSuffix;

			try {

				// 创建文件
				File saveFile = new File(BASE_PATH, newname);
				// 保存文件
				file.transferTo(saveFile);
				// FileTranser.saveFielByFileName(file, uploadPath, newname);
				backInfo(response, true, 0, BASE_PATH + "/" + newname);
			} catch (Exception e) {
				// LOG.error(e.getMessage(), e);
				backInfo(response, false, 1, "");
				return;
			}
		} else {
			backInfo(response, false, -1, "");
			return;
		}

	}

	/**
	 * 文件下载
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/fileDownload", method = RequestMethod.GET)
	public ResponseEntity fileDownload(String filename, String filepath) throws IOException {
		File file = new File(filepath);
		String dfileName = new String(filename.getBytes("gb2312"), "iso8859-1");
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", dfileName);
		return new ResponseEntity(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
	}

	/**
	 * 返回json信息
	 * 
	 * @param response
	 * @param flag
	 * @param message
	 * @param fileName
	 */
	private void backInfo(HttpServletResponse response, boolean flag, int message, String fileName) {
		String json = "";
		String messageStr = "";
		switch (message) {
		case -1:
			messageStr = "没有文件上传";
			break;
		case 0:
			messageStr = "上传成功";
			break;
		case 1:
			messageStr = "上传失败";
			break;
		case 2:
			messageStr = "文件超过上传大小";
			break;
		case 3:
			messageStr = "文件格式错误";
			break;
		case 4:
			messageStr = "上传文件路径非法";
			break;
		case 5:
			messageStr = "上传目录没有写权限";
			break;
		default:
			break;
		}
		// json=fileName;
		if (flag) {
			json = "{ \"status\": \"success";
		} else {
			json = "{ \"status\": \"error";
		}
		json += "\",\"fileName\": \"" + fileName + "\",\"message\": \"" + messageStr + "\"}";
		try {
			// response.setContentType("text/javascript");
			response.setContentType("text/html; charset=utf-8");
			response.getWriter().print(json);
			// LOG.info(json.toString());
		} catch (IOException e) {
			// LOG.error(e.getMessage(), e);
		}
	}
}
