package controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import domain.request.EmployeeRequest;
import domain.response.EmployeeResponse;
import service.interfaces.EmployeeServiceInterface;

@Validated
@RestController
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	private EmployeeServiceInterface service;

	@PreAuthorize("hasAuthority('READ')")
	@GetMapping
	public ResponseEntity<List<EmployeeResponse>> findAll() {
		return new ResponseEntity<List<EmployeeResponse>>(service.findAll(), HttpStatus.OK);
	}
	
	@PreAuthorize("hasAuthority('READ')")
	@GetMapping(value = "/{id}")
	public ResponseEntity<EmployeeResponse> getById(@PathVariable Long id) {
		final Optional<EmployeeResponse> opt = service.getById(id);
		return opt.map(e ->  new ResponseEntity<EmployeeResponse>(e, HttpStatus.OK))
				  .orElseGet(() -> new ResponseEntity<EmployeeResponse>(HttpStatus.NOT_FOUND));
		
	}

	// some http status were read here: https://stackoverflow.com/questions/2342579/http-status-code-for-update-and-delete
	@PreAuthorize("hasAuthority('WRITE')")
	@PostMapping
	public ResponseEntity<EmployeeResponse> registerEmployee(@RequestBody @Valid EmployeeRequest request) {
		return new ResponseEntity<EmployeeResponse>(service.register(request), HttpStatus.CREATED);
	}
	
	// patch usage instead of put https://medium.com/backticks-tildes/restful-api-design-put-vs-patch-4a061aa3ed0b
	@PreAuthorize("hasAuthority('WRITE')")
	@PatchMapping(value = "/{id}")
	public ResponseEntity<EmployeeResponse> updateEmployee(@PathVariable Long id, @RequestBody EmployeeRequest request) {
		return new ResponseEntity<EmployeeResponse>(service.update(id, request), HttpStatus.OK);
	}
	
	//  idempotence: https://stackoverflow.com/questions/4088350/is-rest-delete-really-idempotent
	@PreAuthorize("hasAuthority('WRITE')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<EmployeeResponse> deleteEmployee(@PathVariable Long id) {
		service.delete(id);
		return new ResponseEntity<EmployeeResponse>(HttpStatus.NO_CONTENT);
	}
}
