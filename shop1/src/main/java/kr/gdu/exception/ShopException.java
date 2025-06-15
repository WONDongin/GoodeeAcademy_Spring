package kr.gdu.exception;

import lombok.Getter;

@Getter
public class ShopException extends RuntimeException{
	private String url;
	public ShopException(String msg, String url) {
		super(msg);
		this.url = url;
	}
}
