package com.cmit.testing.utils.selenium;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 表操作类(excel)
 * 
 * @author XXM
 *
 */
public class Table implements Iterable<Table.Row> {
	private List<Row> rows = new ArrayList<>();

	/**
	 * 循环遍历excel行
	 */
	@Override
	public Iterator<Row> iterator() {
		return rows.iterator();
	}

	/**
	 * 添加行
	 * 
	 * @param row
	 */
	public void add(Row row) {
		rows.add(row);
	}

	/**
	 * 获取行数据
	 * 
	 * @param index
	 * @return
	 */
	public Row get(int index) {
		return rows.get(index);
	}

	/**
	 * 创建table
	 * 
	 * @param rows
	 * @return
	 */
	public Table create(Row... rows) {
		Table table = new Table();

		for (Row row : rows) {
			table.add(row);
		}

		return table;
	}

	/**
	 * 判断是否相等
	 */
	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (!o.getClass().equals(getClass())) {
			return false;
		}

		Table table = (Table) o;

		if (rows.size() != table.rows.size()) {
			return false;
		}

		for (int i = 0; i < rows.size(); i++) {
			if (!rows.get(i).equals(table.rows.get(i))) {
				return false;
			}
		}

		return true;
	}

	/**
	 * 转换类型toString 行
	 */
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();

		for (Row row : rows) {
			stringBuilder.append(row.toString() + System.lineSeparator());
		}

		return stringBuilder.toString();
	}

	public static class Row implements Iterable<String> {
		private final List<String> cols = new ArrayList<>();

		/**
		 * 循环列
		 */
		@Override
		public Iterator<String> iterator() {
			return cols.iterator();
		}

		/**
		 * 添加列
		 * 
		 * @param col
		 */
		public void add(String col) {
			cols.add(col);
		}

		/**
		 * 获取列
		 * 
		 * @param index
		 * @return
		 */
		public String get(int index) {
			return cols.get(index);
		}

		/**
		 * 创建列
		 * 
		 * @param cols
		 * @return
		 */
		public static Row create(String... cols) {
			Row row = new Row();

			for (String col : cols) {
				row.add(col);
			}

			return row;
		}

		/**
		 * 判断列
		 */
		@Override
		public boolean equals(Object o) {
			if (o == null) {
				return false;
			}
			if (!o.getClass().equals(getClass())) {
				return false;
			}

			Row row = (Row) o;

			if (cols.size() != row.cols.size()) {
				return false;
			}

			for (int i = 0; i < cols.size(); i++) {
				if (!cols.get(i).equals(row.cols.get(i))) {
					return false;
				}
			}

			return true;
		}

		/**
		 * 转换类型toString 列
		 */
		@Override
		public String toString() {
			StringBuilder stringBuilder = new StringBuilder();
			List<String> row = new ArrayList<>();

			for (String col : cols) {
				row.add(col);
			}

			return stringBuilder.append(String.join(StringCollection.General.SPACE, row)).toString();
		}
	}
}
