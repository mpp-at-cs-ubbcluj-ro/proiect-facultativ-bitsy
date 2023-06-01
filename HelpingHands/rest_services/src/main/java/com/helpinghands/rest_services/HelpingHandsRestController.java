package com.helpinghands.rest_services;

import com.helpinghands.domain.*;
import com.helpinghands.repo.data.EventOrderOption;
import com.helpinghands.rest_services.data.AddVoluntarRequestData;
import com.helpinghands.rest_services.data.Credentials;
import com.helpinghands.rest_services.data.EventParams;
import com.helpinghands.rest_services.data.ParticipantDTO;
import com.helpinghands.rest_services.dto.EvenimentDTO;
import com.helpinghands.rest_services.dto.PostDTO;
import com.helpinghands.service.data.UserInfo;
import com.helpinghands.service.IService;
import com.helpinghands.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
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
            return Collections.singletonList(service.getInterestByName(name.get())).toArray(Interest[]::new);
        return StreamSupport.stream(service.getInterests().spliterator(),false)
                .toArray(Interest[]::new);
    }

    @RequestMapping(value="/login",method = RequestMethod.POST)
    public UserInfo login(@RequestBody Credentials credentials) throws ServiceException {
        return service.login(credentials.getUsername(), credentials.getPassword());
    }

    @RequestMapping(value="/register",method = RequestMethod.POST)
    public ResponseEntity<?> UtilizatorRegister (@RequestParam String username,@RequestParam String password,@RequestParam String email,@RequestParam String nume,@RequestParam String prenume){
        try{
            Utilizator utilizator  =service.createAccount(username,password,email,nume,prenume);
            return new ResponseEntity<Utilizator>(utilizator,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }



    @RequestMapping(value="/logout",method = RequestMethod.GET)
    public ResponseEntity<?> logout(@RequestParam String token){
        service.logout(token);
        return new ResponseEntity<String>("Ok",HttpStatus.OK);
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String serviceException(ServiceException e) {
        System.out.println("Service exc");
        e.printStackTrace();
        System.out.println(e.getMessage());
        return e.getMessage();
    }

    @ExceptionHandler(HHServerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String serverException(HHServerException e) {
        System.out.println("HHServer exc");
        e.printStackTrace();
        return e.getMessage();
    }

    @RequestMapping(value="/evenimente", method = RequestMethod.POST)
    public EvenimentDTO addEveniment(@RequestBody EventParams eventParams) throws ServiceException, HHServerException {
        var token = eventParams.getToken();
        var session = service.getUserSession(token);
        if(!Objects.equals(session.getType(), "Voluntar")){
            throw new HHServerException("Doar voluntarii pot adauga evenimente");
        }

        var evdto = eventParams.getEveniment();
        var voluntar = (Voluntar)session.getUtilizator();
        var ev=new Eveniment(evdto.getName(), evdto.getDescription(), evdto.getStartDate(),
                evdto.getEndDate(), evdto.getLocation(), voluntar,
                evdto.getStatus());
        for(var interestName : evdto.getInterests()){
            var interest = service.getInterestByName(interestName);
            ev.getInterests().add(interest);
        }
        return EvenimentDTO.fromEveniment(service.addEvent(ev));
    }

    @RequestMapping(value="/evenimente", method = RequestMethod.GET)
    public EvenimentDTO[] getEvenimente(@RequestParam Optional<Integer> volId,  @RequestParam Optional<String> filter, @RequestParam Optional<Integer> page, @RequestParam Optional<Integer> perPage) throws ServiceException, HHServerException {
        if(volId.isEmpty()) {
            var _filter = EventOrderOption.valueOf(filter.orElse("NONE").toUpperCase());
            var _page = page.orElse(0);
            var _perPage = perPage.orElse(10);
            return Arrays
                    .stream(service.getOrderedEveniments(_filter, _page, _perPage))
                    .map(EvenimentDTO::fromEveniment)
                    .toArray(EvenimentDTO[]::new);
        }
        return Arrays
                .stream(service.getEvenimentByOrganizerId(volId.get()))
                .map(EvenimentDTO::fromEveniment)
                .toArray(EvenimentDTO[]::new);
    }

    @RequestMapping(value = "/evenimente/{id_eveniment}/participants/{id_participant}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeVoluntarFromEveniment(@PathVariable Integer id_eveniment, @PathVariable Integer id_participant, @RequestParam String token){
        try{
            service.getUserSession(token); // check if valid token

            Participant voluntar = service.getParticipantById(id_participant);
            Eveniment eveniment = service.getEvenimentById(id_eveniment);
            Eveniment eveniment_final = service.deleteParticipantFromEveniment(voluntar,eveniment);
            return new ResponseEntity<EvenimentDTO>(EvenimentDTO.fromEveniment(eveniment_final),HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/evenimente/{id_eveniment}/participants", method = RequestMethod.GET)
    public ParticipantDTO[] getEvenimentParticipants(@PathVariable Integer id_eveniment) throws ServiceException {
        var ev = service.getEvenimentById(id_eveniment);
        return Arrays.stream(service.getParticipants(ev))
                .map(ParticipantDTO::fromParticipant)
                .toArray(ParticipantDTO[]::new);
    }

    @RequestMapping(value = "/evenimente/{id_eveniment}/participants", method = RequestMethod.PUT)
    public ResponseEntity<?> addVoluntarToEveniment(@PathVariable Integer id_eveniment, @RequestBody AddVoluntarRequestData reqData){
        // JSON Body :  {"idVoluntar":"42", "role":"organizer"}
        try{
            Eveniment eveniment = service.getEvenimentById(id_eveniment);
            Voluntar voluntar = service.getVoluntarById(reqData.getIdVoluntar());
            Participant participant;
            System.out.println(reqData.getRole());
            if(Objects.equals(reqData.getRole(), "organizer")){
                participant = service.addOrganizer(voluntar, eveniment);
            }
            else if(Objects.equals(reqData.getRole(), "volunteer")){
                participant = service.addVolunteer(voluntar, eveniment);
            }
            else
                return new ResponseEntity<>("Invalid role", HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(ParticipantDTO.fromParticipant(participant)
                    ,HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);}
    }

    @RequestMapping(value = "/evenimente/{id}", method = RequestMethod.PUT)
    public EvenimentDTO update(@PathVariable Integer id, @RequestBody EventParams eventParams) throws ServiceException, HHServerException {
        var token = eventParams.getToken();
        var session = service.getUserSession(token);
        if(!Objects.equals(session.getType(), "Voluntar")){
            throw new HHServerException("Doar voluntarii pot modifica evenimente");
        }
        var evdto = eventParams.getEveniment();
        var evt = service.getEvenimentById(id);

        var voluntar = (Voluntar)session.getUtilizator();
        if(Arrays.stream(service.getOrganizers(evt))
                .map(Participant::getVoluntar)
                .noneMatch(o-> Objects.equals(o.getId(), voluntar.getId()))){
            throw new HHServerException("Permission denied. User is not organizer for target event");
        }

        if(evdto.getName()!=null)
            evt.setName(evdto.getName());
        if(evdto.getDescription()!=null)
            evt.setDescription(evdto.getDescription());
        if(evdto.getStartDate()!=null)
            evt.setStartDate(evdto.getStartDate());
        if(evdto.getEndDate()!=null)
            evt.setEndDate(evdto.getEndDate());
        evt.setStatus(evdto.getStatus());

        return EvenimentDTO.fromEveniment(service.updateEveniment(evt));
    }

    @RequestMapping(value="/posts", method = RequestMethod.POST)
    public ResponseEntity<?> addPostare (@RequestBody PostDTO postDTO){
        try{
            Post post = new Post(postDTO.getDescriere(),postDTO.getData(),service.getEvenimentById(postDTO.getIdEv()), service.getVoluntarById(postDTO.getIdUser()));
            return new ResponseEntity<Post>(post,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/interests/{id}", method = RequestMethod.GET)
    public Iterable<Interest> getInterests(@PathVariable Integer id) throws ServiceException {
        return service.getVoluntarInterest(id);
    }

}
