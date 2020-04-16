package com.storyart.storyservice.service;

import com.storyart.storyservice.common.constants.*;
import com.storyart.storyservice.dto.GetStoryDto;
import com.storyart.storyservice.dto.TagDto;
import com.storyart.storyservice.dto.create_story.*;
import com.storyart.storyservice.dto.ResultDto;
import com.storyart.storyservice.dto.read_story.ReadStoryDto;
import com.storyart.storyservice.dto.read_story.ReadStoryInformationDto;
import com.storyart.storyservice.dto.read_story.ReadStoryScreenDto;
import com.storyart.storyservice.dto.read_story.ReadStoryTagDto;
import com.storyart.storyservice.dto.statistic.*;
import com.storyart.storyservice.dto.statistics.ReadStatisticDto;
import com.storyart.storyservice.model.*;
import com.storyart.storyservice.repository.*;
import com.storyart.storyservice.utils.MyStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface StoryService {
    HashMap<String, String> validateStoryinfo(CreateStoryDto story);

    GetStoryDto getStoryDetails(int id);

    List<GetStoryDto> getStoriesByUserId(int userId);

    ResultDto getReadingStory(int storyId);

    ResultDto createStory(CreateStoryDto story, int userId);

    ResultDto updateStory(CreateStoryDto story, int userId);

    Page<GetStoryDto> searchStories(Set<Integer> tags, String keyword, boolean isActive,
                                    boolean isPublished, int page, int itemsPerPage);

    Page<GetStoryDto> searchStoriesOfUserProfile(int userid, Set<Integer> tags, String keyword, int page, int itemsPerPage);

    List<GetStoryDto> getTheMostReadingStories();

    Page<GetStoryDto> getStoriesForAdmin(String keyword, String orderBy, boolean asc, int page, int itemsPerPage);

    Page<GetStoryDto> getStoriesForUser(int userId, String keyword, String orderBy, boolean asc, int page, int itemsPerPage);

    ResultDto updateByAdmin(int storyId, boolean disable);

    ResultDto deleteStory(int storyId, int userId);

    ResultDto changePublishedStatus(int storyId, int userId, boolean turnOnPublished);

    ResultDto updateStoryImage(int storyId, int userId, String imageUrl);

    ResultDto increaseStoryRead(int storyId);

    ResultDto saveReadHistory(int storyId, int userId);

    ResultDto rateStory(int storyId, int userId, double stars);

    ResultDto getReadStatisticsByDateRangeOfUser(Date from, Date to, int userId);

    Rating getRatingByStoryAndUser(int storyId, int userId);

}

@Service
class StoryServiceImpl implements StoryService {
    @Autowired
    ScreenRepository screenRepository;

    @Autowired
    StoryRepository storyRepository;

    @Autowired
    ActionRepository actionRepository;

    @Autowired
    InformationRepository informationRepository;

    @Autowired
    InfoConditionRepository infoConditionRepository;

    @Autowired
    InformationActionRepository informationActionRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    RatingRepository ratingRepository;

    @Autowired
    TagService tagService;

    @Autowired
    StoryTagRepository storyTagRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    HistoryRepository historyRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    EntityManager entityManager;

    public boolean isNumber(String value){
        try{
            double val = Double.parseDouble(value);
            return true;
        } catch(Exception ex){
            return false;
        }
    }

    public boolean isNumberCondition(String value){
        List<String> number_conditions = ConstantsList.getNumberConditionList();
        return number_conditions.contains(value);
    }

    public boolean isStringCondition(String value){
        List<String> string_conditions = ConstantsList.getStringConditionList();
        return string_conditions.contains(value);
    }

    public boolean isNumOperation(String value){
        List<String> number_ops = ConstantsList.getNumberOperationList();
        return number_ops.contains(value);
    }

    public boolean isStringOperation(String value){
        List<String> string_ops = ConstantsList.getStringOperationList();
        return string_ops.contains(value);
    }

