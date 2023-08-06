package com.naown.excel.controller;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @DATE: 2021/10/15 8:44 上午
 * @AUTHOR: chenjian
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {

	@ExcelProperty("ID")
	private Integer id;
	/**
	 * 名字
	 */
	@ExcelProperty("名字")
	private String name;
	/**
	 * 年龄
	 */
	@ExcelProperty("年龄")
	private int age;
}
