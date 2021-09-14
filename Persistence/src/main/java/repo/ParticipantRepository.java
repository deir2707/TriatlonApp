package repo;

import domain.Participant;

public interface ParticipantRepository  extends ICrudRepository<Long, Participant>{
    public Iterable<Participant> findAllAlfabetic();
}
