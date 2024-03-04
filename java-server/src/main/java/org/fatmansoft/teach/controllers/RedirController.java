package org.fatmansoft.teach.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class RedirController {
    @RequestMapping("/**/{path:[^.]*}")
    public ModelAndView redirect() {
        return new ModelAndView("forward:/");
    }
}

