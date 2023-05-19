package com.helpinghands.rest_services;

import com.helpinghands.domain.Interest;
import com.helpinghands.service.IService;
import com.helpinghands.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.stream.StreamSupport;

@CrossOrigin
@RestController
@RequestMapping("/helpinghands")
public class HelpingHandsRestController {
    @Autowired
    private final IService service;

    public HelpingHandsRestController(IService service) {
        this.service = service;
    }

    @RequestMapping(value="/interests",method= RequestMethod.GET)
    public Interest[] getInterests() {
        return StreamSupport.stream(service.getInterests().spliterator(),false)
                .toArray(Interest[]::new);
    }

    



    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String userError(ServiceException e) {
        return e.getMessage();
    }
}
