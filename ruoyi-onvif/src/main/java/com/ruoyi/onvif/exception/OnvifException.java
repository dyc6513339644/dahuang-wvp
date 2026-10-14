package com.ruoyi.onvif.exception;

/**
 * ONVIF 调用统一异常
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
