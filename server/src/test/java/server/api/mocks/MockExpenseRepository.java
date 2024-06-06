package server.api.mocks;
import commons.Expense;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.ExpenseRepository;

import java.util.*;
import java.util.function.Function;


public class MockExpenseRepository implements ExpenseRepository {

    public final List<Expense> expenses = new ArrayList<>();

    public final List<String> calledMethods = new ArrayList<>();

    private void call(String name) {
        calledMethods.add(name);
    }
    @Override
    public void flush() {

    }

    @Override
    public <S extends Expense> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Expense> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }


    @Override
    public void deleteAllInBatch(Iterable<Expense> entities) {
        call("deleteAllInBatch");
        for (Expense entity : entities) {
            expenses.remove(entity);
        }
    }


    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {
        call("deleteAllByIdInBatch");
        for( Long l : longs) {
            expenses.removeIf(q -> q.id == l);
        }
    }

    @Override
    public void deleteAllInBatch() {
        call("deleteAllInBatch");
        expenses.clear();
    }

    @Override
    public Expense getOne(Long aLong) {
        call("getOne");
        for (Expense e : expenses) {
            if(e.id == aLong) {
                return e;
            }
        }
        return null;
    }


    @Override
    public Expense getById(Long aLong) {
        call("getById");
        for (Expense e : expenses) {
            if(e.id == aLong) {
                return e;
            }
        }
        return null;
    }


    @Override
    public Expense getReferenceById(Long aLong) {
        return getById(aLong);
    }

    @Override
    public <S extends Expense> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Expense> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Expense> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Expense> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Expense> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Expense> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Expense, R> R findBy(Example<S> example,
                         Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }


    @Override
    public <S extends Expense> S save(S entity) {
        expenses.add(entity);
        return entity;
    }


    @Override
    public <S extends Expense> List<S> saveAll(Iterable<S> entities) {
        call("saveAll");
        ArrayList<Expense> result = new ArrayList<>();
        for (Expense e : entities) {
            expenses.add(e);
            result.add(e);
        }
        return (List<S>) result;
    }


    @Override
    public Optional<Expense> findById(Long aLong) {
        call("findById");
        return expenses.stream().filter(q -> q.id == aLong).findFirst();
    }


    @Override
    public boolean existsById(Long aLong) {
        call("existsById");
        for(Expense e : expenses) {
            if(e.id == aLong) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Expense> findAll() {
        call("findAll");
        return expenses;
    }


    @Override
    public List<Expense> findAllById(Iterable<Long> longs) {
        call("findAllById");
        ArrayList<Expense> expensesResult = new ArrayList<>();
        for( Long l : longs) {
            Optional<Expense> e = findById(l);
            e.ifPresent(expensesResult::add);
        }
        return null;
    }


    @Override
    public long count() {
        call("count");
        return expenses.size();
    }


    @Override
    public void deleteById(Long aLong) {
        call("deleteById");
        for(Expense e : expenses) {
            if(e.id == aLong) {
                expenses.remove(e);
                return;
            }
        }
    }


    @Override
    public void delete(Expense entity) {
        call("deleteById");
        expenses.remove(entity);
    }


    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {
        call("deleteAllById");
        for(Long l : longs) {
            deleteById(l);
        }
    }


    @Override
    public void deleteAll(Iterable<? extends Expense> entities) {
        call("deleteAll");
        for(Expense e : entities) {
            expenses.remove(e);
        }
    }

    @Override
    public void deleteAll() {
        call("deleteAll");
        expenses.clear();
    }


    @Override
    public List<Expense> findAll(Sort sort) {
        call("findAll");
        ArrayList<Comparator<Expense>> comparators = new ArrayList<>();
        sort.stream().forEach(order -> {
            String p = order.getProperty();

            Comparator<Expense> e = new Comparator<Expense>() {
                @Override
                public int compare(Expense o1, Expense o2) {
                    if (p.equals("date")) {
                        return o1.date.compareTo(o2.date);
                    } else if (p.equals("name")) {
                        return o1.name.compareTo(o2.name);
                    }
                    return 0;
                }
            };

            comparators.add(e);
        });

        for(Comparator<Expense> c : comparators) {
            expenses.sort(c);
        }

        return null;
    }


    @Override
    public Page<Expense> findAll(Pageable pageable) {
        return null;
    }
}
