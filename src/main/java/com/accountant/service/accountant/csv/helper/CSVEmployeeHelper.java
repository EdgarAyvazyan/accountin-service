package com.accountant.service.accountant.csv.helper;

import com.accountant.service.accountant.domain.EmployeeDTO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CSVEmployeeHelper {
    public static String TYPE = "text/csv";

    public static boolean hasCSVFormat(MultipartFile file) {

        return TYPE.equals(file.getContentType());
    }


    public static List<EmployeeDTO> csvToEmployees(InputStream is, String fileName, Long uploadedFileId) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            List<EmployeeDTO> employeeDTOS = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                EmployeeDTO dto = new EmployeeDTO(null,
                        csvRecord.get("Employee"),
                        BigDecimal.valueOf(Long.parseLong(csvRecord.get("Salary"))),
                        new Date(), fileName, String.valueOf(uploadedFileId)
                );

                employeeDTOS.add(dto);
            }

            return employeeDTOS;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }
}
