package com.elderlink.backend.controllers;

import com.elderlink.backend.domains.dto.RequestDto;
import com.elderlink.backend.domains.entities.RequestEntity;
import com.elderlink.backend.mappers.Mapper;
import com.elderlink.backend.repositories.UserRepository;
import com.elderlink.backend.services.RequestService;
import com.elderlink.backend.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/requests")
public class RequestsController {

    @Autowired
    private RequestService requestService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Mapper<RequestEntity, RequestDto> requestMapper;
    /**
     * Endpoint to create a new request.
     *
     * @param requestDto The DTO containing request information
     * @return ResponseEntity containing the created request or error status
     */
    @PostMapping("/create")
    public ResponseEntity<RequestDto> createRequest(
            @Valid @RequestBody RequestDto requestDto
    ){
        RequestEntity requestEntity = requestMapper.toEntity(requestDto);
        RequestEntity createdRequest =  requestService.createRequest(requestEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(requestMapper.toDto(createdRequest));
    }
    /**
     * Endpoint to get requests by user ID.
     *
     * @param userId The ID of the user
     * @return ResponseEntity containing the list of requests or error status
     */
    @GetMapping("/{userId}")
    public ResponseEntity<List<RequestDto>> getRequestsByUserId(@Valid @PathVariable Long userId){
            if(!userRepository.existsById(userId)){
                throw new UsernameNotFoundException("User doesn't exist!");
            }
            List<RequestEntity> userRequests = requestService.findRequestsByUserId(userId);
        List<RequestDto> userRequestDto = userRequests.stream()
                .map(requestMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(userRequestDto);

    }
    /**
     * Endpoint to update a request.
     *
     * @param requestId The ID of the request to be updated
     * @param updateRequestDto The DTO containing updated request information
     * @return ResponseEntity containing the updated request or error status
     */
    @PatchMapping("/{requestId}")
    public ResponseEntity<RequestDto> updateRequest(
            @Valid @PathVariable Long requestId,
            @Valid @RequestBody RequestDto updateRequestDto
    ){
        RequestEntity requestEntity = requestMapper.toEntity (updateRequestDto);
        RequestEntity updatedRequest = requestService.updateRequest(requestId,requestEntity);
        return ResponseEntity.status (HttpStatus.OK).body (requestMapper.toDto(updatedRequest));
    }
    /**
     * Endpoint to delete a request by ID.
     *
     * @param requestId The ID of the request to be deleted
     * @return ResponseEntity with no content or error status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity deleteRequest(@Valid @PathVariable("id") Long requestId){
        requestService.deleteRequest(requestId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Retrieves all requests and returns them as a list of RequestDto objects.
     *
     * @return ResponseEntity containing a list of RequestDto objects with OK status.
     */
    @GetMapping()
    public ResponseEntity<List<RequestDto>> getAllRequests(){
        List<RequestEntity> allRequests = requestService.getAllRequests ();
        List<RequestDto> requestDto = allRequests.stream()
                .map(requestMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(requestDto);

    }

    /**
     * Retrieves a request by its ID.
     *
     * @param requestId The ID of the request to retrieve.
     * @return ResponseEntity containing the RequestDto if found with OK status, otherwise NOT_FOUND status.
     */
    @GetMapping("/requestId/{requestId}")
    public ResponseEntity<RequestDto> getRequestsByRequestId(@Valid @PathVariable Long requestId){
        Optional<RequestEntity> request = requestService.findRequestById(requestId);
        return ResponseEntity.status (HttpStatus.OK).body (requestMapper.toDto (request.get ()));
    }


}
