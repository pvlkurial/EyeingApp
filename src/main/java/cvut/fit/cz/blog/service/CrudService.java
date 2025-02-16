package cvut.fit.cz.blog.service;


public interface CrudService<EntityDto, ID> {

    EntityDto save(EntityDto entityDto);
    Iterable<EntityDto> getAll();
    EntityDto findById(ID id);
    boolean deleteById(ID id);
    EntityDto update(ID id, EntityDto entityDto);

    EntityDto findByUsername(String username);



}
