package com.mitocode.service.impl;

import com.mitocode.model.Medic;
import com.mitocode.repo.IGenericRepo;
import com.mitocode.repo.IMedicRepo;
import com.mitocode.service.IMedicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedicServiceImpl extends CRUDImpl<Medic, Integer> implements IMedicService {

    //@Autowired
    private final IMedicRepo repo;// = new MedicRepo();

    @Override
    protected IGenericRepo<Medic, Integer> getRepo() {
        return repo;
    }

}
