/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package server.api.mocks;

import commons.Event;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import server.database.EventRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@SuppressWarnings({"NullableProblems"})
public class MockEventRepository implements EventRepository {

    public final List<Event> events = new ArrayList<>();
    public final List<String> calledMethods = new ArrayList<>();

    private void call(String name) {
        calledMethods.add(name);
    }

    @Override
    public void flush() {

    }

    public Optional<Event> find(Long id) {
        return events.stream().filter(e -> e.id == id).findFirst();
    }

    @Override
    public <S extends Event> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Event> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Event> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Event getOne(Long aLong) {
        return null;
    }

    @Override
    public Event getById(Long id) {
        call("getById");
        return find(id).get();
    }

    @Override
    public Event getReferenceById(Long id) {
        call("getReferenceById");
        return find(id).get();
    }

    @Override
    public <S extends Event> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Event> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Event> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Event> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Event> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Event> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Event, R> R findBy(Example<S> example,
                                         Function<FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends Event> S save(S entity) {
        call("save");
        var e = find(entity.id);
        if (e.isEmpty()) {
            entity.id = (long) events.size() + 1;
            events.add(entity);
        }
        else {
            e.get().setTitle(entity.getTitle());
        }
        return entity;
    }

    @Override
    public <S extends Event> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<Event> findById(Long id) {
        return find(id);
    }

    @Override
    public boolean existsById(Long id) {
        call("existsById");
        return find(id).isPresent();
    }

    @Override
    public List<Event> findAll() {
        calledMethods.add("findAll");
        return events;
    }

    @Override
    public List<Event> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return events.size();
    }

    @Override
    public void deleteById(Long aLong) {
        call("deleteById");
        events.remove(findById(aLong).get());
    }

    @Override
    public void delete(Event entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends Event> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Event> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<Event> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public Optional<Event> findByInviteCode(String code) {
        return events.stream().filter(e -> e.getInviteCode().equals(code)).findFirst();
    }

    @Override
    public Optional<Event> findByTitle(String title) {
        return Optional.empty();
    }

    @Override
    public boolean existsEventByInviteCode(String code) {
        return findByInviteCode(code).isPresent();
    }

    @Override
    public boolean existsEventByTitle(String title) {
        return events.stream().anyMatch(e -> e.getTitle().equals(title));
    }
}