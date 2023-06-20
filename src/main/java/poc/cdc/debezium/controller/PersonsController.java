package poc.cdc.debezium.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import poc.cdc.debezium.config.ChangeDataSender;

@Controller
public class PersonsController {

    @GetMapping("/test")
    public String callDbz(){
        //ChangeDataSender changeDataSender = new ChangeDataSender();
        //changeDataSender.run();
        return "ok";
    }

}