    @Override
    public HashMap<String, String> validateStoryinfo(CreateStoryDto storyDto) {
        HashMap<String, String> errors = new HashMap<>();
        if(storyDto.getTags().size() == 0){
            errors.put("TAGS", "Chưa gắn thẻ cho truyện");
        } else if(storyDto.getScreens().size() == 0){
            errors.put("SCREENS", "Chưa có màn hình cho truyện");
        } else {
            CreateStoryInformationDto informationDto = storyDto.getInformations().size() > 0 ? storyDto.getInformations().get(0) : null;
            List<String> screenIds = storyDto.getScreens().stream().map(s -> s.getId()).collect(Collectors.toList());

            //check story information
            if(storyDto.getInformations().size() > 1){
                errors.put("INFORMATION", "Chỉ được thêm 1 thông tin cho truyện");
                return errors;
            } else if(storyDto.getInformations().size() == 1){
                String type = informationDto.getType();

                if(type.equals(INFORMATION_TYPES.NUMBER.toString())){
                   if(!isNumber(informationDto.getValue())){
                       errors.put("INFORMATION", "Giá trị thông tin truyện không phải là số");
                       return errors;
                   }
                }

                //check information conditions list
                for(CreateStoryConditionDto cond: informationDto.getConditions()){
                    if(!screenIds.contains(cond.getNextScreenId())){
                        errors.put("CONDITION_NEXT_SCREEN_ID", "Chưa có chuyển màn hình cho điều kiện thông tin");
                    } else if(type.equals(INFORMATION_TYPES.NUMBER.toString())){
                        if(!isNumber(cond.getValue())){
                            errors.put("INFORMATION_CONDITION", "Giá trị điều kiện thông tin truyện không phải là số");
                        } else if(!isNumberCondition(cond.getType())){
                            errors.put("INFORMATION_CONDITION", "Điều kiện thông tin truyện không tồn tại");
                        }
                    } else if(type.equals(INFORMATION_TYPES.STRING.toString())){
                        if(!isStringCondition(cond.getType())){
                            errors.put("INFORMATION_CONDITION", "Điều kiện thông tin truyện không tồn tại");
                        }
                    }
                    if(errors.size() > 0) return errors;
                }

                //check all information actions
                for(CreateStoryInformationActionDto informationActionDto: storyDto.getInformationActions()){
                    String value = informationActionDto.getValue();
                    String operation = informationActionDto.getOperation();
                    if(type.equals(INFORMATION_TYPES.NUMBER.toString())){
                        if(!isNumber(value)){
                            errors.put("INFORMATION_ACTION", "Giá trị ảnh hưởng thông tin không phải là số");
                        } else if(!isNumOperation(operation)){
                            errors.put("INFORMATION_ACTION", "Ảnh hưởng thông tin không tồn tại");
                        }
                    } else if(type.equals(INFORMATION_TYPES.STRING.toString())){
                        if(!isStringOperation(operation)){
                            errors.put("INFORMATION_ACTION", "Ảnh hưởng thông tin không tồn tại");
                        }
                    }
                    if(errors.size() > 0) return errors;
                }
            }


            //check first screen exist
            if(!screenIds.contains(storyDto.getFirstScreenId())){
                errors.put("FIRST_SCREEN_ID", "Chưa có màn hình đầu tiên cho truyện");
                return errors;
            }

            //check all screens
            for(CreateStoryScreenDto screen: storyDto.getScreens()){
                screen.getActions().stream().forEach(a -> {
                    if(a.getType().equals(ACTION_TYPES.NEXT_SCREEN.toString())){
                        if(!screenIds.contains(a.getValue())){
                            errors.put("NEXT_SCREEN_ACTION", "Chưa có màn hình kế tiếp cho hành động chuyển màn hình");
                        }
                    } else if(a.getType().equals(ACTION_TYPES.UPDATE_INFORMATION.toString())){
                        if(!screenIds.contains(a.getNextScreenId())){
                            errors.put("UPDATE_INFORMATION_ACTION", "Chưa có màn hình kế tiếp cho hành động cập nhật thông tin");
                        } else if(storyDto.getInformations().size() == 0){
                            errors.put("UPDATE_INFORMATION_ACTION", "Truyện chưa có thông tin!");
                        }
                    } else if(a.getType().equals(ACTION_TYPES.REDIRECT.toString())){
                        if(StringUtils.isEmpty(a.getValue())){
                            errors.put("REDIRECT_ACTION", "Chưa có đường dẫn cho hành động đi tới đường dẫn");
                        }
                    }
                });
                if(errors.size() > 0) break;
            }
        }
        return errors;
    }

    @Override
    public Rating getRatingByStoryAndUser(int storyId, int userId) {
        Rating rating = ratingRepository.findById(storyId, userId);
        return rating;
    }

