package service.interfaces;

import java.util.List;

import javax.validation.Valid;

import domain.request.EmployeeRequest;
import domain.response.EmployeeResponse;

public interface EmployeeServiceInterface {

	public List<EmployeeResponse> findAll();

	public EmployeeResponse getById(long id);

	public EmployeeResponse register(@Valid EmployeeRequest request);

	// doesn't need @Valid since it can contain null values(data that should not be updated)
	public EmployeeResponse update(Long id, EmployeeRequest request);

	// returns true if entity was removed, false if there wasn't any
	public boolean delete(long id);

}
