package com.server.gradleServer.chatController.portalController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PortalController {

    @RequestMapping(value = "/")
    public String FormPage(){
        return "form.html";
    }
    @RequestMapping(value = "/chat")
    public String ChatPage(){
        return "index.html";
    }
}
