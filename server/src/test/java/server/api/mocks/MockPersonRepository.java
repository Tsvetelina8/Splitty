package server.api.mocks;

import commons.Person;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.PersonRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class MockPersonRepository implements PersonRepository {

    public final List<Person> persons = new ArrayList<>();
    public final List<String> calledMethods = new ArrayList<>();

    private void call(String name) {
        calledMethods.add(name);
    }

    @Override
    public List<Person> findAll() {

        call("findAll");
        return persons;
    }
    @Override
    public void flush() {
        // TODO
    }

    @Override
    public <S extends Person> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Person> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Person> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Person getOne(Long aLong) {
        //TODO
        return null;
    }

    @Override
    public Person getById(Long id) {
        call("getById");
        return find(id).get();
    }

    @Override
    public Person getReferenceById(Long id) {
        call("getReferenceById");
        return find(id).get();
    }
    private Optional<Person> find(Long id) {
        call("find");
        return persons.stream().filter(q -> q.id == id).findFirst();
    }

    @Override
    public <S extends Person> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Person> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Person> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Person> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Person> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Person> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Person, R> R findBy(Example<S> example,
                         Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends Person> S save(S entity) {
        call("save");
        entity.id = (long) persons.size();
        persons.add(entity);
        return entity;
    }

    @Override
    public <S extends Person> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Person> findById(Long id) {
        call("findById");
        return persons.stream().filter(q -> q.id == id).findFirst();
    }

    @Override
    public boolean existsById(Long id) {
        call("existsById");
        return find(id).isPresent();
    }

    @Override
    public List<Person> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        call("count");
        return persons.size();
    }

    @Override
    public void deleteById(Long aLong) {

    }

    @Override
    public void delete(Person entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Person> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Person> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Person> findAll(Pageable pageable) {
        return null;
    }
}
