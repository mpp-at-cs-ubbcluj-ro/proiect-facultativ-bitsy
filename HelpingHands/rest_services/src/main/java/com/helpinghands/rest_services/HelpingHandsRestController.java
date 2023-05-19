package com.helpinghands.rest_services;

import com.helpinghands.domain.Admin;
import com.helpinghands.domain.Interest;
import com.helpinghands.domain.Voluntar;
import com.helpinghands.rest_services.data.Credentials;
import com.helpinghands.service.data.UserInfo;
import com.helpinghands.service.IService;
import com.helpinghands.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;
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
    public Interest[] getInterestByName(@RequestParam Optional<String> name) throws ServiceException {
        if(name.isPresent())
            return Arrays.asList(service.getInterestByName(name.get())).toArray(Interest[]::new);
        return StreamSupport.stream(service.getInterests().spliterator(),false)
                .toArray(Interest[]::new);
    }

    @RequestMapping(value="/login",method = RequestMethod.POST)
    public UserInfo login(@RequestBody Credentials credentials) throws ServiceException {
        return service.login(credentials.getUsername(), credentials.getPassword());
    }

    @RequestMapping(value="/logout",method = RequestMethod.GET)
    public ResponseEntity<?> logout(@RequestParam String token){
        service.logout(token);
        return new ResponseEntity<String>("Ok",HttpStatus.OK);
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String userError(ServiceException e) {
        return e.getMessage();
    }
}
