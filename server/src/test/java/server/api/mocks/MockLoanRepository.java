package server.api.mocks;

import commons.Loan;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.LoanRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class MockLoanRepository implements LoanRepository {

    public final List<Loan> loans = new ArrayList<>();

    public final List<String> calledMethods = new ArrayList<>();

    private void call(String name) {
        calledMethods.add(name);
    }
    @Override
    public void flush() {
    }

    @Override
    public <S extends Loan> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Loan> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<Loan> entities) {
        call("deleteAllInBatch");
        for (Loan loan : entities) {
            loans.remove(loan);
        }
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {
        call("deleteAllByIdInBatch");
        for( Long l : longs) {
            loans.removeIf(q -> q.id == l);
        }
    }

    @Override
    public void deleteAllInBatch() {
        call("deleteAllInBatch");
        loans.clear();
    }

    @Override
    public Loan getOne(Long aLong) {
        call("getOne");
        for (Loan e : loans) {
            if(e.id == aLong) {
                return e;
            }
        }
        return null;
    }

    @Override
    public Loan getById(Long aLong) {
        call("getById");
        for (Loan e : loans) {
            if(e.id == aLong) {
                return e;
            }
        }
        return null;
    }

    @Override
    public Loan getReferenceById(Long aLong) {
        return getById(aLong);
    }

    @Override
    public <S extends Loan> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Loan> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends Loan> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Loan> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Loan> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Loan> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Loan, R> R findBy(Example<S> example,
                         Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends Loan> S save(S entity) {
        loans.add(entity);
        return entity;
    }

    @Override
    public <S extends Loan> List<S> saveAll(Iterable<S> entities) {
        call("saveAll");
        ArrayList<Loan> result = new ArrayList<>();
        for (Loan e : entities) {
            loans.add(e);
            result.add(e);
        }
        return (List<S>) result;
    }

    @Override
    public Optional<Loan> findById(Long aLong) {
        call("findById");
        return loans.stream().filter(q -> q.id == aLong).findFirst();
    }

    @Override
    public boolean existsById(Long aLong) {
        call("existsById");
        for(Loan e : loans) {
            if(e.id == aLong) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Loan> findAll() {
        call("findAll");
        return loans;
    }

    @Override
    public List<Loan> findAllById(Iterable<Long> longs) {
        call("findAllById");
        ArrayList<Loan> expensesResult = new ArrayList<>();
        for( Long l : longs) {
            Optional<Loan> e = findById(l);
            e.ifPresent(expensesResult::add);
        }
        return null;
    }

    @Override
    public long count() {
        call("count");
        return loans.size();
    }

    @Override
    public void deleteById(Long aLong) {
        call("deleteById");
        for(Loan e : loans) {
            if(e.id == aLong) {
                loans.remove(e);
                return;
            }
        }
    }

    @Override
    public void delete(Loan entity) {
        call("deleteById");
        loans.remove(entity);
    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {
        call("deleteAllById");
        for(Long l : longs) {
            deleteById(l);
        }
    }

    @Override
    public void deleteAll(Iterable<? extends Loan> entities) {
        call("deleteAll");
        for(Loan e : entities) {
            loans.remove(e);
        }
    }

    @Override
    public void deleteAll() {
        call("deleteAll");
        loans.clear();
    }

    @Override
    public List<Loan> findAll(Sort sort) {
        call("findAll");
        ArrayList<Comparator<Loan>> comparators = new ArrayList<>();
        sort.stream().forEach(order -> {
            String p = order.getProperty();

            Comparator<Loan> e = new Comparator<Loan>() {
                @Override
                public int compare(Loan o1, Loan o2) {
                    if (p.equals("amount")) {
                        return Double.compare(o1.amount, o2.amount);
                    }
                    return 0;
                }
            };

            comparators.add(e);
        });

        for(Comparator<Loan> c : comparators) {
            loans.sort(c);
        }

        return null;
    }

    @Override
    public Page<Loan> findAll(Pageable pageable) {
        return null;
    }
}
