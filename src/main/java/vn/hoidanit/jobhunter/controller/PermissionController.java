package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.PermissionService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/v1")
public class PermissionController {
    private final PermissionService permissionService;
    
    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/permissions")
    @ApiMessage("Create a permission")
    public ResponseEntity<Permission> createNewPermission(@Valid @RequestBody Permission permission) throws IdInvalidException {
        //check exist
        if(this.permissionService.isPermissionExist(permission)) {
            throw new IdInvalidException("Permission da ton tai");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.permissionService.createPermission(permission));
    }
    
    @PutMapping("/permissions")
    @ApiMessage("Update a permission")
    public ResponseEntity<Permission> updatePermission(@Valid @RequestBody Permission p) throws IdInvalidException {
        if(this.permissionService.getPermissionById(p.getId()) == null) {
            throw new IdInvalidException("Permission voi id = " + p.getId() + " khong ton tai");
        }

        if(this.permissionService.isPermissionExist(p)) {
            throw new IdInvalidException("Permission da ton tai");
        }

        return ResponseEntity.ok().body(this.permissionService.update(p));
    }

    @DeleteMapping("/permissions/{id}")
    @ApiMessage("delete a permission")
    public ResponseEntity<Void> deletePermission(@PathVariable("id") long id) throws IdInvalidException {
        // check exist by id
        if(this.permissionService.getPermissionById(id) == null) {
            throw new IdInvalidException("Permission voi id = " + id + " khong ton tao");
        }
        this.permissionService.deletePermission(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/permissions")
    @ApiMessage("get all permissions")
    public ResponseEntity<ResultPaginationDTO> getAllPermissions(
        @Filter Specification<Permission> spec, Pageable pageable
    ) {
        return ResponseEntity.ok().body(this.permissionService.getAllPermission(spec, pageable));
    }
    
}
