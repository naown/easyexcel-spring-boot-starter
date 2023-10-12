package com.naown.excel.controller;

import lombok.Data;

import java.util.List;

/**
 * @date: 2023/8/8 10:45
 * @author: chenjian
 */
@Data
public class RequestExcelParam {
    private List<String> exclude;
}
