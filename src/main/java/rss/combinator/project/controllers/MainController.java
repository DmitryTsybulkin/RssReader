package rss.combinator.project.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping(name = "/index")
    public String index() {
        return "index";
    }

//    @GetMapping(name = "/login")
//    public String login() { return "login"; }

}
