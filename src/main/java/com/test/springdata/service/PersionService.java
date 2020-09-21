package com.test.springdata.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.test.springdata.entity.Persion;
import com.test.springdata.repsotory.PersionRepsotory;

@Service
public class PersionService {
    @Autowired
    private PersionRepsotory persionRepsotory;

    @Transactional
    public Integer update(String email,Integer id){
    	Integer count = persionRepsotory.update(email, id);
        return count;
    }
    @Transactional
    public void save(List<Persion> list){
    	persionRepsotory.save(list);
    }
}
