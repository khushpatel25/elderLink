package com.elderlink.backend.services.impl;

import com.elderlink.backend.domains.entities.RequestEntity;
import com.elderlink.backend.domains.entities.RequestHistoryEntity;
import com.elderlink.backend.domains.enums.ActionType;
import com.elderlink.backend.domains.enums.RequestStatus;
import com.elderlink.backend.repositories.RequestHistoryRepository;
import com.elderlink.backend.repositories.RequestRepository;
import com.elderlink.backend.repositories.UserRepository;
import com.elderlink.backend.services.RequestService;
import com.elderlink.backend.services.RequestsHistoryService;
import com.elderlink.backend.services.UserService;
import com.elderlink.backend.utils.IsUserAuthorized;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class RequestsHistoryServiceImpl implements RequestsHistoryService{

    Logger logger = LoggerFactory.getLogger (RequestsHistoryService.class);

    @Autowired
    private IsUserAuthorized isUserAuthorized;

    @Autowired
    private RequestHistoryRepository requestHistoryRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestService requestService;

    @Autowired
    private RequestRepository requestRepository;

    /**
     * Create a new request history entry.
     *
     * @param requestHistoryEntity The request history entity to create
     * @return The created request history entity
     */
    @Override
    public RequestHistoryEntity createRequestHistory(RequestHistoryEntity requestHistoryEntity) {
        try {

            //To check volunteer's authority
            isUserAuthorized.checkUserAuthority(requestHistoryEntity.getVolunteer().getId());

            //can create separate module
            if(!userService.isUserExisted(requestHistoryEntity.getVolunteer().getId())){
                throw new EntityNotFoundException ("Volunteer with this id doesn't exist!");
            }

            //can create separate module
            if(!userService.isUserExisted (requestHistoryEntity.getElderPerson ().getId ())){
                throw new EntityNotFoundException ("Elder person with this id doesn't exist!");
            }

            //checking if request exist
            if(requestService.isRequestExists(requestHistoryEntity.getRequest().getId())){
                RequestEntity request = requestRepository.findById (requestHistoryEntity.getRequest ().getId ())
                        .orElseThrow (()-> new EntityNotFoundException ("Request with this id doesn't exist!"));
                if(!Objects.equals (request.getUser ().getId (), requestHistoryEntity.getElderPerson ().getId ())){
                    throw new EntityNotFoundException("Elder person id is not associated with this request!");
                }
            }
            RequestHistoryEntity requestHistory = requestHistoryRepository.save(requestHistoryEntity);
            logger.info ("RequestHistory created successfully.");
            return requestHistory;
        }catch(EntityNotFoundException e){
            logger.error ("An error occurred while creating the RequestHistory! -> {}",e.getMessage ());
            throw new EntityNotFoundException ("An error occurred while creating the requestHistory."+ e.getMessage ());
        }
    }
    /**
     * Retrieve request histories by request ID.
     *
     * @param requestId The ID of the request
     * @return List of request history entities
     */

    @Override
    public List<RequestHistoryEntity> getRequestHistoriesByRequestId(Long requestId) {
        try{
            return requestHistoryRepository.findByRequestId (requestId);
        }catch (Exception e){
            throw new RuntimeException ("An error occurred while fetching requestHistoryByRequestId");
        }
    }
    /**
     * Retrieve request histories by elder person ID.
     *
     * @param elderPersonId The ID of the elder person
     * @return List of request history entities
     */

    @Override
    public List<RequestHistoryEntity> getRequestHistoriesByElderPersonId(Long elderPersonId) {
        try{
            return requestHistoryRepository.findByElderPersonId (elderPersonId);
        }catch (Exception e){
            throw new RuntimeException ("An error occurred while fetching requestHistory by ElderPersonId!");
        }
    }
    /**
     * Check if a request history exists by ID.
     *
     * @param id The ID of the request history
     * @return True if the request history exists, false otherwise
     */

    @Override
    public boolean isRequestHistoryExists(Long id) {
        return requestHistoryRepository.existsById(id);
    }
    @Override
    public List<RequestHistoryEntity> getRequestHistories() {
        try {
            return requestHistoryRepository.findAll();
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("An error occurred while fetching requestHistories.");
        }
    }
    /**
     * Update a request history entry.
     *
     * @param id                  The ID of the request history entry to update
     * @param requestHistoryEntity The updated request history entity
     * @return The updated request history entity
     */
    @Override
    public RequestHistoryEntity updateRequestHistory(Long id,RequestHistoryEntity requestHistoryEntity) {
        try {

            //checking if requestHistory is existed or not
            RequestHistoryEntity existingRequestHistory = requestHistoryRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("RequestHistory with this id doesn't not exist!"));

            //checking if request is exsited or not
            if(!requestRepository.existsById (requestHistoryEntity.getRequest ().getId ())){
                throw new EntityNotFoundException ("Request with this id doesn't exist!");
            }

            if(!userRepository.existsById (requestHistoryEntity.getVolunteer ().getId ())){
                throw new EntityNotFoundException ("Volunteer with this id doesn't exist!");
            }

            //Here we are checking that user(ELDER_PERSON) which has issued the request can update the requestHistory status to either ACTED or NOT_ACTED
            isUserAuthorized.checkUserAuthority(existingRequestHistory.getElderPerson ().getId ());


            //To set all other requests to DECLINED if elderPerson is ACCEPTING one volunteer's request
            if(Objects.equals (requestHistoryEntity.getActionType ().toString (), "ACCEPTED")){
                List<RequestHistoryEntity> requestHistoryByRequestId = requestHistoryRepository.findByRequestId (requestHistoryEntity.getRequest ().getId ());
                requestHistoryByRequestId.forEach (reqHistory -> {
                    if(Objects.equals (reqHistory.getId (), id)){
                        reqHistory.setActionType (ActionType.ACCEPTED);
                    }else {
                        reqHistory.setActionType (ActionType.DECLINED);
                    }
                    requestHistoryRepository.save (reqHistory);
                });
                //setting request status to CLOSE
                Optional<RequestEntity> request = requestRepository.findById (requestHistoryEntity.getRequest ().getId ());
                //updating status and database if request is present
                request.ifPresent (requestEntity -> {
                    requestEntity.setRequestStatus (RequestStatus.CLOSE);
                    //updating into database
                    requestRepository.save (requestEntity);
                });
                return existingRequestHistory;
            }else{
                existingRequestHistory.setActionType(requestHistoryEntity.getActionType());
                return requestHistoryRepository.save(existingRequestHistory);
            }

        }catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException("An error occurred while updating the requestHistory.");
        }
    }
    @Override
    public void deleteRequestHistory(Long id) {
        try{
            RequestHistoryEntity existingRequestHistory = requestHistoryRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("RequestHistory with this id doesn't exist!"));

            //Here we are checking that user(ELDER_PERSON) which has issued the request can delete the requestHistory
            //Basically the idea is that multiple volunteers can accept the same request and
            // it's upto ElderPerson whom request he wants to accept so others requestHistory will be deleted by elderPerson when he will click on decline
            isUserAuthorized.checkUserAuthority(existingRequestHistory.getRequest().getUser().getId());
            requestHistoryRepository.deleteById(id);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("An error occurred while deleting the requestHistory.");
        }
    }
}







