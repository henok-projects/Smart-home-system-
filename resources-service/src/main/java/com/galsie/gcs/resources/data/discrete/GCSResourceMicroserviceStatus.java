package com.galsie.gcs.resources.data.discrete;

public enum GCSResourceMicroserviceStatus {

    STARTING_UP,
    RUNNING,
    INSTANCES_WAITING_FOR_MANAGER_TO_SYNC,
    MANAGER_WAITING_FOR_INSTANCES_TO_SYNC,
    SYNCHRONIZING_RESOURCES

}
