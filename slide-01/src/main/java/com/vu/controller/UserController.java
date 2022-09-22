package com.vu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vu.dto.UserDto;
import com.vu.form.SearchForm;
import com.vu.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/list")
    public ResponseEntity<List<UserDto>> getAll() {
		List<UserDto> userDtos = userService.getUsers();
		if (userDtos.isEmpty()) {
			return new ResponseEntity<List<UserDto>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<UserDto>>(userDtos, HttpStatus.OK);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UserDto> getOne(@PathVariable("id") Long id) {
		UserDto userDto = userService.getUserById(id);
		if(userDto == null) {
			return new ResponseEntity<UserDto>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<UserDto>(userDto, HttpStatus.OK);
	}
	
	@PostMapping("/search")
    public ResponseEntity<List<UserDto>> search(@RequestBody SearchForm searchForm) {
        List<UserDto> userDtos = null;
        if (searchForm.getKeyword() != null && !searchForm.getKeyword().isEmpty()) {
            userDtos = userService.getUsersByFullName(searchForm.getKeyword());
            if (userDtos.isEmpty()) {
                return new ResponseEntity<List<UserDto>>(HttpStatus.NO_CONTENT);
            }
        }
        return new ResponseEntity<List<UserDto>>(userDtos, HttpStatus.OK);
    }
	
	@PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody UserDto userDto) {
        try {
            userService.createUser(userDto);
            return new ResponseEntity<String>("Add successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @PutMapping("/edit/{id}")
    public ResponseEntity<String> edit(@RequestBody UserDto userDto, @PathVariable("id") Long id) {
        try {
            if (userService.getUserById(id) == null) {
                return new ResponseEntity<String>("Not found data!", HttpStatus.NOT_FOUND);
            }
            userService.updateUser(userDto, id);
            return new ResponseEntity<String>("Edit successfully!", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping("/remove/{id}")
    public ResponseEntity<String> remove(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<String>("Delete successfully!", HttpStatus.OK);
    }
}
