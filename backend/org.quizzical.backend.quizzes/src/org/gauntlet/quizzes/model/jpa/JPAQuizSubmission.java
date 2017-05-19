package org.gauntlet.quizzes.model.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.gauntlet.core.model.Constants;
import org.gauntlet.core.model.JPABaseEntity;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
@Table(name=Constants.Q7L_TABLE_NAME_PREFIX+Constants.Q7L_TABLE_NAME_SEPARATOR
	+"quiz_submission")
public class JPAQuizSubmission extends JPABaseEntity implements Serializable {
	private static final long serialVersionUID = -7656436051296927169L;

	@ManyToOne(targetEntity = JPAQuiz.class)
	@JoinColumn
	private JPAQuiz quiz;
	
	@OneToMany(targetEntity = JPAQuizProblemResponse.class, fetch=FetchType.EAGER, cascade=CascadeType.ALL, mappedBy="submission")
	private List<JPAQuizProblemResponse> responses = new ArrayList<>();

	public JPAQuizSubmission() {}

	public JPAQuizSubmission(JPAQuiz quiz) {
		this.code = String.format("%s-%d", quiz.getCode(), System.currentTimeMillis());
	}

	public JPAQuiz getQuiz() {
		return quiz;
	}

	public void setQuiz(JPAQuiz quiz) {
		this.quiz = quiz;
	}

	public List<JPAQuizProblemResponse> getResponses() {
		return responses;
	}

	public void setResponses(List<JPAQuizProblemResponse> responses) {
		this.responses = responses;
	}
	
	@PostPersist
	public void postPersist() throws Exception {
		System.out.println("Listening JPAQuizProblemResponse Post Persist ...");
		//sendQuizSubmissionPostUpdate(this.getQuiz().getUserId());
	}
	
	
	@PrePersist
	public void prePersist() throws Exception {
		System.out.println("Listening JPAQuizProblemResponse Pre Persist for userid..."+getQuiz().getUserId());
		sendQuizSubmissionPostUpdate(getQuiz().getUserId());
	}
	
	@PreUpdate
	public void preUpdate() throws Exception {
		System.out.println("Listening JPAQuizProblemResponse Pre Update : " + this.getName());
		//sendQuizSubmissionPostUpdate(this.getQuiz().getUserId());
	}
	
	@PostUpdate
	public void postUpdate() throws Exception {
		System.out.println("Listening JPAQuizProblemResponse Post Update : " + this.getName());
		//sendQuizSubmissionPostUpdate(this.getQuiz().getUserId());
	}
	
	
    public void sendQuizSubmissionPostUpdate(Long userId) throws Exception
    {
        ServiceReference ref = getContext().getServiceReference(EventAdmin.class.getName());
        if (ref != null)
        {
            EventAdmin eventAdmin = (EventAdmin) getContext().getService(ref);

            Dictionary properties = new Hashtable();
            properties.put("userid", userId);

            Event reportGeneratedEvent = new Event("org/quizzical/backend/reporting/SEND_DAILY_REPORT", properties);

            eventAdmin.sendEvent(reportGeneratedEvent);
        }
    }
    
    public static BundleContext getContext() throws Exception {
		return FrameworkUtil.getBundle(JPAQuizSubmission.class).getBundleContext();
    }    
    
    public static Object createServiceFromServiceType(Class type) throws Exception {
		Object svc = null;
		try {
			Bundle bundle = FrameworkUtil.getBundle(JPAQuizSubmission.class);
			ServiceReference sr = getServiceReference(bundle.getBundleContext(), type);
	    	return bundle.getBundleContext().getService(sr);
		} catch (Exception e) {
			throw e;
		}
    }    
    
    private static ServiceReference  getServiceReference(BundleContext context, Class targetServiceClass) throws InvalidSyntaxException {

        ServiceReference[] serviceRefs =
        		context.getServiceReferences(targetServiceClass.getName(),null);
        if (serviceRefs == null || serviceRefs.length == 0) {
            return null;
        }
        else {
        	return serviceRefs[0];
        }

    }  
}

