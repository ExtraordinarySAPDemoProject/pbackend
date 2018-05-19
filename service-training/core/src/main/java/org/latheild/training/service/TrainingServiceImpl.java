package org.latheild.training.service;

import org.latheild.apiutils.api.CommonErrorCode;
import org.latheild.apiutils.exception.AppBusinessException;
import org.latheild.common.api.RabbitMQMessageCreator;
import org.latheild.common.constant.MessageType;
import org.latheild.common.domain.Message;
import org.latheild.training.api.constant.TrainingErrorCode;
import org.latheild.training.api.dto.AttachmentOperationDTO;
import org.latheild.training.api.dto.TraingingDTO;
import org.latheild.training.client.ProjectClient;
import org.latheild.training.client.RelationClient;
import org.latheild.training.client.TaskClient;
import org.latheild.training.client.UserClient;
import org.latheild.training.constant.DAOQueryMode;
import org.latheild.training.dao.TrainingRepository;
import org.latheild.training.domain.Training;
import org.latheild.project.api.constant.ProjectErrorCode;
import org.latheild.relation.api.dto.RelationDTO;
import org.latheild.relation.api.utils.RelationDTOAnalyzer;
import org.latheild.task.api.constant.TaskErrorCode;
import org.latheild.user.api.constant.UserErrorCode;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static org.latheild.apiutils.constant.Constants.ADMIN_CODE;
import static org.latheild.apiutils.constant.Constants.ADMIN_DELETE_ALL;
import static org.latheild.common.constant.RabbitMQExchange.FILE_FAN_OUT_EXCHANGE;
import static org.latheild.common.constant.RabbitMQQueue.FILE_QUEUE;

@Service
@RabbitListener(queues = FILE_QUEUE)
public class TrainingServiceImpl implements TrainingService {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    RelationClient relationClient;

    @Autowired
    UserClient userClient;

    @Autowired
    ProjectClient projectClient;

    @Autowired
    TaskClient taskClient;

    @Autowired
    TrainingRepository trainingRepository;

    private boolean isFileExist(DAOQueryMode mode, String target) {
        switch (mode) {
            case QUERY_BY_ID:
                return (trainingRepository.countById(target) > 0);
            case QUERY_BY_AUTHOR:
                return (trainingRepository.countByAuthor(target) > 0);
            default:
                throw new AppBusinessException(
                        CommonErrorCode.INTERNAL_ERROR
                );
        }
    }

    public TraingingDTO convertFromFileToFileDTO(Training training) {
        TraingingDTO traingingDTO = new TraingingDTO();
        traingingDTO.setAuthor(training.getAuthor());
        traingingDTO.setDescription(training.getDescription());
        traingingDTO.setTitle(training.getTitle());
        traingingDTO.setTime(training.getTime());
        traingingDTO.setTrainingId(training.getId());
        traingingDTO.setComments(training.getComments());
        traingingDTO.setUsers(training.getUsers());
        return traingingDTO;
    }

    public ArrayList<TraingingDTO> convertFromFilesToFileDTOs(ArrayList<Training> trainings) {
        ArrayList<TraingingDTO> traingingDTOS = new ArrayList<>();
        for (Training training : trainings) {
            traingingDTOS.add(convertFromFileToFileDTO(training));
        }
        return traingingDTOS;
    }

    public Training convertFromFileDTOToFile(TraingingDTO traingingDTO) {
        Training training = new Training();
        training.setAuthor(traingingDTO.getAuthor());
        training.setDescription(traingingDTO.getDescription());
        training.setTime(traingingDTO.getTime());
        training.setTitle(traingingDTO.getTitle());
        training.setComments(traingingDTO.getComments());
        training.setUsers(traingingDTO.getUsers());
        return training;
    }

    /*@RabbitHandler
    public void eventHandler(Message message) {
        String messageBody;
        switch (message.getMessageType()) {
            case USER_DELETED:
                messageBody = (String) message.getMessageBody();
                if (messageBody.equals(ADMIN_DELETE_ALL)) {
                    if (trainingRepository.count() > 0) {
                        adminDeleteAllFiles(ADMIN_CODE);
                    }
                } else {
                    if (isFileExist(DAOQueryMode.QUERY_BY_AUTHOR, messageBody)) {
                        trainingRepository.deleteAllByAuthor(messageBody);
                    }
                }
                break;
            default:
                System.out.println(message.toString());
        }
    }*/

    @Override
    public boolean checkFileExistence(String fileId) {
        return isFileExist(DAOQueryMode.QUERY_BY_ID, fileId);
    }

