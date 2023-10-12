package com.naown.excel.controller;

import com.naown.excel.annotation.ResponseExcel;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @date: 2023/7/23 21:57
 * @author: chenjian
 */
@RestController
public class TestController {

    @PostMapping("reportExcel")
    @ResponseExcel(exclude = "#excelParam.exclude")
    public List<Student> reportExcel(@RequestBody RequestExcelParam excelParam) {
        return buildStudent();
    }

    private List<Student> buildStudent() {
        return IntStream.range(0, 11).mapToObj(v -> new Student(v, "name" + v, v + 10)).collect(Collectors.toList());
    }

}
