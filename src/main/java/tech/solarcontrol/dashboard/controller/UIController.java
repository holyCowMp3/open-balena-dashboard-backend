package tech.solarcontrol.dashboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UIController {
    @RequestMapping(value = {"/ui","/index","/"}, method = RequestMethod.GET)
    public String dashboard() {
        return "index";
    }
}
