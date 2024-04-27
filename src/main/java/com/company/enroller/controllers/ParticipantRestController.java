package com.company.enroller.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.company.enroller.model.Participant;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/participants")
public class ParticipantRestController {

	@Autowired
	ParticipantService participantService;

//	@RequestMapping(value = "", method = RequestMethod.GET)
//	public ResponseEntity<?> getParticipants() {
//		Collection<Participant> participants = participantService.getAll();
//		return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
//	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipant(@PathVariable("id") String login) {
		Participant participant = participantService.findByLogin(login);
		if (participant == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Participant>(participant, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> registerParticipant(@RequestBody Participant participant) {
		Participant findParticipant = participantService.findByLogin(participant.getLogin());
		if (findParticipant != null) {
			return new ResponseEntity<>("Unable to create. A participant with login " + participant.getLogin() + " already exist.", HttpStatus.CONFLICT);
		}
		participantService.add(participant);
		return new ResponseEntity<Participant>(participant, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteParticipant(@PathVariable("id") String login) {
		Participant findParticipant = participantService.findByLogin(login);
		if (findParticipant == null) {
			return new ResponseEntity("Unable to delete Participant doesn't exist.", HttpStatus.CONFLICT);
		}
		participantService.delete(findParticipant);
		return new ResponseEntity("Participant has been deleted", HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateParticipant(@PathVariable("id") String login, @RequestBody Participant participant) {
		Participant currentParticipant = participantService.findByLogin(login);
		if (currentParticipant == null) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
		participantService.update(participant);
		return new ResponseEntity<Participant>(participant, HttpStatus.OK);
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> sortParticipants(@RequestParam(value = "sortOrder",required = false, defaultValue = "") String sortOrder,
											  @RequestParam(value = "sortBy", required = false, defaultValue = "login") String sortBy,
											  @RequestParam(value = "key", required = false, defaultValue = "") String key)	{
		if(sortBy.equals("login")){
			if(sortOrder.equals("ASC")|| sortOrder.equals("DESC") || sortOrder.isEmpty()){
				Collection<Participant> participants = participantService.getAllWithParams(sortBy, sortOrder, key);
				return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
			}
		}
		return new ResponseEntity("Invalid parameter",HttpStatus.NOT_FOUND);
	}

}
