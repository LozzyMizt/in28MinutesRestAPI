package com.lozz.springboot.firstrestapi.helloworld;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldResource {

    // /hello-world => "Hello World"
    @RequestMapping("/hello-world")
    public String helloWorld(){
        return "Hello World";
    }

    @RequestMapping("/hello-world-bean")
    public HelloWorldBean helloWorldBean(){
        return new HelloWorldBean("Hello World");
    }

    //Path Variable or Path Params
    @RequestMapping("/hello-world-path-param/{name}")
    public HelloWorldBean helloWorldPathParam(@PathVariable String name){
        return new HelloWorldBean("Hello World, " + name);
    }

    @RequestMapping("/hello-world-path-param/{name}/message/{message}")
    public HelloWorldBean helloWorldMultiplePathParam(@PathVariable String name,
                                                      @PathVariable String message){
        return new HelloWorldBean("Hello World " + name + ", " + message);
    }
}
