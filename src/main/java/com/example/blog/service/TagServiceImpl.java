package com.example.blog.service;

import com.example.blog.dao.TagRepository;
import com.example.blog.po.Tag;
import javassist.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService{

    @Autowired
    private TagRepository tagRepository;

    @Transactional
    @Override
    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Transactional
    @Override
    public Tag getTagById(Long id) {
        return tagRepository.getById(id);
    }

    @Transactional
    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findByName(name);
    }

    @Transactional
    @Override
    public Page<Tag> listTag(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Override
    public List<Tag> listTag() {
        return tagRepository.findAll();
    }

    @Override
    public List<Tag> listTag(String ids) { //1,2,3
        List<Long> nlist = convertToList(ids);
        return tagRepository.findAllById(nlist);
    }

    private List<Long> convertToList(String ids) {
        List<Long> list = new ArrayList<>();
        if (!"".equals(ids) && ids != null) {
            String[] idarray = ids.split(",");
            for (int i=0; i < idarray.length;i++) {
                Long s = Long.parseLong(idarray[i]);
                list.add(s);
            }
        }
        return list;
    }


    @Transactional
    @Override
    public Tag updateTag(Long id, Tag tag) throws NotFoundException {
        Tag updateName = getTagById(id);
        if ( updateName == null) {
            throw new NotFoundException("不存在該類型");
        }
        BeanUtils.copyProperties(tag,updateName);
        return tagRepository.save(updateName);
    }

    @Override
    public List<Tag> listTagTop(Integer size) {
        Pageable paging = PageRequest.of(0,size, Sort.by(Sort.Direction.DESC,"blogs.size"));
        return tagRepository.findTop(paging);
    }

    @Transactional
    @Override
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }
}
