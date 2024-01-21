package com.galsie.gcs.microservicecommon.lib.gcsresponse;

import com.galsie.gcs.microservicecommon.lib.galassets.data.dto.assetref.AppLangTranslationReferenceDTO;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.data.dto.GCSResponseDTO;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.data.dto.GCSResponseErrorDTO;
import com.galsie.gcs.microservicecommon.lib.gcsresponse.exception.GCSResponseException;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.data.entity.GalEntity;
import com.galsie.gcs.serviceutilslibrary.utils.gcs.repository.GalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;

import java.util.*;

/**
 *
 * A GCSResponse holds a an optional GCSResponseErrorDTO and responseDataType
 *
 * @param <responseDataType> the type of response data
 */
@Slf4j
public class GCSResponse<responseDataType>  {

    Optional<GCSResponseErrorDTO> gcsError;

    Optional<responseDataType> responseData;

    /**
     * Either of error or responseDataType must be non-null
     * - Usually, one of these is present
     * - But in some cases, where we need to emphasize the error, we include both
     * @param error
     * @param responseDataType
     */
    public GCSResponse(@Nullable GCSResponseErrorDTO error, @Nullable responseDataType responseDataType){
        assert error != null || responseDataType != null;
        this.gcsError = Optional.ofNullable(error);
        this.responseData = Optional.ofNullable(responseDataType);
    }



    public ResponseEntity<GCSResponseDTO<responseDataType>> toResponseEntity(){
        GCSResponseDTO<responseDataType> errorDTO = new GCSResponseDTO<responseDataType>(gcsError.orElse(null), responseData.orElse(null));
        return new ResponseEntity<GCSResponseDTO<responseDataType>>(errorDTO, HttpStatus.OK);
    }

    public boolean hasError(){
        return this.gcsError.isPresent();
    }

    public GCSResponseErrorDTO getGcsError(){
        return this.gcsError.orElse(null);
    }

    public boolean hasResponseData(){
        return this.responseData.isPresent();
    }

    public Optional<responseDataType> getResponseDataOpt(){
        return this.responseData;
    }

    /**
     * @return the response data unwrapped from the optional
     */
    public responseDataType getResponseData(){
        return this.responseData.orElse(null);
    }


    public static <responseDataType> GCSResponse<responseDataType> response(responseDataType responseData){
        return new GCSResponse<>(null, responseData);
    }

    public static <responseDataType> GCSResponse<responseDataType> errorResponse(GCSResponseErrorDTO error){
        return new GCSResponse<>(error, null);
    }

    public static <responseDataType> GCSResponse<responseDataType> errorResponseWithResponseData(GCSResponseErrorDTO error, responseDataType responseData){
        return new GCSResponse<>(error, responseData);
    }

    public static <responseDataType> GCSResponse<responseDataType> errorResponseWithResponseData(GCSResponseErrorType errorType, responseDataType responseData){
        return new GCSResponse<>(GCSResponseErrorDTO.ofType(errorType), responseData);
    }

    public static <responseDataType> GCSResponse<responseDataType> errorResponse(GCSResponseErrorType errorType){
        return errorResponse(GCSResponseErrorDTO.ofType(errorType));
    }
    public static <responseDataType> GCSResponse<responseDataType> errorResponseWithMessage(GCSResponseErrorType errorType, String msg){
        return errorResponse(GCSResponseErrorDTO.ofMessage(errorType, msg));
    }
    /*
    Entity Operations
     */


    /*
    Save Single Entity
     */
    // Save single entity, throws exceptions
    public static <responseDataType extends GalEntity<ID>, ID> GCSResponse<responseDataType> saveEntityThrows(GalRepository<responseDataType, ID> repository, responseDataType entity) throws GCSResponseException {
        try{
            return response(repository.save(entity));
        }catch (Exception exception){
            log.info("An exception was thrown: " + exception.getMessage());
            log.error("Exception: ", exception);
            throw new GCSResponseException(errorResponse(GCSResponseErrorType.FAILED_TO_SAVE_ENTITY));
        }
    }
    // Save single entity, no exceptions
    public static <responseDataType extends GalEntity<ID>, ID> GCSResponse<responseDataType> saveEntity(GalRepository<responseDataType, ID> repository, responseDataType entity){
        try{
            return saveEntityThrows(repository, entity);
        }catch (GCSResponseException gcsResponseException){
            return errorResponse(gcsResponseException.getGcsError());
        }
    }

    // Save single entity + flush, throws exceptions
    public static <responseDataType extends GalEntity<ID>, ID> GCSResponse<responseDataType> saveEntityAndFlushThrows(GalRepository<responseDataType, ID> repository, responseDataType entity) throws GCSResponseException {
        try{
            repository.save(entity);
            repository.flush(); // Force immediate database sync
            return response(entity);
        }catch (Exception exception){
            log.info("An exception was thrown: " + exception.getMessage());
            log.error("Exception: ", exception);
            throw new GCSResponseException(errorResponse(GCSResponseErrorType.FAILED_TO_SAVE_ENTITY));
        }
    }

    // Save single entity + flush, no exceptions
    public static <responseDataType extends GalEntity<ID>, ID> GCSResponse<responseDataType> saveEntityAndFlush(GalRepository<responseDataType, ID> repository, responseDataType entity){
        try{
            return saveEntityAndFlushThrows(repository, entity);
        }catch (GCSResponseException gcsResponseException){
            return errorResponse(gcsResponseException.getGcsError());
        }
    }

    /*
    Save Multiple Entities
     */

    //// Collections

