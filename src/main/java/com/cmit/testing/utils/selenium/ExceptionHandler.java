package com.cmit.testing.utils.selenium;

import java.util.function.Consumer;
import java.util.function.Function;

import org.junit.Assert;

/**
 * 异常处理类
 * 
 * @author XXM
 *
 */
public class ExceptionHandler {

	/**
	 * 异常处理，存在返回值 Function:接收T对象，返回R对象
	 * 
	 * @param executeFunction
	 * @param exceptionMessageFunction
	 * @param arg
	 * @return
	 */
	public <T, R> R apply(Function<T, R> executeFunction, Function<T, String> exceptionMessageFunction, T arg) {
		R result = null;

		try {
			result = executeFunction.apply(arg);
		} catch (RuntimeException e) {
			errorMessage(exceptionMessageFunction.apply(arg));
		}

		return result;
	}

	/**
	 * 异常处理，不存在返回值
	 * 
	 * @param executeFunction
	 * @param exceptionMessageFunction
	 * @param arg
	 */
	public <T> void apply(Consumer<T> executeFunction, Function<T, String> exceptionMessageFunction, T arg) {
		try {
			executeFunction.accept(arg);
		} catch (RuntimeException e) {
			errorMessage(exceptionMessageFunction.apply(arg));
		}
	}

	/**
	 * 当不包含指定值时抛出异常信息
	 * 
	 * @param containmentValue
	 * @param actualValue
	 * @return
	 */
	public <T> String getNotContainsMessage(T containmentValue, T actualValue) {
		return getMessage(StringCollection.Error.NOT_CONTAINS_HEADER,
				StringCollection.Identifier.ACTUAL_VALUE + actualValue.toString() + System.lineSeparator()
						+ StringCollection.Identifier.CONTAINMENT_VALUE + containmentValue);
	}

	/**
	 * 抛出超时报错
	 * 
	 * @param arg
	 * @return
	 */
	public <T> String getTimeoutMessage(T arg) {
		return getMessage(StringCollection.Error.TIMEOUT_HEADER, arg.toString());
	}

	/**
	 * 当tagName找不到时抛出信息
	 * 
	 * @param arg
	 * @return
	 */
	public <T> String getUnexpectedTagNameMessage(T arg) {
		return getMessage(StringCollection.Error.UNEXPECTED_TAG_NAME_HEADER, arg.toString());
	}

	/**
	 * 未找到元素时抛出信息
	 * 
	 * @param arg
	 * @return
	 */
	public <T> String getNoSuchElementMessage(T arg) {
		return getMessage(StringCollection.Error.NO_SUCH_ELEMENT_HEADER, arg.toString());
	}

	/**
	 * 抛出信息
	 * 
	 * @param header
	 * @param content
	 * @return
	 */
	public String getMessage(String header, String content) {
		return System.lineSeparator() + header + content;
	}

	/**
	 * 抛出错误信息
	 * 
	 * @param errorMessage
	 */
	public void errorMessage(String errorMessage) {
		Assert.fail(errorMessage);
	}
}
