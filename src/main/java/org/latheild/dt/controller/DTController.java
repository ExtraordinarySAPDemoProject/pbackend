package org.latheild.dt.controller;

import org.latheild.dt.dto.BatchTestCaseDTO;
import org.latheild.dt.dto.SingleTestCaseDTO;
import org.latheild.dt.dto.UploadFileDTO;
import org.latheild.dt.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class DTController {

    @Autowired
    TestService testService;

    @RequestMapping("/")
    public String index() {
        return testService.test();
    }

    @GetMapping("/uploadSourceFile")
    @ResponseBody
    public Object uploadSourceFile() {
        return testService.uploadSourceFile(
                "/Users/nemoremold/Documents/Projects/Project DT/src/main/java/org/latheild/dt/testUnit/ComputerSalesTestUnit.java",
                "org.latheild.dt.testUnit.ComputerSalesTestUnit"
        );
        //return "test";
    }

    @GetMapping("/testSingleCase")
    @ResponseBody
    public Object testSingleCase() {
        return testService.testSingleCase(
                "/Users/nemoremold/Documents/Projects/Project DT/src/main/java/org/latheild/dt/testUnit/ComputerSalesTestUnit.java",
                "org.latheild.dt.testUnit.ComputerSalesTestUnit",
                new String[]{"getCommission", "String", "String", "String", "public static", "double"},
                new Object[]{"-1", "2", "3", "Input number for mainframe is out of range."}
        );
    }

    @GetMapping("/testBatchCases")
    @ResponseBody
    public Object testBatchCases() {
        return testService.testBatchCases(
                "/Users/nemoremold/Documents/Projects/Project DT/src/main/java/org/latheild/dt/testUnit/ComputerSalesTestUnit.java",
                "org.latheild.dt.testUnit.ComputerSalesTestUnit",
                new String[]{"getCommission", "String", "String", "String", "public static", "double"},
                "/Users/nemoremold/Documents/Projects/Project DT/src/main/java/org/latheild/dt/testUnit/test2.xlsx"
        );
    }

    @PostMapping("/uploadSource")
    @ResponseBody
    public Object uploadSourceFile(
            @RequestBody UploadFileDTO uploadFileDTO
    ) {
        return testService.uploadSourceFile(
                uploadFileDTO.getFilePath(),
                uploadFileDTO.getClassName()
        );
    }

    @PostMapping("/singleTest")
    @ResponseBody
    public Object singleTest(
            @RequestBody SingleTestCaseDTO singleTestCaseDTO
    ) {
        return testService.testSingleCase(
                singleTestCaseDTO.getFilePath(),
                singleTestCaseDTO.getClassName(),
                singleTestCaseDTO.getOptions(),
                singleTestCaseDTO.getObjects()
        );
    }

    @PostMapping("/batchTest")
    @ResponseBody
    public Object batchTest(
            @RequestBody BatchTestCaseDTO batchTestCaseDTO
    ) {
        return testService.testBatchCases(
                batchTestCaseDTO.getFilePath(),
                batchTestCaseDTO.getClassName(),
                batchTestCaseDTO.getOptions(),
                batchTestCaseDTO.getXlsPath()
        );
    }

}
