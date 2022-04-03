package com.kpp.kpp;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CopyController {
    @GetMapping("/copy")
    public Fields copyValue(@RequestParam(value="value")String value)
    {
        Fields fields =new Fields(value);
        fields.setField2(value);
        fields.setField3(value);
        return fields;
    }

}
