package com.cmit.testing.utils.selenium;

/**
 * 常用变量
 * 
 * @author XXM
 *
 */
final class StringCollection {
	private StringCollection() {

	}

	public static class General {
		private General() {

		}

		public static final String SPACE = " ";
	}

	public static class XPath {
		private XPath() {

		}

		public static final String ANY_ELEMENT = "//*"; // 获取所有元素
		public static final String ANY_ELEMENT_INSIDE = ".//*"; // 获取所有内嵌元素
		public static final String ROW_SELECTOR_INSIDE = ".//tr"; // 获取行元素
		public static final String CELL_SELECTOR = ".//td|.//th"; // 获取列元素
	}

	public static class Identifier {
		private Identifier() {

		}

		public static final String EXPECTED_VALUE = "Expected value: "; // 预期值
		public static final String ACTUAL_VALUE = "Actual value: "; // 实际值
		public static final String CONTAINMENT_VALUE = "Containment value: "; // 包含值
		public static final String ACTUAL_TABLE_HEADER = "Actual table: "; // 实际表单
		public static final String EXPECTED_TABLE_HEADER = "Expected table: "; // 预期表单
		public static final String EXPECTED_ROW_HEADER = "Expected row: "; // 预期行
	}

	public static class Error {
		private Error() {

		}

		public static final String TIMEOUT_HEADER = "Timeout: "; // 超时
		public static final String UNEXPECTED_TAG_NAME_HEADER = "Unexpected tag name: "; // 未找到标签
		public static final String NO_SUCH_ELEMENT_HEADER = "No such element: "; // 未找到element
		public static final String NOT_CONTAINS_HEADER = "Does not contain: "; // 不包含
	}

	public static class ControlType {
		private ControlType() {

		}

		public static final String INPUT = "input"; // 输入框
		public static final String TEXTAREA = "textarea"; // 文本框
		public static final String SELECT = "select"; // 选择
	}

	public static class AttributeType {
		private AttributeType() {

		}

		public static final String VALUE = "value";
	}
}
