package com.elderlink.backend.controllers;

import com.elderlink.backend.domains.dto.RequestHistoryDto;
import com.elderlink.backend.domains.entities.RequestHistoryEntity;
import com.elderlink.backend.mappers.impl.RequestHistoryMapper;
import com.elderlink.backend.services.RequestsHistoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/requestsHistory")
public class RequestsHistoryController{

    @Autowired
    private RequestsHistoryService requestHistoryService;

    @Autowired
    private RequestHistoryMapper requestHistoryMapper;
    /**
     * Endpoint to get request histories by elder person ID.
     *
     * @param elderPersonId The ID of the elder person
     * @return ResponseEntity containing the list of request histories or error status
     */
    @GetMapping("/requestsByElderPersonId/{elderPersonId}")
    public ResponseEntity<List<RequestHistoryDto>> getRequestsHistoryByElderPersonId(
            @Valid @PathVariable("elderPersonId") Long elderPersonId
    ){
        List<RequestHistoryEntity> requestHistoryEntities = requestHistoryService.getRequestHistoriesByElderPersonId (elderPersonId);
        List<RequestHistoryDto> requestHistoryDto = requestHistoryEntities.stream()
                .map(requestHistoryMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(requestHistoryDto);

    }
    /**
     * Endpoint to get request histories by request ID.
     *
     * @param requestId The ID of the request
     * @return ResponseEntity containing the list of request histories or error status
     */
    @GetMapping("/{requestId}")
    public ResponseEntity<List<RequestHistoryDto>> getRequestHistoryByRequestId(
            @Valid @PathVariable("requestId") Long requestId
    ){
        List<RequestHistoryEntity> requestHistoryEntities = requestHistoryService.getRequestHistoriesByRequestId(requestId);
        List<RequestHistoryDto> requestHistoryDto = requestHistoryEntities.stream()
                .map(requestHistoryMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(requestHistoryDto);

    }

    @GetMapping()
    public ResponseEntity<List<RequestHistoryDto>> getRequestHistories(){
            List<RequestHistoryEntity> requestEntities = requestHistoryService.getRequestHistories();
            return ResponseEntity.ok(
                    requestEntities.stream()
                            .map(requestHistoryMapper::toDto)
                            .collect(Collectors.toList())
            );
    }

    /**
     * Creates a new request history.
     *
     * @param requestHistoryDto The request history DTO containing data for the new request history.
     * @return ResponseEntity with the created request history DTO.
     */
    @PostMapping("/create")
    public ResponseEntity<RequestHistoryDto> createRequestHistory(
            @Valid @RequestBody RequestHistoryDto requestHistoryDto
    ){
        RequestHistoryEntity requestHistoryEntity = requestHistoryMapper.toEntity(requestHistoryDto);
        RequestHistoryEntity createdRequestHistory = requestHistoryService.createRequestHistory(requestHistoryEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(requestHistoryMapper.toDto(createdRequestHistory));
    }

    /**
     * Updates an existing request history.
     *
     * @param id The ID of the request history to be updated.
     * @param requestHistoryDto The request history DTO containing updated data.
     * @return ResponseEntity with the updated request history DTO.
     * @throws EntityNotFoundException if the request history with the given ID doesn't exist.
     */
    @PatchMapping("/update/{id}")
    public ResponseEntity<RequestHistoryDto> updateRequestHistory(
            @Valid @PathVariable("id") Long id,
            @Valid @RequestBody RequestHistoryDto requestHistoryDto
    ){
            if(!requestHistoryService.isRequestHistoryExists(id)){
                throw new EntityNotFoundException ("RequestHistory with this id doesn't exist!");
            }
            RequestHistoryEntity requestHistoryEntity = requestHistoryMapper.toEntity(requestHistoryDto);
            RequestHistoryEntity updatedRequestHistory = requestHistoryService.updateRequestHistory(id,requestHistoryEntity);
            return ResponseEntity.ok(requestHistoryMapper.toDto(updatedRequestHistory));
    }

    /**
     * Deletes a request history.
     *
     * @param id The ID of the request history to be deleted.
     * @return ResponseEntity indicating the result of the deletion operation.
     * @throws EntityNotFoundException if the request history with the given ID doesn't exist.
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteRequestHistory(
            @Valid @PathVariable("id") Long id
    ){
            if(!requestHistoryService.isRequestHistoryExists(id)){
                throw new EntityNotFoundException("RequestHistory with this id dones't exist!");
            }
            requestHistoryService.deleteRequestHistory(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
