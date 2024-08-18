package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.Subscriber;
import vn.hoidanit.jobhunter.domain.email.ResEmailJob;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import vn.hoidanit.jobhunter.repository.SubscriberRepository;

@Service
public class SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final SkillRepository skillRepository;
    private final JobRepository jobRepository;
    private final EmailService emailService;

    public SubscriberService(SubscriberRepository subscriberRepository, 
    SkillRepository skillRepository, EmailService emailService,
    JobRepository jobRepository) {
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
        this.jobRepository = jobRepository;
        this.emailService = emailService;
    }

    public boolean isExistByEmail(String email) {
        return this.subscriberRepository.existsByEmail(email);
    }

    public Subscriber createNewSubscriber(Subscriber subscriber) {
        // check skills
        if(subscriber.getSkills() != null) {
            List<Long> reqSkill = subscriber.getSkills()
                    .stream().map(item -> item.getId())
                    .collect(Collectors.toList());

            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkill);
            subscriber.setSkills(dbSkills);
        }
        return this.subscriberRepository.save(subscriber);
    }

    public Subscriber findSubscriberById(long id) {
        return this.subscriberRepository.findById(id);
    }

    public Subscriber updateSubscriber(Subscriber subsDB, Subscriber subsRequest) {

        if(subsRequest.getSkills() != null) {
            List<Long> reqSkills = subsRequest.getSkills()
                    .stream().map(item -> item.getId())
                    .collect(Collectors.toList());
            
            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkills);
            subsDB.setSkills(dbSkills);
        }

        return this.subscriberRepository.save(subsDB);
    }

    public ResEmailJob convertJobToSendEmail(Job j) {
        ResEmailJob res = new ResEmailJob();
        res.setName(j.getName());
        res.setSalary(j.getSalary());
        res.setCompany(new ResEmailJob.CompanyEmail(j.getCompany().getName()));
        List<Skill> skills = j.getSkills();
        List<ResEmailJob.SkillEmail> skillEmails = skills.stream().map(
            skill -> new ResEmailJob.SkillEmail(skill.getName()))
            .collect(Collectors.toList());
        res.setSkills(skillEmails);
        return res;
    }

    public void sendSubscribersEmailJobs() {
        List<Subscriber> listSubs = this.subscriberRepository.findAll();
        if(listSubs != null && listSubs.size() > 0) {
            for(Subscriber sub : listSubs) {
                List<Skill> listSkills = sub.getSkills();
                if(listSkills != null && listSkills.size() > 0) {
                    List<Job> listJobs = this.jobRepository.findBySkillsIn(listSkills);
                    if(listJobs != null && listJobs.size() > 0) {

                        List<ResEmailJob> arr = listJobs.stream().map(
                            job -> this.convertJobToSendEmail(job)
                        ).collect(Collectors.toList());

                        this.emailService.sendEmailFromTemplateSync(sub.getEmail(), 
                        "Co hoi viec lam hot dang cho don ban", 
                        "job",
                        sub.getName(),
                        arr);
                    }
                }
            }
        }
    }
}
