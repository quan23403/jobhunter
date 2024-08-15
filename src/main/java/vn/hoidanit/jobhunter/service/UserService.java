package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResCreateUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResUpdateUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResUserDTO;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.UserRepository;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CompanyService companyService;

    public UserService(UserRepository userRepository, CompanyService companyService) {
        this.userRepository = userRepository;
        this.companyService = companyService;
    }

    public User handleCreateUser(User user) {

        // check Company
        if(user.getCompany() != null) {
            Optional<Company> company = this.companyService.handleGetCompanyById(user.getCompany().getId());
            user.setCompany(company.isPresent() ? company.get() : null);
        }
        return this.userRepository.save(user);
    }

    public void handleDeleteUser(long id) {
        // User user = this.userRepository.findById(id);
        this.userRepository.deleteById(id);
    }

    public User handleGetUserById(long id) {
        return this.userRepository.findById(id);
    }

    public ResultPaginationDTO handleGetAllUser(Specification<User> specification, Pageable pageable) {
        Page<User> pageUser = this.userRepository.findAll(specification, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);
        List<ResUserDTO> listUser = pageUser.getContent()
                .stream().map(item -> new ResUserDTO(
                    item.getId(),
                    item.getName(),
                    item.getEmail(),
                    item.getGender(),
                    item.getAddress(),
                    item.getAge(),
                    item.getUpdatedAt(),
                    item.getCreatedAt(),
                    new ResUserDTO.CompanyUser(
                        item.getCompany() != null ? item.getCompany().getId() : 0,
                        item.getCompany() != null ? item.getCompany().getName() : null
                    )
                )).collect(Collectors.toList());
        rs.setResult(listUser);

        return rs;
    }

    public User handleUpdateUser(User user) {
        User updateUser = this.handleGetUserById(user.getId());
        if (updateUser != null) {
            updateUser.setName(user.getName());
            updateUser.setAddress(user.getAddress());
            updateUser.setAge(user.getAge());
            updateUser.setGender(user.getGender());
        }

        if(user.getCompany() != null) {
            Optional<Company> companyOptional = this.companyService.handleGetCompanyById(user.getCompany().getId());
            updateUser.setCompany(companyOptional.isPresent() ? companyOptional.get() : null);
        }
        this.userRepository.save(updateUser);

        return updateUser;
    }

    public User handleGetUserByUsername(String username) {
        return this.userRepository.findByEmail(username);
    }

    public boolean isEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public ResCreateUserDTO convertToResCreateUserDTO(User user) {
        ResCreateUserDTO res = new ResCreateUserDTO();
        ResCreateUserDTO.CompanyUser companyUser = new ResCreateUserDTO.CompanyUser();

        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setAddress(user.getAddress());
        res.setName(user.getName());
        res.setCreatedAt(user.getCreatedAt());
        res.setGender(user.getGender());
        res.setAge(user.getAge());

        if(user.getCompany() != null) {
            companyUser.setId(user.getCompany().getId());
            companyUser.setName(user.getCompany().getName());
            res.setCompany(companyUser);
        }
        return res;
    }

    public ResUserDTO convertToResUserDTO(User user) {
        ResUserDTO res = new ResUserDTO();
        ResUserDTO.CompanyUser companyUser = new ResUserDTO.CompanyUser();
        if(user.getCompany() != null) {
            companyUser.setId(user.getCompany().getId());
            companyUser.setName(user.getCompany().getName());
            res.setCompany(companyUser);
        }
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setAddress(user.getAddress());
        res.setName(user.getName());
        res.setUpdatedAt(user.getUpdatedAt());
        res.setCreatedAt(user.getCreatedAt());
        res.setGender(user.getGender());
        res.setAge(user.getAge());
        return res;
    }

    public ResUpdateUserDTO convertToResUpdateUserDTO(User user) {
        ResUpdateUserDTO res = new ResUpdateUserDTO();
        ResUpdateUserDTO.CompanyUser companyUser = new ResUpdateUserDTO.CompanyUser();
        if(user.getCompany() != null) {
            companyUser.setId(user.getCompany().getId());
            companyUser.setName(user.getCompany().getName());
            res.setCompany(companyUser);
        }
        res.setId(user.getId());
        res.setAddress(user.getAddress());
        res.setName(user.getName());
        res.setUpdatedAt(user.getUpdatedAt());
        res.setGender(user.getGender());
        res.setAge(user.getAge());
        return res;
    }

    public void updateUserToken(String token, String email) {
        User currentUser = this.handleGetUserByUsername(email);
        if(currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

    public User getUserByRefeshTokenAndEmail(String token, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }
}
