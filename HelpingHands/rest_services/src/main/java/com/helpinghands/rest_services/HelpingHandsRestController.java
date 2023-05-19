package com.helpinghands.rest_services;

import com.helpinghands.domain.Eveniment;
import com.helpinghands.domain.Interest;
import com.helpinghands.repo.data.EventOrderOption;
import com.helpinghands.rest_services.data.Credentials;
import com.helpinghands.rest_services.data.EventParams;
import com.helpinghands.rest_services.dto.EvenimentDTO;
import com.helpinghands.service.data.UserInfo;
import com.helpinghands.service.IService;
import com.helpinghands.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.rmi.ServerException;
import java.util.Arrays;
import java.util.Objects;
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
    public String serviceException(ServiceException e) {
        return e.getMessage();
    }

    @ExceptionHandler(HHServerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String serverException(ServerException e) {
        return e.getMessage();
    }

    @RequestMapping(value="/evenimente", method = RequestMethod.POST)
    public Eveniment addEveniment(@RequestBody EventParams eventParams) throws ServiceException, HHServerException {
        var ev = eventParams.getEveniment();
        var token = eventParams.getToken();
        var session = service.getUserSession(token);

        if(!Objects.equals(session.getType(), "Voluntar")){
            throw new HHServerException("Doar voluntarii pot adauga evenimente");
        }
        return service.addEvent(ev);
    }

    @RequestMapping(value="/evenimente", method = RequestMethod.GET)
    public EvenimentDTO[] getEvenimente(@RequestParam Optional<String> filter, @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> perPage) throws ServiceException, HHServerException {
        var _filter = EventOrderOption.valueOf(filter.orElse("NONE").toUpperCase());
        var _page = page.orElse(0);
        var _perPage = perPage.orElse(10);
        return Arrays
                .stream(service.getOrderedEveniments(_filter, _page, _perPage))
                .map(EvenimentDTO::fromEveniment)
                .toArray(EvenimentDTO[]::new);
        
    }
}
