package yellowpenguin.ninja.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import yellowpenguin.ninja.dto.TaskResponse;
import yellowpenguin.ninja.models.User;
import yellowpenguin.ninja.services.TaskService;

@Controller
@RequestMapping("task")
public class TaskController {
	
	private TaskService taskService;
	
	@GetMapping
	public ResponseEntity<TaskResponse> getAllMyTasks(@RequestHeader (HttpHeaders.AUTHORIZATION) String headerAuth){
		User user;
		taskService.findAllByUser(user);
		return null;
	}

}
