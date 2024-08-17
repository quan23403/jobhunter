package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.SkillRepository;

@Service
public class SkillService {
    private final SkillRepository skillRepository;
    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public boolean isNameExist(String name) {
        return this.skillRepository.existsByName(name);
    }

    public Skill createNewSkill(Skill skill) {
        return this.skillRepository.save(skill);
    }

    public Skill getSkillById(long id) {
        return this.skillRepository.findById(id);
    }

    public Skill updateSkill(Skill skill) {
        return this.skillRepository.save(skill);
    }

    public void deleteSkill(long id) {

        //delete job (inside job_skill table)
        Skill skill = this.skillRepository.findById(id);
        
        skill.getJobs().forEach(jobs -> jobs.getSkills().remove(skill));

        // delete subscriber
        skill.getSubscribers().forEach(subs -> subs.getSkills().remove(skill));
        
        this.skillRepository.delete(skill);
    }

    public ResultPaginationDTO getAllSkill(Specification<Skill> specification, Pageable pageable) {
        Page<Skill> pageSkill = this.skillRepository.findAll(specification, pageable);
        
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageSkill.getTotalPages());
        mt.setTotal(pageSkill.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageSkill.getContent());

        return rs;
        
    }
}