    @Override
    public GetStoryDto getStoryDetails(int storyId) {
        Story story = storyRepository.findById(storyId).orElse(null);
        if (story == null) return null;

        GetStoryDto dto = modelMapper.map(story, GetStoryDto.class);

        List<Tag> tags = tagRepository.findAllByStoryId(story.getId());
        dto.setTags(tagService.mapModelToDto(tags));
        User user = userRepository.findById(story.getUserId()).orElse(null);
        if (user != null) user.setPassword(null);
        dto.setUser(user);

        //get user rating
        Rating rating = ratingRepository.findById(storyId, story.getUserId());
        dto.setRating(rating);

        dto.setNumOfRate(ratingRepository.countRatingByStoryId(story.getId()));

        dto.setNumOfRead(historyRepository.countAllByStoryId(storyId));
        return dto;
    }

    @Override
    public ResultDto changePublishedStatus(int storyId, int userId, boolean turnOnPublished) {
        ResultDto result = new ResultDto();
        Optional<Story> story = storyRepository.findById(storyId);
        result.setSuccess(false);
        if (!story.isPresent()) {
            result.getErrors().put("NOT_FOUND", "Không tìm thấy truyện này");
        } else {
            Story s = story.get();

            if (s.isDeactiveByAdmin()) {
                result.getErrors().put("NOT_FOUND", "Truyện này đã bị xóa bởi admin");
            } else if (!s.isActive()) {
                result.getErrors().put("NOT_FOUND", "Truyện này đã bị xóa");
            } else if (userId != s.getUserId()) {
                result.getErrors().put("NOT_OWN", "Truyện này không thuộc về bạn");
            } else if (turnOnPublished && !s.isPublished()) {
                s.setPublished(true);
                result.setSuccess(true);
                result.setData(s);
                storyRepository.save(s);
            } else if (!turnOnPublished && s.isPublished()) {
                s.setPublished(false);
                result.setSuccess(true);
                result.setData(s);
                storyRepository.save(s);
            } else {
                result.setSuccess(true);
            }
        }
        return result;
    }

    @Override
    public ResultDto updateStoryImage(int storyId, int userId, String imageUrl) {
        ResultDto result = new ResultDto();
        Story story = storyRepository.findById(storyId).orElse(null);
        result.setSuccess(false);
        if(story == null){
            result.getErrors().put("NOT_FOUND", "Không tìm thấy truyện này");
        } else if(!story.isActive() || story.isDeactiveByAdmin()){
            result.getErrors().put("NOT_FOUND", "Truyện này đã bị xóa");
        } else if(story.getUserId() != userId){
            result.getErrors().put("NOT_OWN", "Truyện này không thuộc về bạn");
        } else {
            story.setImage(imageUrl);
            story = storyRepository.save(story);
            result.setSuccess(true);
            result.setData(story);
        }
        return result;
    }

    @Override
    public ResultDto increaseStoryRead(int storyId) {
        ResultDto result = new ResultDto();
        result.setSuccess(true);
        Story story = storyRepository.findById(storyId).orElse(null);
        if (story != null) {
            storyRepository.save(story);
        } else {
            result.setSuccess(false);
            result.getErrors().put("NOT_FOUND", "Không tìm thấy truyện này");
        }
        return result;
    }

    @Override
    public ResultDto saveReadHistory(int storyId, int userId) {
        ResultDto result = new ResultDto();
        result.setSuccess(false);
        Story story = storyRepository.findById(storyId).orElse(null);
        if (story == null) {
            result.getErrors().put("NOT_FOUND", "Truyện này không tồn tại");
        } else if (!story.isActive() || story.isDeactiveByAdmin()) {
            result.getErrors().put("NOT_FOUND", "Truyện này đã bị xóa");
        } else {

        }
        return result;
    }

    @Override
    public ResultDto rateStory(int storyId, int userId, double stars) {
        ResultDto result = new ResultDto();
        result.setSuccess(false);
        Story story = storyRepository.findById(storyId).orElse(null);
        if (story == null) {
            result.getErrors().put("NOT_FOUND", "Truyện này không tồn tại");
        } else if (!story.isActive() || story.isDeactiveByAdmin()) {
            result.getErrors().put("NOT_FOUND", "Truyện này đã bị xóa");
        } else {
            Rating rating = ratingRepository.findById(storyId, userId);
            if (rating == null) {
                rating = new Rating();
                rating.setStoryId(storyId);
                rating.setUserId(userId);
                rating.setUpdatedAt(new Date());
            }
            rating.setStars(stars);
            ratingRepository.save(rating);

            double avgStars = ratingRepository.findAvgStarsByStoryId(storyId);
            story.setAvgRate(avgStars);
            storyRepository.save(story);
            result.setData(rating);
            result.setSuccess(true);
        }
        return result;
    }

