package com.gestion.stock.exception;

public class StockInsuffisantException extends BusinessException {
	public StockInsuffisantException(String message) {
		super(message);
	}
    
	public StockInsuffisantException(String message, Throwable cause) {
		super(message, cause);
	}
}
