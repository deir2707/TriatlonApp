package repo;

import domain.Arbitru;

public interface ArbitruRepository extends ICrudRepository<Long, Arbitru> {
    public Arbitru findUserPw(String user,String pw);

}
