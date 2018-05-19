package org.latheild.training.controller;

import org.latheild.apiutils.api.BaseResponseBody;
import org.latheild.apiutils.api.CommonErrorCode;
import org.latheild.apiutils.api.ExceptionResponseBody;
import org.latheild.apiutils.exception.AppBusinessException;
import org.latheild.training.api.dto.TraingingDTO;
import org.latheild.training.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.latheild.apiutils.constant.URLRequestSetting.PRODUCE_JSON;
import static org.latheild.training.api.constant.TrainingURL.*;

@RestController
public class TrainingController {

    @Autowired
    TrainingService trainingService;

    @RequestMapping(value = CHECK_FILE_EXIST_URL, method = RequestMethod.GET, produces =  PRODUCE_JSON)
    public boolean checkFileExistence(
            @RequestParam(value = "fileId") String fileId
    ) {
        return trainingService.checkFileExistence(fileId);
    }

    /*@RequestMapping(value = UPLOAD_FILE_URL, method = RequestMethod.POST, produces = PRODUCE_JSON)
    @ResponseBody
    public Object uploadFile(
            @RequestBody TraingingDTO traingingDTO
    ) {
        try {
            return new BaseResponseBody(CommonErrorCode.SUCCESS, trainingService.uploadFile(traingingDTO));
        } catch (AppBusinessException e) {
            return new ExceptionResponseBody(e.getHttpStatus(), e.getCode(), e.getExceptionType(), e.getMessage());
        }
    }

    @RequestMapping(value = RENAME_FILE_URL, method = RequestMethod.POST, produces = PRODUCE_JSON)
    @ResponseBody
    public Object renameFile(
            @RequestBody TraingingDTO traingingDTO
    ) {
        try {
            return new BaseResponseBody(CommonErrorCode.SUCCESS, trainingService.renameFile(traingingDTO));
        } catch (AppBusinessException e) {
            return new ExceptionResponseBody(e.getHttpStatus(), e.getCode(), e.getExceptionType(), e.getMessage());
        }
    }

    @RequestMapping(value = DELETE_FILE_BY_ID_URL, method = RequestMethod.POST, produces = PRODUCE_JSON)
    @ResponseBody
    public Object deleteFileById(
            @RequestBody TraingingDTO traingingDTO
    ) {
        try {
            trainingService.deleteFileById(traingingDTO);
            return new BaseResponseBody(CommonErrorCode.SUCCESS);
        } catch (AppBusinessException e) {
            return new ExceptionResponseBody(e.getHttpStatus(), e.getCode(), e.getExceptionType(), e.getMessage());
        }
    }

    @RequestMapping(value = GET_FILE_BY_ID_URL, method = RequestMethod.GET, produces = PRODUCE_JSON)
    @ResponseBody
    public Object getFileById(
            @RequestParam(value = "id") String id
    ) {
        try {
            return new BaseResponseBody(CommonErrorCode.SUCCESS, trainingService.getFileById(id));
        } catch (AppBusinessException e) {
            return new ExceptionResponseBody(e.getHttpStatus(), e.getCode(), e.getExceptionType(), e.getMessage());
        }
    }

    @RequestMapping(value = GET_FILES_URL, method = RequestMethod.GET, produces = PRODUCE_JSON)
    @ResponseBody
    public Object getFiles(
            @RequestParam(value = "ownerId", required = false) String ownerId,
            @RequestParam(value = "projectId", required = false) String projectId
    ) {
        try {
            if (ownerId != null && projectId != null) {
                return new BaseResponseBody(CommonErrorCode.SUCCESS, trainingService.getFilesByOwnerIdAndProjectId(ownerId, projectId));
            } else if (ownerId != null) {
                return new BaseResponseBody(CommonErrorCode.SUCCESS, trainingService.getFilesByOwnerId(ownerId));
            } else if (projectId != null) {
                return new BaseResponseBody(CommonErrorCode.SUCCESS, trainingService.getFilesByProjectId(projectId));
            } else {
                throw new AppBusinessException(
                        CommonErrorCode.INVALID_ARGUMENT
                );
            }
        } catch (AppBusinessException e) {
            return new ExceptionResponseBody(e.getHttpStatus(), e.getCode(), e.getExceptionType(), e.getMessage());
        }
    }

    @RequestMapping(value = ADMIN_GET_ALL_FILES_URL, method = RequestMethod.GET, produces = PRODUCE_JSON)
    @ResponseBody
    public Object adminGetAllFiles(
            @RequestParam(value = "code") String code
    ) {
        try {
            return new BaseResponseBody(CommonErrorCode.SUCCESS, trainingService.adminGetAllFiles(code));
        } catch (AppBusinessException e) {
            return new ExceptionResponseBody(e.getHttpStatus(), e.getCode(), e.getExceptionType(), e.getMessage());
        }
    }

    @RequestMapping(value = ADMIN_DELETE_FILE_BY_ID_URL, method = RequestMethod.GET, produces = PRODUCE_JSON)
    @ResponseBody
    public Object adminDeleteFileById(
            @RequestParam(value = "id") String id,
            @RequestParam(value = "code") String code
    ) {
        try {
            trainingService.adminDeleteFileById(id, code);
            return new BaseResponseBody(CommonErrorCode.SUCCESS);
        } catch (AppBusinessException e) {
            return new ExceptionResponseBody(e.getHttpStatus(), e.getCode(), e.getExceptionType(), e.getMessage());
        }
    }

    @RequestMapping(value = ADMIN_DELETE_FILES_URL, method = RequestMethod.GET, produces = PRODUCE_JSON)
    @ResponseBody
    public Object adminDeleteFiles(
            @RequestParam(value = "ownerId", required = false) String ownerId,
            @RequestParam(value = "projectId", required = false) String projectId,
            @RequestParam(value = "code") String code
    ) {
        try {
            if (ownerId != null && projectId != null) {
                trainingService.adminDeleteFilesByOwnerIdAndProjectId(ownerId, projectId, code);
            } else if (ownerId != null) {
                trainingService.adminDeleteFilesByOwnerId(ownerId, code);
            } else if (projectId != null) {
                trainingService.adminDeleteFilesByProjectId(projectId, code);
            } else {
                trainingService.adminDeleteAllFiles(code);
            }
            return new BaseResponseBody(CommonErrorCode.SUCCESS);
        } catch (AppBusinessException e) {
            return new ExceptionResponseBody(e.getHttpStatus(), e.getCode(), e.getExceptionType(), e.getMessage());
        }
    }*/

}