    @Override
    public ResultDto getReadStatisticsByDateRangeOfUser(Date from, Date to, int userId) {
        ResultDto result = new ResultDto();

        List<ReadStatisticDto> data = historyRepository.findReadingStatisticsByDateRangeOfUser(from, to, userId);
        result.setData(data);
        result.setSuccess(true);
        return result;
    }

    @Override
    public ResultDto deleteStory(int storyId, int userId) {
        ResultDto result = new ResultDto();
        Optional<Story> story = storyRepository.findById(storyId);
        result.setSuccess(false);
        if (!story.isPresent()) {
            result.getErrors().put("NOT_FOUND", "Không tìm thấy truyện này");
        } else {
            Story s = story.get();
            if (!s.isActive()) {
                result.getErrors().put("NOT_FOUND", "Truyện này đã bị xóa");
            } else if (s.isDeactiveByAdmin()) {
                result.getErrors().put("NOT_FOUND", "Truyện này đã bị xóa bới admin");
            } else if (userId != s.getUserId()) {
                result.getErrors().put("NOT_OWN", "Truyện này không thuộc về bạn");
            } else {
                s.setActive(false);
                storyRepository.save(s);
                result.setData(s);
                result.setSuccess(true);
            }
        }
        return result;
    }

    @Override
    public List<GetStoryDto> getStoriesByUserId(int userId) {
        List<Story> stories = storyRepository.findAllByUserId(userId);
        return stories.stream().map(s -> {
            GetStoryDto dto = modelMapper.map(s, GetStoryDto.class);
            List<Tag> tags = tagRepository.findAllByStoryId(dto.getId());
            List<TagDto> tagDtoList = tagService.mapModelToDto(tags);
            dto.setTags(tagDtoList);
            dto.setNumOfRead(historyRepository.countAllByStoryId(s.getId()));
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public ResultDto getReadingStory(int storyId) {
        ResultDto result = new ResultDto();
        result.setSuccess(false);
        result.setData(null);

        Story story = storyRepository.findById(storyId).orElse(null);
        if (story == null) {
            result.getErrors().put("NOT_FOUND", "Không tìm thấy truyện này");
        } else if (!story.isActive() || story.isDeactiveByAdmin()) {
            result.getErrors().put("DELETED", "Truyện này đã bị xóa");
        } else {
            User user = userRepository.findById(story.getUserId()).orElse(null);
            if(user == null || (!user.isActive() || user.isDeactiveByAdmin())){
                result.getErrors().put("DELETED", "Truyện này đã bị xóa");
            } else {
                ReadStoryDto readStoryDto = modelMapper.map(story, ReadStoryDto.class);
                List<Screen> screens = screenRepository.findByStoryId(storyId);

                List<ReadStoryScreenDto> screenDtoList = screens.stream().map(screen -> {
                    ReadStoryScreenDto screenDto = modelMapper.map(screen, ReadStoryScreenDto.class);
                    screenDto.setActions(actionRepository.findAllByScreenId(screen.getId()));
                    return screenDto;
                }).collect(Collectors.toList());

                List<Information> informations = informationRepository.findAllByStoryId(storyId);

                List<ReadStoryInformationDto> informationDtos = informations.stream().map(info -> {
                    ReadStoryInformationDto informationDto = modelMapper.map(info, ReadStoryInformationDto.class);
                    List<InfoCondition> conditions = infoConditionRepository.findAllByInformationId(informationDto.getId());
                    informationDto.setConditions(conditions);
                    return informationDto;
                }).collect(Collectors.toList());

                List<String> informationIds = informations.stream().map(info -> info.getId())
                        .collect(Collectors.toList());

                List<InformationAction> informationActions = informationActionRepository.findAllByInformationIdIn(informationIds);

                List<Tag> tagList = tagRepository.findAllByStoryId(storyId);
                List<ReadStoryTagDto> readStoryTagDtoList = tagList.stream().map(t -> modelMapper.map(t, ReadStoryTagDto.class)).collect(Collectors.toList());

                readStoryDto.setInformationActions(informationActions);
                readStoryDto.setScreens(screenDtoList);
                readStoryDto.setInformations(informationDtos);
                readStoryDto.setTags(readStoryTagDtoList);

                result.setData(readStoryDto);
                result.setSuccess(true);
            }
        }
        return result;
    }

    @Override
    public ResultDto createStory(CreateStoryDto createStoryDto, int userId) {
        ResultDto result = new ResultDto();
        HashMap<String, String> errors = validateStoryinfo(createStoryDto);

        if(errors.size() > 0){
            result.setSuccess(false);
            result.setErrors(errors);
            return result;
        }

        Story story = modelMapper.map(createStoryDto, Story.class);



        HashMap<String, String> screenIdsMap = new HashMap<>();
        HashMap<String, String> actionIdsMap = new HashMap<>();
        HashMap<String, String> informationIdsMap = new HashMap<>();

        createStoryDto.getScreens().stream().forEach(screen -> {
            screenIdsMap.put(screen.getId(), MyStringUtils.generateUniqueId());
        });

        story.setFirstScreenId(null);
        story.setActive(true);
        story.setPublished(createStoryDto.isPublished());
        story.setDeactiveByAdmin(false);
        story.setUpdatedAt(new Date());
        story.setUserId(userId);

        story = storyRepository.save(story);
        int storyId = story.getId();

        //insert story tags
        createStoryDto.getTags().stream().forEach(tagId -> {
            StoryTag st = new StoryTag();
            st.setTagId(tagId);
            st.setStoryId(storyId);
            storyTagRepository.save(st);
        });

        //save all screens
        createStoryDto.getScreens().stream().forEach(screen -> {
            Screen savedScreen = modelMapper.map(screen, Screen.class);

            savedScreen.setStoryId(storyId);
            savedScreen.setId(screenIdsMap.get(screen.getId()));
            screenRepository.save(savedScreen);

            screen.getActions().stream().forEach(action -> {
                Action savedAction = modelMapper.map(action, Action.class);

                savedAction.setId(MyStringUtils.generateUniqueId());
                savedAction.setScreenId(savedScreen.getId());
                if (action.getType().equals(ACTION_TYPES.NEXT_SCREEN.toString())) {
                    savedAction.setValue(screenIdsMap.get(action.getValue()));
                }
                savedAction.setNextScreenId(screenIdsMap.get(action.getNextScreenId()));

                actionRepository.save(savedAction);
                actionIdsMap.put(action.getId(), savedAction.getId());
            });
        });

        story.setFirstScreenId(screenIdsMap.get(createStoryDto.getFirstScreenId()));
        storyRepository.save(story);
        //save all informations
        createStoryDto.getInformations().stream().forEach(information -> {
            Information savedInformation = modelMapper.map(information, Information.class);
            savedInformation.setStoryId(storyId);
            savedInformation.setId(MyStringUtils.generateUniqueId());
            informationRepository.save(savedInformation);

            informationIdsMap.put(information.getId(), savedInformation.getId());

            information.getConditions().stream().forEach(condition -> {
                InfoCondition savedInfoCondition = modelMapper.map(condition, InfoCondition.class);

                savedInfoCondition.setInformationId(savedInformation.getId());
                savedInfoCondition.setId(MyStringUtils.generateUniqueId());
                savedInfoCondition.setNextScreenId(screenIdsMap.get(condition.getNextScreenId()));

                infoConditionRepository.save(savedInfoCondition);
            });
        });

        //save information action
        createStoryDto.getInformationActions().stream().forEach(informationAction -> {
            InformationAction savedInformationAction = modelMapper.map(informationAction, InformationAction.class);
            savedInformationAction.setActionId(actionIdsMap.get(informationAction.getActionId()));
            savedInformationAction.setInformationId(informationIdsMap.get(informationAction.getInformationId()));

            informationActionRepository.save(savedInformationAction);
        });

        result.setSuccess(true);
        result.setErrors(null);
        result.setData(story);

        return result;
    }

    @Override
    public ResultDto updateStory(CreateStoryDto storyDto, int userId) {
        ResultDto resultDto = new ResultDto();
        HashMap<String, String> errors = validateStoryinfo(storyDto);

        if(errors.size() > 0){
            resultDto.setSuccess(false);
            resultDto.setErrors(errors);
            return resultDto;
        }

        Story foundStory = storyRepository.findById(storyDto.getId()).orElse(null);

        if (foundStory == null) {
            resultDto.getErrors().put("NOT_FOUND", "Truyện này không có trong hệ thống!");
        } else if (!foundStory.isActive() || foundStory.isDeactiveByAdmin()) {
            resultDto.getErrors().put("DELETED", "Truyện đã bị xóa!");
        } else if (userId != foundStory.getUserId()) {
            resultDto.getErrors().put("NOT_OWN", "Truyện này không thuộc về bạn");
        } else {
            Story story = modelMapper.map(storyDto, Story.class);
            HashMap<String, String> screenIdsMap = new HashMap<>();
            HashMap<String, String> actionIdsMap = new HashMap<>();
            HashMap<String, String> informationIdsMap = new HashMap<>();

            //delete all old screens

            List<String> screenIdList = storyDto.getScreens().stream().map(scr -> scr.getId()).collect(Collectors.toList());
            List<Screen> screenList = screenRepository.findByStoryId(story.getId());
            //delete unused screen
            screenList.stream().forEach(screen -> {
                if(!screenIdList.contains(screen.getId())){
                    screenRepository.delete(screen);
                }
            });

            storyDto.getScreens().stream().forEach(screen -> {
                if(screenRepository.existsById(screen.getId())){
                    screenIdsMap.put(screen.getId(), screen.getId());
                } else {
                    screenIdsMap.put(screen.getId(), MyStringUtils.generateUniqueId());
                }
            });

            story.setFirstScreenId(screenIdsMap.get(storyDto.getFirstScreenId()));
            story.setCreatedAt(foundStory.getCreatedAt());
            story.setActive(true);
            story.setDeactiveByAdmin(foundStory.isDeactiveByAdmin());
            story.setAvgRate(foundStory.getAvgRate());
            story.setUserId(foundStory.getUserId());
            storyRepository.save(story);
            int storyId = story.getId();

            //delete all story tags;
            List<StoryTag> storyTags = storyTagRepository.findAllByStoryId(storyId);
            storyTagRepository.deleteAll(storyTags);

            //insert story tags
            storyDto.getTags().stream().forEach(tagId -> {
                StoryTag st = new StoryTag();
                st.setTagId(tagId);
                st.setStoryId(storyId);
                storyTagRepository.save(st);
            });

            //insert new screens
            storyDto.getScreens().stream().forEach(screen -> {
                Screen savedScreen = modelMapper.map(screen, Screen.class);

                savedScreen.setStoryId(storyId);
                savedScreen.setId(screenIdsMap.get(screen.getId()));
                screenRepository.save(savedScreen);

                //delete all actions
                List<Action> actionList  = actionRepository.findAllByScreenId(savedScreen.getId());
                actionRepository.deleteAll(actionList);

                screen.getActions().stream().forEach(action -> {
                    Action savedAction = modelMapper.map(action, Action.class);

                    savedAction.setId(MyStringUtils.generateUniqueId());
                    savedAction.setScreenId(savedScreen.getId());
                    if (action.getType().equals(ACTION_TYPES.NEXT_SCREEN.toString())) {
                        savedAction.setValue(screenIdsMap.get(action.getValue()));
                    }
                    savedAction.setNextScreenId(screenIdsMap.get(action.getNextScreenId()));

                    actionRepository.save(savedAction);
                    actionIdsMap.put(action.getId(), savedAction.getId());
                });
            });

            //delete all informations
            List<Information> informations = informationRepository.findAllByStoryId(storyId);
            List<String> informationIds = informations.stream().map((i -> i.getId())).collect(Collectors.toList());
            informationRepository.deleteAll(informations);

            //delete all information actions
            List<InformationAction> informationActionList = informationActionRepository.findAllByInformationIdIn(informationIds);
            informationActionRepository.deleteAll(informationActionList);

            //insert new informations
            storyDto.getInformations().stream().forEach(information -> {
                Information savedInformation = modelMapper.map(information, Information.class);
                savedInformation.setStoryId(storyId);
                savedInformation.setId(MyStringUtils.generateUniqueId());
                informationRepository.save(savedInformation);

                informationIdsMap.put(information.getId(), savedInformation.getId());

                information.getConditions().stream().forEach(condition -> {
                    InfoCondition savedInfoCondition = modelMapper.map(condition, InfoCondition.class);

                    savedInfoCondition.setInformationId(savedInformation.getId());
                    savedInfoCondition.setId(MyStringUtils.generateUniqueId());
                    savedInfoCondition.setNextScreenId(screenIdsMap.get(condition.getNextScreenId()));

                    infoConditionRepository.save(savedInfoCondition);
                });
            });

            //save information action

            storyDto.getInformationActions().stream().forEach(informationAction -> {
                InformationAction savedInformationAction = modelMapper.map(informationAction, InformationAction.class);
                savedInformationAction.setActionId(actionIdsMap.get(informationAction.getActionId()));
                savedInformationAction.setInformationId(informationIdsMap.get(informationAction.getInformationId()));

                informationActionRepository.save(savedInformationAction);
            });
            resultDto.setSuccess(true);
            resultDto.setData(story);
        }
        return resultDto;
    }

    @Override
    public Page<GetStoryDto> searchStories(Set<Integer> tags, String keyword, boolean isActive,
                                           boolean isPublished, int page, int itemsPerPage) {
        if (StringUtils.isEmpty(keyword)) keyword = "";
        if (tags.size() == 0) {
            tags = tagRepository.findAll().stream().map(t -> t.getId()).collect(Collectors.toSet());
        }

        Pageable pageable = PageRequest.of(page - 1, itemsPerPage, Sort.by("id").ascending());
        Page<Story> page1 = storyRepository.findAllBySearchCondition(keyword, tags, isActive, isPublished, pageable);
        Page<GetStoryDto> page2 = page1.map(new Function<Story, GetStoryDto>() {
            @Override
            public GetStoryDto apply(Story story) {
                List<Tag> tagList = tagRepository.findAllByStoryId(story.getId());
                GetStoryDto dto = modelMapper.map(story, GetStoryDto.class);
                dto.setTags(tagService.mapModelToDto(tagList));
                User user = userRepository.findById(story.getUserId()).orElse(null);
                if (user != null) user.setPassword(null);
                dto.setUser(user);
                dto.setNumOfRead(historyRepository.countAllByStoryId(story.getId()));
                return dto;
            }
        });


        return page2;
    }

    @Override
    public Page<GetStoryDto> searchStoriesOfUserProfile(int userId, Set<Integer> tags, String keyword, int page, int itemsPerPage) {
        if (StringUtils.isEmpty(keyword)) keyword = "";
        if (tags.size() == 0) {
            tags = tagRepository.findAll().stream().map(t -> t.getId()).collect(Collectors.toSet());
        }

        Pageable pageable = PageRequest.of(page - 1, itemsPerPage, Sort.by("id").descending());
        Page<Story> page1 = storyRepository.findAllByUserProfile(userId, keyword, tags, pageable);
        Page<GetStoryDto> page2 = page1.map(new Function<Story, GetStoryDto>() {
            @Override
            public GetStoryDto apply(Story story) {
                List<Tag> tagList = tagRepository.findAllByStoryId(story.getId());
                GetStoryDto dto = modelMapper.map(story, GetStoryDto.class);
                User user = userRepository.findById(story.getUserId()).orElse(null);
                if(user != null) user.setPassword(null);
                dto.setUser(user);
                dto.setTags(tagService.mapModelToDto(tagList));
                dto.setNumOfRead(historyRepository.countAllByStoryId(story.getId()));
                return dto;
            }
        });

        return page2;
    }

    GetStoryDto mapModelToDto(Story story) {
        List<Tag> tagList = tagRepository.findAllByStoryId(story.getId());
        GetStoryDto dto = modelMapper.map(story, GetStoryDto.class);
        dto.setTags(tagService.mapModelToDto(tagList));
        dto.setNumOfRead(historyRepository.countAllByStoryId(story.getId()));
        return dto;
    }

    @Override
    public List<GetStoryDto> getTheMostReadingStories() {
        List<Story> storyList = storyRepository.findTheMostReadingStories();
        return storyList.stream().map(s -> {
            GetStoryDto dto = modelMapper.map(s, GetStoryDto.class);
            List<Tag> tags = tagRepository.findAllByStoryId(s.getId());
            dto.setTags(tagService.mapModelToDto(tags));
            dto.setUser(userRepository.findById(s.getUserId()).orElse(null));
            dto.setNumOfRead(historyRepository.countAllByStoryId(s.getId()));
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public Page<GetStoryDto> getStoriesForAdmin(String keyword, String orderBy, boolean asc, int page, int itemsPerPage) {

        Pageable pageable = PageRequest.of(page - 1, itemsPerPage);
        Page<Story> page1 = null;
        switch (orderBy) {
            case "avg_rate":
                if (asc) page1 = storyRepository.findForAdminOrderByAvgRateASC(keyword, pageable);
                else page1 = storyRepository.findForAdminOrderByAvgRateDESC(keyword, pageable);
                break;
            case "comment":
                if (asc) page1 = storyRepository.findForAdminOrderByNumOfCommentASC(keyword, pageable);
                else page1 = storyRepository.findForAdminOrderByNumOfCommentDESC(keyword, pageable);
                break;
            case "rating":
                if (asc) page1 = storyRepository.findForAdminOrderByNumOfRatingASC(keyword, pageable);
                else page1 = storyRepository.findForAdminOrderByNumOfRatingDESC(keyword, pageable);
                break;
            case "screen":
                if (asc) page1 = storyRepository.findForAdminOrderByNumOfScreenASC(keyword, pageable);
                else page1 = storyRepository.findForAdminOrderByNumOfScreenDESC(keyword, pageable);
                break;
            case "read":
                if (asc) page1 = storyRepository.findForAdminOrderByNumOfReadASC(keyword, pageable);
                else page1 = storyRepository.findForAdminOrderByNumOfReadDESC(keyword, pageable);
                break;
            case "date":
                if(asc) page1 = storyRepository.findForAdminOrderDateASC(keyword, pageable);
                else page1 = storyRepository.findForAdminOrderDateDESC(keyword, pageable);
                break;
        }

        if (page1 == null) return null;

        Page<GetStoryDto> page2 = page1.map(new Function<Story, GetStoryDto>() {
            @Override
            public GetStoryDto apply(Story story) {
                return mapStoryModelToGetStoryDto(story);
            }
        });
        return page2;
    }

    public GetStoryDto mapStoryModelToGetStoryDto(Story story) {
        List<Tag> tagList = tagRepository.findAllByStoryId(story.getId());
        GetStoryDto dto = modelMapper.map(story, GetStoryDto.class);
        dto.setTags(tagService.mapModelToDto(tagList));
        dto.setNumOfComment(commentRepository.countCommentByStoryId(story.getId()));
        dto.setNumOfScreen(screenRepository.countAllByStoryId(story.getId()));
        dto.setNumOfRate(ratingRepository.countRatingByStoryId(story.getId()));
        User user = userRepository.findById(story.getUserId()).orElse(null);
        if(user != null) user.setPassword(null);
        dto.setUser(user);
        dto.setNumOfRead(historyRepository.countAllByStoryId(story.getId()));
        return dto;
    }

    @Override
    public Page<GetStoryDto> getStoriesForUser(int userId, String keyword, String orderBy, boolean asc, int page, int itemsPerPage) {
        Pageable pageable = PageRequest.of(page - 1, itemsPerPage);
        Page<Story> page1 = null;
        switch (orderBy) {
            case "avg_rate":
                if (asc) page1 = storyRepository.findForUserOrderByAvgRateASC(userId, keyword, pageable);
                else page1 = storyRepository.findForUserOrderByAvgRateDESC(userId, keyword, pageable);
                break;
            case "read":
                if (asc) page1 = storyRepository.findForUserOrderByNumOfReadASC(userId, keyword, pageable);
                else page1 = storyRepository.findForUserOrderByNumOfReadDESC(userId, keyword, pageable);
                break;
            case "comment":
                if (asc) page1 = storyRepository.findForUserOrderByNumOfCommentASC(userId, keyword, pageable);
                else page1 = storyRepository.findForUserOrderByNumOfCommentDESC(userId, keyword, pageable);
                break;
            case "rating":
                if (asc) page1 = storyRepository.findForUserOrderByNumOfRatingASC(userId, keyword, pageable);
                else page1 = storyRepository.findForUserOrderByNumOfRatingDESC(userId, keyword, pageable);
                break;
            case "screen":
                if (asc) page1 = storyRepository.findForUserOrderByNumOfScreenASC(userId, keyword, pageable);
                else page1 = storyRepository.findForUserOrderByNumOfScreenDESC(userId, keyword, pageable);
                break;
            case "date":
                if(asc) page1 = storyRepository.findForUserOrderByDateASC(userId, keyword,  pageable);
                else page1 = storyRepository.findForUserOrderByDateDESC(userId, keyword, pageable);
                break;
        }

        if (page1 == null) return null;

        Page<GetStoryDto> page2 = page1.map(new Function<Story, GetStoryDto>() {
            @Override
            public GetStoryDto apply(Story story) {
                return mapStoryModelToGetStoryDto(story);
            }
        });
        return page2;
    }

    @Override
    public ResultDto updateByAdmin(int storyId, boolean disable) {
        ResultDto result = new ResultDto();
        Story story = storyRepository.findById(storyId).orElse(null);
        if (story == null) {
            result.getErrors().put("NOT_FOUND", "Không tìm thấy truyện này");
            result.setSuccess(false);
        } else {
            if (!story.isDeactiveByAdmin() && disable) {
                story.setDeactiveByAdmin(true);
            } else if (story.isDeactiveByAdmin() && !disable) {
                story.setDeactiveByAdmin(false);
            }
            storyRepository.save(story);
            result.setSuccess(true);
            result.setData(story);
        }
        return result;
    }



    @Autowired
    CommentMicroService commentService;

    StoryReactByRange storyReactByRange = new StoryReactByRange();

    @Autowired
    HistoryService historyService;
    //lấy so lieu cho do thi reaction(share, view, click link, hitpoint, comment)


    @Autowired
    LinkClickService clickService;

}