    // Save multiple entities (passed as collection), throws exception
    public static <responseDataType extends GalEntity<ID>, ID> GCSResponse<List<responseDataType>> saveEntitiesThrows(GalRepository<responseDataType, ID> repository, Collection<responseDataType> entities) throws GCSResponseException{
        try {
            return response(repository.saveAll(entities));
        }catch (Exception exception){
            log.info("An exception was thrown: " + exception.getMessage());
            log.error("Exception: ", exception);
            throw new GCSResponseException(errorResponse(GCSResponseErrorType.FAILED_TO_SAVE_ENTITY));
        }
    }
    // Save multiple entities (passed as collection), no exception
    public static <responseDataType extends GalEntity<ID>, ID> GCSResponse<List<responseDataType>> saveEntities(GalRepository<responseDataType, ID> repository, Collection<responseDataType> entities){
        try{
            return saveEntitiesThrows(repository, entities);
        }catch (GCSResponseException gcsResponseException){
            return errorResponse(gcsResponseException.getGcsError());
        }
    }

    // Save multiple entities + flush (passed as collection), throws exceptions
    public static <responseDataType extends GalEntity<ID>, ID> GCSResponse<List<responseDataType>> saveEntitiesAndFlushThrows(GalRepository<responseDataType, ID> repository, Collection<responseDataType> entities) throws GCSResponseException{
        try {
            repository.saveAll(entities);
            repository.flush(); // Force immediate database sync
            return response(new ArrayList<>(entities));
        }catch (Exception exception){
            log.info("An exception was thrown: " + exception.getMessage());
            log.error("Exception: ", exception);
            throw new GCSResponseException(errorResponse(GCSResponseErrorType.FAILED_TO_SAVE_ENTITY));
        }
    }

    // Save multiple entities + flush (passed as collection), no exceptions
    public static <responseDataType extends GalEntity<ID>, ID> GCSResponse<List<responseDataType>> saveEntitiesAndFlush(GalRepository<responseDataType, ID> repository, Collection<responseDataType> entities){
        try{
            return saveEntitiesAndFlushThrows(repository, entities);
        }catch (GCSResponseException gcsResponseException){
            return errorResponse(gcsResponseException.getGcsError());
        }
    }

    //// arrays

    // Save multiple entities (passed as array), throws exception
    public static <responseDataType extends GalEntity<ID>, ID> GCSResponse<List<responseDataType>> saveEntitiesThrows(GalRepository<responseDataType, ID> repository, responseDataType... entities) throws GCSResponseException{
        try{
            return saveEntitiesThrows(repository, Arrays.asList(entities));
        }catch (GCSResponseException gcsResponseException){
            return errorResponse(gcsResponseException.getGcsError());
        }
    }

    // Save multiple entities (passed as array), no exception
    public static <responseDataType extends GalEntity<ID>, ID> GCSResponse<List<responseDataType>> saveEntities(GalRepository<responseDataType, ID> repository, responseDataType... entities){
        try{
            return saveEntitiesThrows(repository, entities);
        }catch (GCSResponseException gcsResponseException){
            return errorResponse(gcsResponseException.getGcsError());
        }
    }

    // Save multiple entities + flush (passed as array), throws exception
    public static <responseDataType extends GalEntity<ID>, ID> GCSResponse<List<responseDataType>> saveEntitiesAndFlushThrows(GalRepository<responseDataType, ID> repository, responseDataType... entities) throws GCSResponseException{
        try{
            return saveEntitiesAndFlushThrows(repository, Arrays.asList(entities));
        }catch (GCSResponseException gcsResponseException){
            return errorResponse(gcsResponseException.getGcsError());
        }
    }

    // Save multiple entities + flush (passed as array), no exception
    public static <responseDataType extends GalEntity<ID>, ID> GCSResponse<List<responseDataType>> saveEntitiesAndFlush(GalRepository<responseDataType, ID> repository, responseDataType... entities){
        try{
            return saveEntitiesAndFlushThrows(repository, entities);
        }catch (GCSResponseException gcsResponseException){
            return errorResponse(gcsResponseException.getGcsError());
        }
    }

    /*
    Remove Entities
     */

    public static <responseDataType extends GalEntity<ID>, ID> GCSResponse<Boolean> removeEntityThrows(GalRepository<responseDataType, ID> repository, responseDataType entity) throws GCSResponseException{
        try {
            repository.delete(entity);
        }catch (Exception exception){
            log.info("An exception was thrown: " + exception.getMessage());
            log.error("Exception: ", exception);
            throw new GCSResponseException(errorResponse(GCSResponseErrorType.FAILED_TO_DELETE_ENTITY));
        }
        return response(true);
    }

    public static <responseDataType extends GalEntity<ID>, ID> GCSResponse<Boolean> removeEntity(GalRepository<responseDataType, ID> repository, responseDataType entity){
        try{
            return removeEntityThrows(repository, entity);
        }catch (GCSResponseException gcsResponseException){
            return errorResponse(gcsResponseException.getGcsError());
        }
    }

    public static <responseDataType extends GalEntity<ID>, ID> GCSResponse<Boolean> removeEntitiesThrows(GalRepository<responseDataType, ID> repository, List<responseDataType> entities) throws GCSResponseException{
        try {
            repository.deleteAll(entities);
        }catch (Exception exception){
            log.info("An exception was thrown: " + exception.getMessage());
            log.error("Exception: ", exception);
            throw new GCSResponseException(errorResponse(GCSResponseErrorType.FAILED_TO_DELETE_ENTITY));
        }
        return response(true);
    }

    public static <responseDataType extends GalEntity<ID>, ID> GCSResponse<Boolean> removeEntities(GalRepository<responseDataType, ID> repository, List<responseDataType> entities){
        try{
            return removeEntitiesThrows(repository, entities);
        }catch (GCSResponseException gcsResponseException){
            return errorResponse(gcsResponseException.getGcsError());
        }
    }
}
