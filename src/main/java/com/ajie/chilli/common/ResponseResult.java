package com.ajie.chilli.common;

import java.util.Collections;
import java.util.List;

import com.ajie.chilli.utils.common.JsonUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 请求响应信息封装
 * 
 * @author niezhenjie
 */
public class ResponseResult {

	/** 成功状态码 */
	public static int CODE_SUC = 200;

	/** 成功状态码 , 调用结果为空 */
	public static int CODE_NORET = 300;

	/** session过期状态吗 */
	public static final int CODE_SESSION_INVALID = 400;

	/** 失败状态，有错误或异常抛出 */
	public static int CODE_ERR = 500;

	/**
	 * 状态码
	 */
	protected int code;

	/**
	 * 返回消息
	 */
	protected String msg;

	/**
	 * 返回数据
	 */
	protected Object data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	/**
	 * 将data转换成T类型，data只能是简单的对象类型，不能是复合的
	 * 
	 * @param clazz
	 */
	@SuppressWarnings("unchecked")
	public <T> T getData(Class<T> clazz) {
		if (null == data)
			return null;
		if (data instanceof String == false
				&& data instanceof JSONObject == false)
			return (T) data;
		if (data instanceof String) {
			String str = (String) data;
			// json串
			if (str.startsWith("{") && str.endsWith("}")) {
				return JsonUtils.toBean(str, clazz);
			}
		}

		if (data instanceof JSONObject) {
			JSONObject json = (JSONObject) data;
			T t = JsonUtils.toBean(json, clazz);
			return t;
		}

		return (T) data;
	}

	/**
	 * 将data转换成T类型的集合
	 * 
	 * @param clazz
	 * @return
	 */
	public <T> List<T> listData(Class<T> clazz) {
		if (null == data)
			return Collections.emptyList();
		if (data instanceof String == false
				&& data instanceof JSONArray == false)
			return Collections.emptyList();
		if (data instanceof String) {
			String str = (String) data;
			// json串
			if (str.startsWith("{[") && str.endsWith("}]")) {
				return JsonUtils.toList(str, clazz);
			}
		}
		return Collections.emptyList();
	}

	public static ResponseResult newResult(int code, String msg, Object data) {
		ResponseResult ret = new ResponseResult();
		ret.setCode(code);
		ret.setMsg(msg);
		ret.setData(data);
		return ret;
	}

	public static ResponseResult newResult(int code, String msg) {
		ResponseResult ret = new ResponseResult();
		ret.setCode(code);
		ret.setMsg(msg);
		return ret;
	}

	public static ResponseResult newResult(int code, Object data) {
		ResponseResult ret = new ResponseResult();
		ret.setCode(code);
		ret.setData(data);
		return ret;
	}

	/**
	 * 成功
	 * 
	 * @param msg
	 *            如果是String类型，则设置msg，否则设置data
	 * @return
	 */
	public static ResponseResult success(Object data) {
		ResponseResult ret = new ResponseResult();
		ret.setCode(CODE_SUC);
		String msg = null;
		if (data instanceof String) {
			msg = (String) data;
			ret.setMsg(msg);
		} else {
			ret.setData(data);
		}
		return ret;
	}

	public static ResponseResult success(String msg, Object data) {
		ResponseResult ret = new ResponseResult();
		ret.setCode(CODE_SUC);
		ret.setMsg(msg);
		ret.setData(data);
		return ret;
	}

	/**
	 * 失败返回
	 * 
	 * @param data
	 *            如果是String类型，则setMsg否则setData
	 * @return
	 */
	public static ResponseResult fail(Object data) {
		ResponseResult ret = new ResponseResult();
		ret.setCode(CODE_ERR);
		String msg = null;
		if (data instanceof String) {
			msg = (String) data;
			ret.setMsg(msg);
		} else {
			ret.setData(data);
		}
		return ret;
	}

	public static ResponseResult fail(String msg, Object data) {
		ResponseResult ret = new ResponseResult();
		ret.setCode(CODE_ERR);
		ret.setMsg(msg);
		ret.setData(data);
		return ret;
	}

	public static ResponseResult empty() {
		ResponseResult ret = new ResponseResult();
		ret.setCode(CODE_NORET);
		return ret;
	}

	/**
	 * 将相应对象转换成jsonp所需的格式
	 * 
	 * @param res
	 * @param callback
	 * @return
	 */
	public static <T> String toJsonp(ResponseResult res, String callback) {
		StringBuilder sb = new StringBuilder();
		sb.append(callback);
		sb.append("(");
		if (null == res) {
			sb.append(")");
			return sb.toString();
		}
		sb.append("{");
		sb.append("\"");
		sb.append("code");
		sb.append("\"");
		sb.append(":");
		sb.append("\"");
		sb.append(res.getCode());
		sb.append("\"");
		if (null != res.getMsg()) {
			sb.append(",");
			sb.append("\"");
			sb.append("msg");
			sb.append("\"");
			sb.append(":");
			sb.append("\"");
			sb.append(res.getMsg());
			sb.append("\"");
		}
		if (null != res.getData()) {
			sb.append(",");
			sb.append("\"");
			sb.append("data");
			sb.append("\"");
			sb.append(":");
			sb.append(JsonUtils.toJSONString(res.getData()));
		}

		sb.append("}");
		sb.append(")");
		return sb.toString();
	}
}
