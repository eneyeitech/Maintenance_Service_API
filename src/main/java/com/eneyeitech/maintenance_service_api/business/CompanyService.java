package com.eneyeitech.maintenance_service_api.business;

import com.eneyeitech.maintenance_service_api.persistence.IBuildingRepository;
import com.eneyeitech.maintenance_service_api.persistence.ICompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {
    private final ICompanyRepository companyRepository;

    @Autowired
    public CompanyService(
            ICompanyRepository companyRepository
    ){
        this.companyRepository = companyRepository;
    }

    public Company createCompany(Company companyToCreate){
        return companyRepository.save(companyToCreate);
    }
}
