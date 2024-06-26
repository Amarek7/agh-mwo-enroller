package com.company.enroller.persistence;

import com.company.enroller.model.Participant;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component("participantService")



public class ParticipantService {



	DatabaseConnector connector;

	public ParticipantService() {
		connector = DatabaseConnector.getInstance();
	}


	public Collection<Participant> getAll() {
		String hql = "FROM Participant";
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	}

    public Participant findByLogin(String login) {
		return connector.getSession().get(Participant.class,login);
  }

//	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public void add(Participant participant) {
		String hashedPassword = passwordEncoder().encode(participant.getPassword());
		participant.setPassword(hashedPassword);
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().save(participant);
		transaction.commit();
	}

	public void delete(Participant participant) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().remove(participant);
		transaction.commit();
	}

	public void update(Participant newParticipant) {
		Transaction transaction = connector.getSession().beginTransaction();
		connector.getSession().merge(newParticipant);
		transaction.commit();
	}

	public Collection<Participant> getAllWithParams(String sortBy, String sortOrder, String key) {
		String hql = "FROM Participant P WHERE P.login LIKE '%" +key+ "%' ORDER BY P." + sortBy + " " + sortOrder;
		Query query = connector.getSession().createQuery(hql);
		return query.list();
	}
}
