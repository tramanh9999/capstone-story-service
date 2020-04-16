package com.storyart.storyservice.controller;

import com.storyart.storyservice.dto.AddTagDTO;
import com.storyart.storyservice.model.Tag;
import com.storyart.storyservice.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/tags")
@CrossOrigin(origins = "*")
public class TagController {

    @Autowired
    TagService tagService;

    @GetMapping("public/all")
    public ResponseEntity getTags(){
        return ResponseEntity.ok(tagService.getTags());
    }

    @GetMapping(value = "/public/getAll")
    public Page<Tag> getAll() {
        Page<Tag> list = tagService.findAll();
        return list;
    }

    @PostMapping("")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity createTag(@RequestBody @Valid AddTagDTO tagDTO) {
        Tag tag = tagService.create(tagDTO);
        return new ResponseEntity(tag, HttpStatus.OK);
    }

    @PutMapping("")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity updateTag(@RequestBody @Valid AddTagDTO tagDTO) {
        Tag tag = tagService.update(tagDTO);
        return new ResponseEntity(tag, HttpStatus.OK);
    }
}
