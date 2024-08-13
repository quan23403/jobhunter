package vn.hoidanit.jobhunter.domain.response.job;

import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.LevelEnum;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class ResCreateJobDTO {
    private long id;
    private String name;
    private String location;
    private double salary;
    private int quantity;
    private LevelEnum level;
    private boolean active;

    private Instant startDate;
    private Instant endDate;


    private List<String> skills;

    private Instant createdAt;
    private String createdBy; 
}
