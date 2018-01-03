package com.deportel.common.callback;

public interface CallBackListener
{
	/**
	 *
	 * @param command
	 */
	public void receiveCallBack(String command);

	/**
	 *
	 * @param command
	 * @param data
	 */
	public void receiveCallBack(String command, Object data);
}
