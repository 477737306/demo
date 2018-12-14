package com.cmit.testing.utils.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import com.cmit.testing.utils.selenium.Table.Row;

/**
 * 预期值实际值工具类
 * 
 * @author XXM
 *
 */
public class ExpectedConditions extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Common common;
	/**
	 * 选择器
	 * 
	 * @param actualDropdownLocation
	 * @param expectedText
	 * @return
	 */
	public ExpectedCondition<Boolean> selectionToBe(final By actualDropdownLocation, final String expectedText) {
		return new ExpectedCondition<Boolean>() {
			/**
			 * 判断是否相等
			 */
			@Override
			public Boolean apply(WebDriver driver) {
				return isEquals(actualDropdownLocation, expectedText);
			}

			/**
			 * 转换类型
			 */
			@Override
			public String toString() {
				return System.lineSeparator() + StringCollection.Identifier.ACTUAL_VALUE + System.lineSeparator()
						+ common.getSelection(actualDropdownLocation) + System.lineSeparator()
						+ StringCollection.Identifier.EXPECTED_VALUE + System.lineSeparator() + expectedText;
			}
		};
	}

	/**
	 * 判断预期值 实际值
	 * 
	 * @param actualTableLocation
	 * @param expectedTable
	 * @return
	 */
	public ExpectedCondition<Boolean> tableToBe(final By actualTableLocation, final Table expectedTable) {
		return new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				return isEquals(actualTableLocation, expectedTable);
			}

			@Override
			public String toString() {
				Table actualTable = common.getTable(actualTableLocation);

				return System.lineSeparator() + StringCollection.Identifier.ACTUAL_TABLE_HEADER + System.lineSeparator()
						+ actualTable.toString() + System.lineSeparator()
						+ StringCollection.Identifier.EXPECTED_TABLE_HEADER + System.lineSeparator()
						+ expectedTable.toString();
			}
		};
	}

	/**
	 * 判断表单预期值 实际值
	 * 
	 * @param actualTableLocation
	 * @param expectedRow
	 * @return
	 */
	public ExpectedCondition<Boolean> tableRowToBe(final By actualTableLocation, final Row expectedRow) {
		return new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				return isEquals(actualTableLocation, expectedRow);
			}

			@Override
			public String toString() {
				Table actualTable = common.getTable(actualTableLocation);

				return System.lineSeparator() + StringCollection.Identifier.ACTUAL_TABLE_HEADER + System.lineSeparator()
						+ actualTable.toString() + System.lineSeparator()
						+ StringCollection.Identifier.EXPECTED_ROW_HEADER + System.lineSeparator()
						+ expectedRow.toString();
			}
		};
	}

	/**
	 * 判断是否相等
	 * 
	 * @param actualTableLocation
	 * @param expectedTable
	 * @return
	 */
	public boolean isEquals(By actualTableLocation, Table expectedTable) {
		return common.getTable(actualTableLocation).equals(expectedTable);
	}

	public boolean isEquals(By actualTableLocation, Row expectedRow) {
		Table actualTable = common.getTable(actualTableLocation);

		for (Row row : actualTable) {
			if (row.equals(expectedRow)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 判断是否相等
	 * 
	 * @param location
	 * @param text
	 * @return
	 */
	public boolean isEquals(By location, String text) {
		return common.getSelection(location).equals(text);
	}
}
