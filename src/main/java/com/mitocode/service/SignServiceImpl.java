package com.mitocode.service;

import com.mitocode.model.Sign;
import com.mitocode.repo.IGenericRepo;
import com.mitocode.repo.ISignRepo;
import com.mitocode.service.impl.CRUDImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignServiceImpl extends CRUDImpl<Sign, Integer> implements ISignService {

    private final ISignRepo repo;

    @Override
    protected IGenericRepo<Sign, Integer> getRepo() {
        return repo;
    }

    @Override
    public Page<Sign> listPage(Pageable pageable) {
        return repo.findAll(pageable);
    }
}
