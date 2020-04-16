package com.storyart.storyservice.service;


import com.storyart.storyservice.dto.AddTagDTO;
import com.storyart.storyservice.dto.TagDto;
import com.storyart.storyservice.model.Tag;
import com.storyart.storyservice.repository.TagRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface TagService {
    List<Tag> getTags();
    List<TagDto> mapModelToDto(List<Tag> tags);
    Tag create(AddTagDTO tag);

    Tag update(AddTagDTO tag);
    Page<Tag> findAll();
    Tag findById(Integer id);
}

@Service
class TagServiceImpl implements TagService{
    @Autowired
    TagRepository tagRepository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<Tag> getTags() {
        List<Tag> tags = tagRepository.findAllByActive(true);
        return tags;
    }

    @Override
    public List<TagDto> mapModelToDto(List<Tag> tags) {
        return tags.stream().map(t -> modelMapper.map(t, TagDto.class)).collect(Collectors.toList());
    }

    @Override
    public Tag create(AddTagDTO tagDTO) {
        Tag tag = new Tag();
        Tag checktag = tagRepository.findByTitle(tagDTO.getTitle());
        if (checktag != null) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Xin lỗi, Nhãn này đã tồn tại");
        } else if (tagDTO.getTitle().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.LENGTH_REQUIRED, "Xin hãy nhập Nhãn:");
        } else {
            tag.setTitle(tagDTO.getTitle());
            tag.setActive(true);
            tagRepository.save(tag);
        }
        return tag;
    }

    @Override
    public Tag update(AddTagDTO tagDTO) {
        Optional<Tag> tagCheck = tagRepository.findById(tagDTO.getId());
        Tag tag = tagCheck.get();
        if (!tagCheck.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Xin lỗi, Nhãn không tồn tại ");
        } else if (tagDTO.getTitle().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.LENGTH_REQUIRED, "Xin hãy nhập Nhãn:");
        } else {
            tag.setTitle(tagDTO.getTitle());
            boolean flag = tagDTO.isActive();
            if (!flag) {
                tag.setActive(false);
            } else {
                tag.setActive(true);
            }
            tagRepository.save(tag);
        }
        return tag;
    }

    @Override
    public Page<Tag> findAll() {
        Pageable sort =
                PageRequest.of(0, 50, Sort.by("title").ascending().and(Sort.by("isActive")));
        Page<Tag> list = tagRepository.findAll(sort);
        return list;
    }

    @Override
    public Tag findById(Integer id) {
        Optional<Tag> option = tagRepository.findById(id);
        if (option.isPresent()) {
            return option.get();
        }
        return null;
    }
}
