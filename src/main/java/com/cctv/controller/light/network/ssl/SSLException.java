package com.cctv.controller.light.network.ssl;


import com.cctv.controller.light.network.NetworkException;

public class SSLException extends NetworkException {

	private static final long serialVersionUID = 1L;
	
	public SSLException(){
		
	}
	
	public SSLException(String msg){
		super(msg);
	}
	
	public SSLException(String msg,Throwable throwable){
		super(msg,throwable);
	}

}
