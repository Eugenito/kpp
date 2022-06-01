package com.kpp.kpp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;

@Validated
@RestController
public class CopyController {

    private static final Logger logger = LogManager.getLogger(CopyController.class);

    @Autowired
    private ResultsCache resultsCache;

    @GetMapping("/copy")
    public Fields copyValue(@RequestParam(value = "value") @NotBlank String value) {
        new Thread(CounterService::increment).start();
        if(resultsCache.findByKeyInHashMap(value)) {
            return resultsCache.getParameters(value);
        } else {
            Fields fields = new Fields();
            fields.setField2(value);
            fields.setField3(value);
            resultsCache.addToMap(value, fields);
            logger.info("Successfully request");
            return fields;
        }
    }

    @PostMapping("/copy")
    public ResponseEntity<?> calculateBulk(@Valid @RequestBody List<String> bodyList) {
        List<Fields> resultList = new LinkedList<>();
        bodyList.forEach((currentElem) -> {
            Fields fields = new Fields();
            fields.setField2(currentElem);
            fields.setField3(currentElem);
            resultList.add(fields);
        });

        logger.info("Successfully postMapping");

        double averageLendth = 0;
        if (!resultList.isEmpty()) {
            averageLendth = resultList.stream().mapToInt(Fields::getLength).average().getAsDouble();
        }
        int maxLength = 0;
        if (!resultList.isEmpty()) {
            maxLength = resultList.stream().mapToInt(Fields::getLength).max().getAsInt();
        }
        int minResult = 0;
        if (!resultList.isEmpty()) {
            minResult = resultList.stream().mapToInt(Fields::getLength).min().getAsInt();
        }

        return new ResponseEntity<>(resultList + "\nAverage length: " + averageLendth + "\nMax length: " +
                maxLength + "\nMin length: " + minResult, HttpStatus.OK);
    }

    @GetMapping("/counter")
    public ResponseEntity<Integer> getCount() {
        return new ResponseEntity<>(CounterService.getCounter(), HttpStatus.OK);
    }

    @GetMapping("/cache")
    public Map<String, Fields> getCache(){
        logger.info("Get cache");
        return resultsCache.getHashMap();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<String> errorEntryParams(ConstraintViolationException e){
        logger.error("Entry wrong params");
        return new ResponseEntity<>("<h1>ERROR 400: WRONG INPUT PARAMS</h1>", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    ResponseEntity<String> errorServer(RuntimeException e){
        logger.error(e.getMessage());
        return new ResponseEntity<>("<h1>ERROR 500</h1>", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
