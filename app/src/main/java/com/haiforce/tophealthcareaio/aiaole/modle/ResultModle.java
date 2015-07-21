package com.haiforce.tophealthcareaio.aiaole.modle;

import java.io.Serializable;

public class ResultModle implements Serializable {

	public byte[] getMessageByte() {
		return messageByte;
	}
	public void setMessageByte(byte[] messageByte) {
		this.messageByte = messageByte;
	}
	public byte[] getResultByte() {
		return resultByte;
	}
	public void setResultByte(byte[] resultByte) {
		this.resultByte = resultByte;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	byte[] messageByte;
	byte[] resultByte;
	
	

}
