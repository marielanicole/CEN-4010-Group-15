package com.bookstore.GeekText.controller;

import com.bookstore.GeekText.model.RatingComment;
import com.bookstore.GeekText.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/rating")
public class RatingCommentController {
    @Autowired
    RatingService ratingService;

    @GetMapping("/")
    public List<RatingComment> list(){
        return ratingService.listAllRatings();
    }
    @GetMapping("/{id}")
    public ResponseEntity<RatingComment> get (@PathVariable Integer id){
        try {
            RatingComment ratingComment = ratingService.getRating(id);
            return new ResponseEntity<RatingComment>(ratingComment, HttpStatus.OK);
        }catch (NoSuchElementException e){
            return new ResponseEntity<RatingComment>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/create-rating")
    public ResponseEntity<?> create (@RequestParam  Integer userid, @RequestParam BigInteger isbn,
                         @RequestParam String comment, @RequestParam Integer rating){
        try {
            if(userid != null && isbn != null){
                RatingComment ratingComment = new RatingComment();

                ratingComment.setUserId(userid);
                ratingComment.setIsbn(isbn);
                ratingComment.setRating(rating);
                ratingComment.setComment(comment);

                long now = System.currentTimeMillis();
                Timestamp sqlTimeStamp = new Timestamp(now);
                ratingComment.setDateStamp(sqlTimeStamp);

                ratingService.saveRating(ratingComment);
                return new ResponseEntity<>(HttpStatus.CREATED);
            }else{
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        }catch(Exception e){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @PostMapping("/create-rating")
    public void create(@RequestBody RatingComment rating, @RequestParam  Integer userid, @RequestParam String isbn ){


        ratingService.saveRating(rating);
    }

    @PutMapping("/")
    public ResponseEntity<?> update (@RequestParam Integer id, @RequestParam Integer newId){
        try {
            RatingComment existsRating = ratingService.getRating(id);
            existsRating.setRatingId(newId);
            ratingService.saveRating(existsRating);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
    @DeleteMapping("ratings/delete/{id}")
    public void delete (@PathVariable Integer id){
        ratingService.deleteRating(id);
    }


    //Create a rating for a book by a user on a 5-star scale. Include datestamp.
    //Create a comment for a book by a user with a date stamp.
    //Retrieve a SORTED ratings list by highest rating
    //Retrieve average rating of a book
}