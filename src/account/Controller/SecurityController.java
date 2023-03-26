package account.Controller;

import account.DTO.SecurityEventResponse;
import account.Service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/security/")
public class SecurityController {

  @Autowired
  EventService eventService;

  @GetMapping("events")
  List<SecurityEventResponse> getEvents() {
    return eventService.getAllEvents();
  }
}
