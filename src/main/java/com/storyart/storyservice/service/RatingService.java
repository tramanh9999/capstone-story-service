package com.storyart.storyservice.service;

import com.storyart.storyservice.dto.ResultDto;
import com.storyart.storyservice.dto.story_suggestion.*;
import com.storyart.storyservice.model.Rating;
import com.storyart.storyservice.model.ids.RatingId;
import com.storyart.storyservice.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface RatingService {
    List<Integer>  getSuggestion(Integer id, boolean flag);
 //   List<Integer> getSuggestByCommentAndReaction();
    ResultDto rateStory(double stars, int  userId, int storyId);
 //   List<Integer> listAvgRate();
}


@Service
class RatingServiceIml implements RatingService {

    @Autowired
    StoryRepository storyRepository;

    @Autowired
    HistoryRepository historyRepository;


    @Autowired
    RatingRepository ratingRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TagService tagService;

    @Autowired
    CommentRepository commentRepository;

    @Override
    public List<Integer> getSuggestion(Integer id, boolean flagcheck) {
        // Step 1
        // find All User Rating
        List<Integer> listStory = new ArrayList<>();
        if(flagcheck){
            listStory = storyRepository.findStoryThisWeek();

        }else{
            listStory = storyRepository.findAllStory();
        }
        List<RatedStoryDTO> ListAssumRatedStory = new ArrayList<>();

        List<Rating> ratingUser = ratingRepository.findRatingByStoryIdEXceptId(listStory, id);

        //----------------------

        RatingDTO currUser = new RatingDTO();
        currUser.setUserid(id);
        List<Rating> currUserRating = ratingRepository.findRatingByUserId(id, listStory);
        double sumCurr = 0.0;
        for (Rating rate : currUserRating) {
            sumCurr += rate.getStars();
        }
        double normalizeCurrUser = sumCurr / currUserRating.size();

        List<RatedStoryDTO> listCurrRated = new ArrayList<>();
        for (Integer storyid : listStory) {
            boolean flag = true;
            RatedStoryDTO ratedDTO = new RatedStoryDTO();
            for (Rating rate : currUserRating) {
                if (storyid.equals(rate.getStoryId())) {
                    ratedDTO.setStoryId(rate.getStoryId());
                    double userPoint = rate.getStars() - normalizeCurrUser;
                    ratedDTO.setRatedPoint(userPoint);
                    flag = false;
                    listCurrRated.add(ratedDTO);
                }
            }
            if(flag){
                ratedDTO.setStoryId(storyid);
                double userPoint = 0;
                ratedDTO.setRatedPoint(userPoint);
                listCurrRated.add(ratedDTO);
            }
        }

        for(Integer integer : listStory) {
            List<RatingDTO> listRatingUser = new ArrayList<>();
            boolean flag2 = false;
            List<Rating> userInStory = new ArrayList<>();
            for (Rating rate : ratingUser) {

                if (integer.equals(rate.getStoryId())) {
                    userInStory.add(rate);
                    flag2 = true;
                }
            }
            RatedStoryDTO AssumRatedStory = new RatedStoryDTO();
            if (!flag2) {
                AssumRatedStory = new RatedStoryDTO();
                AssumRatedStory.setStoryId(integer);
                AssumRatedStory.setRatedPoint(0.0);
                ListAssumRatedStory.add(AssumRatedStory);
            } else {

                for (Rating rated : userInStory) {


                    List<Rating> userRating = new ArrayList<>();
                    for (Rating rate : ratingUser) {
                        if (rate.getUserId() == rated.getUserId()) {
                            userRating.add(rate);
                        }
                    }
                    RatingDTO dtoUSER = new RatingDTO();

                    List<RatedStoryDTO> listRated = new ArrayList<>();
                    double sum = 0.0;
                    // Step 2
                    // Count Normalize Number of selected User
                    for (Rating rateUser : userRating) {
                        sum = sum + rateUser.getStars();

                    }
                    double normalizeNumber = sum / userRating.size();


                    // Step 3
                    // Find Rating of selected User
                    dtoUSER.setUserid(rated.getUserId());

                    for (Integer storyid : listStory) {
                        boolean flag = true;
                        RatedStoryDTO ratedDTO = new RatedStoryDTO();

                        for (Rating rateUser : userRating) {
                            if (storyid.equals(rateUser.getStoryId())) {
                                ratedDTO.setStoryId(rateUser.getStoryId());
                                double userPoint = rateUser.getStars() - normalizeNumber;
                                ratedDTO.setRatedPoint(userPoint);
                                flag = false;
                            }
                        }
                        if (flag) {
                            ratedDTO.setStoryId(storyid);
                            double userPoint = 0;
                            ratedDTO.setRatedPoint(userPoint);
                        }
                        listRated.add(ratedDTO);
                    }
                    dtoUSER.setListPoint(listRated);
                    boolean flag = false;
                    for(int i =0 ; i< listRatingUser.size(); i++ ){
                        if(dtoUSER.getUserid() == listRatingUser.get(i).getUserid()){
                            flag = true;
                        }
                    }
                    if(!flag){
                        listRatingUser.add(dtoUSER);
                    }
                }
            // ------------------------------------------

                List<SimilarityDTO> listsimi = new ArrayList<>();
                // ------------
                for (RatingDTO dto : listRatingUser) {
                    List<Double> listRatingSelectedUser = new ArrayList<>();
                    List<Double> listRatingCurrentUser = new ArrayList<>();
                    for (RatedStoryDTO user : dto.getListPoint()) {
                        listRatingSelectedUser.add(user.getRatedPoint());
                    }
                    for (RatedStoryDTO user : listCurrRated) {
                        listRatingCurrentUser.add(user.getRatedPoint());
                    }
                    if (dto.getUserid() == currUser.getUserid()) {

                    } else {
                        double cosine = cosineSimilarity(listRatingCurrentUser, listRatingSelectedUser);
                        SimilarityDTO simidto = new SimilarityDTO();
                        simidto.setUserid(dto.getUserid());
                        simidto.setSimilarity(cosine);
                        listsimi.add(simidto);
                    }
                }
                //Step 6
                // Count Rate Prediction

                List<SimilarityDTO> listuser = new ArrayList<>();
                for (SimilarityDTO dtosimi : listsimi) {
                    SimilarityDTO simidto = new SimilarityDTO();

                    if (dtosimi.getSimilarity() > 0.5) {
                        simidto.setUserid(dtosimi.getUserid());
                        simidto.setSimilarity(dtosimi.getSimilarity());
                        listuser.add(simidto);
                    }
                }
                int MostFitId = 0;
                int SecondFitId = 0;
                double MostFit = 0.0;
                double SecondFit = 0.0;

                if (listuser.size() == 0) {


                } else {
                    for (int i = 0; i < listuser.size(); i++) {
                        if (listuser.get(i).getSimilarity() >= MostFit) {
                            SecondFit = MostFit;
                            MostFit = listuser.get(i).getSimilarity();
                            SecondFitId = MostFitId;
                            MostFitId = listuser.get(i).getUserid();
                        } else if (listuser.get(i).getSimilarity() >= SecondFit) {
                            SecondFit = listuser.get(i).getSimilarity();
                            SecondFitId = listuser.get(i).getUserid();
                        }
                    }


                    double normalizePointMostFit = 0.0;
                    double normalizePointSecondFit = 0.0;
                    for (RatingDTO dto : listRatingUser) {
                        if (dto.getUserid() == MostFitId) {
                            for (RatedStoryDTO dto2 : dto.getListPoint()) {
                                if (dto2.getStoryId() == integer) {
                                    normalizePointMostFit = dto2.getRatedPoint();
                                }
                            }
                        }
                        if (dto.getUserid() == SecondFitId) {
                            for (RatedStoryDTO dto2 : dto.getListPoint()) {
                                if (dto2.getStoryId() == integer) {
                                    normalizePointSecondFit = dto2.getRatedPoint();
                                }
                            }
                        }


                    }

                    AssumRatedStory.setStoryId(integer);
                    Double ra = ((normalizePointMostFit * MostFit) + (normalizePointSecondFit * SecondFit)) / ((Math.abs(MostFit)) + (Math.abs(SecondFit)));
                    Double AssumtionPoint = ra + normalizeCurrUser;
                    AssumRatedStory.setRatedPoint(AssumtionPoint);
                    ListAssumRatedStory.add(AssumRatedStory);
                }
            }
        }

        if(ListAssumRatedStory.size() == 0){
            List<Integer> listnull = null;
            return listnull;
        }else{



        // Step 7
        // remove all lesser than 2.5
        List<Integer> listSuggestStory = new ArrayList<>();
        for (RatedStoryDTO dto : ListAssumRatedStory){
            if(dto.getRatedPoint() >= 2.5){
                listSuggestStory.add(dto.getStoryId());
            }
        }

        // Step 8
        // remove all Duplicate with Current User
        List<Integer> listStoryCurrUser = ratingRepository.findStoryRatingByUserId(id);
        listSuggestStory.removeAll(listStoryCurrUser);

        // Step 9
        // get All suggest Story


        return listSuggestStory;
        }
    }

