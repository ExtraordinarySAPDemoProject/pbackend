package org.latheild.training.service;

import org.latheild.training.api.dto.AttachmentOperationDTO;
import org.latheild.training.api.dto.TraingingDTO;

import java.util.ArrayList;

public interface TrainingService {

    boolean checkFileExistence(String fileId);

    /*TraingingDTO uploadFile(TraingingDTO traingingDTO);

    TraingingDTO renameFile(TraingingDTO traingingDTO);

    void deleteFileById(TraingingDTO traingingDTO);

    TraingingDTO getFileById(String id);

    ArrayList<TraingingDTO> getFilesByOwnerId(String ownerId);

    ArrayList<TraingingDTO> getFilesByProjectId(String projectId);

    ArrayList<TraingingDTO> getFilesByOwnerIdAndProjectId(String ownerId, String projectId);

    ArrayList<TraingingDTO> adminGetAllFiles(String code);

    void adminDeleteFileById(String id, String code);

    void adminDeleteFilesByOwnerId(String ownerId, String code);

    void adminDeleteFilesByProjectId(String projectId, String code);

    void adminDeleteFilesByOwnerIdAndProjectId(String ownerId, String projectId, String code);

    void adminDeleteAllFiles(String code);

    void attachFileToTask(AttachmentOperationDTO attachmentOperationDTO);

    void detachFileToTask(AttachmentOperationDTO attachmentOperationDTO);

    ArrayList<TraingingDTO> getAllFilesByTaskId(String taskId);*/

}
