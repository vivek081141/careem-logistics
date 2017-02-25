package ca.customer.services;

import ca.customer.api.Customer;
import ca.customer.api.EntityApi;
import ca.customer.dao.models.CustomerModel;
import ca.customer.dao.models.EntityModel;
import ca.customer.dao.models.Status;
import ca.customer.dao.repositories.CustomerRepository;
import ca.customer.dao.repositories.EntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vivek on 19-01-2017.
 */
@Service
public class CustomerService {
    private CustomerRepository customerRepository;
    private EntityRepository entityRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, EntityRepository entityRepository){
        this.customerRepository=customerRepository;
        this.entityRepository=entityRepository;
    }


    @Transactional
    public Customer createCustomer(Customer customer) {
        CustomerModel customerModel=new CustomerModel();
        EntityModel entityModel=entityRepository.findOne(customer.entityId);
        customerModel.setEntityModel(entityModel);
        customerModel.setStatus(Status.ACTIVE);
        Customer customerApi =toCustomerModel(customerRepository.save(customerModel));
        return customerApi;
    }

    public Customer getCustomer(Long customerId) {
        CustomerModel customerModel=customerRepository.findOne(customerId);
        Customer customer =toCustomerAPI(customerModel);
        return customer;
    }

    @Transactional
    public Customer deactivateCustomer(Long customerId) {
        CustomerModel customerModel=customerRepository.findOne(customerId);
        customerModel.setStatus(Status.INACTIVE);
        Customer customerApi =toCustomerModel(customerRepository.save(customerModel));
        return customerApi;
    }

    public List<Customer> getCustomerList(){
        List<Customer> customers=new ArrayList<Customer>();
        Iterable<CustomerModel> customerModelList=customerRepository.findAll();
        if(customerModelList!=null){
            for(CustomerModel customerModel:customerModelList){
                Customer customer =toCustomerAPI(customerModel);
                customers.add(customer);
            }
        }
        return customers;

    }

    @Transactional
    public EntityApi createEntity(EntityApi entityApi){
        EntityModel entityModel=new EntityModel();
        entityModel.setStatus(Status.ACTIVE);
        EntityApi entityApi1=toEntityModel(entityRepository.save(entityModel));
        return entityApi1;
    }

    public EntityApi getEntity(Long entityId) {
        EntityModel entityModel=entityRepository.findOne(entityId);
        EntityApi entityApi =toEntityAPI(entityModel);
        return entityApi;
    }


    public List<EntityApi> getEntityList() {
        List<EntityApi> entityApiList = new ArrayList<EntityApi>();
        Iterable<EntityModel> entityModels = entityRepository.findAll();
        if (entityModels != null) {
            for (EntityModel entityModel: entityModels) {
                EntityApi entityApi = toEntityAPI(entityModel);
                entityApiList.add(entityApi);
            }
        }
        return entityApiList;
    }





    private EntityApi toEntityModel(EntityModel entityModel) {
        EntityApi entityApi =new EntityApi();
        entityModel.setEmail(entityApi.email);
        entityModel.setStatus(toStatusModel(entityApi.status));
        entityModel.setMobile(entityApi.mobile);
        entityModel.setFirstName(entityApi.name.first);
        entityModel.setMiddleName(entityApi.name.middle);
        entityModel.setLastName(entityApi.name.last);
        return entityApi;
    }

    private EntityApi toEntityAPI(EntityModel model) {
        EntityApi e =new EntityApi();
        e.id=model.getId();
        e.email=model.getEmail();
        e.status=toStatus(model.getStatus());
        e.mobile=model.getMobile();
        e.name.first=model.getFirstName();
        e.name.middle=model.getMiddleName();
        e.name.last=model.getLastName();
        return e;
    }

    private Customer toCustomerAPI(CustomerModel customerModel) {
        Customer customer =new Customer();
        customer.id=customerModel.getId();
        customer.email=customerModel.getEmail();
        customer.status=toStatus(customerModel.getStatus());
        customer.mobile=customerModel.getMobile();
        customer.name.first=customerModel.getFirstName();
        customer.name.middle=customerModel.getMiddleName();
        customer.name.last=customerModel.getLastName();
        return customer;
    }

    private Customer toCustomerModel(CustomerModel customerModel) {
        Customer customer =new Customer();
        customerModel.setEmail(customer.email);
        customerModel.setStatus(toStatusModel(customer.status));
        customerModel.setMobile(customer.mobile);
        customerModel.setFirstName(customer.name.first);
        customerModel.setMiddleName(customer.name.middle);
        customerModel.setLastName(customer.name.last);
        return customer;
    }

    private ca.customer.api.Status toStatus(Status status) {
        ca.customer.api.Status statusApi=null;

        switch (status){
            case ACTIVE:
                statusApi=ca.customer.api.Status.ACTIVE;
                break;
            case INACTIVE:
                statusApi=ca.customer.api.Status.INACTIVE;
                break;
            case DRAFT:
                statusApi=ca.customer.api.Status.DRAFT;
                break;
        }
        return statusApi;
    }

    private Status toStatusModel(ca.customer.api.Status status) {
        Status statusModel=null;
        switch (status){
            case ACTIVE:
                statusModel=Status.ACTIVE;
                break;
            case INACTIVE:
                statusModel=Status.INACTIVE;
                break;
            case DRAFT:
                statusModel=Status.DRAFT;
                break;
        }
        return statusModel;
    }


}