    @Override
    public ResultDto rateStory(double stars, int userId, int storyId) {
        ResultDto result = new ResultDto();
        result.setSuccess(false);
        RatingId ratingId = new RatingId();
        ratingId.setStoryId(storyId);
        ratingId.setUserId(userId);
        Optional<Rating> rating = ratingRepository.findById(ratingId);
        Rating r = rating.orElseGet(null);
        if(r != null){
          r.setStars(stars);
        } else {
            Rating rate = new Rating();
            RatingId id = new RatingId();
            id.setUserId(userId);
            id.setStoryId(storyId);
            rate.setStars(stars);


        }
        return null;
    }


    public Double cosineSimilarity(List<Double> currUser, List<Double> SelectedUser) {

        double AB = 0.0;
        double A = 0.0;
        double B = 0.0;
        for (int i = 0; i < currUser.size(); i++) {
            AB += currUser.get(i) * SelectedUser.get(i);
            A += currUser.get(i) * currUser.get(i);
            B += SelectedUser.get(i) * SelectedUser.get(i);
        }
        double cosineSimilarity = 0.0;

        cosineSimilarity = AB / (Math.sqrt(A) * Math.sqrt(B));
        return cosineSimilarity;
    }


    /*
    @Override
    public List<Integer> getSuggestByCommentAndReaction() {

        List<StoryCommentDTO> list = new ArrayList<>();
        List<Integer> listStory = storyRepository.findAllStory();
        List<Integer> comment = commentRepository.countCommentByStoryIds(listStory);
        List<Integer> like = commentRepository.countLikeCommentByStoryIds(listStory);
        List<Integer> dislike = commentRepository.countDisLikeCommentByStoryIds(listStory);

        for(Integer integer : listStory){
            int sum = 0;
            for(Integer inter : comment){
                if(inter.equals(integer)){
                    sum = sum +1;
                }
            }
            int sumlike = 0;
            for(Integer inter : like){
                if(inter.equals(integer)){
                    sumlike = sumlike +1;
                }
            }

            int sumdislike = 0;
            for(Integer inter : dislike){
                if(inter.equals(integer)){
                    sumdislike = sumdislike +1;
                }
            }

            double point = ((sum * 0.4) + (sumlike * 0.3) + (sumdislike * 0.3))/(sum + sumlike + sumdislike);
            StoryCommentDTO dto = new StoryCommentDTO();
            dto.setStoryId(integer);
            dto.setPoint(point);
            list.add(dto);
        }

        List<Integer> listSuggestion = new ArrayList<>();
        int topid = 0;
        int secondtopid = 0;
        int thirdtopid = 0;
        int fourtopid = 0;
        double top = 0.0;
        double second = 0.0;
        double third = 0.0;
        double four = 0.0;
        for (StoryCommentDTO dto : list){
            if(dto.getPoint() >= top){
                fourtopid = thirdtopid;
                thirdtopid = secondtopid;
                secondtopid = topid;
                        topid = dto.getStoryId();
                        four = third;
                        third = second;
                        second = top;
                        top = dto.getPoint();
            }else if(dto.getPoint() >= second){
                fourtopid = thirdtopid;
                thirdtopid = secondtopid;
                secondtopid = dto.getStoryId();
                four = third;
                third = second;
                second = dto.getPoint();
            }else if(dto.getPoint() >= third){
                fourtopid = thirdtopid;
                thirdtopid = dto.getStoryId();
                four = third;
                third = dto.getPoint();
            } else if(dto.getPoint() >= four){
                fourtopid = dto.getStoryId();
                four = dto.getPoint();
            }
        }
        listSuggestion.add(topid);
        listSuggestion.add(secondtopid);
        listSuggestion.add(thirdtopid);
        listSuggestion.add(fourtopid);

        return listSuggestion;
    }*/
}