    /*@Override
    public TraingingDTO uploadFile(TraingingDTO traingingDTO) {
        if (userClient.checkUserExistence(traingingDTO.getAuthor())) {
            if (projectClient.checkProjectExistence(traingingDTO.getTrainingId())) {
                Training training = convertFromFileDTOToFile(traingingDTO);
                trainingRepository.save(training);
                return convertFromFileToFileDTO(training);
            } else {
                throw new AppBusinessException(
                        ProjectErrorCode.PROJECT_NOT_EXIST,
                        String.format("Project %s does not exist", traingingDTO.getProjectId())
                );
            }
        } else {
            throw new AppBusinessException(
                    UserErrorCode.USER_NOT_EXIST,
                    String.format("User %s does not exist", traingingDTO.getOwnerId())
            );
        }
    }

    @Override
    public TraingingDTO renameFile(TraingingDTO traingingDTO) {
        if (isFileExist(DAOQueryMode.QUERY_BY_ID, traingingDTO.getFileId())) {
            Training training = trainingRepository.findById(traingingDTO.getFileId());
            if (training.getOwnerId().equals(traingingDTO.getOwnerId())) {
                training.setName(traingingDTO.getName());
                trainingRepository.save(training);
                return convertFromFileToFileDTO(training);
            } else {
                throw new AppBusinessException(
                        CommonErrorCode.UNAUTHORIZED
                );
            }
        } else {
            throw new AppBusinessException(
                    TrainingErrorCode.FILE_NOT_EXIST,
                    String.format("Training %s does not exist", traingingDTO.getFileId())
            );
        }
    }

    @Override
    public void deleteFileById(TraingingDTO traingingDTO) {
        if (isFileExist(DAOQueryMode.QUERY_BY_ID, traingingDTO.getFileId())) {
            Training training = trainingRepository.findById(traingingDTO.getFileId());
            if (training.getOwnerId().equals(traingingDTO.getOwnerId())) {
                trainingRepository.deleteById(training.getId());

                rabbitTemplate.convertAndSend(
                        FILE_FAN_OUT_EXCHANGE,
                        "",
                        RabbitMQMessageCreator.newInstance(
                                MessageType.FILE_DELETED,
                                training.getId()
                        )
                );
            } else {
                throw new AppBusinessException(
                        CommonErrorCode.UNAUTHORIZED
                );
            }
        } else {
            throw new AppBusinessException(
                    TrainingErrorCode.FILE_NOT_EXIST,
                    String.format("Training %s does not exist", traingingDTO.getFileId())
            );
        }
    }

    @Override
    public TraingingDTO getFileById(String id) {
        if (isFileExist(DAOQueryMode.QUERY_BY_ID, id)) {
            return convertFromFileToFileDTO(trainingRepository.findById(id));
        } else {
            throw new AppBusinessException(
                    TrainingErrorCode.FILE_NOT_EXIST,
                    String.format("Training %s does not exist", id)
            );
        }
    }

    @Override
    public ArrayList<TraingingDTO> getFilesByOwnerId(String ownerId) {
        if (isFileExist(DAOQueryMode.QUERY_BY_OWNER_ID, ownerId)) {
            return convertFromFilesToFileDTOs(trainingRepository.findAllByOwnerId(ownerId));
        } else {
            throw new AppBusinessException(
                    TrainingErrorCode.FILE_NOT_EXIST,
                    String.format("User %s does not have any file", ownerId)
            );
        }
    }

    @Override
    public ArrayList<TraingingDTO> getFilesByProjectId(String projectId) {
        if (isFileExist(DAOQueryMode.QUERY_BY_PROJECT_ID, projectId)) {
            return convertFromFilesToFileDTOs(trainingRepository.findAllByProjectId(projectId));
        } else {
            throw new AppBusinessException(
                    TrainingErrorCode.FILE_NOT_EXIST,
                    String.format("Project %s does not have any file", projectId)
            );
        }
    }

    @Override
    public ArrayList<TraingingDTO> getFilesByOwnerIdAndProjectId(String ownerId, String projectId) {
        if (isFileExist(DAOQueryMode.QUERY_BY_OWNER_ID, ownerId)) {
            if (isFileExist(DAOQueryMode.QUERY_BY_PROJECT_ID, projectId)) {
                return convertFromFilesToFileDTOs(trainingRepository.findAllByOwnerIdAndProjectId(ownerId, projectId));
            } else {
                throw new AppBusinessException(
                        TrainingErrorCode.FILE_NOT_EXIST,
                        String.format("Project %s does not have any file assigned to user %s", projectId, ownerId)
                );
            }
        } else {
            throw new AppBusinessException(
                    TrainingErrorCode.FILE_NOT_EXIST,
                    String.format("User %s does not have any file", ownerId)
            );
        }
    }

    @Override
    public ArrayList<TraingingDTO> adminGetAllFiles(String code) {
        if (code.equals(ADMIN_CODE)) {
            if (trainingRepository.count() > 0) {
                return convertFromFilesToFileDTOs(trainingRepository.findAll());
            } else {
                throw new AppBusinessException(
                        TrainingErrorCode.FILE_NOT_EXIST
                );
            }
        } else {
            throw new AppBusinessException(
                    CommonErrorCode.UNAUTHORIZED
            );
        }
    }

    @Override
    public void adminDeleteFileById(String id, String code) {
        if (code.equals(ADMIN_CODE)) {
            if (isFileExist(DAOQueryMode.QUERY_BY_ID, id)) {
                trainingRepository.deleteById(id);

                rabbitTemplate.convertAndSend(
                        FILE_FAN_OUT_EXCHANGE,
                        "",
                        RabbitMQMessageCreator.newInstance(
                                MessageType.FILE_DELETED,
                                id
                        )
                );
            } else {
                throw new AppBusinessException(
                        TrainingErrorCode.FILE_NOT_EXIST,
                        String.format("Training %s does not exist", id)
                );
            }
        } else {
            throw new AppBusinessException(
                    CommonErrorCode.UNAUTHORIZED
            );
        }
    }

    @Override
    public void adminDeleteFilesByOwnerId(String ownerId, String code) {
        if (code.equals(ADMIN_CODE)) {
            if (isFileExist(DAOQueryMode.QUERY_BY_OWNER_ID, ownerId)) {
                ArrayList<Training> trainings = trainingRepository.findAllByOwnerId(ownerId);
                trainingRepository.deleteAllByOwnerId(ownerId);

                for (Training training : trainings) {
                    rabbitTemplate.convertAndSend(
                            FILE_FAN_OUT_EXCHANGE,
                            "",
                            RabbitMQMessageCreator.newInstance(
                                    MessageType.FILE_DELETED,
                                    training.getId()
                            )
                    );
                }
            } else {
                throw new AppBusinessException(
                        TrainingErrorCode.FILE_NOT_EXIST,
                        String.format("User %s does not have any file", ownerId)
                );
            }
        } else {
            throw new AppBusinessException(
                    CommonErrorCode.UNAUTHORIZED
            );
        }
    }

    @Override
    public void adminDeleteFilesByProjectId(String projectId, String code) {
        if (code.equals(ADMIN_CODE)) {
            if (isFileExist(DAOQueryMode.QUERY_BY_PROJECT_ID, projectId)) {
                ArrayList<Training> trainings = trainingRepository.findAllByProjectId(projectId);
                trainingRepository.deleteAllByProjectId(projectId);

                for (Training training : trainings) {
                    rabbitTemplate.convertAndSend(
                            FILE_FAN_OUT_EXCHANGE,
                            "",
                            RabbitMQMessageCreator.newInstance(
                                    MessageType.FILE_DELETED,
                                    training.getId()
                            )
                    );
                }
            } else {
                throw new AppBusinessException(
                        TrainingErrorCode.FILE_NOT_EXIST,
                        String.format("Project %s does not have any file", projectId)
                );
            }
        } else {
            throw new AppBusinessException(
                    CommonErrorCode.UNAUTHORIZED
            );
        }
    }

    @Override
    public void adminDeleteFilesByOwnerIdAndProjectId(String ownerId, String projectId, String code) {
        if (code.equals(ADMIN_CODE)) {
            if (isFileExist(DAOQueryMode.QUERY_BY_OWNER_ID, ownerId)) {
                if (isFileExist(DAOQueryMode.QUERY_BY_PROJECT_ID, projectId)) {
                    ArrayList<Training> trainings = trainingRepository.findAllByOwnerIdAndProjectId(ownerId, projectId);
                    trainingRepository.deleteAllByOwnerIdAndProjectId(ownerId, projectId);

                    for (Training training : trainings) {
                        rabbitTemplate.convertAndSend(
                                FILE_FAN_OUT_EXCHANGE,
                                "",
                                RabbitMQMessageCreator.newInstance(
                                        MessageType.FILE_DELETED,
                                        training.getId()
                                )
                        );
                    }
                } else {
                    throw new AppBusinessException(
                            TrainingErrorCode.FILE_NOT_EXIST,
                            String.format("Project %s does not have any file assigned to user %s", projectId, ownerId)
                    );
                }
            } else {
                throw new AppBusinessException(
                        TrainingErrorCode.FILE_NOT_EXIST,
                        String.format("User %s does not have any file", ownerId)
                );
            }
        } else {
            throw new AppBusinessException(
                    CommonErrorCode.UNAUTHORIZED
            );
        }
    }

    @Override
    public void adminDeleteAllFiles(String code) {
        if (code.equals(ADMIN_CODE)) {
            if (trainingRepository.count() > 0) {
                trainingRepository.deleteAll();

                rabbitTemplate.convertAndSend(
                        FILE_FAN_OUT_EXCHANGE,
                        "",
                        RabbitMQMessageCreator.newInstance(
                                MessageType.FILE_DELETED,
                                ADMIN_DELETE_ALL
                        )
                );
            } else {
                throw new AppBusinessException(
                        TrainingErrorCode.FILE_NOT_EXIST
                );
            }
        } else {
            throw new AppBusinessException(
                    CommonErrorCode.UNAUTHORIZED
            );
        }
    }

    @Override
    public void attachFileToTask(AttachmentOperationDTO attachmentOperationDTO) {
        if (userClient.checkUserExistence(attachmentOperationDTO.getExecutorId())) {
            if (taskClient.checkTaskExistence(attachmentOperationDTO.getTaskId())) {
                if (isFileExist(DAOQueryMode.QUERY_BY_ID, attachmentOperationDTO.getFileId())) {
                    Training training = trainingRepository.findById(attachmentOperationDTO.getFileId());
                    if (training.getOwnerId().equals(attachmentOperationDTO.getExecutorId())) {
                        relationClient.addTaskAttachment(
                                attachmentOperationDTO.getFileId(),
                                attachmentOperationDTO.getTaskId()
                        );
                    } else {
                        throw new AppBusinessException(
                                CommonErrorCode.UNAUTHORIZED
                        );
                    }
                } else {
                    throw new AppBusinessException(
                            TrainingErrorCode.FILE_NOT_EXIST,
                            String.format("Training %s does not exist", attachmentOperationDTO.getFileId())
                    );
                }
            } else {
                throw new AppBusinessException(
                        TaskErrorCode.TASK_NOT_EXIST,
                        String.format("Task %s does not exist", attachmentOperationDTO.getTaskId())
                );
            }
        } else {
            throw new AppBusinessException(
                    UserErrorCode.USER_NOT_EXIST,
                    String.format("User %s does not exist", attachmentOperationDTO.getExecutorId())
            );
        }
    }

    @Override
    public void detachFileToTask(AttachmentOperationDTO attachmentOperationDTO) {
        if (userClient.checkUserExistence(attachmentOperationDTO.getExecutorId())) {
            if (taskClient.checkTaskExistence(attachmentOperationDTO.getTaskId())) {
                if (isFileExist(DAOQueryMode.QUERY_BY_ID, attachmentOperationDTO.getFileId())) {
                    Training training = trainingRepository.findById(attachmentOperationDTO.getFileId());
                    if (training.getOwnerId().equals(attachmentOperationDTO.getExecutorId())) {
                        relationClient.deleteTaskAttachment(
                                attachmentOperationDTO.getFileId(),
                                attachmentOperationDTO.getTaskId()
                        );
                    } else {
                        throw new AppBusinessException(
                                CommonErrorCode.UNAUTHORIZED
                        );
                    }
                } else {
                    throw new AppBusinessException(
                            TrainingErrorCode.FILE_NOT_EXIST,
                            String.format("Training %s does not exist", attachmentOperationDTO.getFileId())
                    );
                }
            } else {
                throw new AppBusinessException(
                        TaskErrorCode.TASK_NOT_EXIST,
                        String.format("Task %s does not exist", attachmentOperationDTO.getTaskId())
                );
            }
        } else {
            throw new AppBusinessException(
                    UserErrorCode.USER_NOT_EXIST,
                    String.format("User %s does not exist", attachmentOperationDTO.getExecutorId())
            );
        }
    }

    @Override
    public ArrayList<TraingingDTO> getAllFilesByTaskId(String taskId) {
        try {
            ArrayList<RelationDTO> relationDTOs = relationClient.getTaskAttachments(taskId);
            ArrayList<TraingingDTO> traingingDTOS = new ArrayList<>();
            TraingingDTO traingingDTO;
            for (RelationDTO relationDTO : relationDTOs) {
                traingingDTO = getFileById(RelationDTOAnalyzer.getFileId(relationDTO));
                traingingDTOS.add(traingingDTO);
            }
            return traingingDTOS;
        } catch (Exception e) {
            throw new AppBusinessException(
                    CommonErrorCode.INTERNAL_ERROR,
                    e.getMessage()
            );
        }
    }*/

}
