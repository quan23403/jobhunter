package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Subscriber;
import vn.hoidanit.jobhunter.service.SubscriberService;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/api/v1")
public class SubscriberController {
    private final SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @PostMapping("/subscribers")
    @ApiMessage("Create a subscriber")
    public ResponseEntity<Subscriber> createSubscriber(@Valid @RequestBody Subscriber subscriber) throws IdInvalidException {
        // check email
        boolean isExist = this.subscriberService.isExistByEmail(subscriber.getEmail());
        if(isExist == true) {
            throw new IdInvalidException("Email " + subscriber.getEmail() + " da ton tai");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(this.subscriberService.createNewSubscriber(subscriber));
    }
    
    @PutMapping("/subscribers")
    @ApiMessage("Update a subscriber")
    public ResponseEntity<Subscriber> updateSubscriber(@RequestBody Subscriber subscriber) throws IdInvalidException {
        //TODO: process PUT request
        Subscriber subsDB = this.subscriberService.findSubscriberById(subscriber.getId());
        if(subsDB == null) {
            throw new IdInvalidException("Id " + subscriber.getId() + " khong ton tai");
        }
        return ResponseEntity.ok().body(this.subscriberService.updateSubscriber(subsDB, subscriber));
    }

    @PostMapping("/subscribers/skills")
    @ApiMessage("Get subscriber's skill")
    public ResponseEntity<Subscriber> getSubscribersSkill() {
        //TODO: process POST request
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get() : "";
            
        return ResponseEntity.ok().body(this.subscriberService.findByEmail(email));
    }
    
}
