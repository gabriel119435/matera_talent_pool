package service.interfaces;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import domain.request.EmployeeRequest;
import domain.response.EmployeeResponse;

public interface EmployeeServiceInterface {

	List<EmployeeResponse> findAll();

	Optional<EmployeeResponse> getById(long id);

	EmployeeResponse register(@Valid EmployeeRequest request);

	// doesn't need @Valid since it can contain null values(data that should not be
	// updated)
	EmployeeResponse update(Long id, EmployeeRequest request);

	// doesn't actually delete, only tries to change status to INACTIVE
	void delete(long id);

}
