package com.ruoyi.onvif.exception;

/**
 * ONVIF 调用统一异常。
 * <p>
 * 所有协议层、网络层错误都封装为本异常抛出，使用方只需 catch 这一种类型。
 * 通过 {@link #getMessage()} 即可拿到中文友好的失败说明。
 *
 * @author yangchen
 * @since 1.0.0
 */
public class OnvifException extends RuntimeException {

	public OnvifException(String message) {
		super(message);
	}

	public OnvifException(String message, Throwable cause) {
		super(message, cause);
	}
}
