package com.accountant.service.accountant.csv.helper;

import com.accountant.service.accountant.domain.CurrencyDTO;
import com.accountant.service.accountant.entity.CurrencyEntity;
import com.accountant.service.accountant.entity.EmployeeEntity;
import com.accountant.service.accountant.enums.IsoCodeEnum;
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

public class CSVCurrencyHelper {
    public static String TYPE = "text/csv";

    public static boolean hasCSVFormat(MultipartFile file) {

        return TYPE.equals(file.getContentType());
    }


    public static List<CurrencyDTO> csvToEmployees(InputStream is, String fileName) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            List<CurrencyDTO> currencyDTOS = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                CurrencyDTO currency = new CurrencyDTO(null,
                        csvRecord.get("Date"),
                        csvRecord.get("Rate"),
                        IsoCodeEnum.valueOf(csvRecord.get("ISO Code From")),
                        IsoCodeEnum.valueOf(csvRecord.get("ISO Code To")),
                        new Date(), fileName
                );

                currencyDTOS.add(currency);
            }

            return currencyDTOS;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }
}
