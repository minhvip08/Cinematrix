package old12t_it.cinematrix.service;

import java.util.Objects;


import lombok.RequiredArgsConstructor;
import old12t_it.cinematrix.entity.DatabaseSequence;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/*
 * Service is used to generate sequence for record id
 */
@Service
@RequiredArgsConstructor
public class SequenceGeneratorService {
    private final MongoOperations mongoOp;

    public long generateSequence(String seqName) {
        DatabaseSequence counter = mongoOp.findAndModify(query(where("_id").is(seqName)),
                new Update().inc("seq", 1), options().returnNew(true).upsert(true),
                DatabaseSequence.class);
        return !Objects.isNull(counter) ? counter.getSeq() : 1;
    }
}
